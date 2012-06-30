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

package edu.cmu.cs.in.hoop.project;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Element;

/** 
 * @author Martin van Velsen
 */
public class INHoopProject extends INHoopProjectFile
{
	private ArrayList <INHoopProjectFile> files=null;
	
	/**
	 * 
	 */
	public INHoopProject ()
	{
		setClassName ("INHoopProject");
		debug ("INHoopProject ()");
		
		files=new ArrayList<INHoopProjectFile> ();
	}
	/**
	 * 
	 */
	public Boolean newProject (String aURI)
	{
		debug ("newProject ("+aURI+")");
		
		setFileURI (aURI+"/.hprj");
		
		return (true);
	}
	/**
	 * 
	 */
	public Boolean save ()
	{
		debug ("save ()");
		
		for (int i=0;i<files.size();i++)
		{
			INHoopProjectFile tFile=files.get(i);
			
			tFile.save();
		}		
		
		return (true);
	}	
	/**
	 * 
	 */
	public Boolean saveAs (String aURI)
	{
		debug ("saveAs ("+aURI+")");
		
		return (true);
	}		
	/**
	 * 
	 */
	public Boolean load (String aURI)
	{
		debug ("load ("+aURI+")");
		
		File fLoader=new File (aURI);
		if (fLoader.isDirectory()==true)
		{
			setFileURI (aURI+"/.hprj");
		}
		
		if (fManager.doesFileExist (this.getFileURI())==false)
		{
			debug ("Error, file does not exist: " + this.getFileURI());
			return (false);
		}
		
		return (true);
	}
	/**
	*
	*/	
	public Boolean fromXML(Element root) 
	{
		debug ("fromXML ()");
				
		return (true);
	}	
	/**
	*
	*/	
	public String toXML() 
	{
		debug ("toXML ()");
	
		StringBuffer formatted=new StringBuffer ();
		formatted.append (super.toXML());
				
		return (formatted.toString());
	}		
}
