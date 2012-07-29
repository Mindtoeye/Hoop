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

import java.util.ArrayList;
import java.util.Enumeration;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JViewport;
//import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
///import javax.swing.JList;
import javax.swing.JScrollPane;
//import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxRectangle;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopStringTools;
//import edu.cmu.cs.in.controls.HoopShadowBorder;
import edu.cmu.cs.in.controls.HoopJTreeHoopRenderer;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 * 
 */
public class HoopTextViewer extends HoopEmbeddedJPanel
{	
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
	 * When this method is called we should assume that we have to re-evaluate all existing hoop templates
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
	}	    
}
