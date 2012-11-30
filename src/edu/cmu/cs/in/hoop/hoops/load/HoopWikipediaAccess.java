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

package edu.cmu.cs.in.hoop.hoops.load;

import java.io.IOException;
import java.net.MalformedURLException;

import edu.cmu.cs.in.base.io.HoopHTTPReader;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;

/**
 * http://www.mediawiki.org/wiki/API:Main_page
 */
public class HoopWikipediaAccess extends HoopLoadBase
{
	private static final long serialVersionUID = -3966005215302847296L;

	private HoopHTTPReader reader=null;
	
	/**
	 *
	 */ 
	public HoopWikipediaAccess () 
	{		
		setClassName ("HoopWikipediaAccess");
		debug ("HoopWikipediaAccess ()");
				
		setHoopDescription ("Read data directly from Wikipedia");		
	}
	/**
	 * We're expecting a KV object that has a key of type String representing the file URI,
	 * where the content value (also a String) represents the flat text of the file.
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
						
		HoopKVString newData=new HoopKVString ();
		
		newData.setKey("http://en.wikipedia.org/w/api.php?format=xml&action=query&titles=Main%20Page&prop=revisions&rvprop=content");
		
		String aResult="";
		
		try 
		{
			aResult = reader.loadURL(newData.getKeyString());
		} 
		catch (MalformedURLException e) 
		{
			this.setErrorString("Error: the provide url is malformed");
			e.printStackTrace();
			return (false);
		} 
		catch (IOException e) 
		{
			this.setErrorString("Error: unable to connect to server");
			e.printStackTrace();
			return (false);
		}
		
		newData.setValue (aResult);
		
		this.addKV (newData);
		
		return (true);
	}		
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopWikipediaAccess ());
	}	
}
