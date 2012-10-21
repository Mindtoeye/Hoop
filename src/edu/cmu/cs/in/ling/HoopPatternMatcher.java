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
 * Presented here is a basic pattern matcher quite useful to base other matchers
 * on. Given a list of patterns, a list of tokens to be matched and the current
 * index into the token list, it will try to build a list of best matches and
 * return this list. 
 * 
 * Be careful using this class in an inner loop since it can rapidly create
 * large memory chunks. Depending on how the matcher function is configured you
 * might get a large amount of ranked matches back. In the method that receives
 * these matches you will have to decided what to do with the data and a first
 * pass might only use the top match.
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
	 * Currently perhaps a weak way of obtaining patterns, this method will
	 * load patterns directly from a text file. That also means no structural
	 * information is loaded other than whitespaces. In refined versions of
	 * the pattern class the patterns should either be loaded through an
	 * XML file or directly from a serialized pattern storage class.
	 */
	public Boolean loadFromText (String aText)
	{
		debug ("loadFromText ()");
		
		String lines [] = aText.split("\\n");
		
		if (lines.length==0)
		{
			debug ("0 patterns found in text");
			return (false);
		}

		for (int i = 0; i < lines.length; i++) 
		{
			String aSeq = lines[i];

			HoopPattern newPattern=new HoopPattern ();
			
			String terms[] = aSeq.split("\\s+");

			for (int j = 0; j < terms.length; j++) 
			{
				newPattern.tokens.add(terms[j]);
			}
			
			patterns.add(newPattern);
		}
		
		debug (patterns.size() + " patterns loaded");
		
		return (true);
	}
	/**
	 * 
	 */
	private ArrayList <HoopPatternMatch> scoreMatch (ArrayList <HoopPatternMatch> aList)
	{
		return (aList);
	}
	/**
	 * The most basic matching function. It takes a list of patterns and the current
	 * index as arguments. It will then find all the matches between the string of
	 * patterns in the loaded list and the provided list of terms. Every found pattern
	 * is stored in a temporarily list, which is sorted by matching rank and returned
	 * after all the patterns are compared.
	 */
	public ArrayList <HoopPatternMatch> matchPattern (ArrayList<Object> aTokenList,
													  int anIndex) 
	{
		debug ("matchPattern ()");
		
		ArrayList<HoopPatternMatch> newPatterns=new ArrayList<HoopPatternMatch> ();
		
		//String aToken=aTokenList.get(anIndex);

		for (int i = 0; i < patterns.size(); i++) 
		{
			HoopPattern seq = patterns.get(i);

			int ptSize = seq.tokens.size();

			//debug ("Looking at pattern of size: " + ptSize);

			int matched = 0;

			for (int j = 0; j < ptSize; j++) 
			{
				if ((anIndex + j) < aTokenList.size()) 
				{
					String fromList = (String) aTokenList.get(anIndex + j);
					String toList = seq.tokens.get(j);

					//debug ("Comparing " + fromList + " to " + toList);

					if (fromList.equals(toList))
						matched++;
				}
			}

			// See if we've matched all the tokens. If there is a partial match
			// we can see if we can incorporate that score at some point

			//if (matched==ptSize)
			if (matched>0)
			{												
				debug ("MATCHED: " + matched);
				
				HoopPatternMatch matchResult=new HoopPatternMatch ();
				
				matchResult.score=matched/seq.tokens.size();
				matchResult.size=matched;
				
				StringBuffer formatter = new StringBuffer();

				for (int t = 0; t < seq.tokens.size(); t++) 
				{
					if (t > 0)
						formatter.append(" ");

					formatter.append(seq.tokens.get(t));
				}

				matchResult.matchedPattern=formatter.toString();
				
				newPatterns.add(matchResult);
			}
		}
				
		return (scoreMatch (newPatterns));
	}
}

	