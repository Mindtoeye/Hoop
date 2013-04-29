/*
 *  Copyright 2012 Carnegie Mellon University
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package edu.cmu.lti.oaqa.ecd.phase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPOutputStream;

import mx.bigdata.anyobject.AnyObject;
import mx.bigdata.anyobject.AnyTuple;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCopier;
import org.uimafit.component.JCasMultiplier_ImplBase;
import org.uimafit.descriptor.OperationalProperties;
import org.uimafit.factory.AggregateBuilder;
import org.uimafit.factory.AnalysisEngineFactory;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.ResourceHandle;
import edu.cmu.lti.oaqa.ecd.ResourceHandle.HandleType;
import edu.cmu.lti.oaqa.ecd.util.CasUtils;
import edu.cmu.lti.oaqa.framework.types.ExperimentUUID;
import edu.cmu.lti.oaqa.framework.types.InputElement;
import edu.cmu.lti.oaqa.framework.types.ProcessingStep;

/**
 * The superclass also adds this annotation, we just want to be explicit about doing it
 *
 */
@OperationalProperties(outputsNewCases = true)
public final class BasePhase extends JCasMultiplier_ImplBase 
{
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  public static final String QA_INTERNAL_PHASEID = "__.qa.internal.phaseid.__";
  private static final String CROSS_OPTS_PARAM = "cross-opts";
  private AnalysisEngine[] options;
  private int nextAnnotator;
  private JCas cas;
  private Integer phaseNo;
  private String phaseName;
  private PhasePersistenceProvider persistence;

  private void debug (String aMessage)
  {
	  HoopRoot.debug("BasePhase",aMessage);
  }
  /**
   * 
   */
  @Override
  public void initialize(UimaContext ctx) throws ResourceInitializationException 
  {
    super.initialize(ctx);
    
    debug ("initialize (UimaContext)");
    
    String pp = (String) ctx.getConfigParameterValue("persistence-provider");
    
    if (pp == null) 
    {
      throw new ResourceInitializationException(new IllegalArgumentException(
              "Must provide a parameter of type <persistence-provider>"));
    }
    
    this.persistence = BaseExperimentBuilder.loadProvider(pp, PhasePersistenceProvider.class);
    this.phaseName = (String) ctx.getConfigParameterValue("name");
    this.phaseNo = (Integer) ctx.getConfigParameterValue(QA_INTERNAL_PHASEID);
    debug ("Phase: " + toString());
    String experimentId = (String) ctx.getConfigParameterValue(BaseExperimentBuilder.EXPERIMENT_UUID_PROPERTY);
    String optDescr = (String) ctx.getConfigParameterValue("options");
    
    this.options = loadOptions(optDescr, ctx);
    
    for (AnalysisEngine ae : options) 
    {
      debug (ae.getAnalysisEngineMetaData().getName());
    }
    
    debug (" Total # of options configured: " + size());

    int stageId = (Integer) ctx.getConfigParameterValue(BaseExperimentBuilder.STAGE_ID_PROPERTY);
    persistence.insertExperimentMeta(experimentId, phaseNo, stageId, size());
    nextAnnotator = 0;
  }
  /**
   * 
   */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException 
  {
	  debug ("process (JCas)");
	  
	  cas = aJCas;
	  nextAnnotator = 0;
  }
  /**
   * 
   */
  @Override
  public boolean hasNext() throws AnalysisEngineProcessException 
  {
	  debug ("hasNext ()");
	  
	  return (nextAnnotator < options.length);
  }
  /**
   * 
   */
  @Override
  public void collectionProcessComplete() throws AnalysisEngineProcessException 
  {
	  debug ("collectionProcessComplete ()");
	  
	  for (AnalysisEngine ae : options) 
	  {
		  ae.collectionProcessComplete();
	  }
    
	  executor.shutdown();
  }
  /**
   * 
   * @return
   */
  public Integer getPhaseNo() 
  {
    return phaseNo;
  }
  /**
   * 
   */
  @Override
  public JCas next() throws AnalysisEngineProcessException 
  {
	  debug ("next ()");
	  
	  AnalysisEngine ae = options[nextAnnotator];
	  JCas nextCas = getEmptyJCas();
	  greedyCopy(cas, nextCas);
	  
	  try 
	  {
		  AnnotationIndex<Annotation> prevSteps = nextCas.getAnnotationIndex(ProcessingStep.type);
		  String prevCasId = ProcessingStepUtils.getPreviousCasId(prevSteps);
		  Trace prevTrace = ProcessingStepUtils.getTrace(prevSteps);
		  String optionId = ae.getAnalysisEngineMetaData().getName();
		  int sequenceId = ProcessingStepUtils.getSequenceId(nextCas);
		  Trace trace = ProcessingStepUtils.getPartialTrace(prevTrace.getTrace(), getPhaseNo(), optionId);
		  
		  if (!loadCasFromStorage(nextCas, trace, sequenceId)) 
		  {
			  // TODO: Why do we call process on the next() method?
			  // See: http://uima.apache.org/downloads/releaseDocs/2.3.0-incubating/docs/api/org/apache/uima/analysis_component/JCasMultiplier_ImplBase.html
			  process(ae, nextCas, prevCasId, prevTrace, optionId, sequenceId, trace);
		  }
		  
		  nextAnnotator++; // TODO:Should this be in a final clause?
		  return nextCas;
	  } 
	  catch (Exception e) 
	  {
		  throw new AnalysisEngineProcessException(e);
	  }
  }
  /**
   * 
   * @param ae
   * @param nextCas
   * @param prevCasId
   * @param prevTrace
   * @param optionId
   * @param sequenceId
   * @param trace
   * @throws IOException
   * @throws SAXException
   * @throws Exception
   */
  private void process (final AnalysisEngine ae, 
		  				JCas nextCas,
		  				String prevCasId,
		  				Trace prevTrace,
		  				String optionId,
		  				int sequenceId,
		  				Trace trace) throws IOException, SAXException, Exception 
	{
	  debug ("process ()");
	  
	  long a = System.currentTimeMillis();
	  final WrappedJCas wrapped = new WrappedJCas(nextCas);
	  final String uuid = ProcessingStepUtils.getCurrentExperimentId(nextCas);
	  final String key = getExecutionHash(uuid, trace, sequenceId);
	  
	  try 
	  {
		  insertExecutionTrace(nextCas, optionId, a, prevCasId, trace, key);
		  		  
		  debug (String.format("[%s] Executing option: %s on trace %s\n", sequenceId, optionId, prevTrace));
		  
		  Future<?> future = executor.submit(new Runnable() 
		  {
			  @Override
			  public void run () 
			  {
				  try 
				  {
					  ae.process(wrapped); // Process the next option 
				  } 
				  catch (Exception e) 
				  {
					  Throwables.propagate(e);
				  }
			  }
		  });
		  
		  future.get(15, TimeUnit.MINUTES);
		  long b = System.currentTimeMillis();
		  
		  updateTrace(nextCas, optionId, key);
		  
		  storeCas(nextCas, b, key);
		  
		  debug (String.format("[%s]  Execution time for option %s: %ss\n", sequenceId, optionId, (b - a) / 1000));
	  } 
	  catch (TimeoutException e) 
	  {
		  wrapped.invalidate();
		  long b = System.currentTimeMillis();
		  storeException(b, e, key, ExecutionStatus.TIMEOUT);
		  
		  debug (String.format("[%s]  Execution timed out for option: %s after %ss\n", sequenceId, optionId, (b - a) / 1000));
      
		  throw e; // Re-throw exception to allow the Flow controller do its job
	  } 
	  catch (Exception e) 
	  {
		  long b = System.currentTimeMillis();
		  
		  try 
		  {
			  storeException(b, e, key, ExecutionStatus.FAILURE);
			  debug (String.format ("[%s]  Execution failed for option: %s after %ss\n", sequenceId, optionId, (b - a) / 1000));
		  } 
		  finally 
		  {
			  nextCas.release();
		  }
		  
		  throw e; // Re-throw exception to allow the Flow controller do its job
	  }
  }
  /**
   * 
   * @param existingCas
   * @param emptyCas
   */
  private void greedyCopy(JCas existingCas, JCas emptyCas) 
  {
	  debug ("greedyCopy ()");
    // nothing 'greedy' about the default implementation, which copies everything.
    CasCopier.copyCas(existingCas.getCas(), emptyCas.getCas(), true);
  }
  /**
   * 
   * @param jcas
   * @param optionId
   * @param key
   */
  private void updateTrace(JCas jcas, String optionId, String key) 
  {
	  debug ("updateTrace ()");
	  
	  ProcessingStep s = new ProcessingStep(jcas);
	  s.setComponent(optionId);
	  s.setPhaseId(getPhaseNo());
	  s.setCasId(key);
	  s.addToIndexes();
  }
  /**
   * 
   * @param jcas
   * @param optionId
   * @param startTime
   * @param prevCas
   * @param trace
   * @param key
   * @throws IOException
   */
  private void insertExecutionTrace(JCas jcas, 
		  							final String optionId, 
		  							final long startTime,
		  							final String prevCas,
		  							final Trace trace,
		  							final String key) throws IOException 
	{
	  debug ("insertExecutionTrace ()");
	  
	  final String uuid = ProcessingStepUtils.getCurrentExperimentId(jcas);
	  InputElement input = (InputElement) CasUtils.getFirst(jcas, InputElement.class.getName());
	  final String dataset = input.getDataset();
	  final int sequenceId = input.getSequenceId();
	  
	  persistence.insertExecutionTrace(optionId, sequenceId, dataset, getPhaseNo(), uuid, startTime, getHostName(), trace.getTrace(), key);
  }
  /**
   * 
   * @param jcas
   * @param endTime
   * @param key
   * @throws IOException
   * @throws SAXException
   */
  private void storeCas(JCas jcas, final long endTime, final String key) throws IOException, SAXException 
  {
	  debug ("storeCas ()");
	  
	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
	  GZIPOutputStream gz = new GZIPOutputStream(baos);
	  XmiCasSerializer.serialize(jcas.getCas(), gz);
	  gz.finish();
	  
	  final byte[] bytes = baos.toByteArray();
	  
	  persistence.storeCas(bytes, ExecutionStatus.SUCCESS, endTime, key);
  }
  /**
   * 
   * @param endTime
   * @param e
   * @param key
   * @param status
   * @throws IOException
   * @throws SAXException
   */
  private void storeException (final long endTime,
		  						Exception e, 
		  						final String key,
		  						ExecutionStatus status) throws IOException, SAXException 
  {
	  debug ("storeException ()");
	  
	  Throwable rootCause = Throwables.getRootCause(e);
	  String rootCauseString = Throwables.getStackTraceAsString(rootCause);
	  final byte[] bytes = rootCauseString.getBytes("UTF8");
	  persistence.storeException(bytes, status, endTime, key);
  }
  /**
   * 
   * @param jcas
   * @param trace
   * @param sequenceId
   * @return
   * @throws SQLException
   */
  private boolean loadCasFromStorage(JCas jcas, Trace trace, int sequenceId) throws SQLException 
  {
	  debug ("loadCasFromStorage (JCas jcas, Trace trace, int sequenceId)");
	  
	  ExperimentUUID experiment = ProcessingStepUtils.getCurrentExperiment(jcas);
	  String experimentId = experiment.getUuid();
	  int stageId = experiment.getStageId();
	  final String hash = getExecutionHash(experimentId, trace, sequenceId);
	  
	  CasDeserializer deserializer = persistence.deserialize(jcas, hash);
	  
	  if (deserializer.processedCas()) 
	  {
		  ExperimentUUID expUuid = new ExperimentUUID(jcas);
		  expUuid.setUuid(experimentId);
		  expUuid.setStageId(stageId);
		  expUuid.addToIndexes();
		  
		  debug (String.format("Loaded cas for %s @ %s\n", sequenceId, trace.getTrace()));
	  }
	  
	  return deserializer.processedCas();
  }
  /**
   * 
   * @param experimentId
   * @param trace
   * @param sequenceId
   * @return
   */
  private String getExecutionHash(String experimentId, final Trace trace, int sequenceId) 
  {
	  debug ("getExecutionHash ()");
	  
	  HashFunction hf = Hashing.md5();
	  Hasher hasher = hf.newHasher();
	  hasher.putString(experimentId);
	  hasher.putString(trace.getTrace());
	  hasher.putString(String.valueOf(sequenceId));
	  HashCode hash = hasher.hash();
	  final String traceHash = hash.toString();
	  return traceHash;
  }
  /**
   * WARNING: This is an incredibly naive way of obtaining the hostname. We
   * should add more elaborate code as soon as possible.
   * @return
   * @throws IOException
   */
  private String getHostName() throws IOException 
  {
	  debug ("getHostName ()");
	  
	  InetAddress addr = InetAddress.getLocalHost();
	  return addr.getHostName();
  }
  /**
   * 
   * @param options
   * @param c
   * @return
   */
  private AnalysisEngine[] loadOptions(String options, UimaContext c) 
  {
	  debug ("loadOptions ("+options+",UimaContext c)");
	  
    List<AnalysisEngineDescription> aeds = loadOptions(options);
    List<AnalysisEngine> aes = new ArrayList<AnalysisEngine>();
    
    for (AnalysisEngineDescription aeDesc : aeds) 
    {
      try 
      {
        aes.add(AnalysisEngineFactory.createAggregate(aeDesc));
      } 
      catch (ResourceInitializationException e) 
      {
        e.printStackTrace();
      }
    }
    
    return aes.toArray(new AnalysisEngine[0]);
  }
  /**
   * 
   * @param options
   * @return
   */
  List<AnalysisEngineDescription> loadOptions(String options) 
  {
	  debug ("loadOptions ("+options+")");
	  
	  List<AnalysisEngineDescription> aeds = new ArrayList<AnalysisEngineDescription>();
	  Yaml yaml = new Yaml();
	  @SuppressWarnings("unchecked")
	  List<Map<String, Object>> ao = (List<Map<String, Object>>) yaml.load(options);
	  
	  for (Map<String, Object> optionDescription : ao) 
	  {
		  loadOption(optionDescription, aeds);
	  }
	  
	  return aeds;
  }
  /**
   * 
   * @param description
   * @param aes
   */
  @SuppressWarnings("unchecked")
  void loadOption(Map<String, Object> description, List<AnalysisEngineDescription> aes) 
  {
	  debug ("loadOption ()");
	  
	  try 
	  {
		  Map.Entry<String, Object> first = getFirst(description);
		  HandleType type = HandleType.getInstance(first.getKey());
		  
		  if (type == HandleType.PIPELINE) 
		  {
			  List<Map<String, Object>> pipeline = (List<Map<String, Object>>) first.getValue();
			  List<AnalysisEngineDescription> options = createInnerPipeline(pipeline);
			  aes.addAll(options);
		  } 
		  else 
		  {
			  String resource = (String) first.getValue();
			  List<AnalysisEngineDescription> options = doLoadOptions(ResourceHandle.newHandle(type,resource));
			  aes.addAll(options);
		  }
	  } 
	  catch (Exception e) 
	  {
		  debug (String.format("Unable to load option %s caused by:\n", description));
		  
		  Throwable cause = e.getCause();
		  
		  if (cause != null) 
		  {
			  cause.printStackTrace();
		  } 
		  else 
		  {
			  e.printStackTrace();
		  }
	  }
  }
  /**
   * 
   * @param pipeline
   * @return
   * @throws ResourceInitializationException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  private List<AnalysisEngineDescription> createInnerPipeline(List<Map<String, Object>> pipeline) throws ResourceInitializationException, IOException 
  {
	  debug ("createInnerPipeline ()");
	  
	  List<AnalysisEngineDescription> options = Lists.newArrayList();
	  List<Set<AnalysisEngineDescription>> sets = Lists.newArrayList();
	  
	  for (Map<String, Object> map : pipeline) 
	  {
		  try 
		  {
			  Map.Entry<String, Object> me = getFirst(map);
			  String type = me.getKey();
			  Object o = me.getValue();
			  
			  if (o instanceof String) 
			  {
				  String component = ((String) o).trim();
				  ResourceHandle handle = ResourceHandle.newHandle(type, component);
				  Set<AnalysisEngineDescription> local = Sets.newLinkedHashSet(doLoadOptions(handle));
				  sets.add(local);
			  } 
			  else if (o instanceof Iterable) 
			  {
				  Iterable<Object> components = (Iterable<Object>) o;
				  Set<AnalysisEngineDescription> local = Sets.newLinkedHashSet();
				  
				  for (Object o2 : components) 
				  {
					  if (o2 instanceof Map) 
					  {
						  Map<String, Object> component = (Map<String, Object>) o2;
						  List<AnalysisEngineDescription> aes = new ArrayList<AnalysisEngineDescription>();
						  loadOption(component, aes);
						  local.addAll(aes);
					  } 
					  else 
					  {
						  throw new IllegalArgumentException("Illegal experiment descriptor, all options must be specified as a pair 'key: value'");
					  }
				  }
				  
				  sets.add(local);
			  } 
			  else 
			  {
				  throw new IllegalArgumentException("Illegal experiment descriptor, must contain either an iterable or a string");
			  }
		  } 
		  catch (Exception e) 
		  {
			  debug (String.format ("Unable to load option %s caused by:\n", map));
			  
			  Throwable cause = e.getCause();
			  
			  if (cause != null) 
			  {
				  cause.printStackTrace();
			  } 
			  else 
			  {
				  e.printStackTrace();
			  }
		  }
	  }
	  
    // AED equality is based on equality of the MetaDataObject attributes    
    Set<List<AnalysisEngineDescription>> product = Sets.cartesianProduct(sets);
    for (List<AnalysisEngineDescription> local : product) 
    {
      AggregateBuilder builder = new AggregateBuilder();
      
      List<String> names = Lists.newArrayList();
      
      for (AnalysisEngineDescription aeDesc : local) 
      {
        builder.add(aeDesc);
        names.add(aeDesc.getAnalysisEngineMetaData().getName());
      }
      
      String aeName = Joiner.on(";").join(names);
      
      AnalysisEngineDescription aee = builder.createAggregateDescription();
      
      aee.getAnalysisEngineMetaData().setName(String.format("pipeline:(%s)", aeName));
      
      options.add(aee);
    }
    
    return options;
  }
  /**
   * 
   * @param handle
   * @return
   * @throws Exception
   */
  private List<AnalysisEngineDescription> doLoadOptions(ResourceHandle handle) throws Exception 
  {
	  List<AnalysisEngineDescription> aes = Lists.newArrayList();
	  Map<String, Object> tuples = Maps.newLinkedHashMap();
	  Class<? extends AnalysisComponent> comp = BaseExperimentBuilder.loadFromClassOrInherit(handle,AnalysisComponent.class, tuples);
	  
	  AnyObject crossOpts = (AnyObject) tuples.remove(CROSS_OPTS_PARAM);
    
	  if (crossOpts == null) 
	  {
		  AnalysisEngineDescription aeDesc = BaseExperimentBuilder.createAnalysisEngineDescription(tuples, comp);
		  
		  aes.add(aeDesc);
	  } 
	  else 
	  {
		  Set<List<String>> product = doCartesianProduct(crossOpts);
		  
		  for (List<String> configuration : product) 
		  {
			  Map<String, Object> inner = Maps.newLinkedHashMap(tuples);
			  setInnerParams(configuration, inner);
			  AnalysisEngineDescription aeDesc = BaseExperimentBuilder.createAnalysisEngineDescription(inner, comp);
			  aes.add(aeDesc);
		  }
	  }
	  
	  return aes;
  }
  /**
   * 
   * @param crossOpts
   * @return
   */
  private Set<List<String>> doCartesianProduct(AnyObject crossOpts) 
  {
	  debug ("doCartesianProduct ()");
	  
	  List<Set<String>> sets = Lists.newArrayList();
    
	  for (AnyTuple tuple : crossOpts.getTuples()) 
	  {
		  Set<String> params = Sets.newHashSet();
		  String key = tuple.getKey();
		  @SuppressWarnings("unchecked")
		  Iterable<Object> values = (Iterable<Object>) tuple.getObject();
		  
		  for (Object o : values) 
		  {
			  params.add(String.format("%s||%s", key, o));
		  }
		  
		  sets.add(params);
	  }
	  
	  Set<List<String>> product = Sets.cartesianProduct(sets);
	  
	  return product;
  }
  /**
   * 
   * @param configuration
   * @param inner
   */
  private void setInnerParams(List<String> configuration, Map<String, Object> inner) 
  {
	  for (String param : configuration) 
	  {
		  String[] pair = param.split("\\|\\|");
		  inner.put(pair[0], pair[1]);
	  }
  }
  /**
   * 
   * @param map
   * @return
   */
  private Map.Entry<String, Object> getFirst(Map<String, Object> map) 
  {
    return Iterators.get(map.entrySet().iterator(), 0);
  }
  /**
   * 
   */
  @Override
  public String toString() 
  {
    return String.format("%s|%s>", getPhaseNo(), phaseName);
  }
  /**
   * 
   * @return
   */
  int size() 
  {
    return options.length;
  }
}
