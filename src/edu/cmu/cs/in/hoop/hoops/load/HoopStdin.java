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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.HoopDialogConsole;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;

/**
* 
*/
public class HoopStdin extends HoopBase implements HoopInterface
{    	
	public static int lineCounter=0;
    private BufferedReader in=null;		
    private HoopDialogConsole userIO=null;
    
    private List<String> holder = null;
	
	/**
	 *
	 */
    public HoopStdin () 
    {
		setClassName ("HoopStdin");
		debug ("HoopStdin ()");
								
		setHoopCategory ("load");
		
		setHoopDescription ("Read from Standard Input");
		
		in = new BufferedReader(new InputStreamReader(System.in));
		
		holder = new LinkedList<String>();
    }
    /**
     * 
     */
    public List<String> getHolder ()
    {
    	return (holder);
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		if (getInEditor()==true)
		{
			userIO=(HoopDialogConsole) HoopLink.getWindow("User Dialog");
			if (userIO==null)
			{
				HoopLink.addView ("User Dialog",new HoopDialogConsole (),HoopLink.bottom);
				userIO=(HoopDialogConsole) HoopLink.getWindow("User Dialog");
			}
			
			userIO.setInHoop(this);
			
			// create the structures here that will block this method until
			// a user enters text in the Swing designated input box
			
			synchronized (holder) 
			{
		        // wait for input from field
		        while (holder.isEmpty())
		        {
		        	debug ("Waiting for input ...");
		        	
		            try 
		            {
						holder.wait();
					} 
		            catch (InterruptedException e) 
		            {			
						e.printStackTrace();
						debug ("Internal error: Hoop interrupted");
						return (false);
					}
		        } 

		        String nextString = holder.remove(0);
		        
		        // Now we can continue!
		        
		        processInput (nextString);
		    }
			
		}
		else
		{			
			System.out.println("Enter text : ");
			 
			try 
			{
				String str = in.readLine();
				
				processInput (str);
				
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
	public HoopBase copy ()
	{
		return (new HoopStdin ());
	}		
	/**
	 * 
	 */
	public void processInput (String anInput)
	{
		debug ("processInput ("+anInput+")");
		
		addKV (new HoopKVInteger (lineCounter,anInput));		
	}
}
