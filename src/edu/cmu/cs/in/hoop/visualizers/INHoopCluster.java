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

package edu.cmu.cs.in.hoop.visualizers;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jdom.Element;

import edu.cmu.cs.in.INHoopMessageHandler;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.INXMLBase;
import edu.cmu.cs.in.controls.INGridNodeVisualizer;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
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
	private INXMLBase xmlHelper=null;
	private INHoopMessageHandler handler=null;
		
	/**
	 * 
	 */
	public INHoopCluster() 
    {		
		xmlHelper=new INXMLBase ();
		handler=new INHoopMessageHandler ();
		
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));				
		
		Box controlBox=Box.createHorizontalBox();
		
		hostInput=new JTextField ();
		hostInput.setText ("172.19.159.76");
		hostInput.setFont(new Font("Dialog", 1, 10));
		hostInput.setMinimumSize(new Dimension (120,20));
		hostInput.setPreferredSize(new Dimension (120,20));
		hostInput.setMaximumSize(new Dimension (120,20));
		
		portInput=new JTextField ();
		portInput.setText("8080");
		portInput.setFont(new Font("Dialog", 1, 10));
		portInput.setMinimumSize(new Dimension (75,20));
		portInput.setPreferredSize(new Dimension (75,20));
		portInput.setMaximumSize(new Dimension (75,20));
		
		connectButton=new JButton ();
		connectButton.setText("Connect");
		connectButton.setFont(new Font("Dialog", 1, 10));
		connectButton.setMinimumSize(new Dimension (100,20));
		connectButton.setPreferredSize(new Dimension (100,20));
		connectButton.setMaximumSize(new Dimension (100,20));
		connectButton.addActionListener(this);
						
		driver=new INGridNodeVisualizer ();
		driver.setMinimumSize(new Dimension (100,200));
		
		JScrollPane contentScroller=new JScrollPane (driver);
		
		controlBox.add(hostInput);
		controlBox.add(portInput);
		controlBox.add(connectButton);
		controlBox.add(Box.createHorizontalGlue());
		
		this.add(controlBox);
		this.add(contentScroller);		
    }
	/**
	 * 
	 */
	public INGridNodeVisualizer getDriver ()
	{
		return (driver);
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
						
			if (INHoopLink.brokerConnection==null)
			{
				INHoopLink.brokerConnection=new INStreamedSocket ();
			
				INHoopLink.brokerConnection.sendAndKeepOpen(hostInput.getText(),Integer.parseInt(portInput.getText()),"<?xml version=\"1.0\" encoding=\"utf-8\"?><register type=\"monitor\" />",this);
			}	
		}
		
		if (button.getText()=="Disconnect")
		{
			debug ("Disconnect ...");

			if (INHoopLink.brokerConnection!=null)
			{
				INHoopLink.brokerConnection.sendAndKeepOpen(hostInput.getText(),
															Integer.parseInt(portInput.getText()),
															"<?xml version=\"1.0\" encoding=\"utf-8\"?><unregister type=\"monitor\" />",
															this);
				INHoopLink.brokerConnection.close();
				INHoopLink.brokerConnection=null;
			}							
		}		
	}
	/**
	 *
	 */		
	@Override
	public void handleIncomingData(String data) 
	{
		debug ("handleIncomingData ()");
		
		debug (data);
		
		connectButton.setText("Disconnect");
		
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
		connectButton.setText("Connect");
	}		
}
