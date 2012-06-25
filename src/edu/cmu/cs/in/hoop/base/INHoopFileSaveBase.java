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

package edu.cmu.cs.in.hoop.base;

import edu.cmu.cs.in.base.io.INFileManager;

/**
* 
*/
public class INHoopFileSaveBase extends INHoopBase implements INHoopInterface
{    				
	private String content=null;
	
	private INFileManager fManager=null;
	
	private String outputStreamPath=null;	
	
	/**
	 *
	 */
    public INHoopFileSaveBase () 
    {
		setClassName ("INHoopFileSaveBase");
		debug ("INHoopFileSaveBase ()");
		setHoopCategory ("save");
		
		setHoopDescription ("Save To File");
    }
	/**
	 *
	 */	
	public void setOutputStreamPath(String outputStreamPath) 
	{
		this.outputStreamPath = outputStreamPath;
	}
	/**
	 *
	 */	
	public String getOutputStreamPath() 
	{
		return outputStreamPath;
	}	    
	/**
	 *
	 */
	public void setContent(String content) 
	{
		this.content = content;
	}
	/**
	 *
	 */
	public String getContent() 
	{
		return content;
	}    
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		super.runHoop(inHoop);		
		
		fManager.saveContents(outputStreamPath, content);
				
		return (true);
	}		
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopFileSaveBase ());
	}	
}
