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

import java.awt.Container;
import java.awt.Cursor;
import java.io.IOException;
//import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.HoopCommandProcessor;

/*
 * 
 */
public class HoopHTMLPane extends JScrollPane implements HyperlinkListener 
{
	private static final long serialVersionUID = 5848133235678518042L;
	
	private JEditorPane html=null;
	
	private HoopCommandProcessor commandProcessor=null;
	
    /**
     * 
     */    
    public HoopHTMLPane () 
    {    	
    	debug ("HoopHTMLPane ()");
    	
    	html = new JEditorPane();
    	html.setEditable(false);
    	html.addHyperlinkListener(this);
    	html.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,Boolean.TRUE);
    	JViewport vp = getViewport();
    	vp.add(html);	
    }
    /**
     * 
     */
    private void debug (String aMessage)
    {
    	HoopRoot.debug("HoopHTMLPane",aMessage);
    }
    /**
     * Notification of a change relative to a hyperlink.
     */
    public void hyperlinkUpdate(HyperlinkEvent e) 
    {
    	debug ("hyperlinkUpdate ()");
    	
    	if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) 
    	{
    		if (e.getURL()!=null)
    			linkActivated(e.getURL());
    		else
    			debug ("Error: no URL object in hyperlink event");
    	}
    }
    /**
     * Follows the reference in an
     * link.  The given url is the requested reference.
     * By default this calls <a href="#setPage">setPage</a>,
     * and if an exception is thrown the original previous
     * document is restored and a beep sounded.  If an 
     * attempt was made to follow a link, but it represented
     * a malformed url, this method will be called with a
     * null argument.
     *
     * @param u the URL to follow
     */
    protected void linkActivated(URL u) 
    {
    	debug ("linkActivated ("+u.toString()+")");
    	
    	String tester=u.toString();
    	
    	int commandIndex=tester.indexOf("hoop_");
    	
    	if (commandIndex!=-1)
    	{
    		debug ("Processing internal hoop command ...");
    		
    		processHoopCommand (tester.substring(commandIndex+5));
    		
    		return;
    	}
    	
    	Cursor c = html.getCursor();
    	Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    	html.setCursor(waitCursor);
    	SwingUtilities.invokeLater(new PageLoader(u, c));
    }
    /**
     * temporary class that loads synchronously (although
     * later than the request so that a cursor change
     * can be done).
     */
    class PageLoader implements Runnable 
    {
    	PageLoader(URL u, Cursor c) 
    	{
    		url = u;
    		cursor = c;
    	}

        public void run() 
        {
        	if (url == null) 
        	{
        		// restore the original cursor
        		html.setCursor(cursor);

        		// PENDHoopG(prinz) remove this hack when 
        		// automatic validation is activated.
        		Container parent = html.getParent();
        		parent.repaint();
        	} 
        	else 
        	{
        		Document doc = html.getDocument();
        		try 
        		{
        			html.setPage(url);
        		} 
        		catch (IOException ioe) 
        		{
        			html.setDocument(doc);
        			getToolkit().beep();
        		} 
        		finally 
        		{
        			// schedule the cursor to revert after
        			// the paint has happended.
        			url = null;
        			SwingUtilities.invokeLater(this);
        		}
        	}
        }

        URL url;
        Cursor cursor;
    }
    /**
     * 
     */
    public void navigateTo (String aURL)
    {
    	URL url = getClass().getResource(aURL);
    
    	linkActivated (url);
    }
    /**
     * 
     */
    private void processHoopCommand (String aCommand)
    {
    	debug ("processHoopCommand ("+aCommand+")");
    	
    	if (commandProcessor!=null)
    		commandProcessor.processCommand(aCommand);
    }
    /**
     * 
     * @return
     */
	public HoopCommandProcessor getCommandProcessor() 
	{
		return commandProcessor;
	}
	/**
	 * 
	 * @param commandProcessor
	 */
	public void setCommandProcessor(HoopCommandProcessor commandProcessor) 
	{
		this.commandProcessor = commandProcessor;
	}
}

