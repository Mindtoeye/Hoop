/**  
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

package edu.cmu.cs.in.hoop.hoops.analyze;

import static org.uimafit.factory.AnalysisEngineFactory.createPrimitive;
import static org.uimafit.factory.JCasFactory.createJCas;
import static org.uimafit.factory.TypeSystemDescriptionFactory.createTypeSystemDescription;

//import java.util.ArrayList;
//import java.util.regex.Matcher;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import edu.cmu.cs.in.base.io.HoopClassLoader;
//import edu.cmu.cs.in.base.kv.HoopKV;
//import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopAnalyze;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.load.HoopUIMAConfigurationLoadBase;

public class HoopGenericAnnotator extends HoopAnalyze implements HoopInterface{
	
	private static final long serialVersionUID = -5592278513446875291L;
	
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
	        
	        HoopClassLoader classLoader = new HoopClassLoader();
	        Class c = classLoader.loadClass(HoopUIMAConfigurationLoadBase.aePath, null);
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

// Different version originally on server. Need to figure out which one
// to use

/**
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

import edu.cmu.cs.in.base.io.HoopClassLoader;
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
	
	public Boolean runHoop (HoopBase inHoop)
	{		
		
        TypeSystemDescription tsd = createTypeSystemDescription(HoopUIMAConfigurationLoadBase.tsdPath);
        JCas jCas;
		try {
			jCas = createJCas(tsd);
	        jCas.setDocumentText(inHoop.getjCasList().get(0).getDocumentText());        
	        getjCasList().add(0, jCas);
	        
	        HoopClassLoader classLoader = new HoopClassLoader();
	        Class c = classLoader.loadClass(HoopUIMAConfigurationLoadBase.aePath, null);
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

	public HoopBase copy ()
	{
		return (new HoopGenericAnnotator ());
	}	
	
}
*/

