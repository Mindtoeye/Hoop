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

import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopPatternMatcher extends HoopRoot
{		
	private ArrayList<HoopPattern> patterns=null;
	
	/**
	 * 
	 */
	public HoopPatternMatcher ()
	{
		setClassName ("HoopPatternMatcher");
		debug ("HoopPatternMatcher ()");
		
		patterns=new ArrayList<HoopPattern> ();
	}
	/**
	 * 
	 */
	public HoopPatternMatch matchPattern (String aToken, 
										  ArrayList<String> aTokenList,
										  int anIndex) 
	{
		// debug ("matchPattern ("+aToken+" <aTokenList> " + anIndex + ")");

		for (int i = 0; i < patterns.size(); i++) 
		{
			HoopPattern seq = patterns.get(i);

			int ptSize = seq.tokens.size();

			// debug ("Looking at pattern of size: " + ptSize);

			int matched = 0;

			for (int j = 0; j < ptSize; j++) 
			{
				if ((anIndex + j) < aTokenList.size()) 
				{
					String fromList = aTokenList.get(anIndex + j);
					String toList = seq.tokens.get(j);

					// debug ("Comparing " + fromList + " to " + toList);

					if (fromList.equals(toList))
						matched++;
				}
			}

			// See if we've matched all the tokens. If there is a partial match
			// we can see if we can incorporate that score at some point

			if (matched>0) 
			{												
				debug ("MATCHED: " + matched);
				
				HoopPatternMatch matchResult=new HoopPatternMatch ();
				
				matchResult.score=seq.tokens.size()/matched;
				matchResult.size=matched;
				
				StringBuffer formatter = new StringBuffer();

				for (int t = 0; t < seq.tokens.size(); t++) 
				{
					if (t > 0)
						formatter.append(" ");

					formatter.append(seq.tokens.get(t));
				}

				matchResult.matchedPattern=formatter.toString();
				
				return (matchResult);
			}
		}

		return (null);
	}
}

	