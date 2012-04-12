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

package edu.cmu.cs.in.hoop;

import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import edu.cmu.cs.in.search.INDocument;
import edu.cmu.cs.in.base.INHoopLink;
//import edu.cmu.cs.in.controls.INVisualFeature;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.INJInternalFrame;

/** 
 * @author vvelsen
 *
 */
public class INHoopDocumentList extends INEmbeddedJPanel
{	
	private static final long serialVersionUID = 2319368351656283482L;
	private JList docList=null;
	
	/**
	 * Constructs a new frame that is initially invisible.
	 */	
	public INHoopDocumentList()
	{
    	//super("Document Set Viewer", true, true, true, true);
		
		setClassName ("INHoopDocumentList");
		debug ("INHoopDocumentList ()");
		
	   	Box holder = new Box (BoxLayout.Y_AXIS);
	   	
	   	docList=new JList ();	   	
		docList.setCellRenderer (new INHoopDocumentListRenderer ());
		
	    JScrollPane docScrollList = new JScrollPane (docList);	   

	    holder.add (docScrollList);
	   	
		setContentPane (holder);
		//setSize (325,200);
		//setLocation (75,75);	   	
		
		updateContents(); // Just in case we already have something
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");

		if (INHoopLink.dataSet==null)
		{
			return;
		}
		
		ArrayList<INDocument> docs=INHoopLink.dataSet.getDocuments();		
		
		DefaultListModel mdl=new DefaultListModel ();
		
		for (int i=0;i<docs.size();i++)
		{
			INDocument doc=docs.get(i);						
			mdl.addElement(doc);
		}
		
		docList.setModel (mdl);		
	}			
}
