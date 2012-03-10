/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
 * 
 */

package edu.cmu.cs.in.controls;

import java.util.ArrayList;

import edu.cmu.cs.in.base.INFeatureMatrixBase;

public class INFeaturePerspective  extends INFeatureMatrixBase
{		
	private ArrayList <INVisualFeature> features=null;
	private String perspectiveName="";
	
	/**------------------------------------------------------------------------------------
	 *
	 */
	public INFeaturePerspective () 
	{
		setClassName ("INFeaturePerspective");
		//debug ("INFeaturePerspective ()");	
		
		setFeatures(new ArrayList<INVisualFeature> ());		
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public INFeaturePerspective (String aName) 
	{
		setClassName ("INFeaturePerspective");
		//debug ("INFeaturePerspective ()");	
		
		setPerspectiveName (aName);
		setFeatures(new ArrayList<INVisualFeature> ());				
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void setFeatures(ArrayList <INVisualFeature> features) 
	{
		this.features = features;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public ArrayList <INVisualFeature> getFeatures() 
	{
		return features;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public INVisualFeature getFeature (String aFeature) 
	{
		return (features.get(features.indexOf(aFeature)));
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */		
	public void setPerspectiveName(String perspectiveName) 
	{
		this.perspectiveName = perspectiveName;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public String getPerspectiveName() 
	{
		return perspectiveName;
	}
	//-------------------------------------------------------------------------------------	
}

