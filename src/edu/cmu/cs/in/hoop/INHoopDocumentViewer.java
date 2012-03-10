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

/*
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
*/

package edu.cmu.cs.in.hoop;

//import org.lobobrowser.html.gui.HtmlPanel;
import java.net.MalformedURLException;

import org.lobobrowser.html.parser.*;
import org.lobobrowser.html.test.*;
import org.lobobrowser.html.gui.*;
import org.lobobrowser.html.*;
import org.w3c.dom.*;

import edu.cmu.cs.in.controls.INGridNodeVisualizer;
import edu.cmu.cs.in.controls.base.INJInternalFrame;

/**
 * 
 */
public class INHoopDocumentViewer extends INJInternalFrame 
{  
	private static final long serialVersionUID = 8387762921834350566L;
	
	private HtmlPanel panel=null;

	public INHoopDocumentViewer() 
    {
    	super("Document Viewer", true, true, true, true);
    	
    	panel=new HtmlPanel ();

/*    	
    	UserAgentcontext ucontext = new SimpleUserAgentContext();
    	SimpleHtmlRendererContext rcontext = new SimpleHtmlRendererContext (panel, ucontext);
    	// Note that document builder should receive both contexts.
    	DocumentBuilderImpl dbi = new DocumentBuilderImpl(ucontext, rcontext);
    	// A documentURI should be provided to resolve relative URIs.
    	Document document = dbi.parse(new InputSourceImpl(documentReader, documentURI));
    	// Now set document in panel. This is what causes the document to render.
    	panel.setDocument(document, rcontext);
*/    	    	
   	
		setContentPane (panel);
		setSize (450,400);
		setLocation (75,75);
		
		try 
		{
			new SimpleHtmlRendererContext(panel, new SimpleUserAgentContext()).navigate("http://wiki.pdl.cmu.edu/opencloudwiki/Main/WebHome");
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}		
    }
}
