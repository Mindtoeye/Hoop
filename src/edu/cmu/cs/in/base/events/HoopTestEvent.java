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
 * Notes:
 * 
 * Based on: http://stackoverflow.com/questions/937302/simple-java-message-dispatching-system
 */

package edu.cmu.cs.in.base.events;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public final class HoopTestEvent extends HoopRoot implements HoopEvent<HoopTestListener> 
{	
   private Integer inp=-1;
   private Integer outp=-1;

   /**
    * 
    * @param anInp
    * @param anOutp
    */
   public HoopTestEvent(final Integer anInp, final Integer anOutp) 
   {
	   setClassName ("HoopBase");
	   debug ("HoopBase ()");
	   
	   inp=anInp;
	   outp=anOutp;
   }

   /**
    * 
    */
   public void notify( final HoopTestListener listener) 
   {
	   debug ("notify ()");
	   
	   listener.processEvent(100,100);
   }
}