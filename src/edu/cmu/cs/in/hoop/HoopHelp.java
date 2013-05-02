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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.HoopButtonBox;
import edu.cmu.cs.in.controls.HoopHTMLPane;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;

/*
 * 
 */
public class HoopHelp extends HoopEmbeddedJPanel implements ActionListener 
{  
	private static final long serialVersionUID = -1L;

	private HoopHTMLPane html=null;
	
	private JButton homeButton=null;
	private JButton backButton=null;
	private JButton forwardButton=null;
	
	private ArrayList <String> history=null;
	
	/**
	 * 
	 */
	public HoopHelp () 
    {
		setClassName ("HoopHelp");
		debug ("HoopHelp ()");  
		
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
		
		HoopButtonBox buttonBox=new HoopButtonBox ();
	   	buttonBox.setMinimumSize(new Dimension (100,24));
	   	buttonBox.setPreferredSize(new Dimension (200,24));
	   	buttonBox.setMaximumSize(new Dimension (500,24));
		
	    //Box buttonBox = new Box (BoxLayout.X_AXIS);
	    
	    homeButton=new JButton ();
	    homeButton.setIcon (HoopLink.getImageByName("gtk-home.png"));
	    homeButton.setFont(new Font("Dialog", 1, 8));
	    homeButton.setPreferredSize(new Dimension (20,20));
	    homeButton.setMaximumSize(new Dimension (20,20));	    	    
	    homeButton.addActionListener(this);
	    
	    backButton=new JButton ();
	    backButton.setIcon (HoopLink.getImageByName("gtk-go-back-ltr.png"));
	    backButton.setFont(new Font("Dialog", 1, 8));
	    backButton.setPreferredSize(new Dimension (20,20));
	    backButton.setMaximumSize(new Dimension (20,20));	    	    
	    backButton.addActionListener(this);
	    
	    forwardButton=new JButton ();
	    forwardButton.setIcon (HoopLink.getImageByName("gtk-go-forward-ltr.png"));
	    forwardButton.setFont(new Font("Dialog", 1, 8));
	    forwardButton.setPreferredSize(new Dimension (20,20));
	    forwardButton.setMaximumSize(new Dimension (20,20));	    	    
	    forwardButton.addActionListener(this);	    
	    
	    buttonBox.addComponent (homeButton);
	    buttonBox.addComponent (backButton);
	    buttonBox.addComponent (forwardButton);

       	html=new HoopHTMLPane ();
       	
       	this.add (buttonBox);
       	this.add (html);
       	
       	//setContentPane(html);
       	
       	history=new ArrayList<String> ();
       	
       	home ();
    }
	/**
	 * 
	 */
	public void navigateToTopic (String aTopic)
	{
		debug ("navigateToTopic ("+aTopic+")");
		
		String newURL="/assets/help/" + aTopic + ".html";
		
		html.navigateTo(newURL);
		
		history.add(newURL);
	}
	/**
	 * 
	 */
	private void home ()
	{
		debug ("home ()");
		
		html.navigateTo("/assets/help/" + "toc" + ".html");
	}
	/**
	 * 
	 */
	private void back ()
	{
		debug ("back ()");
	
		String targetURL=history.get(history.size()-1);
		
		html.navigateTo(targetURL);		
	}
	/**
	 * 
	 */
	private void forward ()
	{
		debug ("forward ()");
		
		String targetURL=history.get(history.size()-1);
		
		html.navigateTo(targetURL);		
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
		
		if (button==homeButton)
		{
			home ();
		}
		
		if (button==backButton)
		{
			back ();
		}
		
		if (button==forwardButton)
		{
			forward ();
		}		
	}
}
