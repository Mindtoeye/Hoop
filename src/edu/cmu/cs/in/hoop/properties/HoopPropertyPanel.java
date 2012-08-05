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

package edu.cmu.cs.in.hoop.properties;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;

/** 
 * @author vvelsen
 *
 */
public class HoopPropertyPanel extends HoopEmbeddedJPanel implements ActionListener
{	
	private static final long serialVersionUID = 1L;
		
    private Box hoopPropertyBox=null;
    private JScrollPane contentScroller=null;
    private Box contentBox=null;
    
    private JToggleButton linkButton=null;
    	
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public HoopPropertyPanel()
	{
		setClassName ("HoopPropertyPanel");
		debug ("HoopPropertyPanel ()");
						
		hoopPropertyBox=new Box (BoxLayout.Y_AXIS);
		hoopPropertyBox.setMinimumSize(new Dimension (20,20));
		hoopPropertyBox.setPreferredSize(new Dimension (200,300));
		
		linkButton=new JToggleButton ();
		linkButton.setIcon(HoopLink.getImageByName("link-views.png"));		
		linkButton.setMargin(new Insets(1, 1, 1, 1));
		//clearButton.setText("Clear");
		//linkButton.setFont(new Font("Courier",1,8));
		linkButton.setPreferredSize(new Dimension (20,20));
		linkButton.addActionListener(this);		
		
		Box controlBox = new Box (BoxLayout.X_AXIS);
		controlBox.add(linkButton);
		controlBox.add(Box.createHorizontalGlue());		
                        
		contentBox=new Box (BoxLayout.Y_AXIS);
		contentBox.setMinimumSize(new Dimension (20,20));
		contentBox.setPreferredSize(new Dimension (200,300));
        		
        contentScroller=new JScrollPane (contentBox);
        contentScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        hoopPropertyBox.add (controlBox);
        hoopPropertyBox.add (Box.createRigidArea(new Dimension(0,2)));
        hoopPropertyBox.add (contentScroller);
        
        this.add (hoopPropertyBox);        
	}
	/**
	 * 
	 */
	public void addPropertyPanel (HoopInspectablePanel aPanel)
	{
		debug ("addPropertyPanel ()");
		
		contentBox.add (aPanel);
		//contentBox.add(Box.createVerticalGlue());
		
		debug ("addPropertyPanel () done");
	}
	/**
	 * 
	 */
	public void removePropertyPanel (HoopBase aHoop)
	{
		debug ("removePropertyPanel ()");
		
		Component [] childList=contentBox.getComponents();
		
		for (int i=0;i<childList.length;i++)
		{
			HoopInspectablePanel tester=(HoopInspectablePanel) childList [i];
		
			if (tester.getHoop ()==aHoop)
			{
				contentBox.remove(tester);
			}
		}	
	}	
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");

	}
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
		JComponent control=(JComponent) event.getSource();
		
		if (control==linkButton)
		{
			debug ("Toggling link views ...");
		}
	}	
}
