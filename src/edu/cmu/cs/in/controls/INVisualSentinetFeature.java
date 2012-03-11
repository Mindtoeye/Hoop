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

import java.text.DecimalFormat;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class INVisualSentinetFeature extends INVisualFeature implements INVisualFeatureBase
{	
	private float posSentiment=(float) 0.0;
	private float negSentiment=(float) 0.0;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
    public INVisualSentinetFeature () 
    {
		setClassName ("INVisualSentinetFeature");
		//debug ("INVisualSentinetFeature ()");		
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public INVisualSentinetFeature (String aText) 
    {
		setClassName ("INVisualSentinetFeature");
		//debug ("INVisualSentinetFeature ()");
		
		text=aText;
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public Boolean isPositive ()
    {
    	if (posSentiment>negSentiment)
    		return (true);
    	
    	return (false);
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public Boolean isNegative ()
    {
    	if (posSentiment<negSentiment)
    		return (true);
   	
    	return (false);
    }    
	/**------------------------------------------------------------------------------------
	 *
	 */
    public INVisualSentinetFeature (String aText,float aPos,float aNeg) 
    {
		setClassName ("INVisualSentinetFeature ("+aText+","+aPos+","+aNeg+")");
		//debug ("INVisualSentinetFeature ()");
		
		text=aText;
		posSentiment=aPos;
		negSentiment=aNeg;
    }     
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public String toString()
	{
		StringBuffer formatter=new StringBuffer ();
		formatter.append(text);
		formatter.append (" p(");
		formatter.append(new DecimalFormat("#.##").format(posSentiment));
		formatter.append (") n(");
		formatter.append(new DecimalFormat("#.##").format(negSentiment));
		formatter.append(")");
		return formatter.toString();
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void setPosSentiment(float posSentiment) 
	{
		this.posSentiment = posSentiment;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public float getPosSentiment() 
	{
		return posSentiment;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void setNegSentiment(float negSentiment) 
	{
		this.negSentiment = negSentiment;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public float getNegSentiment() 
	{
		return negSentiment;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void fromXML (Node aNode)
	{
		 if (aNode.getNodeName ().equals ("feature")==true)
		 {
	    	 NamedNodeMap attr=(NamedNodeMap) aNode.getAttributes();
            for (int t=0;t<attr.getLength();t++) 
            {
                Attr attrib=(Attr)attr.item(t);
                if (attrib.getName ().equals ("name")==true)
                {
               	 debug ("Processing feature: " + attrib.getValue ());                                    	 
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
		formatter.append("<feature name=\"");
		formatter.append(text);
		formatter.append("\" selected=\"");
		if (isSelected()==true)
			formatter.append("true");
		else
			formatter.append("false");		
		formatter.append ("\" pos=\"");
		formatter.append(new DecimalFormat("#.##").format(posSentiment));
		formatter.append ("\" neg=\"");
		formatter.append(new DecimalFormat("#.##").format(negSentiment));
		formatter.append("\" />");
		return (formatter.toString());
	}	
	//-------------------------------------------------------------------------------------	
}
