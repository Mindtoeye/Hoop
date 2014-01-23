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

import java.util.LinkedList;

/**
*
*/
public class HoopFixedSizeQueue<E> extends LinkedList<E> 
{	
	private static final long serialVersionUID = 1L;
	private int limit;

	/**
	 *
	 */	
    public HoopFixedSizeQueue(int limit) 
    {
        this.limit = limit;
    }
	/**
	 *
	 */
    @Override
    public boolean add(E o) 
    {
        super.add(o);
        while (size() > limit) 
        { 
        	super.remove(); 
        }
        
        return true;
    }
    /**
     * 
     */
    public String toConsoleString ()
    {
    	StringBuffer formatted=new StringBuffer ();
    	
		for (int i=0;i<this.size();i++)
		{			
			String aString=(String) this.get(i);
			formatted.append(aString);
			formatted.append("\n");
		}    	
		
		return (formatted.toString());
    }
}

