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

package edu.cmu.cs.in.hoop;

import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import edu.cmu.cs.in.INDocument;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.controls.INVisualFeature;
import edu.cmu.cs.in.controls.base.INJInternalFrame;

/** 
 * @author vvelsen
 *
 */
public class INHoopDocumentList extends INJInternalFrame
{	
	private static final long serialVersionUID = 2319368351656283482L;
	private JList docList=null;
	
	/**
	 * Constructs a new frame that is initially invisible.
	 */	
	public INHoopDocumentList()
	{
    	super("Document Set Viewer", true, true, true, true);
		
		setClassName ("INHoopDocumentList");
		debug ("INHoopDocumentList ()");
		
	   	Box holder = new Box (BoxLayout.Y_AXIS);
	   	
	   	docList=new JList ();
	   	
		//docList.setCellRenderer (new INJCheckListItem ());
	    JScrollPane docScrollList = new JScrollPane (docList);	   

	    holder.add (docScrollList);
	   	
		setContentPane (holder);
		setSize (325,200);
		setLocation (75,75);	   	
		
		updateContents(); // Just in case we already have something
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");

		if (INLink.dataSet==null)
		{
			return;
		}
		
		ArrayList<INDocument> docs=INLink.dataSet.getDocuments();		
		
		DefaultListModel mdl=new DefaultListModel ();
		
		for (int i=0;i<docs.size();i++)
		{
			INDocument doc=docs.get(i);
						
			mdl.addElement(doc.getDocID());
		}
		
		docList.setModel (mdl);		
	}			
}
