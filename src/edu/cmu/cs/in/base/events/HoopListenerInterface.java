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

/**
 * 
 */
public interface HoopListenerInterface 
{
	/** 
	 * @param anInp
	 * @param anOutp
	 */
    public void processEvent(final Integer anInp, final Integer anOutp);
}
