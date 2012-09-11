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

package edu.cmu.cs.in.controls.base;

import javax.swing.JDialog; 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

import edu.cmu.cs.in.base.HoopRoot;

import java.awt.event.ActionEvent;

/**
 * 
 */
public class HoopJDialog extends JDialog implements ActionListener 
{
	private static final long serialVersionUID = 9207892759985898888L;
	
	private JPanel myPanel = null;
    private JButton yesButton = null;
    private JButton noButton = null;
    
    private JPanel frame=null;
    
    private boolean answer = false;
        
	private String className="HoopBase";	
	private String instanceName="Undefined";    

    /**
     * 
     */
    public HoopJDialog (JFrame aFrame, 
    					boolean modal, 
    					String title) 
	{
		super (aFrame, modal);
		
		setClassName ("HoopJDialog");
		debug ("HoopJDialog ()");		
				
		this.setTitle(title);
				
		getContentPane ().setLayout(new BoxLayout (getContentPane (),BoxLayout.Y_AXIS));
				
		Box mainBox = new Box (BoxLayout.Y_AXIS);
		
		frame = new JPanel();
		frame.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		//frame.setLayout(new BoxLayout (frame,BoxLayout.Y_AXIS));
		frame.setBackground(new Color (220,220,220));
		frame.setMinimumSize (new Dimension (100,100));
		frame.setMaximumSize (new Dimension (400,400));
		mainBox.add(frame);
		
		myPanel = new JPanel();
		myPanel.setMaximumSize (new Dimension (400,25));
		mainBox.add(myPanel);
		
		yesButton = new JButton("Yes");
		yesButton.addActionListener(this);
		myPanel.add(yesButton);	
		
		noButton = new JButton("No");
		noButton.addActionListener(this);
		myPanel.add(noButton);
		
		this.getContentPane().add(mainBox);
		
		addContent ();
		
		pack();
		
		setLocationRelativeTo(frame);
		
		this.setSize(250,200);
		
		setVisible(true);
    }
    /**
     * 
     */
    protected void addContent ()
    {
    	
    }
	/**
	 *
	 */
	public void setClassName (String aName)
	{
		className=aName;
	}
	/**
	 *
	 */
	public String getClassName ()
	{
		return (className);
	}	
	/**
	 *
	 */	
	public String getInstanceName() 
	{
		return instanceName;
	}
	/**
	 *
	 */	
	public void setInstanceName(String instanceName) 
	{
		this.instanceName = instanceName;
	}		
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopRoot.debug(getClassName(),aMessage);
	}    
    /**
     * 
     */
    public void actionPerformed(ActionEvent e) 
	{
		if(yesButton == e.getSource()) 
		{
			//System.err.println("User chose yes.");
			answer = true;
			setVisible(false);
		}
		else if(noButton == e.getSource()) 
		{
			//System.err.println("User chose no.");
			answer = false;
			setVisible(false);
		}
    }
    /**
     * 
     */
    public boolean getAnswer() 
    { 
    	return answer; 
    }
    /**
     * 
     */
    public JPanel getFrame ()
    {
    	return (frame);
    }
}
