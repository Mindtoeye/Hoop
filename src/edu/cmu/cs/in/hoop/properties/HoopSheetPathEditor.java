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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.cmu.cs.in.base.io.HoopFileFilter;
import edu.cmu.cs.in.base.io.HoopVFSL;
import edu.cmu.cs.in.controls.base.HoopJPanel;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

/*
 * 
 */
class HoopSheetPathEditor extends HoopJPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;		
	private HoopURISerializable pathObject=null;
		
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
	private void changePath (String aPath)
	{
		//debug ("changePath ("+aPath+")");
		
		if (pathObject.getValue().equals(aPath)==true)
		{
			// Already set
			return;
		}
		
		String translated=HoopVFSL.absoluteToRelative(aPath);
		
		pathObject.setValue (translated);
		
		if (translated.indexOf(pathObject.getFileExtension())==-1)
		{
			// It's missing the file extension
			
			pathObject.setValue (translated+"."+pathObject.getFileExtension());
		}
				
		debug ("New path set to: " + pathObject.getValue());
	}
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent actionEvent) 
	{
		debug ("actionPerformed ("+pathObject.getFileExtension()+")");
		
	    JFileChooser fc=new JFileChooser ();
	    
	    if (pathObject.getDirsOnly()==false)
	    {
			//FileNameExtensionFilter filter=new FileNameExtensionFilter (pathObject.getFileExtension()+" files", pathObject.getFileExtension());
			fc.setFileFilter(new HoopFileFilter(pathObject.getFileExtension()));
			
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
