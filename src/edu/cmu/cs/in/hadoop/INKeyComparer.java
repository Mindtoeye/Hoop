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

package edu.cmu.cs.in.hadoop;

import org.apache.hadoop.io.RawComparator;
import org.apache.hadoop.io.Text;

import edu.cmu.cs.in.base.INBase;

/**
* Future class to work with the partitioner
*/
public class INKeyComparer extends INBase implements RawComparator<Text> 
{
	/**
	 *
	 */
	public int compare (byte[] text1, int start1, int length1, byte[] text2, int start2, int length2) 
	{
		// look at first character of each text byte array
		return new Character((char)text1[0]).compareTo((char)text2[0]);
	}
	/**
	 *
	 */
	public int compare(Text o1, Text o2) 
	{
		return compare(o1.getBytes(), 0, o1.getLength(), o2.getBytes(), 0, o2.getLength());
	}
}