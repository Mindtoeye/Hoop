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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopJPanel;

/**
* Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
* 
* To convert it to an array:
* 
* Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
*/
public class HoopThreadView extends HoopJPanel implements ActionListener
{
	private static final long serialVersionUID = -4749410024446913114L;
	
	private JTree tree=null;
	private Border border=null;
	private JButton refreshButton=null;
	private JToggleButton autoRefresh=null;
	
	private Timer timer;
	
	private class RemindTask extends TimerTask 
	{
        public void run() 
        {
        	showThreads ();
        	
            //timer.cancel(); //Terminate the timer thread
        }
	}    

	public HoopThreadView() 
    {
		setClassName ("HoopThreadView");
		debug ("HoopThreadView ()");

		border = BorderFactory.createEmptyBorder (1,1,1,2);
		
		this.setFont(new Font("Dialog", 1, 10));
		this.setOpaque(false);
		this.setBorder (border);
		
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
		
		tree=new JTree ();
		tree.setFont(new Font("Dialog", 1, 9));
		
		HoopButtonBox buttonBox=new HoopButtonBox ();
	   	buttonBox.setMinimumSize(new Dimension (100,24));
	   	buttonBox.setPreferredSize(new Dimension (200,24));
	   	buttonBox.setMaximumSize(new Dimension (2000,24));		
	    
	    refreshButton=new JButton ();
	    refreshButton.setFont(new Font("Dialog", 1, 8));
	    refreshButton.setPreferredSize(new Dimension (20,20));
	    refreshButton.setMaximumSize(new Dimension (20,20));
	    refreshButton.setIcon(HoopLink.getImageByName("gtk-refresh.png"));
	    refreshButton.addActionListener(this);
	    
	    autoRefresh=new JToggleButton ();
	    autoRefresh.setFont(new Font("Dialog", 1, 8));
	    autoRefresh.setPreferredSize(new Dimension (20,20));
	    autoRefresh.setMaximumSize(new Dimension (20,20));
	    autoRefresh.setIcon(HoopLink.getImageByName("clock.png"));
	    autoRefresh.addActionListener(this);	    
	    
	    buttonBox.addComponent(refreshButton);
	    buttonBox.addComponent(autoRefresh);
	    
	    //>----------------------------------------------------
	    
		JScrollPane threadScrollPane=new JScrollPane(tree);
		threadScrollPane.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));	    
	    
	    //>----------------------------------------------------
	    
	    this.add (buttonBox);		
		this.add(threadScrollPane);
		
		showThreads ();
    }
	/**
	 * 
	 */
	public void showThreads ()
	{
		debug ("showThreads ()");
		
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		
		for (int i=0;i<threadArray.length;i++)
		{
			Thread visThread=threadArray [i];
			
			DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(visThread);
			catNode.setUserObject(new String ("ID: " + visThread.getId() + ", " + visThread.getName()));
			root.add(catNode);
			
			DefaultMutableTreeNode subNode = null;
		
			/*
			subNode = new DefaultMutableTreeNode(visThread);
			subNode.setUserObject(new String ("Active Count: " + visThread.activeCount()));
			catNode.add(subNode);
			*/
			
			subNode = new DefaultMutableTreeNode(visThread);
			subNode.setUserObject(new String ("Priority: " + visThread.getPriority()));
			catNode.add(subNode);
			
			subNode = new DefaultMutableTreeNode(visThread);
			subNode.setUserObject(new String ("Is Alive: " + visThread.isAlive()));
			catNode.add(subNode);
			
			/*
			subNode = new DefaultMutableTreeNode(visThread);
			subNode.setUserObject(new String ("Interrupted: " + visThread.interrupted()));
			*/
			
			subNode = new DefaultMutableTreeNode(visThread);
			subNode.setUserObject(new String ("Is Deamon: " + visThread.isDaemon()));
			catNode.add(subNode);
			
			subNode = new DefaultMutableTreeNode(visThread);
			subNode.setUserObject(new String ("Is Interrupted: " + visThread.isInterrupted()));						
			catNode.add(subNode);
		}
		
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		
		tree.setModel(treeModel);
	}
	/**
	 * 
	 * @param arg0
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{	
		debug ("actionPerformed ()");
		
		//String act=event.getActionCommand();
		Object test=event.getSource();
		
		if (test==refreshButton)
		{
			showThreads ();
		}		
		
		if (test==autoRefresh)
		{
			if (autoRefresh.isSelected()==true)
			{
				if (timer==null)
				{
			        timer = new Timer();
			        timer.schedule(new RemindTask(),0,5000);
				}
			}
			else
			{
				if (timer!=null)
				{
					timer.cancel();
					timer=null;
				}
			}
		}
	}
}