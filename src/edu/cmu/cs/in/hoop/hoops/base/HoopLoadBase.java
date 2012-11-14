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

import edu.cmu.cs.in.hoop.properties.types.HoopEnumSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopLoadBase extends HoopIOBase implements HoopInterface
{
	private static final long serialVersionUID = 4117447861954274834L;
	private String content=null;
	
    public HoopStringSerializable batchSize=null;    
    public HoopStringSerializable queryMax=null;
	public HoopEnumSerializable mode=null; // LINEAR,SAMPLE
	
	/**
	 *
	 */
    public HoopLoadBase () 
    {
		setClassName ("HoopLoadBase");
		debug ("HoopLoadBase ()");
		
		setHoopCategory ("Load");								
		setHoopDescription ("Abstract Hoop Loader");
		
		removeInPort ("KV");
		
    	batchSize=new HoopStringSerializable (this,"batchSize","100");    	
    	queryMax=new HoopStringSerializable (this,"queryMax","");
		mode=new HoopEnumSerializable (this,"mode","LINEAR,SAMPLE");
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
		return (new HoopLoadBase ());
	}	
}
