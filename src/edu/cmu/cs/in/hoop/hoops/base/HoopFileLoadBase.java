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

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopFileManager;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

/**
* 
*/
public class HoopFileLoadBase extends HoopLoadBase implements HoopInterface
{    				
	private HoopFileManager fManager=null;
	private HoopKVString fileKV=null;		
	protected HoopURISerializable URI=null;
	
	/**
	 *
	 */
    public HoopFileLoadBase () 
    {
		setClassName ("HoopFileLoadBase");
		debug ("HoopFileLoadBase ()");
		
		setHoopDescription ("Load Text from a File");
		
		fManager=new HoopFileManager ();
			
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
		
		String contents=fManager.loadContents(HoopLink.relativeToAbsolute(URI.getValue()));
		
		if (contents==null)
		{
			this.setErrorString(fManager.getErrorString());
			return (false);
		}	
		
		fileKV=new HoopKVString ();
		
		fileKV.setKeyString(fManager.getURI());
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
}
