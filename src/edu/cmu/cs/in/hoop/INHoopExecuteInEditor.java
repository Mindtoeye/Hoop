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

import edu.cmu.cs.in.hoop.hoops.INHoopFilterStopWords;
import edu.cmu.cs.in.hoop.hoops.INHoopSentence2Tokens;
import edu.cmu.cs.in.hoop.hoops.INHoopStdin;
import edu.cmu.cs.in.hoop.hoops.INHoopStdout;
import edu.cmu.cs.in.hoop.hoops.INHoopTokenCaseChange;
import edu.cmu.cs.in.hoop.hoops.INHoopUniqueTerms;

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
		INHoopSentence2Tokens tokenizer=new INHoopSentence2Tokens ();
		INHoopTokenCaseChange lowercaser=new INHoopTokenCaseChange ();
		INHoopUniqueTerms countUnique=new INHoopUniqueTerms ();
		INHoopFilterStopWords removeStops=new INHoopFilterStopWords ();
		INHoopStdout outp=new INHoopStdout ();
		
		inp.addOutHoop(tokenizer);
		tokenizer.addOutHoop(lowercaser);
		lowercaser.addOutHoop(countUnique);
		countUnique.addOutHoop(removeStops);
		removeStops.addOutHoop(outp);
		
		setRoot (inp);
	}	
}

