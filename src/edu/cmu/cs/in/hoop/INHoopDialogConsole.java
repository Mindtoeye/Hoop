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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
import edu.cmu.cs.in.hoop.hoops.INHoopStdin;
import edu.cmu.cs.in.hoop.hoops.INHoopStdout;

/** 
 * @author vvelsen
 *
 */
public class INHoopDialogConsole extends INEmbeddedJPanel implements KeyListener
{	
	private static final long serialVersionUID = 1L;
	
    private JTextField entry=null;
    private JTextArea textArea=null;
    
    private INHoopStdin inHoop=null;
    private INHoopStdout outHoop=null;
		
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public INHoopDialogConsole()
	{
		setClassName ("INHoopDialogConsole");
		debug ("INHoopDialogConsole ()");
		
		Box holder = new Box (BoxLayout.Y_AXIS);
		
		Border blackborder=BorderFactory.createLineBorder(Color.black);
		
		entry=new JTextField ();
		entry.setFont(new Font("Dialog", 1, 10));
		entry.setBorder(blackborder);
		entry.setMinimumSize(new Dimension (50,25));
		entry.setPreferredSize(new Dimension (100,25));		
		entry.addKeyListener(this);		
		holder.add(entry);
		
		textArea=new JTextArea ();
		textArea.setEditable(false);
		textArea.setBorder(blackborder);
		textArea.setFont(new Font("Dialog", 1, 10));
		textArea.setMinimumSize(new Dimension (50,50));
		//textArea.setPreferredSize(new Dimension (100,50));
		holder.add(textArea);
		
		setContentPane (holder);		
	}
	/**
	 * 
	 */	
	public INHoopStdin getInHoop() 
	{
		return inHoop;
	}
	/**
	 * 
	 */	
	public void setInHoop(INHoopStdin inHoop) 
	{
		this.inHoop = inHoop;
	}
	/**
	 * 
	 */	
	public INHoopStdout getOutHoop() 
	{
		return outHoop;
	}
	/**
	 * 
	 */	
	public void setOutHoop(INHoopStdout outHoop) 
	{
		this.outHoop = outHoop;
	}	
	/**
	 * 
	 */
	@Override
	public void keyPressed(KeyEvent event) 
	{
	    int key = event.getKeyCode();
	    
        if (key == KeyEvent.VK_ENTER)
        {
        	processInput ();
        }  		
	}
	/**
	 * 
	 */
	@Override
	public void keyReleased(KeyEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 */
	@Override
	public void keyTyped(KeyEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}	
	/**
	 *
	 */		
	private void processInput ()
	{
		debug ("processInput ()");
				
		if (entry.getText().isEmpty()==true)
		{
			debug ("Please enter a query");
			JOptionPane.showMessageDialog(null, "Please enter a query");
			return;
		}
		
		if (inHoop!=null)
		{
			inHoop.processInput(entry.getText());
		}
		else
			JOptionPane.showMessageDialog(null, "Error: no input hoop available process request");
	}	
}
