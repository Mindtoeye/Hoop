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

import java.awt.Color;
import java.util.HashMap;
//import java.util.HashSet;

import edu.cmu.cs.in.base.INFeatureMatrixBase;

public class INVisualClass extends INFeatureMatrixBase
{
	private String name="";
	private int classIndex=0;
	private HashMap <String,INVisualFeature>uniqueFeatures=null;
	private Color color=Color.BLACK;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
    public INVisualClass () 
    {
		setClassName ("INVisualClass");
		//debug ("INVisualClass ()");	
		uniqueFeatures=new HashMap<String,INVisualFeature> ();
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public void addFeature (INVisualFeature aFeature)
    {
    	//debug ("addFeature ("+aFeature.text+")");
    	
    	uniqueFeatures.put(aFeature.text,aFeature);
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public HashMap<String,INVisualFeature> getFeatures ()
    {
    	return (uniqueFeatures);
    }
	/**------------------------------------------------------------------------------------
	 *
	 */    
	public void setName(String name) 
	{
		this.name=name;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public String getName() 
	{
		return name;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void setClassIndex(int classIndex) 
	{
		this.classIndex=classIndex;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public int getClassIndex() 
	{
		return classIndex;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */		
	public void setColor(Color color) 
	{
		this.color = color;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public Color getColor() 
	{
		return color;
	}
	//-------------------------------------------------------------------------------------	
}
