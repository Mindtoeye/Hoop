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

import java.io.IOException;
import java.util.List;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.Progress;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import mx.bigdata.anyobject.AnyObject;

import com.google.common.collect.Lists;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopControlBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;
import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.ExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.config.Stage;
import edu.cmu.lti.oaqa.ecd.config.StagedConfiguration;
import edu.cmu.lti.oaqa.ecd.config.StagedConfigurationImpl;
import edu.cmu.lti.oaqa.ecd.flow.FunneledFlow;
import edu.cmu.lti.oaqa.ecd.flow.strategy.FunnelingStrategy;
import edu.cmu.lti.oaqa.ecd.impl.DefaultFunnelingStrategy;

/**
* 
*/
public class HoopUIMACSE extends HoopControlBase implements HoopInterface
{    					
	private static final long serialVersionUID = 5343404003031040810L;
	
	// CSE Specific ...
	
	private ExperimentBuilder builder=null;
	private AnyObject config=null;
	private List<Long> processedItems = Lists.newArrayList();
	
	// Generic ...
	
	public HoopURISerializable URI=null;	
	
	/**
	 *
	 */
    public HoopUIMACSE () 
    {
		setClassName ("HoopUIMACSE");
		debug ("HoopUIMACSE ()");
										
		setHoopDescription ("Run a UIMA CSE");
		
		URI=new HoopURISerializable (this,"URI","");
		URI.setFileExtension(".yaml");
    }
    /**
     * 
     */
    public void reset ()
    {
    	debug ("reset ()");
    	
    	super.reset();
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		HoopUIMAPipeline CSEPipeline=new HoopUIMAPipeline();
		
		try 
		{
			initExperiment ();
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
			return (false);
		}
		
		StagedConfiguration stagedConfig = new StagedConfigurationImpl(config);
		FunnelingStrategy ps;
		
		try 
		{
			ps = getProcessingStrategy();
		} 
		catch (ResourceInitializationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}
		
		debug ("For each staged config, get a stage ...");
    
		for (Stage stage : stagedConfig) 
		{
			debug ("Processing stage ...");
			
			FunneledFlow funnel = ps.newFunnelStrategy(builder.getExperimentUuid());
			AnyObject conf = stage.getConfiguration();
			
			CollectionReader reader=null;
			
			debug ("Attempting to build collection reader ...");
			
			try 
			{
				reader = builder.buildCollectionReader(conf, stage.getId());
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return (false);
			}
			
			AnalysisEngine pipeline=null;
			
			debug ("Building main pipeline ...");
			
			try 
			{
				pipeline = builder.buildPipeline(conf, "pipeline", stage.getId(), funnel);
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return (false);
			}
			
			debug ("Building post-process pipeline ...");
      
			if (conf.getIterable("post-process") != null) 
			{
				AnalysisEngine post;
				
				try 
				{
					post = builder.buildPipeline(conf, "post-process", stage.getId());
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return (false);
				}
				
				try 
				{
					CSEPipeline.runPipeline(reader, pipeline, post);
				} 
				catch (UIMAException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return (false);
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return (false);
				}
			} 
			else 
			{
				debug ("Attempting to execute pipeline ...");
				
				try 
				{
					CSEPipeline.runPipeline(reader, pipeline);
				} 
				catch (UIMAException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return (false);
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return (false);
				}
			}
      
			Progress progress = reader.getProgress()[0];
			
			long total = progress.getCompleted();
			
			processedItems.add(total);
		}		
						
		return (true);
	}	
	/**
	 * 
	 */
	private void initExperiment () throws Exception 
	{
		debug ("initExperiment ()");
		
		TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
	    
		this.builder = new BaseExperimentBuilder(HoopLink.runner.getExperimentID(), URI.getValue(), typeSystem);
    
		this.config = builder.getConfiguration();
	}
	/**
	 *
	 * @return
	 * @throws ResourceInitializationException
	 */
	private FunnelingStrategy getProcessingStrategy() throws ResourceInitializationException 
	{
		debug ("getProcessingStrategy ()");
		
		FunnelingStrategy ps = new DefaultFunnelingStrategy();
		
		AnyObject map = config.getAnyObject("processing-strategy");
		if (map != null) 
		{
			ps = BaseExperimentBuilder.loadProvider(map, FunnelingStrategy.class);
		}
		
		return ps;
	}
	/**
	 *
	 * @return
	 */
	Iterable<Long> getProcessedItems() 
	{
		return processedItems;
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopUIMACSE ());
	}	
}
