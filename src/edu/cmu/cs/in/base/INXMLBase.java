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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//import edu.cmu.hcii.ctat.INBase;

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
		buffer.append("<xml class=\""+this.getClassName()+"\" instance=\""+this.getInstanceName()+"\" />");
				
		return (buffer.toString());
	}
}
