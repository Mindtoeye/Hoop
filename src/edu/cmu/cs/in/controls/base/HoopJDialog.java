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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;

import java.awt.event.ActionEvent;

/**
 * 
 */
public class HoopJDialog extends JDialog implements ActionListener, WindowListener 
{
	private static final long serialVersionUID = 9207892759985898888L;
	
	public static final int NONE=0;
	public static final int YES=1;
	public static final int NO=2;
	public static final int CANCEL=3;
	public static final int YESNO=4;
	public static final int OK=5;
	public static final int OKCANCEL=6;
	public static final int CLOSE=7;
		
	protected JPanel myPanel = null;
    protected JButton yesButton = null;
    protected JButton noButton = null;
    
    private JPanel frame=null;
    
    protected boolean answer = false;
        
	private String className="HoopBase";	
	private String instanceName="Undefined";    

	
    /**
     * 
     */
    public HoopJDialog (int btnConfig,
    					JFrame aFrame, 
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
		frame.setLayout(new BoxLayout (frame,BoxLayout.Y_AXIS));
		//frame.setLayout(new BoxLayout (frame,BoxLayout.Y_AXIS));
		frame.setBackground(new Color (220,220,220));
		frame.setMinimumSize (new Dimension (100,100));
		//frame.setMaximumSize (new Dimension (400,400));
		mainBox.add(frame);
		
		myPanel = new JPanel();
		myPanel.setMaximumSize (new Dimension (400,25));
		mainBox.add(myPanel);
				
		if (btnConfig==YESNO)
		{		
			yesButton = new JButton("Yes");
			yesButton.addActionListener(this);
			myPanel.add(yesButton);	
		
			noButton = new JButton("No");
			noButton.addActionListener(this);
			myPanel.add(noButton);
		}	
		
		if (btnConfig==OK)
		{		
			yesButton = new JButton("OK");
			yesButton.addActionListener(this);
			myPanel.add(yesButton);			
		}		
		
		if (btnConfig==OKCANCEL)
		{		
			yesButton = new JButton("OK");
			yesButton.addActionListener(this);
			myPanel.add(yesButton);			
			
			noButton = new JButton("Cancel");
			noButton.addActionListener(this);
			myPanel.add(noButton);			
		}		
		
		if (btnConfig==CLOSE)
		{		
			yesButton = new JButton("Close");
			yesButton.addActionListener(this);
			myPanel.add(yesButton);			
		}	

		this.getContentPane().add(mainBox);
						
		this.resizeAndCenter(HoopLink.dialogDefaultWidth,HoopLink.dialogDefaultHeight);
		
		this.addWindowListener(this);
    }	
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
		frame.setLayout(new BoxLayout (frame,BoxLayout.Y_AXIS));
		frame.setBackground(new Color (220,220,220));
		frame.setMinimumSize (new Dimension (100,100));

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
						
		this.resizeAndCenter(HoopLink.dialogDefaultWidth,HoopLink.dialogDefaultHeight);	
		
		this.addWindowListener(this);
    }
    /**
     * 
     */
    protected JPanel getFrame ()
    {
    	return (frame);
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
    	debug ("actionPeformed ()");
    	
    	if (processDecision (e)==true)
    		setVisible (false);    	
    }
    /**
     * 
     */
    protected Boolean processDecision (ActionEvent e)
    {
    	debug ("processDecision ()");
    	
    	if (e==null)
    		return (true);
    	
		if (yesButton == e.getSource()) 
			answer = true;

		if (noButton == e.getSource()) 
			answer = false;
		
		return (answer);
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
    public void resizeAndCenter (int aWidth,int aHeight)
    {
    	debug ("resizeAndCenter ("+aWidth+","+aHeight+")");
    			
		this.setSize(aWidth,aWidth);	    	
		
		setLocationRelativeTo(frame);
    }
    /**
     * 
     */
	@Override
	public void windowActivated(WindowEvent arg0) 
	{
		debug ("windowActivated ()");
		
	}
    /**
     * 
     */	
	@Override
	public void windowClosed(WindowEvent arg0) 
	{
		debug ("windowClosed ()");
		
	}
    /**
     * 
     */	
	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		debug ("windowClosing ()");
				
		processDecision (null);
		
		setVisible (false);
	}
    /**
     * 
     */	
	@Override
	public void windowDeactivated(WindowEvent arg0) 
	{
		debug ("windowDeactivated ()");
		
	}
    /**
     * 
     */	
	@Override
	public void windowDeiconified(WindowEvent arg0) 
	{
		debug ("windowDeiconified ()");
		
	}
    /**
     * 
     */	
	@Override
	public void windowIconified(WindowEvent arg0) 
	{
		debug ("windowIconified ()");
		
	}
    /**
     * 
     */	
	@Override
	public void windowOpened(WindowEvent arg0) 
	{
		debug ("processWinwindowOpeneddowEvent ()");
		
	}
}
