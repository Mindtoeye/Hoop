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

import edu.cmu.cs.in.base.INFileManager;

/**
* 
*/
public class INHoopFileLoadBase extends INHoopBase
{    				
	private String content=null;
	private INFileManager fManager=null;
	
	private String inputStreamPath=null;
	
	/**
	 *
	 */
    public INHoopFileLoadBase () 
    {
		setClassName ("INHoopFileLoadBase");
		debug ("INHoopFileLoadBase ()");
		
		setHoopCategory ("load");
		fManager=new INFileManager ();
		
		setHoopDescription ("Load From File");
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
	public Boolean runHoop ()
	{
		setContent (fManager.loadContents(inputStreamPath));
		
		return (true);
	}
	/**
	 *
	 */	
	public void setInputStreamPath(String inputStreamPath) 
	{
		this.inputStreamPath = inputStreamPath;
	}
	/**
	 *
	 */	
	public String getInputStreamPath() 
	{
		return inputStreamPath;
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopFileLoadBase ());
	}	
}
