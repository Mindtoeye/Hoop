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

package edu.cmu.cs.in.hoop.hoops.base;

import java.io.File;
import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

/**
* 
*/
public class HoopFileLoadBase extends HoopLoadBase implements HoopInterface
{    				
	protected HoopKVString fileKV=null;		
	protected HoopURISerializable URI=null;
	
	private Integer fileIndex=0;
	private ArrayList <String> files=null;
	
	/**
	 *
	 */
    public HoopFileLoadBase () 
    {
		setClassName ("HoopFileLoadBase");
		debug ("HoopFileLoadBase ()");
		
		setHoopDescription ("Load Text File(s)");
					
		URI=new HoopURISerializable (this,"URI","");
    }
	/**
	 * 
	 */
	public String getFileExtension() 
	{
		return URI.getFileExtension();
	}
	/**
	 * 
	 */	
	public void setFileExtension(String fileExtension) 
	{
		this.setFileExtension(fileExtension);
	}    
	/**
	 *
	 */
	public void setContent(String content) 
	{
		fileKV.setValue(content);
	}
	/**
	 *
	 */
	public String getContent() 
	{
		return fileKV.getValue();
	}
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		if (URI.getValue().indexOf("<PROJECTPATH>")!=-1)
		{
			if (HoopLink.project==null)
			{
				this.setErrorString ("You need a project first for this hoop to be useful");
				return (false);
			}
		
			if (HoopLink.project.getVirginFile()==true)
			{
				this.setErrorString ("You to save your project first for this hoop to be useful");
				return (false);
			}
		}	
				
		if (URI.getDirsOnly()==false)
		{				
			if (processSingleFile (HoopLink.relativeToAbsolute(URI.getValue()))==false)
				return (false);
		}
		
		if (URI.getDirsOnly()==true)
		{
			if (files==null)
			{
				fileIndex=0;
				
				files=new ArrayList<String> ();
				
				ArrayList <String> tempList=HoopLink.fManager.listFiles(URI.getValue());
								
				for (int i=0;i<tempList.size ();i++)
				{
					String testEntry=tempList.get(i);
					
					if ((testEntry.equals(".")==false) && (testEntry.equals("..")==false))
					{
						File finalTest=new File (testEntry);
						
						if (finalTest.isDirectory()==false)
						{
							files.add(testEntry);
						}
					}
				}
				
				showFiles ();				
			}

			String nextFile=files.get(fileIndex);
			
			if (processSingleFile (URI.getValue()+"/"+nextFile)==false)
			{
				return (false);
			}
			
			String aStatus=(fileIndex + " out of " + files.size());
			
			getVisualizer ().setExecutionInfo (aStatus);
			
			fileIndex++;
			
			if (fileIndex<files.size())
			{
				this.setDone(false);
			}
		}
		
		return (true);
	}	
	/**
	 * 
	 */
	private Boolean processSingleFile (String aPath)
	{
		debug ("processSingleFile ("+aPath+")");
		
		String contents=HoopLink.fManager.loadContents(aPath);
		
		if (contents==null)
		{
			this.setErrorString(HoopLink.fManager.getErrorString());
			return (false);
		}
		
		fileKV=new HoopKVString ();
	
		fileKV.setKeyString(HoopLink.fManager.getURI());
		fileKV.setValue(contents);
							
		addKV (fileKV);
		
		return (true);
	}
	/**
	 *
	 */	
	public void setInputStreamPath(String inputStreamPath) 
	{
		URI.setValue(inputStreamPath);
	}
	/**
	 *
	 */	
	public String getInputStreamPath() 
	{
		return URI.getValue();
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopFileLoadBase ());
	}		
	/**
	 * 
	 */
	public void showFiles ()
	{
		debug ("showFiles ()");
		
		for (int i=0;i<files.size();i++)
		{
			String aFile=files.get(i);
			
			debug ("File: " + aFile);
		}
	}
}
