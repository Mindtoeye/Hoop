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

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

import edu.cmu.cs.in.controls.base.HoopJDialog;

/**
 * 
 */
public class HoopPatternEditor extends HoopJDialog implements ActionListener 
{		
	private static final long serialVersionUID = -2275264716559312322L;
	
	private class DragMouseAdapter extends MouseAdapter 
	{
		public void mousePressed(MouseEvent e) 
		{
			JComponent c = (JComponent) e.getSource();
			TransferHandler handler = c.getTransferHandler();
			handler.exportAsDrag(c, e, TransferHandler.COPY);
		}
	}
	
	private JTextField textField=null;
	private JLabel label=null;
	
	/**
     * 
     */
    public HoopPatternEditor (JFrame frame, boolean modal) 
	{
		super (HoopJDialog.CLOSE,frame, modal,"Hoop Pattern Editor");
		
		setClassName ("HoopPatternEditor");
		debug ("HoopPatternEditor ()");
				
		JPanel contentFrame=this.getFrame();
		
		Box contentBox = new Box (BoxLayout.Y_AXIS);
		
		JPanel mainPanel=new JPanel ();
		
		textField = new JTextField(40);
		textField.setDragEnabled(true);
    
		JPanel tfpanel = new JPanel(new GridLayout(1, 1));
		TitledBorder t1 = BorderFactory.createTitledBorder("JTextField: drag and drop is enabled");
		tfpanel.add(textField);
		tfpanel.setBorder(t1);

		label = new JLabel("I'm a Label!", SwingConstants.LEADING);
		label.setTransferHandler(new TransferHandler("text"));

		MouseListener listener = new DragMouseAdapter();
		label.addMouseListener(listener);
		JPanel lpanel = new JPanel(new GridLayout(1, 1));
		TitledBorder t2 = BorderFactory.createTitledBorder("JLabel: drag from or drop to this label");
		lpanel.add(label);
		lpanel.setBorder(t2);

		mainPanel.add(tfpanel);
		mainPanel.add(lpanel);
		
		contentBox.add (mainPanel);
		
		contentFrame.add (contentBox);			
    }	
}
