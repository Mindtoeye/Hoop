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

/**
 * IMPORTANT: this class is not derived from HoopRoot because it needs to be small
 * and compact. We use this class as the base class for objects that are used in
 * arrays, lists and tables and we can't waste memory.
 */
public class HoopAnnotation
{	
	public int begin=0;
	public int end=0;
	
	/**
	 *
	 */
    public HoopAnnotation () 
    {

    }    
}
