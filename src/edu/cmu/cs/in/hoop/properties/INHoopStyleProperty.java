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

//import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.cmu.cs.in.hoop.properties.INHoopSerializable;

/**

*/
public class INHoopStyleProperty extends INHoopSerializable
{		
	/**
	 *
	 */
    public INHoopStyleProperty () 
    {
    	setClassName ("INHoopStyleProperty");
    	debug ("INHoopStyleProperty ()");
    }
	/**
	*	
	*/		
	public String toString()
	{
		StringBuffer buffer=new StringBuffer();
		buffer.append(getClassOpen ()+"<name>"+getInstanceName ()+"</name><value fmt=\"text\" type=\""+getType ()+"\" >"+getValue ()+"</value>"+getClassClose ());
		return (buffer.toString ());
	}     	
	/**
	 *
	 */
    public Boolean fromXML (Node node)
    {
    	debug ("fromXML ("+node.getNodeName ()+")");
    	
    	setClassType (node.getNodeName ());
    	  
    	NodeList children=node.getChildNodes ();
    	      
    	if (children==null)
    	{
    		debug ("Internal error: children list is null");
    		return (false);
    	}
		
    	if (children.getLength()>0)
    	{     	
    		debug ("Parsing attributes ...");    	
		
    		for (int i=0;i<children.getLength();i++) 
    		{
    			Node childNode=children.item (i);
            
    			debug ("Parsing text node ("+childNode.getNodeName()+")...");
                        
    			if (childNode.getNodeName().equals ("name")==true)
    			{
    				debug ("Parsing name: " + childNode.getNodeValue());
    				setInstanceName (childNode.getNodeValue());
    			}   
			
    			if (childNode.getNodeName().equals ("value")==true)
    			{
    				debug ("Parsing value: " + childNode.getNodeValue());				
    				setValue (childNode.getNodeValue());				
    				setType (getAttributeValue(childNode,"type")); 				
    			}                        
    		}
    	}	

    	return (true);
    }	
}
