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

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.hoop.hoops.INHoopStdin;
import edu.cmu.cs.in.hoop.hoops.INHoopStdout;

/** 
 * @author Martin van Velsen
 */
public class INHoopExecuteStandalone extends INHoopExecute
{		
	/**
	 *
	 */
	public INHoopExecuteStandalone () 
	{
		setClassName ("INHoopExecuteStandalone");
		debug ("INHoopExecuteStandalone ()");		
		
		this.setInEditor(false);
		
		INHoopStdin inp=new INHoopStdin ();
		INHoopStdout outp=new INHoopStdout ();
		inp.addOutHoop(outp);
		inp.addOutHoop(outp);	
		
		setRoot (inp);		
	}	
	/**
	 * 
	 */
	private void help ()
	{
		System.out.println ("Usage: java edu.cmu.cs.in.hoop.INHoopExecuteStandalone <options>");
		System.out.println ("");
		System.out.println ("Options, one of:");
		System.out.println ("	-nodebug");
		System.out.println ("	-execute <N>");
	}
	/** 
	 * @param args
	 */
	public void parseArgs (String [] args)
	{
		if (args.length<1)
		{
			help ();
			System.exit(0);
		}
		
		for (int i=0;i<args.length;i++)
		{
			if (args [i].equals("-nodebug")==true)
			{
				INHoopLink.nodebug=true;
			}
			
			if (args [i].equals("-execute")==true)
			{
				this.setLoopCount(Integer.parseInt(args [i+1]));
			}			
		}
	}
	/**
	 *
	 */	
	public static void main (String[] args) 
	{
    	// run the INHoopLink constructor; We need this to have a global settings registry
    	@SuppressWarnings("unused")
		INHoopLink link = new INHoopLink();     	    	
   	
    	INBase.debug ("INHoopExecuteStandalone","main ()");		
    	
    	INHoopExecuteStandalone runtime=new INHoopExecuteStandalone ();
		
		runtime.parseArgs(args);
		
		new Thread(runtime).start(); 
	}
}

