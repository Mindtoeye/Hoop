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

//import org.lobobrowser.html.parser.*;
import org.lobobrowser.html.test.*;
import org.lobobrowser.html.gui.*;
//import org.lobobrowser.html.*;
//import org.w3c.dom.*;

//import edu.cmu.cs.in.controls.INGridNodeVisualizer;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.INJInternalFrame;

/**
 * 
 */
public class INHoopDocumentViewer extends INEmbeddedJPanel
{  
	private static final long serialVersionUID = 8387762921834350566L;
	
	private HtmlPanel panel=null;

	public INHoopDocumentViewer() 
    {
    	//super("Document Viewer", true, true, true, true);
    	
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
		//setSize (450,400);
		//setLocation (75,75);
		
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
