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

package edu.cmu.cs.in.hoop.properties;

import java.util.ArrayList;

import javax.swing.JCheckBox;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.cmu.cs.in.base.INXMLBase;
import edu.cmu.cs.in.hoop.properties.INHoopStyleProperty;
//import edu.cmu.cs.in.hoop.properties.INHoopParameter;

/**
*
*/
public class INHoopInspectable extends INXMLBase
{	
	private float x=0;
	private float y=0;
	private float width=0;
	private float height=0;
	
	private int previewX=0;
	private int previewY=0;
	private int previewWidth=0;
	private int previewHeight=0;
	
	private Boolean selected=false;
	
	//private ArrayList <INHoopParameter>parameters=null;
	private ArrayList <INHoopStyleProperty>styles=null;
	
	private JCheckBox checker=null;
	
	/**
	 *
	 */
    public INHoopInspectable () 
    {
    	setClassName ("INHoopInspectable");
    	setClassType ("INHoopInspectable");
    	debug ("INHoopComponent ()");
    	
    	//SAIs=new ArrayList<INHoopSAI> ();
    	//parameters=new ArrayList<INHoopParameter> ();
    	styles=new ArrayList<INHoopStyleProperty> ();
    }
	/**
	 *
	 */    
    public void setChecker (JCheckBox aChecker)
    {
    	checker=aChecker;
    }
	/**
	 *
	 */    
    public JCheckBox getChecker ()
    {
    	return (checker);
    }    
	/**
	 *
	 */	
    /*
	public ArrayList <INHoopSAI> getSAIs ()
	{
		return (SAIs);
	}
	*/
	/**
	 *
	 */
	public void setSelected(Boolean aValue) 
	{
		this.selected=aValue;
	}
	/**
	 *
	 */
	public Boolean getSelected() 
	{
		return selected;
	}
	/**
	 *
	 */
	public void setPreviewDimensions (int aX,int aY,int aWidth,int aHeight)
	{
		previewX=aX;
		previewY=aY;
		previewWidth=aWidth;
		previewHeight=aHeight;
	}
	/**
	 *
	 */	
	public void setPreviewX(int previewX) 
	{
		this.previewX = previewX;
	}
	/**
	 *
	 */	
	public int getPreviewX() 
	{
		return previewX;
	}
	/**
	 *
	 */	
	public void setPreviewY(int previewY) 
	{
		this.previewY = previewY;
	}
	/**
	 *
	 */	
	public int getPreviewY() 
	{
		return previewY;
	}
	/**
	 *
	 */	
	public void setPreviewWidth(int previewWidth) 
	{
		this.previewWidth = previewWidth;
	}
	/**
	 *
	 */	
	public int getPreviewWidth() 
	{
		return previewWidth;
	}
	/**
	 *
	 */	
	public void setPreviewHeight(int previewHeight) 
	{
		this.previewHeight = previewHeight;
	}
	/**
	 *
	 */	
	public int getPreviewHeight() 
	{
		return previewHeight;
	}	
	/**
	 *
	 */	
	public void setX(String aValue) 
	{
		debug ("setX ("+aValue+")");
		this.x =Float.parseFloat (aValue);
		debug ("this.x: " + this.x);
	}
	/**
	 *
	 */	
	public void setX(float aValue) 
	{
		this.x=aValue;
	}
	/**
	 *
	 */	
	public float getX() 
	{
		return (this.x);
	}	
	/**
	 *
	 */	
	public void setY(String aValue) 
	{
		debug ("setY ("+aValue+")");
		this.y =Float.parseFloat (aValue);
		debug ("this.y: " + this.y);
	}	
	/**
	 *
	 */	
	public void setY(float aValue) 
	{
		this.y=aValue;
	}
	/**
	 *
	 */	
	public float getY() 
	{
		return (this.y);
	}	
	/**
	 *
	 */	
	public void setWidth(String aValue) 
	{
		debug ("setWidth ("+aValue+")");
		this.width =Float.parseFloat (aValue);
		debug ("this.width: " + this.width);
	}
	/**
	 *
	 */	
	public void setWidth(float aValue) 
	{
		this.width=aValue;
	}
	/**
	 *
	 */	
	public float getWidth() 
	{
		return (this.width);
	}	
	/**
	 *
	 */	
	public void setHeight(String aValue) 
	{
		debug ("setHeight ("+aValue+")");
		this.height =Float.parseFloat (aValue);
		debug ("this.height: " + this.height);
	}	
	/**
	 *
	 */	
	public void setHeight(float aValue) 
	{
		this.height=aValue;
	}	
	/**
	 *
	 */	
	public float getHeight() 
	{
		return (this.height);
	}	
	/**
	 *
	 */
	/*
	public ArrayList<INHoopParameter> getParameters ()
	{
		return (parameters);
	}
	*/
	/**
	 *
	 */
	public ArrayList<INHoopStyleProperty> getStyleProperties ()
	{
		return (styles);
	}	
	/**
	 *
	 */	
	/*
	public String getAllComponentParametersXML (Boolean isTouched)
	{
		StringBuffer buffer=new StringBuffer();
		
		if (isTouched==true)
		{
			// Only send those items that have been changed ...
			
			for (int i=0;i<parameters.size();i++)
			{
				INHoopParameter parameter=parameters.get(i);
			
				if (parameter.getTouched()==true)
				{
					buffer.append("<selection>");
					buffer.append(parameter.toString());
					buffer.append("</selection>");
				}	
			}
		}
		else
		{
			for (int i=0;i<parameters.size();i++)
			{
				INHoopParameter parameter=parameters.get(i);
			
				buffer.append("<selection>");
				buffer.append(parameter.toString());
				buffer.append("</selection>");
			}			
		}
		
		return (buffer.toString ());		
	}
	*/
	/**
	 *
	 */	
	public String getAllStylePropertiesXML (boolean isTouched)
	{
		StringBuffer buffer=new StringBuffer();
		
		if (isTouched==true)
		{
			// Only send those items that have been changed ...
			
			for (int i=0;i<styles.size();i++)
			{
				INHoopStyleProperty style=styles.get(i);
				
				if (style.getTouched()==true)
				{
					buffer.append("<selection>");
					buffer.append(style.toString());
					buffer.append("</selection>");
				}	
			}
		}
		else
		{
			for (int i=0;i<styles.size();i++)
			{
				INHoopStyleProperty style=styles.get(i);
			
				buffer.append("<selection>");
				buffer.append(style.toString());
				buffer.append("</selection>");
			}			
		}
		
		return (buffer.toString ());		
	}
	/**
	*	
	*/		
	public String toXML()
	{
		debug ("toXML ()");
		
		StringBuffer buffer=new StringBuffer();
				
		buffer.append(getClassOpen (getInstanceName ()));

		/*
		buffer.append("<Parameters>");
		buffer.append(getAllComponentParametersXML (false));
		buffer.append("</Parameters>");
		*/
		
		buffer.append("<Styles>");
		buffer.append(getAllStylePropertiesXML (false));
		buffer.append("</Styles>");		
		buffer.append(getClassClose ());
		
		return (buffer.toString ());
	} 	
	/**
	*	
	*/		
	public String toStringTouched()
	{
		debug ("toStringTouched ()");
		
		StringBuffer buffer=new StringBuffer();
				
		buffer.append(getClassOpen (getInstanceName ()));

		/*
		buffer.append("<Parameters>");
		buffer.append(getAllComponentParametersXML (true));
		buffer.append("</Parameters>");
		*/
		
		buffer.append("<Styles>");
		buffer.append(getAllStylePropertiesXML (true));
		buffer.append("</Styles>");
		buffer.append(getClassClose ());
		
		return (buffer.toString ());
	} 		
	/**
	 * @return 
	 *
	 */
    public Boolean fromXML (Element anElement)
    {
    	debug ("fromXML ("+anElement.getNodeName ()+")");
    	
    	setClassType (anElement.getNodeName ());
    	
    	//Iterator<Element> itr = (node.getChildren()).iterator();
    	
    	if (anElement.getNodeType()!=Node.ELEMENT_NODE)
    		return (false);
    	
    	Node node=anElement;
  
    	NodeList children=node.getChildNodes ();
    	      
    	if (children==null)
    	{
    		debug ("Internal error: children list is null");
    		return (false);
    	}
		
    	if (children.getLength()>0)
    	{     	
    		debug ("Parsing attributes ...");    	
    	
    		setInstanceName (getAttributeValue(node,"name"));
    		setX (getAttributeValue(node,"x"));
    		setY (getAttributeValue(node,"y"));
    		setWidth (getAttributeValue(node,"width"));
    		setHeight (getAttributeValue(node,"height"));
    	
    		for (int i=0;i<children.getLength();i++) 
    		{
    			Node childNode=children.item (i);    	
			            
    			if (childNode.getNodeName ().equals ("Styles"))
    			{            
    				debug ("Parsing Styles");
				
    			   	NodeList props=childNode.getChildNodes ();
    			   	
    	    		for (int j=0;j<props.getLength();j++) 
    	    		{
    					Node styleNode=props.item(j);
		            
    					INHoopStyleProperty styleProperty=new INHoopStyleProperty ();
    					styleProperty.fromXML (styleNode);
    					styles.add (styleProperty);		            
    				}    
    			}									
    		}
    	}	
		
		return (true);
    }
}
