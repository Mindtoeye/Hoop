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
import java.util.LinkedList;
import java.util.List;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.hoop.INHoopDialogConsole;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopInterface;

/**
* 
*/
public class INHoopStdin extends INHoopBase implements INHoopInterface
{    	
	public static int lineCounter=0;
    private BufferedReader in=null;		
    private INHoopDialogConsole userIO=null;
    
    private List<String> holder = null;
	
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
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		if (getInEditor()==true)
		{
			userIO=(INHoopDialogConsole) INHoopLink.getWindow("User Dialog");
			if (userIO==null)
			{
				INHoopLink.addView ("User Dialog",new INHoopDialogConsole (),INHoopLink.bottom);
				userIO=(INHoopDialogConsole) INHoopLink.getWindow("User Dialog");
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
	public INHoopBase copy ()
	{
		return (new INHoopStdin ());
	}		
	/**
	 * 
	 */
	public void processInput (String anInput)
	{
		debug ("processInput ("+anInput+")");
		
		addKV (new INKV (lineCounter,anInput));		
	}
}
