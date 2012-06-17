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

import edu.cmu.cs.in.hoop.hoops.INHoopStdin;
import edu.cmu.cs.in.hoop.hoops.INHoopStoud;

/** 
 * @author Martin van Velsen
 */
public class INHoopExecuteInEditor extends INHoopExecute
{		
	/**
	 *
	 */
	public INHoopExecuteInEditor () 
	{
		setClassName ("INHoopExecuteInEditor");
		debug ("INHoopExecuteInEditor ()");		
		
		this.setInEditor(true);
		
		INHoopStdin inp=new INHoopStdin ();
		INHoopStoud outp=new INHoopStoud ();
		inp.addOutHoop(outp);
		inp.addOutHoop(outp);			
		
		setRoot (inp);
	}	
}

