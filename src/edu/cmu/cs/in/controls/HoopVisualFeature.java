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

package edu.cmu.cs.in.controls;

import java.awt.Color;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;

import edu.cmu.cs.in.base.HoopDataCollection;
import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopVisualFeature extends HoopRoot implements HoopVisualFeatureBase
{
	public String text="";
	public String classification="";
	public float intensity=(float) 0.1;
	private boolean isSelected=false;
	
	public int startFeatureMarker=0;
	public int endFeatureMarker=0;
				
	/**
	 *
	 */
    public HoopVisualFeature () 
    {
		setClassName ("HoopVisualFeature");
		//debug ("HoopVisualFeature ()");		
    }
	/**
	 *
	 */
    public HoopVisualFeature (String aText) 
    {
    	setClassName ("HoopVisualFeature");
		//debug ("HoopVisualFeature ()");
		text=aText;
    }   
	/**
	 *
	 */
   	public String getText ()
   	{
   		return text;
   	}
	/**
	 *
	 */
   	public void setText (String aText)
   	{
   		text=aText;
   	}
	/**
	 *
	 */
    public Color featureToClassification ()
    {
    	float inverse=(float) (1.0-intensity);
    	Color form=new Color (inverse,inverse,inverse);
    	return (form);
    }
	/**
	 *
	 */
    public Color featureToCoverage ()
    {
    	float conv=(float) 0.15;
	   
    	if (isSelected==true)
    		conv=(float) 1.0;
	   
   		float inverse=(float) (1.0-conv);
   		Color form=new Color (inverse,inverse,inverse);
   		
   		return (form);
    }    
	/**
	 *
	 */	
	public boolean isSelected()
	{
		return isSelected;
	}
	/**
	 *
	 */	
	public void setSelected (boolean isSelected)
	{
		this.isSelected = isSelected;
	}
	/**
	 *
	 */	
	public String toString()
	{
		return text;
	}
	/**
	 *
	 */	
	public Boolean isPronoun ()
	{
		for (int i=0;i<HoopDataCollection.pronouns.size();i++)
		{
			if (text.equals(HoopDataCollection.pronouns.get(i)))
				return (true);
		}
		
		return (false);
	}
	/**
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
                	 //debug ("Processing feature attribute name: " + attrib.getValue ());
                	 text=attrib.getValue();
                 }
                 
                 if (attrib.getName ().equals ("selected")==true)
                 {
                	 //debug ("Processing feature attribute selected: " + attrib.getValue ());
                	 
                	 if (attrib.getValue().equals("true")==true)
                		 setSelected (true);
                	 else
                		 setSelected (false);
                 }                 
             }    
		 }	
	}
	/**
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
		formatter.append("\" />");
		
		return (formatter.toString());
	}	
}
