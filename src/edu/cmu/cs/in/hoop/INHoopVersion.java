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

package edu.cmu.cs.in.hoop;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Date;

import edu.cmu.cs.in.base.INBase;

/** 
 * @author vvelsen
 *
 */
public class INHoopVersion extends INBase
{
	public static String version="0.55";
	public static String compiledBy="vvelsen";
	public static String compiledDate="**undef***";
	
	/**
	 * 
	 */
	public INHoopVersion ()
	{
		INHoopVersion.compiledDate=this.getCompileDate();
	}
	/**
	 * Obtain the date on which the jar was compiled that holds this class
	 */
	public String getCompileDate ()
	{
		StringBuffer formatted=new StringBuffer ();
		
		try 
		{
			File jarFile = new File	(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			formatted.append(new Date(jarFile.lastModified()));
		} 
		catch (URISyntaxException e1) 
		{
			formatted.append ("Unable to obtain compile date");
		}
		
		return (formatted.toString());
	}	
}
