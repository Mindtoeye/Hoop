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

//import java.awt.BorderLayout;
//import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
//import javax.swing.border.Border;

import edu.cmu.cs.in.INHoopMessageHandler;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.INXMLBase;
import edu.cmu.cs.in.controls.INGridNodeVisualizer;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.hadoop.INHadoopReporter;
import edu.cmu.cs.in.network.INMessageReceiver;
import edu.cmu.cs.in.network.INStreamedSocket;

/**
 * 
 */
public class INHoopCluster extends INEmbeddedJPanel implements ActionListener, INMessageReceiver 
{  
	private static final long serialVersionUID = 8387762921834350566L;

	private JTextField hostInput=null;
	private JTextField portInput=null;
    private JButton connectButton=null;
	private INGridNodeVisualizer driver=null;
	private INStreamedSocket socket=null;
	private INXMLBase xmlHelper=null;
	private INHoopMessageHandler handler=null;
	
	/**
	 * 
	 */
	public INHoopCluster() 
    {
		driver=new INGridNodeVisualizer ();
		xmlHelper=new INXMLBase ();
		handler=new INHoopMessageHandler ();
		
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));				
		
		Box controlBox = new Box (BoxLayout.X_AXIS);
		
		hostInput=new JTextField ();
		hostInput.setMinimumSize(new Dimension (30,20));
		hostInput.setMaximumSize(new Dimension (5000,20));
		
		portInput=new JTextField ();
		portInput.setMinimumSize(new Dimension (30,20));
		portInput.setMaximumSize(new Dimension (5000,20));
		
		connectButton=new JButton ();
		connectButton.setText("Connect");
		connectButton.setMinimumSize(new Dimension (30,20));
		connectButton.setMaximumSize(new Dimension (30,20));
		connectButton.addActionListener(this);
		
		controlBox.add(hostInput);
		controlBox.add(portInput);
		controlBox.add(connectButton);
		
		this.add(controlBox);
		this.add(driver);		
    }
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
		
		if (driver!=null)
		{			
			// This should result in a paint operation
			driver.updateUI();
		}	
	}
	/**
	 *
	 */	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
		//String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();
		
		if (button.getText()=="Connect")
		{
			debug ("Connect ...");
			
		    if (socket==null)
		    	socket=new INStreamedSocket ();
		    
    		socket.sendAndKeepOpen(hostInput.getText(),Integer.parseInt(portInput.getText()),"<?xml version=\"1.0\" encoding=\"utf-8\"?><register type=\"monitor\" />",this);		    
		}
		
		if (button.getText()=="Disconnect")
		{
			debug ("Disconnect ...");

	    	if (socket==null)
	    		socket=new INStreamedSocket ();			
		}		
	}
	/**
	 *
	 */		
	@Override
	public void handleIncomingData(String data) 
	{
		debug ("handleIncomingData ()");
		
		Element root=xmlHelper.fromXMLString(data);
	
		handler.handleIncomingXML(-1,root);
	}
	/**
	 *
	 */		
	@Override
	public void handleConnectionClosed() 
	{
		debug ("handleConnectionClosed ()");
		
	}		
}
