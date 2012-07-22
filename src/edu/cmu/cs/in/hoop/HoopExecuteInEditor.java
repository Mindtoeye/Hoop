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

import edu.cmu.cs.in.hoop.hoops.transform.HoopFilterStopWords;
import edu.cmu.cs.in.hoop.hoops.transform.HoopSentence2Tokens;
import edu.cmu.cs.in.hoop.hoops.load.HoopStdin;
import edu.cmu.cs.in.hoop.hoops.save.HoopStdout;
import edu.cmu.cs.in.hoop.hoops.transform.HoopTokenCaseChange;
import edu.cmu.cs.in.hoop.hoops.transform.HoopUniqueTerms;

/** 
 * @author Martin van Velsen
 */
public class HoopExecuteInEditor extends HoopExecute
{		
	/**
	 *
	 */
	public HoopExecuteInEditor () 
	{
		setClassName ("HoopExecuteInEditor");
		debug ("HoopExecuteInEditor ()");		
		
		this.setInEditor(true);

		/*
		HoopStdin inp=new HoopStdin ();
		HoopSentence2Tokens tokenizer=new HoopSentence2Tokens ();
		HoopTokenCaseChange lowercaser=new HoopTokenCaseChange ();
		HoopUniqueTerms countUnique=new HoopUniqueTerms ();
		HoopFilterStopWords removeStops=new HoopFilterStopWords ();
		HoopStdout outp=new HoopStdout ();
		
		inp.addOutHoop(tokenizer);
		tokenizer.addOutHoop(lowercaser);
		lowercaser.addOutHoop(countUnique);
		countUnique.addOutHoop(removeStops);
		removeStops.addOutHoop(outp);
		
		setRoot (inp);
		*/
	}	
}

