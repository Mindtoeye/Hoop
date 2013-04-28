/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as 
 *  published by the Free Software Foundation, either version 3 of the 
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package edu.cmu.cs.in.hoop.hoops.task;

//import static java.util.Arrays.asList;
import static org.uimafit.factory.AnalysisEngineFactory.createAggregate;
import static org.uimafit.factory.AnalysisEngineFactory.createAggregateDescription;
//import static org.uimafit.factory.CollectionReaderFactory.createCollectionReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
//import org.apache.uima.collection.CollectionReader;
//import org.apache.uima.collection.CollectionReaderDescription;
//import org.apache.uima.collection.base_cpm.BaseCollectionReader;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.metadata.ResourceMetaData;
//import org.apache.uima.util.CasCreationUtils;

import edu.cmu.cs.in.base.HoopRoot;

/** 
 * Based on the original UIMA version with an extensive rewrite for Hoop
 * 
 * Original note: We added this file here to bring uimaFit 1.3.1 bug improved 
 * pipeline. while preserving compatibility with UIMA 2.3.1
 * 
 *  Elmer
 */
public class HoopUIMAPipeline extends HoopRoot
{
	private ArrayList <JCas> jCasList = null;
	
	public HoopUIMAPipeline(ArrayList <JCas> aCasList) 
	{
		setClassName ("HoopUIMAPipeline");
		debug ("HoopUIMAPipeline ()");		
		
		jCasList=aCasList;
	}
	/**
	 * Run the CollectionReader and AnalysisEngines as a pipeline. After processing all CASes
	 * provided by the reader, the method calls {@link AnalysisEngine#collectionProcessComplete()
	 * collectionProcessComplete()} on the engines and {@link Resource#destroy() destroy()} on all
	 * engines.
	 *
	 * @param reader
	 *           The CollectionReader that loads the documents into the CAS.
	 * @param descs
	 *            Primitive AnalysisEngineDescriptions that process the CAS, in order. If you have a
	 *            mix of primitive and aggregate engines, then please create the AnalysisEngines
	 *            yourself and call the other runPipeline method.
	 * @throws UIMAException
	 * @throws IOException
	 */
	public void runPipeline (AnalysisEngineDescription... descs) throws UIMAException, IOException 
	{
		debug ("runPipeline ()");
		
		// Create AAE
		final AnalysisEngineDescription aaeDesc = createAggregateDescription(descs);

		// Instantiate AAE
		final AnalysisEngine aae = createAggregate(aaeDesc);

		/*
		// Create 1 CAS from merged metadata
		final CAS cas = CasCreationUtils.createCas(asList(reader.getMetaData(), aae.getMetaData()));
    
		try 
		{
			// Process and pass the CAS along the pipeline ...
			while (reader.hasNext()) 
			{
				reader.getNext(cas);
				aae.process(cas);
				cas.reset();
			}
			
			// Signal end of processing
			aae.collectionProcessComplete();
		}
		finally 
		{
			// Destroy
			aae.destroy();
			destroy(reader);
		}
		*/
		
		for (int i=0;i<jCasList.size();i++)
		{
			CAS cas=(CAS) jCasList.get(i);
			
			aae.process(cas);
			
			cas.reset();	
		}
		
		aae.collectionProcessComplete();
	}
	/**
	 * Run the CollectionReader and AnalysisEngines as a pipeline. After processing all CASes
	 * provided by the reader, the method calls {@link AnalysisEngine#collectionProcessComplete()
	 * collectionProcessComplete()} on the engines, {@link CollectionReader#close() close()} on the
	 * reader and {@link Resource#destroy() destroy()} on the reader and all engines.
	 *
	 * @param readerDesc
	 *            The CollectionReader that loads the documents into the CAS.
	 * @param descs
	 *            Primitive AnalysisEngineDescriptions that process the CAS, in order. If you have a
	 *            mix of primitive and aggregate engines, then please create the AnalysisEngines
	 *            yourself and call the other runPipeline method.
	 * @throws UIMAException
	 * @throws IOException
	 */
	/*
	public void runPipeline (final CollectionReaderDescription readerDesc,
			            	 final AnalysisEngineDescription... descs) throws UIMAException, IOException 
	{
		debug ("runPipeline (CollectionReaderDescription,AnalysisEngineDescription)");
		
		// Create the components
		final CollectionReader reader = createCollectionReader(readerDesc);

		try 
		{
			// Run the pipeline
			runPipeline(reader, descs);
		}
		finally 
		{
			close(reader);
			destroy(reader);
		}
	}
	*/
	/**
	 * Provides a simple way to run a pipeline for a given collection reader and sequence of
	 * analysis engines. After processing all CASes provided by the reader, the method calls
	 * {@link AnalysisEngine#collectionProcessComplete() collectionProcessComplete()} on the
	 * engines.
	 *
	 * @param reader
	 *            a collection reader
	 * @param engines
	 *            a sequence of analysis engines
	 * @throws UIMAException
	 * @throws IOException
	 */
	public void runPipeline(AnalysisEngine... engines) throws UIMAException, IOException 
	{
		debug ("runPipeline (CollectionReader,AnalysisEngine) MAIN");
		
		final List<ResourceMetaData> metaData = new ArrayList<ResourceMetaData>();
		
		//metaData.add(reader.getMetaData());
		
		for (AnalysisEngine engine : engines)
		{
			metaData.add(engine.getMetaData());
		}

		/*
		final CAS cas = CasCreationUtils.createCas(metaData);
		
		try 
		{
			while (reader.hasNext()) 
			{
				reader.getNext(cas);
				runPipeline(cas, engines);
				cas.reset();
			}
		} 
		finally 
		{
			collectionProcessComplete(engines);
			destroy(reader);
		}
		*/
		
		for (int i=0;i<jCasList.size();i++)
		{
			CAS cas=(CAS) jCasList.get(i);
			
			runPipeline(cas, engines);
			
			cas.reset();
		}
		
		collectionProcessComplete(engines);
	}
	/**
	 * Run a sequence of {@link AnalysisEngine analysis engines} over a {@link JCas}. The result of
	 * the analysis can be read from the JCas.
	 *
	 * @param aCas
	 *            the CAS to process
	 * @param aDescs
	 *            a sequence of analysis engines to run on the jCas
	 * @throws UIMAException
	 * @throws IOException
	 */
	public void runPipeline(CAS aCas,AnalysisEngineDescription... aDescs) throws UIMAException, IOException 
	{
		debug ("runPipeline (aCas,AnalysisEngineDescription)");
		
		// Create aggregate AE
		final AnalysisEngineDescription aaeDesc = createAggregateDescription(aDescs);

		// Instantiate
		final AnalysisEngine aae = createAggregate(aaeDesc);
		
		try 
		{
			// Process
			aae.process(aCas);
      
			// Signal end of processing
			aae.collectionProcessComplete();
		}
		finally 
		{
			// Destroy
			aae.destroy();
		}
	}
	/**
	 * Run a sequence of {@link AnalysisEngine analysis engines} over a {@link JCas}. The result of
	 * the analysis can be read from the JCas.
	 *
	 * @param jCas
	 *            the jCas to process
	 * @param descs
	 *            a sequence of analysis engines to run on the jCas
	 * @throws UIMAException
	 * @throws IOException
	 */
	public void runPipeline(JCas jCas,AnalysisEngineDescription... descs) throws UIMAException, IOException 
	{
		debug ("runPipeline (JCas,AnalysisEngineDescription)");
		
		runPipeline(jCas.getCas(), descs);
	}
	/**
	 * Run a sequence of {@link AnalysisEngine analysis engines} over a {@link JCas}. This method
	 * does not {@link AnalysisEngine#destroy() destroy} the engines or send them other events like
	 * {@link AnalysisEngine#collectionProcessComplete()}. This is left to the caller.
	 *
	 * @param jCas
	 *            the jCas to process
	 * @param engines
	 *            a sequence of analysis engines to run on the jCas
	 * @throws UIMAException
	 * @throws IOException
	 */
	public void runPipeline(JCas jCas,AnalysisEngine... engines) throws UIMAException, IOException 
	{
		debug ("runPipeline (JCas,AnalysisEngine)");
		
		for (AnalysisEngine engine : engines) 
		{
			engine.process(jCas);
		}
	}
	/**
	 * Run a sequence of {@link AnalysisEngine analysis engines} over a {@link CAS}. This method
	 * does not {@link AnalysisEngine#destroy() destroy} the engines or send them other events like
	 * {@link AnalysisEngine#collectionProcessComplete()}. This is left to the caller.
	 *
	 * @param cas
	 *            the CAS to process
	 * @param engines
	 *            a sequence of analysis engines to run on the jCas
	 * @throws UIMAException
	 * @throws IOException
	 */
	public void runPipeline(CAS cas,AnalysisEngine... engines)throws UIMAException, IOException 
	{
		debug ("runPipeline (CAS,AnalysisEngine)");
		
		for (AnalysisEngine engine : engines) 
		{
			engine.process(cas);
		}
	}
	/**
	 * Notify a set of {@link AnalysisEngine analysis engines} that the collection process is complete.
	 */
	public void collectionProcessComplete(AnalysisEngine... engines) throws AnalysisEngineProcessException 
	{
		debug ("collectionProcessComplete (AnalysisEngine)");
		
		for (AnalysisEngine e : engines) 
		{
			e.collectionProcessComplete();
		}
	}
	/**
	 * Destroy a set of {@link Resource resources}.
	 */
	public void destroy(Resource... resources)
	{
		debug ("destroy (Resource)");
		
		for (Resource r : resources) 
		{
			if (r != null) 
			{
				r.destroy();
			}
		}
	}
	/**
	 * 
	 * @param aReader
	 */
	/*
	private void close(BaseCollectionReader aReader)
	{
		debug ("close (BaseCollectionReader)");
		
		if (aReader == null) 
		{
			return;
		}
    
		try 
		{
			aReader.close();
		}
		catch (IOException e) 
		{
			// Ignore.
		}
	}
	*/
}
