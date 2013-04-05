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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
//import javax.swing.border.Border;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;

/** 
 * @author vvelsen
 */
public class HoopPropertyPanel extends HoopEmbeddedJPanel implements ActionListener
{	
	private static final long serialVersionUID = 1L;
		
    private Box hoopPropertyBox=null;
    private JScrollPane contentScroller=null;
    private JPanel contentBox=null;
    
    private JButton expandButton=null;
    private JButton foldButton=null;
    
    private JToggleButton linkButton=null;
    private Boolean viewsLinked=false;
    
    private Component panelGlue=null;
    	
	/**
	 * Creates a new JPanel with a double buffer and a flow layout.
	 */	
	public HoopPropertyPanel()
	{		
		super (HoopLink.getImageByName("gtk-properties.png"));
		
		setClassName ("HoopPropertyPanel");
		debug ("HoopPropertyPanel ()");
						
		hoopPropertyBox=new Box (BoxLayout.Y_AXIS);
		hoopPropertyBox.setMinimumSize(new Dimension (20,20));
		hoopPropertyBox.setPreferredSize(new Dimension (200,300));
		
		linkButton=new JToggleButton ();
		linkButton.setIcon(HoopLink.getImageByName("link-views.png"));		
		linkButton.setMargin(new Insets(1, 1, 1, 1));
		linkButton.setPreferredSize(new Dimension (20,20));
		linkButton.addActionListener(this);		
		
	    expandButton=new JButton ();
	    expandButton.setFont(new Font("Dialog", 1, 8));
	    expandButton.setPreferredSize(new Dimension (20,20));
	    expandButton.setMaximumSize(new Dimension (20,20));
	    expandButton.setIcon(HoopLink.getImageByName("tree-expand-icon.png"));
	    expandButton.addActionListener(this);
	    
	    foldButton=new JButton ();
	    foldButton.setFont(new Font("Dialog", 1, 8));
	    foldButton.setPreferredSize(new Dimension (20,20));
	    foldButton.setMaximumSize(new Dimension (20,20));
	    foldButton.setIcon(HoopLink.getImageByName("tree-fold-icon.png"));
	    foldButton.addActionListener(this);		
		
		Box controlBox = new Box (BoxLayout.X_AXIS);
		controlBox.add(linkButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add(expandButton);
		controlBox.add (Box.createRigidArea(new Dimension(2,0)));
		controlBox.add(foldButton);			
		controlBox.add(Box.createHorizontalGlue());
          		
		contentBox=new JPanel ();
		contentBox.setLayout(new GridBagLayout());
		contentBox.setBackground(new Color (220,220,220));
		contentBox.setOpaque(true);
		contentBox.setMinimumSize(new Dimension (20,20));
        		
		//Border border=BorderFactory.createLineBorder(Color.red);
		
        contentScroller=new JScrollPane (contentBox);
        //contentScroller.setBorder(border);        
        contentScroller.setMinimumSize(new Dimension (20,20));
        contentScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        hoopPropertyBox.add (controlBox);
        hoopPropertyBox.add (Box.createRigidArea(new Dimension(0,2)));
        hoopPropertyBox.add (contentScroller);
                                
        //this.add (hoopPropertyBox);
        this.setContentPane(hoopPropertyBox);
	}
	/**
	 * Remove all panels
	 */
	public void reset ()
	{
		debug ("reset ()");
		
		ArrayList <HoopBase> hoops=HoopLink.hoopGraphManager.getHoopList ();
		
		for (int i=0;i<hoops.size();i++)
		{
			removePropertyPanel (hoops.get(i));
		}	
	}
	/**
	 * 
	 */
	public static void popupPropertyPanel (HoopBase aHoop)
	{
		HoopBase.debug ("HoopNodePanel","popupPropertyPanel ()");
		
		if (aHoop==null)
		{
			HoopBase.debug ("HoopNodePanel","Error no hoop object available in node panel");
			return;
		}
		
		HoopPropertyPanel propPanel=(HoopPropertyPanel) HoopLink.getWindow("Properties");
		
		if ((aHoop.getPropertiesPanel()!=null) || (aHoop.getProperties().size()>0))
		{	
			if ((propPanel!=null) && (aHoop.getClassName().equals("HoopStart")==false))
			{
				HoopInspectablePanel propertiesPanel=new HoopInspectablePanel (aHoop.getHoopDescription());

				propPanel.addPropertyPanel (propertiesPanel);

				JPanel hoopPanel=aHoop.getPropertiesPanel();

				if (hoopPanel!=null)
				{
					propertiesPanel.setPanelContent (hoopPanel,aHoop);
					hoopPanel.setPreferredSize(propertiesPanel.getCurrentDimensions ());
				}
				else
				{
					HoopBase.debug ("HoopNodePanel","No custom config panel provided, switching to properties panel instead");
					propertiesPanel.setHoop(aHoop);
				}
			}
		}			
	}	
	/**
	 * 
	 */
	public void addPropertyPanel (HoopInspectablePanel aPanel)
	{
		debug ("addPropertyPanel ("+(contentBox.getComponentCount()-1)+")");

		if (panelGlue!=null)
			contentBox.remove(panelGlue);
		
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor=GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;		
						
		gbc.gridy = contentBox.getComponentCount();
		contentBox.add (aPanel,gbc);
		
        gbc = new GridBagConstraints();
		gbc.weighty = 1;
		gbc.gridy = contentBox.getComponentCount();
		
		panelGlue=Box.createVerticalGlue();				
		contentBox.add (panelGlue,gbc);
		
		contentBox.revalidate();
		
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
			Object tester=childList [i];
			
			if (tester instanceof HoopInspectablePanel)
			{
				HoopInspectablePanel panel=(HoopInspectablePanel) childList [i];
		
				if (panel.getHoop ()==aHoop)
				{
					contentBox.remove(panel);
				}
			}	
		}	
	}	
	/**
	 * 
	 */
	public void highlightHoop (HoopBase aHoop)
	{
		debug ("highlightHoop ()");
		
		if (this.viewsLinked==false)
			return;
		
		Component [] childList=contentBox.getComponents();
		
		for (int i=0;i<childList.length;i++)
		{
			HoopInspectablePanel tester=(HoopInspectablePanel) childList [i];
		
			if (tester.getHoop ()==aHoop)
			{
				tester.setHighlighted(true);
			}
			else
				tester.setHighlighted(false);				
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
			
			if (linkButton.isSelected()==true)
			{
				this.setViewsLinked(true);
			}
			else
				this.setViewsLinked(false);
		}
		
		if (control==foldButton)
		{
			debug ("Folding all ...");
			
			Component [] childList=contentBox.getComponents();
			
			for (int i=0;i<childList.length;i++)
			{
				Object checker=childList [i];
				
				if (checker instanceof HoopInspectablePanel)
				{
					HoopInspectablePanel tester=(HoopInspectablePanel) checker;
			
					tester.foldIn();
				}	
			}				
		}
		
		if (control==expandButton)
		{
			debug ("Expanding all ...");
			
			Component [] childList=contentBox.getComponents();
			
			for (int i=0;i<childList.length;i++)
			{
				Object checker=childList [i];
				
				if (checker instanceof HoopInspectablePanel)
				{
					HoopInspectablePanel tester=(HoopInspectablePanel) checker;
			
					tester.foldOut();
				}
			}					
		}
	}
	/**
	 * 
	 */
	public void setViewsLinked(Boolean viewsLinked) 
	{
		this.viewsLinked = viewsLinked;
	}
	/**
	 * 
	 */
	public Boolean getViewsLinked() 
	{
		return viewsLinked;
	}
	/**
	 * 
	 */
	public void listViews ()
	{
		debug ("listViews ()");
		
		Component [] clist=contentBox.getComponents();
		
		for (int i=0;i<clist.length;i++)
		{
			Component test=clist [i];
			
			debug ("Component " + i + " is a: " + test.toString());
		} 
	}
}
