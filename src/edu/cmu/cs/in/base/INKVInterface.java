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
 * @author vvelsen
 */
public interface INKVInterface
{
	/** 
	 * @return String
	 */
	public String getKeyString ();	
	
	/** 
	 * @return Object
	 */	
	public Object getValue ();
	
	/** 
	 * @return Object
	 */	
	public Object getValue (int anIndex);
	
	/** 
	 * @return Object
	 */	
	public String getValueAsString ();
	
	/** 
	 * @return Object
	 */	
	public String getValueAsString (int anIndex);	
}
