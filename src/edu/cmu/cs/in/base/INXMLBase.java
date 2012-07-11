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

package edu.cmu.cs.in.base;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 */
public class INXMLBase extends INBase
{			
	/**
	*
	*/	
	public INXMLBase ()
	{  
    	setClassName ("INXMLBase");
    	//debug ("INXMLBase ()");
	}
	/**
	 * 
	 */
	public void debugSAXException (SAXParseException spe)
	{
		StringBuffer sb = new StringBuffer( spe.toString() );		
		sb.append("\n Line number: " + spe.getLineNumber());
		sb.append("\n Column number: " + spe.getColumnNumber() );
		sb.append("\n Public ID: " + spe.getPublicId() );
		sb.append("\n System ID: " + spe.getSystemId() + "\n");
		
		debug (sb.toString ());		
	}
	/**
	*
	*/	
    public Document loadXMLFromString (String xml)
    {
    	debug ("loadXMLFromString ()");
    	
    	xml=xml.trim();
    	
    	if (xml.indexOf ("<?xml")==-1)
    	{
    		xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+xml;
    	}
    	
    	//debug ("Parsing: " +xml);
    	
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        
		try 
		{
			builder = factory.newDocumentBuilder();
		} 
		catch (ParserConfigurationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (null);			
		}
		
        InputSource is = new InputSource(new StringReader(xml));
        
        Document result=null;
        
		try 
		{
			result = builder.parse(is);
		} 
		catch (SAXParseException spe) 
		{
			// TODO Auto-generated catch block
			debugSAXException (spe);
			return (null);
		}		
		catch (SAXException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (null);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (null);
		}
        
        return result;
    }		
	/**
	*
	*/	
    public Element fromXMLString (String xml)
    {
    	debug ("fromXMLString ()");
    
    	Document parsed=loadXMLFromString (xml);
    	if (parsed==null)
    		return (null);
    	
    	return (parsed.getDocumentElement());
    }
	/**
	*
	*/	
	public Boolean fromXML(Element root) 
	{
		debug ("fromXML ()");
		
		// Do something in child classes
		
		return (true);
	}
	/**
	*
	*/	
	public String toXML() 
	{
		debug ("toXML ()");
		
		StringBuffer buffer=new StringBuffer ();
		buffer.append ("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		buffer.append ("<xml class=\""+this.getClassName()+"\" instance=\""+this.getInstanceName()+"\" />\n");
				
		return (buffer.toString());
	}
	/**
	*	
	*/  
	public void showNodeType (Node a_node)
	{
		if (a_node==null)
		{
			debug ("Error: provided node is null");
			return;
		}
	  
		switch (a_node.getNodeType ())
		{
			case Node.ATTRIBUTE_NODE:         
										debug ("Node type: The node is an Attr.");
	                                    break;  
			case Node.CDATA_SECTION_NODE: 
	                                     debug ("Node type: The node is a CDATASection.");
	                                     break;  
			case Node.COMMENT_NODE:           
	                                     debug ("Node type: The node is a Comment.");
	                                     break;  
			case Node.DOCUMENT_FRAGMENT_NODE: 
	                                     debug ("Node type: The node is a DocumentFragment.");
	                                     break;  
			case Node.DOCUMENT_NODE: 
	                                     debug ("Node type: The node is a Document.");
	                                     break;  
			case Node.DOCUMENT_TYPE_NODE: 
	                                     debug ("Node type: The node is a DocumentType.");
	                                     break;  
			case Node.ELEMENT_NODE: 
	                                     debug ("Node type: The node is an Element.");
	                                     break;  
			case Node.ENTITY_NODE: 
	                                     debug ("Node type: The node is an Entity.");
	                                     break;  
			case Node.ENTITY_REFERENCE_NODE: 
	                                     debug ("Node type: The node is an EntityReference.");
	                                     break;  
			case Node.NOTATION_NODE: 
	                                     debug ("Node type: The node is a Notation.");
	                                     break;  
			case Node.PROCESSING_INSTRUCTION_NODE: 
	                                     debug ("Node type: The node is a ProcessingInstruction.");
	                                     break;  
			case Node.TEXT_NODE:                   
	                                     debug ("Node type: The node is a Text node.");
	                                     break;   
		}
	} 		
	/**
	*	
	*/	    
    protected String getAttributeValue (Node aNode,String anAttribute)
    {    	
        NamedNodeMap attrs = aNode.getAttributes();
        
        for(int i = 0 ; i<attrs.getLength() ; i++) 
        {
          Attr attribute = (Attr)attrs.item(i);
          
          //System.out.println(indent+ " " + attribute.getName()+" = "+attribute.getValue());
          
          if (attribute.getName().toLowerCase().equals(anAttribute.toLowerCase())==true)
          {
        	  return (attribute.getValue());
          }
        }    	
    	
    	return ("");
    }	
}
