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

package edu.cmu.cs.in.hoop.hoops.task;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.execute.HoopUIMACPEDriver;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopControlBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

/**
* 
*/
public class HoopUIMACPE extends HoopControlBase implements HoopInterface
{    					
	private static final long serialVersionUID = 8756323464548123680L;
	private HoopUIMACPEDriver UIMADriver=null;
	
	public HoopURISerializable URI=null;
	
	/**
	 *
	 */
    public HoopUIMACPE () 
    {
		setClassName ("HoopUIMACPE");
		debug ("HoopUIMACPE ()");
										
		setHoopDescription ("Run Native UIMA CPE");
		
		removeInPort ("KV");
		addInPort ("CAS");
		
		URI=new HoopURISerializable (this,"URI","");
		
		removeOutPort ("KV");		
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		String fullXMLPath=HoopLink.relativeToAbsolute(URI.getValue());
		
		debug ("Processing UIMA XML descriptor: " + fullXMLPath);
		
		UIMADriver=new HoopUIMACPEDriver ();
		UIMADriver.runCPE (fullXMLPath);
						
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopUIMACPE ());
	}	
}
