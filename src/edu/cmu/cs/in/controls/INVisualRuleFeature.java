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

//import java.text.DecimalFormat;

import java.util.ArrayList;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import edu.cmu.cs.in.base.INSimpleFeatureMaker;

public class INVisualRuleFeature extends INVisualFeature implements INVisualFeatureBase
{		
	private String description="undefined";
	private String example="";
	private String breakTerm="";
	private ArrayList <String> breakFeatures=null;
	private float frequency=(float) 0.0;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
    public INVisualRuleFeature () 
    {
		setClassName ("INVisualRuleFeature");
		//debug ("INVisualRuleFeature ()");
		breakFeatures=new ArrayList<String> ();
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public INVisualRuleFeature (String aText) 
    {
		setClassName ("INVisualRuleFeature");
		//debug ("INVisualRuleFeature ()");
		
		text=aText;
		
		breakFeatures=new ArrayList<String> ();		
    }     
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public String toString()
	{
		StringBuffer formatted=new StringBuffer ();
		formatted.append (description);
		formatted.append (" : (");
		formatted.append (example);
		formatted.append (")");
		
		return formatted.toString();
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void fromXML (Node aNode)
	{
		 if (aNode.getNodeName ().equals ("rule")==true)
		 {
	    	 NamedNodeMap attr=(NamedNodeMap) aNode.getAttributes();
             for (int t=0;t<attr.getLength();t++) 
             {
                 Attr attrib=(Attr)attr.item(t);
                 
                 if (attrib.getName ().equals ("name")==true)
                 {
                	 debug ("Processing feature attribute name: " + attrib.getValue ());
                	 description=attrib.getValue();
                 }
                 
                 if (attrib.getName ().equals ("example")==true)
                 {
                	 debug ("Processing feature attribute example: " + attrib.getValue ());
                	 example=attrib.getValue();
                 }    
                 
                 if (attrib.getName ().equals ("breakterm")==true)
                 {
                	 debug ("Processing feature attribute breakterm: " + attrib.getValue ());
                	 breakTerm=attrib.getValue();
                 }                   
                 
                 if (attrib.getName ().equals ("frequency")==true)
                 {
                	 debug ("Processing feature attribute frequency: " + attrib.getValue ());
                	 frequency=new Float (attrib.getValue());
                 }                 
                 
                 if (attrib.getName ().equals ("selected")==true)
                 {
                	 debug ("Processing feature attribute selected: " + attrib.getValue ());
                	 
                	 if (attrib.getValue().equals("true")==true)
                		 setSelected (true);
                	 else
                		 setSelected (false);
                 }                 
             }    
		 }	
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */
	public String toXML ()
	{
		StringBuffer formatter=new StringBuffer ();
		formatter.append("<rule name=\"");
		formatter.append(description);
		formatter.append("\" selected=\"");
		if (isSelected()==true)
			formatter.append("true");
		else
			formatter.append("false");
		formatter.append("\" example=\"");
		formatter.append(example);
		formatter.append("\" breakterm=\"");
		formatter.append(breakTerm);		
		formatter.append("\" frequency=\"");
		formatter.append(String.valueOf(frequency));
		formatter.append("\" />");
		return (formatter.toString());
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */
	public INVisualRuleFeature clone ()
	{
		INVisualRuleFeature newRule=new INVisualRuleFeature ();
		newRule.setDescription(getDescription());
		newRule.setExample(getExample());
		newRule.setFrequency(getFrequency());
		return (newRule);
	}
	/**------------------------------------------------------------------------------------
	 *
	 */		
	public void setDescription(String description) 
	{
		this.text=description;
		this.description=description;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public String getDescription() 
	{
		return description;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */		
	public void setExample(String example) 
	{
		this.example = example;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public String getExample() 
	{
		return example;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */		
	public void setFrequency(float frequency) 
	{
		this.frequency = frequency;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public float getFrequency() 
	{
		return frequency;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */		
	public void setBreakTerm(String breakTerm) 
	{
		debug ("setBreakTerm ()");
		
		this.breakTerm = breakTerm;
		
		INSimpleFeatureMaker maker=new INSimpleFeatureMaker ();
		breakFeatures=(ArrayList<String>) maker.unigramTokenize(breakTerm);
		
		debug ("Generated " + breakFeatures.size() + " break features");
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public String getBreakTerm() 
	{
		return breakTerm;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public ArrayList <String> getBreakFeatures () 
	{
		return breakFeatures;
	}	
	//-------------------------------------------------------------------------------------	
}