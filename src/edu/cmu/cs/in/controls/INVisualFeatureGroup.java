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

import edu.cmu.cs.in.controls.INVisualFeature;

public class INVisualFeatureGroup extends INVisualFeature
{	
	private ArrayList <INVisualFeature> features=null;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
    public INVisualFeatureGroup () 
    {
		setClassName ("INVisualFeatureGroup");
		//debug ("INVisualFeatureGroup ()");
		setFeatures(new ArrayList<INVisualFeature> ());
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public INVisualFeatureGroup (String aText) 
    {
		setClassName ("INVisualFeatureGroup");
		//debug ("INVisualFeatureGroup ()");
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
	//-------------------------------------------------------------------------------------	
}
