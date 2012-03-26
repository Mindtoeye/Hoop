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

package edu.cmu.cs.in.base;

//import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*
*/
public class INFeatureMakerBase extends INBase implements INFeatureMaker
{	
	private String [] splitter=null;
	
	/**
	 *
	 */
	public INFeatureMakerBase () 
	{
		setClassName ("INFeatureMakerBase");
		//debug ("INFeatureMakerBase ()");
	}
	/**
	 *
	 */	
	public List<String> unigramTokenize (String aSource)
	{				
		//debug ("unigramTokenize (String)");
		
		splitter=aSource.split("\\s+");
				
		return Arrays.asList(splitter);		
	}	
	/**
	 *
	 */	
	public List<String> unigramTokenize (String aSource, int n)
	{
		//debug ("unigramTokenize (String,int)");
		
		splitter=aSource.split("\\s+");
		
		return Arrays.asList(splitter);		
	}	
}

