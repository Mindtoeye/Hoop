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

import java.util.ArrayList;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.hoops.INHoopStdin;
import edu.cmu.cs.in.hoop.hoops.INHoopStoud;

/** 
 * @author Martin van Velsen
 */
public class INHoopExecute extends INBase 
{
	private INHoopBase root=null;
	
	/**
	 *
	 */
	public INHoopExecute () 
	{
		setClassName ("INHoopExecute");
		debug ("INHoopExecute ()");		
	}
	/**
	 * 
	 */
	public void testExecute ()
	{
		debug ("testExecute ()");
				
		INHoopStdin inp=new INHoopStdin ();
		INHoopStoud outp=new INHoopStoud ();
		inp.addOutHoop(outp);
		inp.addOutHoop(outp);
		
		root=inp;
				
		execute ();
	}
	/**
	 * 
	 */
	public Boolean execute ()
	{
		debug ("execute ()");
		
		if (root==null)
		{
			debug ("Nothing to execute");
			return (false);
		}
		
		if (root.runHoop(null)==false)
			return (false);
		
		return (execute (root));	
	}
	/** 
	 * @param aRoot
	 * @return
	 */
	private Boolean execute (INHoopBase aRoot)
	{
		debug ("execute (INHoopBase)");
						
		ArrayList<INHoopBase> outHoops=aRoot.getOutHoops();
		
		debug ("Executing " + outHoops.size() + " hoops ...");
		
		for (int i=0;i<outHoops.size();i++)
		{
			INHoopBase current=outHoops.get(i);
			
			current.runHoop(aRoot);
			
			if (current.getOutHoops().size()>0) // quick test before we execute
				execute (current);
		}
		
		return (true);
	}
}

