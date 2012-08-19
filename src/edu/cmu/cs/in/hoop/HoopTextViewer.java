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
 * 	Notes:
 * 
 * 	http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-JTree.html
 *  http://docs.oracle.com/javase/tutorial/uiswing/dnd/intro.html
 * 
 */

package edu.cmu.cs.in.hoop;

import javax.swing.JEditorPane;
import javax.swing.JViewport;
import javax.swing.JScrollPane;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.project.HoopWrapperFile;

/**
 * 
 */
public class HoopTextViewer extends HoopEmbeddedJPanel
{	
	private static final long serialVersionUID = -1L;
	private JEditorPane textViewer=null;
	
	/**
	 * 
	 */
	public HoopTextViewer ()  
    {    	
		setClassName ("HoopTextViewer");
		debug ("HoopTextViewer ()");    	
		
		this.setSingleInstance(false);
		
		//Box mainBox = new Box (BoxLayout.Y_AXIS);
					    
		textViewer = new JEditorPane();
		textViewer.setEditable(false);

		JScrollPane scroller=new JScrollPane (textViewer);
		
    	JViewport vp = scroller.getViewport();
    	vp.add(textViewer);			
		
		setContentPane (scroller);			
    }
	/**
	 * 
	 */
	public void showFile (HoopWrapperFile aFile)
	{
		debug ("showFile (HoopWrapperFile)");
		
		String text=HoopLink.fManager.loadContents(aFile.getFileURI());
		
		textViewer.setText(text);
	}
	/**
	 * 
	 */
	public void showFile (String aFile)
	{
		debug ("showFile (String)");
		
		String text=HoopLink.fManager.loadContents(aFile);
		
		textViewer.setText(text);
	}	
	/**
	 * When this method is called we should assume that we have to re-evaluate all existing hoop templates
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
	}	    
}
