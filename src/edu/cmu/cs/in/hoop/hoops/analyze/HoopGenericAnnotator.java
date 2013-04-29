package edu.cmu.cs.in.hoop.hoops.analyze;

import static org.uimafit.factory.AnalysisEngineFactory.createPrimitive;
import static org.uimafit.factory.JCasFactory.createJCas;
import static org.uimafit.factory.TypeSystemDescriptionFactory.createTypeSystemDescription;

import java.util.ArrayList;
import java.util.regex.Matcher;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopAnalyze;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.load.HoopUIMAConfigurationLoadBase;

public class HoopGenericAnnotator extends HoopAnalyze implements HoopInterface{
	
    public HoopGenericAnnotator () 
    {
		setClassName ("HoopGenericAnnotator");
		debug ("HoopGenericAnnotator ()");
								
		setHoopDescription ("Generic Annotator");				
    }    
	
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		
        TypeSystemDescription tsd = createTypeSystemDescription(HoopUIMAConfigurationLoadBase.tsdPath);
        JCas jCas;
		try {
			jCas = createJCas(tsd);
	        jCas.setDocumentText(inHoop.getjCasList().get(0).getDocumentText());        
	        getjCasList().add(0, jCas);
	        Class c = Class.forName(HoopUIMAConfigurationLoadBase.aePath);
	        AnalysisEngine analysisEngine = createPrimitive(c, tsd);
	        analysisEngine.process(jCas);
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopGenericAnnotator ());
	}	
	
}
