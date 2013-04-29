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

package edu.cmu.cs.in.hoop.project.export;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.project.HoopProject;

/**
 * 
 */
public class HoopProjectExportInterface extends HoopRoot
{
	private String description="Undefined";
	
	/**
	 * 
	 */
	public HoopProjectExportInterface ()
	{
		setClassName ("HoopProjectExportInterface");
		debug ("HoopProjectExportInterface ()");		
	}
	/**
	 * 
	 */
	public Boolean exportProject (HoopProject aProject)
	{
		debug ("exportProject ()");
		
		return (true);
	}
	/**
	 * 
	 */
	public String getDescription() 
	{
		return description;
	}
	/**
	 * 
	 */
	public void setDescription(String description) 
	{
		this.description = description;
	}
}
