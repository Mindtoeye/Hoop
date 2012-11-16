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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopJFileChooser extends JFileChooser implements ActionListener
{
	private static final long serialVersionUID = 7100156718902543535L;

	/**
	 * Constructs a JFileChooser pointing to the user's default directory.
	 */
	public HoopJFileChooser()
	{
		String lastPath=HoopLink.preferences.get ("lastDir","");
		
		if (lastPath.isEmpty()==false)
		{
			this.setCurrentDirectory(new File (lastPath));
		}
		
		this.addActionListener(this);
	}
	/**
	 * Constructs a JFileChooser using the given File as the path.
	 */
	public HoopJFileChooser(File currentDirectory)
	{
		super (currentDirectory);

		HoopLink.preferences.put("lastDir",currentDirectory.getAbsolutePath());
		
		this.addActionListener(this);
	}
	/**
	 * Constructs a JFileChooser using the given current directory and FileSystemView.
	 */
	public HoopJFileChooser(File currentDirectory, FileSystemView fsv)
    {
		super (currentDirectory,fsv);
		
		HoopLink.preferences.put("lastDir",currentDirectory.getAbsolutePath());
		
		this.addActionListener(this);
    } 
	/**
	 * Constructs a JFileChooser using the given FileSystemView.
	 */
	public HoopJFileChooser(FileSystemView fsv)
	{
		super (fsv);
		
		String lastPath=HoopLink.preferences.get ("lastDir","");
		
		if (lastPath.isEmpty()==false)
		{
			this.setCurrentDirectory(new File (lastPath));
		}		
		
		this.addActionListener(this);
	}
	/**
	 * Constructs a JFileChooser using the given path.
	 */
	public HoopJFileChooser(String currentDirectoryPath)
	{
		super (currentDirectoryPath);
		
		HoopLink.preferences.put("lastDir",currentDirectoryPath);
		
		this.addActionListener(this);
	}
	/**
	 * Constructs a JFileChooser using the given current directory path and FileSystemView.
	 */
	public HoopJFileChooser(String currentDirectoryPath, FileSystemView fsv)
	{
		super (currentDirectoryPath,fsv);
		
		HoopLink.preferences.put("lastDir",currentDirectoryPath);
		
		this.addActionListener(this);
	}
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopRoot.debug("HoopJFileChooser",aMessage);
	}	
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		debug ("actionPerformed ("+e.getActionCommand()+")");
		
		if (e.getActionCommand().equals("ApproveSelection")==true)
		{
			HoopLink.preferences.put("lastDir",this.getCurrentDirectory().getAbsolutePath());
		}
	}
}
