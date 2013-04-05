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
 * We mimic the UIMA annotation class here, but it is important to
 * not that we do not derive from it since that would create 
 * incredibly large memory footprints. Also the precise implementation
 * and meaning might be slightly different. The begin and end 
 * pointers are not just used to index into text but could also be
 * used to index into lists of KV objects or any other structured
 * data set
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
    /**
     * 
     */
    public Boolean isAnnotationValid ()
    {
    	if (begin<end)
    		return (true);
    	
    	return (false);
    }
    /**
     * 
     */
    public int getAnnotationLength ()
    {
    	if (isAnnotationValid ()==false)
    		return (0);
    	
    	return (end-begin);
    }
    /**
     * 
     */
    public void resetAnnotation ()
    {
    	begin=0;
    	end=0;
    }
}
