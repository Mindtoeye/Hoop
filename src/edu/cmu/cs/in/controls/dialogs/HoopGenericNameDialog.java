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

package edu.cmu.cs.in.controls.dialogs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.cmu.cs.in.base.io.HoopFileTools;
import edu.cmu.cs.in.controls.base.HoopJDialog;

/**
 * 
 */
public class HoopGenericNameDialog extends HoopJDialog implements ActionListener 
{	
	private static final long serialVersionUID = -7131882853652470437L;
	
	public static final int REGULAR=0;
	public static final int PASSWORD=1;
	public static final int DIRECTORY=2;	
	
	private JTextField labelField=null;
	private JTextField nameField=null;
	private int nameMode=REGULAR;
	protected String chosenName="";
	
	/**
     * 
     */
    public HoopGenericNameDialog (int aMode,JFrame frame, boolean modal) 
	{
		super (frame, modal,"Enter a Name");
		
		setClassName ("HoopGenericNameDialog");
		debug ("HoopGenericNameDialog ()");
		
		nameMode=aMode;		 
		
		JPanel contentFrame=this.getFrame();
		
		Box contentBox = new Box (BoxLayout.Y_AXIS);
		
		labelField=new JTextField ();
		labelField.setMinimumSize (new Dimension (200,75));
		labelField.setPreferredSize (new Dimension (200,75));
		labelField.setMaximumSize (new Dimension (400,75));
		labelField.setFont(new Font("Dialog", 1, 10));
		
		contentBox.add(labelField);
		
		nameField=new JTextField ();
		nameField.setMinimumSize (new Dimension (200,20));
		nameField.setPreferredSize (new Dimension (200,20));
		nameField.setMaximumSize (new Dimension (400,20));
		nameField.setFont(new Font("Dialog", 1, 10));
		
		contentBox.add(nameField);
		
		contentFrame.add (contentBox);			
    }
    /**
     * 
     */
    public void setDescription (String aDesc)
    {
    	labelField.setText (aDesc);
    }    
    /**
     * 
     */
    public void setChosenName (String aName)
    {
    	nameField.setText (aName);
    }
    /**
     * 
     */
    public String getChosenName ()
    {
    	return (chosenName);
    }
	/**
	 * 
	 */
	public void setMode (int aMode) 
	{
		this.nameMode = aMode;
	}    
    /**
     * 
     */
	public int getMode() 
	{
		return nameMode;
	}
    /**
     * 
     */
    protected Boolean testInput ()
    {
    	String result=nameField.getText();
    	
    	if (result.isEmpty()==true)
    	{
    		return (false);
    	}    	
    	
    	if (nameMode==DIRECTORY)
    	{
    		if (HoopFileTools.isValidName(result)==false)
    		{
    			return (false);
    		}
    	}    	
    	
    	return (true);
    }	
    /**
     * 
     */
    protected Boolean processDecision (ActionEvent e)
    {
    	debug ("processDecision ()");
    	    	
    	chosenName=nameField.getText();
    	    	
    	if (testInput ()==false)
    	{
    		debug ("Name " + chosenName + " is not a valid entry");
    		return (false);
    	}
    	
    	debug ("Name: " + chosenName);
    	    	    	
		if (yesButton == e.getSource()) 
			answer = true;

		if (noButton == e.getSource()) 
			answer = false;
		
		return (answer);
    }	
}
