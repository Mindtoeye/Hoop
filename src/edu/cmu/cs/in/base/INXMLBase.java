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
import java.io.Reader;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * This class is the workhorse for anything XML. You can derive from it to get easy
 * xml parsing and creation, or you can use it as an exernal tool. Be aware that
 * INXMLBase inherits from INBase and that means you shouldn't create loads of
 * instances of it since that would waste a lot of memory. You might notice that
 * the code has some old commented out w3c methods and usages. We've completely
 * switched to jdom for easy parsing and generation of xml where we also don't
 * need to get the SAX parser separately.
 */
public class INXMLBase extends INBase
{			
	private Document doc = null;
	
	/**
	*
	*/	
	public INXMLBase ()
	{  
    	setClassName ("INXMLBase");
    	debug ("INXMLBase ()");
	}
	/**
	 * 
	 */	
	public Document getDoc() 
	{
		return doc;
	}
	/**
	 * 
	 */	
	public void setDoc(Document doc) 
	{
		this.doc = doc;
	}	
	/**
	 * 
	 */
	/*
	public void debugSAXException (SAXParseException spe)
	{
		StringBuffer sb = new StringBuffer( spe.toString() );		
		sb.append("\n Line number: " + spe.getLineNumber());
		sb.append("\n Column number: " + spe.getColumnNumber() );
		sb.append("\n Public ID: " + spe.getPublicId() );
		sb.append("\n System ID: " + spe.getSystemId() + "\n");
		
		debug (sb.toString ());		
	}
	*/
	/**
	*
	*/	
    public Element loadXMLFromString (String xml)
    {
    	debug ("loadXMLFromString ()");
    	
    	xml=xml.trim();
    	
    	if (xml.indexOf ("<?xml")==-1)
    	{
    		xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>"+xml;
    	}
    	
    	SAXBuilder builder = new SAXBuilder();
    	Reader in = new StringReader(xml);
    	Element root = null;
  
    	try
    	{
    		doc = builder.build(in);
    		root = doc.getRootElement();

    	} 
    	catch (JDOMException e)
    	{
    		// do what you want
    		return (null);
    	} 
    	catch (IOException e)
    	{
    		// do what yo want
    		return (null);
    	} 
    	catch (Exception e)
    	{
    		// do what you want
    		return (null);
    	}
    	
    	return (root);
    }		
	/**
	*
	*/	
    public Element fromXMLString (String xml)
    {
    	debug ("fromXMLString ()");
    
    	return (loadXMLFromString (xml));
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
	public Element toXML() 
	{
		debug ("toXML ()");
		
		Element baseElement=new Element ("xml");
		
		baseElement.setAttribute("class",this.getClassName());
		baseElement.setAttribute("instance",this.getInstanceName());		
		
		/*
		StringBuffer buffer=new StringBuffer ();
		buffer.append ("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		buffer.append ("<xml class=\""+this.getClassName()+"\" instance=\""+this.getInstanceName()+"\" />\n");			
		return (buffer.toString());
		*/
		
		return (baseElement);
	}
	/**
	*	
	*/  
	/*
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
	*/	
	/**
	*	
	*/	    
    protected String getAttributeValue (Element aNode,String anAttribute)
    {    	
    	    	
    	/*
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
    	*/
    	
    	return (aNode.getAttributeValue(anAttribute));
    }	
}
