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
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
 * 
 */
public class HoopURLReader extends HoopLoadBase
{	
	public	HoopStringSerializable URL=null;
	private HoopHTTPReader reader=null;
	
	/**
	 *
	 */ 
	public HoopURLReader () 
	{		
		setClassName ("HoopURLReader");
		debug ("HoopURLReader ()");
				
		setHoopDescription ("Read text data from a URL");	
	
		URL=new HoopStringSerializable (this,"URL","http://localhost/robots.txt");
		
		reader=new HoopHTTPReader ();
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopURLReader ());
	}	
	/**
	 * We're expecting a KV object that has a key of type String representing the file URI,
	 * where the content value (also a String) represents the flat text of the file.
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		if (URL.getValue().isEmpty()==true)
		{
			this.setErrorString("Please provide a url");
			return (false);
		}
		
		if (URL.getValue().toLowerCase().indexOf("http://")==-1)
		{
			this.setErrorString("Error: the url does not start with http://");
			return (false);
		}		
		
		HoopKVString newData=new HoopKVString ();
		
		newData.setKey(URL.getValue());
		
		String aResult="";
		
		try 
		{
			aResult = reader.loadURL(URL.getValue());
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
}
