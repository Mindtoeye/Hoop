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

package edu.cmu.cs.in.ling;

import edu.cmu.cs.in.base.io.HoopResourceLoader;

/**
 * http://en.wikipedia.org/wiki/English_personal_pronouns
 */
public class HoopEngPronounModel extends HoopLingModel
{		
	private HoopResourceLoader loader=null;
	
	/**
	 * 
	 */
	public HoopEngPronounModel ()
	{
		setClassName ("HoopEngPronounModel");
		debug ("HoopEngPronounModel ()");
			
		
		loader=new HoopResourceLoader ();
		
		String rawMatrix=loader.getTextResource2("assets/ling/EngPronouns.csv");
		
		if (rawMatrix!=null)
			processRawData (rawMatrix);
	}
	/**
	 * 
	 */
	private void addRawEntry (String anEntry[])
	{
		debug ("addRawEntry ()");
		
		HoopLingTerm aTerm=addTerm (anEntry [0]);
		
		if (aTerm==null)
		{
			debug ("Internal error: unable to add term to internal model: " + anEntry [0]);
			return;
		}
	}
	/**
	 * 
	 */
	public void processRawData (String aData)
	{
		debug ("processRawData ()");
		
		String split[]=aData.split("\\n");
		
		debug ("CSV data contains " + split.length + " rows");
		
		for (int i=0;i<split.length;i++)
		{									
			String entries[]=split [i].split("(?<!\\\\),");

			if (entries.length>1)
			{
				addRawEntry (entries);
			}
			else
				debug ("Warning: current row doesn't contain any entries");
		}	
	}
}
