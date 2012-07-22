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

import edu.cmu.cs.in.search.HoopDocument;
import edu.cmu.cs.in.base.HoopLink;
//import edu.cmu.cs.in.controls.HoopVisualFeature;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.HoopJInternalFrame;

/** 
 * @author vvelsen
 *
 */
public class HoopDocumentList extends HoopEmbeddedJPanel
{	
	private static final long serialVersionUID = 2319368351656283482L;
	private JList docList=null;
	
	/**
	 * Constructs a new frame that is initially invisible.
	 */	
	public HoopDocumentList()
	{
    	//super("Document Set Viewer", true, true, true, true);
		
		setClassName ("HoopDocumentList");
		debug ("HoopDocumentList ()");
		
	   	Box holder = new Box (BoxLayout.Y_AXIS);
	   	
	   	docList=new JList ();	   	
		docList.setCellRenderer (new HoopDocumentListRenderer ());
		
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

		if (HoopLink.dataSet==null)
		{
			return;
		}
		
		ArrayList<HoopDocument> docs=HoopLink.dataSet.getDocuments();		
		
		DefaultListModel mdl=new DefaultListModel ();
		
		for (int i=0;i<docs.size();i++)
		{
			HoopDocument doc=docs.get(i);						
			mdl.addElement(doc);
		}
		
		docList.setModel (mdl);		
	}			
}
