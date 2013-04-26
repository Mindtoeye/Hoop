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
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellRenderer;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopVFSL;
import edu.cmu.cs.in.controls.base.HoopJPanel;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

/*
 * 
 */
class HoopSheetPathRenderer extends HoopJPanel implements TableCellRenderer, ActionListener 
{
	private static final long serialVersionUID = 1L;

	private JButton chooser = null;
	private JButton dirChooser = null;
	private JTextField chosenPath;
		
	private HoopURISerializable pathObject=null;
	
	/**
	 *
	 */  
	public HoopSheetPathRenderer() 
	{		
		setClassName ("HoopSheetPathRenderer");
		debug ("HoopSheetPathRenderer ()");
		
		this.setLayout(new BoxLayout (this,BoxLayout.X_AXIS));
		
		chosenPath=new JTextField ();
		chosenPath.setFont(new Font("Dialog", 1, 10));
		
		chooser = new JButton();
		chooser.setIcon(HoopLink.getImageByName("gtk-new.png"));
		chooser.setBorder(BorderFactory.createEmptyBorder(1,0,1,0));
		chooser.setMargin(new Insets (0,0,0,0));
		chooser.setOpaque(false);
		chooser.setPreferredSize(new Dimension (20,22));
		chooser.setMaximumSize(new Dimension (20,22));		
		chooser.addActionListener(this);

		dirChooser = new JButton();
		dirChooser.setIcon(HoopLink.getImageByName("folder.png"));
		dirChooser.setBorder(BorderFactory.createEmptyBorder(1,0,1,0));
		dirChooser.setMargin(new Insets (0,0,0,0));
		dirChooser.setOpaque(false);
		dirChooser.setPreferredSize(new Dimension (20,22));
		dirChooser.setMaximumSize(new Dimension (20,22));		
		dirChooser.addActionListener(this);		
		
		this.add (chosenPath);
		this.add (chooser);
		this.add (dirChooser);
	}
	/**
	 * 
	 */
	public HoopURISerializable getPathObject() 
	{
		return pathObject;
	}
	/**
	 * 
	 */
	public void setPathObject(HoopURISerializable pathObject) 
	{
		this.pathObject = pathObject;
		chosenPath.setText(pathObject.getValue());
	}
	/**
	 * 
	 */
	public String getPath ()
	{
		return (pathObject.getValue());
	}
	/**
	 * 
	 */
	public JTextField getPathEditor ()
	{
		return (chosenPath);
	}
	/**
	 * 
	 */
	private void changePath (String aPath)
	{
		if (pathObject.getValue().equals(aPath)==true)
		{
			// Already set
			return;
		}
		
		String translated=HoopVFSL.absoluteToRelative(aPath);
		
		chosenPath.setText (translated);	
		pathObject.setValue (translated);
		
		debug ("New path set to: " + pathObject.getValue());			
	}
	/**
	 * 
	 */
	public void processMouseclick (int anX,int anY)
	{
		debug ("processMouseClick ("+anX+","+anY+")");
		
		if (anX<chosenPath.getWidth())		
		{
			
		}
		else
		{
			if ((anX>chosenPath.getWidth()) && (anX<(chosenPath.getWidth()+chooser.getWidth())))
			{
				pathObject.setDirsOnly(false);
				chooser.doClick();
			}
			else
			{
				pathObject.setDirsOnly(true);
				dirChooser.doClick();
			}
		}
	}
	/**
	 * 
	 */
	@Override
	public Component getTableCellRendererComponent (JTable table, 
													Object aValue,
													boolean isSelected, 
													boolean hasFocus,
													int row, 
													int column) 
	{
		//debug ("getTableCellRendererComponent ("+aValue.getClass().getName()+")");
		
    	if (aValue instanceof String)
    	{
    		String safety=(String) aValue;
    		debug ("For some reason we're now getting a string back, going into safety mode ...");
        	debug ("Parsing: " + safety);
        	
        	changePath (safety);    		
    	}
    	else
    	{
    		
    		HoopSerializableTableEntry value=(HoopSerializableTableEntry) aValue;
    	
    		HoopSerializable object=(HoopSerializable) value.getEntry();
    		
    		//debug ("Setting path to: " + object.getValue());
    		
    		changePath (object.getValue());
    	}	
		
		return this;
	}
	/**
	 * 
	 */
	@Override	
	public void actionPerformed(ActionEvent arg0) 
	{
		debug ("actionPerformed ("+pathObject.getFileExtension()+")");
		
	    JFileChooser fc=new JFileChooser ();
	    
	    if (pathObject.getDirsOnly()==false)
	    {
			FileNameExtensionFilter filter=new FileNameExtensionFilter (pathObject.getFileExtension()+" files", "." + pathObject.getFileExtension());
			fc.setFileFilter(filter);
			
			int returnVal=fc.showOpenDialog (this);

			if (returnVal==JFileChooser.APPROVE_OPTION) 
			{
		       	File file = fc.getSelectedFile();
		       	
		       	changePath (file.getAbsolutePath());
			}	    	
	    }	 
	    else
	    {			
			FileNameExtensionFilter filter=new FileNameExtensionFilter ("Target Directories", "Directories");
			fc.setFileFilter(filter);    			
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);	    	
	    	
			int returnVal=fc.showOpenDialog (this);

			if (returnVal==JFileChooser.APPROVE_OPTION) 
			{
		       	File file = fc.getSelectedFile();
		       	
		       	changePath (file.getAbsolutePath());
			}	    	
	    }		
	}	
}
