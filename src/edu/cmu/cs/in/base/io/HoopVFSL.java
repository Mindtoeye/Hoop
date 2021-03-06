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

package edu.cmu.cs.in.base.io;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Stack;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;

/**
 * Notes:

 * When combining jar as a FS with HDFS, use fully qualified
 * URIs to detect how file systems should be mounted. For
 * example, we could have:
 * 
 * 		file://textfile.txt
 * 		file://archive.jar?textfile.txt
 * 		hdfs://archive.jar?textfile.txt
 * 
 * The last one would result in a set of layers that would
 * be: hdfs -> jar -> file
 */
public class HoopVFSL extends HoopRoot implements HoopVFSLInterface
{	
	private ArrayList<HoopVFSLInterface> layers=null;
		
	/**
	 * 
	 */
	public HoopVFSL ()
	{
		setClassName ("HoopVFSL");
		debug ("HoopVFSL ()");		
		
		layers=new ArrayList<HoopVFSLInterface> ();
		layers.add(new HoopFileManager ());
		
		if (HoopLink.projectPathStack==null)
			HoopLink.projectPathStack=new Stack<String> ();
	}
	/**
	 * 
	 */
	@Override
	public StringBuilder getContents() 
	{
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.getContents());
		}
		
		return null;
	}
	/**
	 * 
	 */
	@Override
	public String getURI() 
	{	
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.getURI());
		}
		
		return null;
	}

	/**
	 * 
	 */
	@Override
	public void setURI(String uRI) 
	{
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			aLayer.setURI(uRI);
		}
	}

	/**
	 * 
	 */
	@Override
	public boolean isStreamOpen() 
	{
		//debug ("isStreamOpen ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.isStreamOpen());
		}
		
		return false;
	}

	/**
	 * 
	 */
	@Override
	public Writer getOutputStream() 
	{
		//debug ("getOutputStream ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.getOutputStream());
		}
		
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public OutputStream getOutputStreamBinary() 
	{
		//debug ("getOutputStreamBinary ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.getOutputStreamBinary());
		}
		
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean openStream(String aFileURI) 
	{
		//debug ("openStream ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.openStream(aFileURI));
		}
		
		return false;
	}

	@Override
	public boolean openStreamBinary(String aFileURI) 
	{
		//debug ("openStreamBinary ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.openStreamBinary(aFileURI));
		}
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public void closeStream() 
	{
		//debug ("closeStream ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			aLayer.closeStream();
		}
	}

	/**
	 * 
	 */
	@Override
	public void writeToStream(String aContents) 
	{
		//debug ("writeToStream ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			aLayer.writeToStream(aContents);
		}
	}

	/**
	 * 
	 */
	@Override
	public String loadContents(String aFileURI) 
	{
		//debug ("loadContents ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.loadContents(aFileURI));
		}
		
		return null;
	}

	/**
	 * 
	 */
	@Override
	public boolean saveContents(String aFileURI, String aContents) 
	{
		//debug ("saveContents ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.saveContents(aFileURI, aContents));
		}
		
		return false;
	}

	/**
	 * 
	 */
	@Override
	public boolean appendContents(String aFileURI, String aContents) 
	{
		//debug ("appendContents ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.appendContents(aFileURI, aContents));
		}
		
		return false;
	}

	/**
	 * 
	 */
	@Override
	public boolean createDirectory(String aDirURI) 
	{
		//debug ("createDirectory ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.createDirectory(aDirURI));
		}
		
		return false;
	}

	/**
	 * 
	 */
	@Override
	public boolean removeDirectory(String aDirURI) 
	{
		//debug ("removeDirectory ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.removeDirectory(aDirURI));
		}
		
		return false;
	}

	/**
	 * 
	 */	
	@Override
	public boolean doesFileExist(String aFileURI) 
	{
		//debug ("doesFileExist ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.doesFileExist(aFileURI));
		}
		
		return false;
	}

	/**
	 * 
	 */	
	@Override
	public ArrayList<String> listDirectoryEntries(String aDirURI) 
	{
		//debug ("listDirectoryEntries ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.listDirectoryEntries(aDirURI));
		}
		
		return null;
	}

	/**
	 * 
	 */	
	@Override
	public ArrayList<String> listFiles(String aDirURI) 
	{
		//debug ("listFiles ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.listFiles(aDirURI));
		}
		
		return null;
	}

	/**
	 * 
	 */
	@Override
	public Long getFileTime(String aPath) 
	{
		//debug ("getFileTime ()");
		
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.getFileTime(aPath));
		}
		
		return null;
	}

	@Override
	public InputStream openInputStream(String aFileURI) 
	{
		if (layers.size()>0)
		{
			HoopVFSLInterface aLayer=layers.get(0);
			return (aLayer.openInputStream (aFileURI));
		}
		
		return null;
	}
	/**
	 * 
	 */
	public static String relativeToAbsolute (String aPath)
	{		
		HoopRoot.debug ("HoopVFSL","relativeToAbsolute (" + aPath + ")");
		
		if (HoopLink.project==null)
			return (aPath); // Nothing to do

		if (HoopLink.project.getVirginFile()==true)
			return (aPath);
		
		if (aPath.indexOf(HoopLink.PROJECTPATHMARKER)==-1)
		{
			HoopRoot.debug ("HoopVFSL","Resource does not contain " + HoopLink.PROJECTPATHMARKER);

			HoopVFSL.listProjectPaths ();
			
			if (HoopLink.projectPathStack.size()>1)
			{								
				//HoopRoot.debug ("HoopVFSL","However we have a modified project-relative path stack ("+HoopVFSL.projectPathStack.size()+"), using that ...");
				
				HoopRoot.debug ("HoopVFSL","Patching with: " + HoopLink.projectPathStack.get(0));
				
				return (HoopLink.projectPathStack.get(HoopLink.projectPathStack.size()-1) + File.separator + aPath);
			}
			else
				HoopRoot.debug ("HoopVFSL","We do not have a project stack with a depth greater than 1: " + HoopLink.projectPathStack.size());
			
			return (aPath);
		}
				
		HoopRoot.debug ("HoopVFSL","Replacing " + HoopLink.PROJECTPATHMARKER + " ...");
		
		StringBuffer formatted=new StringBuffer ();
		
		String lastPart=aPath.substring(HoopLink.PROJECTPATHMARKER.length()); // index of <PROJECTPATH> which is 13 long
		
		if (HoopLink.projectPathStack.size()==0)
		{
			HoopVFSL.pushProjectPath(HoopLink.project.getBasePath());
		}
		
		String projectPath=HoopLink.projectPathStack.get(HoopLink.projectPathStack.size()-1);
		
		HoopRoot.debug ("HoopLink","Using as the project base: " + projectPath);
		
		formatted.append (projectPath);
		formatted.append (File.separator);
		formatted.append (lastPart);
		
		return (formatted.toString());
	}
	/**
	 * 
	 */
	public static String absoluteToRelative (String aPath)
	{				
		if (HoopLink.project==null)
			return (aPath);
		
		if (HoopLink.project.getVirginFile()==true)
			return (aPath);
		
		String projectPath=HoopLink.project.getBasePath();
		
		if (aPath.indexOf(projectPath)==-1)
			return (aPath);
		
		HoopRoot.debug ("HoopLink","Subtracting ["+HoopLink.project.getBasePath()+"] from path: " + aPath);
		
		String remainder=aPath.substring(HoopLink.project.getBasePath().length());
		
		StringBuffer formatted=new StringBuffer ();
		
		formatted.append("<PROJECTPATH>");

		formatted.append(remainder);
		
		return (formatted.toString());
	}		
	/**
	 * 	
	 */
	public static void pushProjectPath (String aPath)
	{
		HoopRoot.debug ("HoopVFSL","pushProjectPath ("+aPath+")");
		
		HoopLink.projectPathStack.push(aPath);
		
		HoopLink.PROJECTPATH=aPath;
	}
	/**
	 * 
	 */
	public static void listProjectPaths ()
	{
		HoopRoot.debug("HoopVFSL","listProjectPaths ()");
		
		for (int i=0;i<HoopLink.projectPathStack.size();i++)
		{
			HoopRoot.debug("HoopVFSL","Path ["+i+"] " + HoopLink.projectPathStack.get(i));
		}
	}
	/**
	 * 
	 */
	public static void popProjectPath ()
	{
		HoopLink.PROJECTPATH=HoopLink.projectPathStack.pop();
	}	
}
