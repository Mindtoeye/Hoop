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

/**
* 
*/
public class HoopSaveBase extends HoopIOBase implements HoopInterface
{    				
	private static final long serialVersionUID = 5440684516502943618L;
	private String content=null;
	
	/**
	 *
	 */
    public HoopSaveBase () 
    {
		setClassName ("HoopSaveBase");
		debug ("HoopSaveBase ()");
		
		setHoopCategory ("Save");
		
		setHoopDescription ("Abstract Hoop Saving");
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
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
						
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopSaveBase ());
	}	
}
