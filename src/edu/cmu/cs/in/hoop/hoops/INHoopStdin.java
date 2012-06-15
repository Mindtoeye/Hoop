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

package edu.cmu.cs.in.hoop.hoops;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopFileLoadBase;

/**
* 
*/
public class INHoopStdin extends INHoopBase
{    	
	public static int lineCounter=0;
    private BufferedReader in=null;		
	
	/**
	 *
	 */
    public INHoopStdin () 
    {
		setClassName ("INHoopStdin");
		debug ("INHoopStdin ()");
								
		setHoopCategory ("load");
		
		setHoopDescription ("Read from Standard Input");
		
		in = new BufferedReader(new InputStreamReader(System.in));		
    }
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		super.runHoop(inHoop);
		
		if (getInEditor()==true)
		{
			
		}
		else
		{			
			System.out.println("Enter text : ");
			 
			try 
			{
				String str = in.readLine();
				
				addKV (new INKV (lineCounter,str));
				
				lineCounter++;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				debug ("Error reading standard input!");
				return (false);
			}
		}
		
		return (true);
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopStdin ());
	}		
}
