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
public class INXMLBase extends INFeatureMatrixBase
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
