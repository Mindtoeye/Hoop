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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.project.HoopFile;
import edu.cmu.cs.in.hoop.project.HoopProject;
import edu.cmu.cs.in.hoop.project.HoopProjectFile;

/**
 *
 */
public class HoopJTreeHoopRenderer extends JLabel implements TreeCellRenderer
{
	private static final long serialVersionUID = -1L;
	private JTree tree=null;
	private Border border=null;
	
	/**
	 * 
	 */
	public HoopJTreeHoopRenderer ()
	{	
		border = BorderFactory.createEmptyBorder (1,1,1,2);
		
		this.setFont(new Font("Dialog", 1, 10));
		this.setOpaque(true);
		this.setBorder (border);
	}
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopBase.debug("HoopJTreeHoopRenderer",aMessage);
	}	
	/**
	 * 
	 */
	public void setTree (JTree aTree)
	{
		tree=aTree;
	}
	/**
	 * 
	 */
	public JTree getTree ()
	{
		return (tree);
	}	
	/**
	 * 
	 */
    public Component getTreeCellRendererComponent (JTree tree,
    											   Object value,
    											   boolean isSelected,
    											   boolean isExpanded, 
    											   boolean isLeaf, 
    											   int row, 
    											   boolean hasFocus) 
    {
    	//debug ("getTreeCellRendererComponent ()");
    	
    	DefaultMutableTreeNode node=(DefaultMutableTreeNode) value;
    	    	
    	Object userObject=node.getUserObject();
    	    	
    	if (userObject!=null)
    	{
    		//debug ("We have a userObject");
    		
    		if (userObject instanceof String)
    		{
    			//debug ("userObject is instance of String");
    			
    			setText ((String) userObject);    			
    			setIcon (HoopLink.getImageByName("unknown_216_16.png"));
    		}
    		else
    		{
    			Boolean found=false;
    			
    			//>-----------------------------------------------------------------
    			
    			if ((userObject instanceof HoopBase) && (found==false))
    			{
    				//debug ("userObject is instance of HoopBase");
    				
    				HoopBase hoop=(HoopBase) userObject;
    				setText (hoop.getHoopDescription());
    				setIcon (HoopLink.getImageByName("hoop.png"));
    				
    				found=true;
    			}
    			
    			//>-----------------------------------------------------------------    			
    			
    			if ((userObject instanceof HoopProject) && (found==false))
    			{
    				HoopProject aFile=(HoopProject) userObject;
    				setText (aFile.getName());
   					setIcon (HoopLink.getImageByName("folder.png"));
   					
   					found=true;
    			}
    			
    			//>-----------------------------------------------------------------    			
    			
    			if ((userObject instanceof HoopProjectFile) && (found==false))
    			{
    				//debug ("userObject is instance of HoopProjectFile");
    				
    				HoopProjectFile aFile=(HoopProjectFile) userObject;
    				setText (aFile.getName());
    				
    				if (aFile.getInstanceName().toLowerCase().indexOf(".xml")!=-1)
    				{
    					debug ("userObject represents an xml file");
    					
    					setIcon (HoopLink.fileToIcon(aFile.getInstanceName()));
    				}
    				else
    				{
    					setIcon (HoopLink.fileToIcon(aFile.getInstanceName()));
    					
    					/*
        				if (aFile.getInstanceName().toLowerCase().indexOf(".txt")!=-1)
        				{
        					debug ("userObject represents a text file");
        					
        					setIcon (HoopLink.getImageByName("text_icon.png"));
        				}
        				else
        				{
        					debug ("userObject represents a default file");
        					
        					setIcon (HoopLink.getImageByName("unknown_216_16.png"));
        				}
        				*/
    				}
    				
    				found=true;
    			}    			
    			
    			//>-----------------------------------------------------------------    			
    			
    			if ((userObject instanceof HoopFile) && (found==false))
    			{
    				debug ("userObject is instance of HoopFile");
    				
    				HoopFile fileIcon=(HoopFile) userObject;
    				setText (fileIcon.getInstanceName());
    				
    				if (fileIcon.getIsDir()==true)
    				{
    					if (
    						 (fileIcon.getInstanceName().toLowerCase().equals("data")==true) ||
    						 (fileIcon.getInstanceName().toLowerCase().equals("db")==true) ||
    						 (fileIcon.getInstanceName().toLowerCase().equals("system")==true)
    						)
    						setIcon (HoopLink.getImageByName("system-folder.png"));
    					else
    						setIcon (HoopLink.getImageByName("folder.png"));    					 
    				}
    				else
    				{
    					setIcon (HoopLink.getImageByName("gtk-new.png"));
    				}
    				
    				found=false;
    			}
    			
    			//>-----------------------------------------------------------------
    		}
    	}
    	else
    	{
    		//debug ("No user object in node, using toString instead");
    		setText (value.toString());
    	}
    	
    	if (isSelected==true)
    		this.setBackground(new Color (220,220,220));
    	else
    		this.setBackground(new Color (255,255,255));
    	
    	setEnabled(tree.isEnabled());
    	
    	return this;
    }   
}