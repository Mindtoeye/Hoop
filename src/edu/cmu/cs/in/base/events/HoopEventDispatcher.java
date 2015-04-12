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

import java.util.ArrayList;
import java.util.HashMap;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public final class HoopEventDispatcher extends HoopRoot
{
	/** mapping of class events to active listeners **/
	private final HashMap<Class,ArrayList> map = new HashMap<Class,ArrayList >( 10 );
	
	public HoopEventDispatcher ()
	{
		setClassName ("HoopEvents");
		debug ("HoopEvents ()");		
	}
	
	/** 
	 * Add a listener to an event class
	 */
	public <L> void listen( Class<? extends HoopEvent<L>> evtClass, L listener) 
	{
		debug ("listen ()");
		
	   final ArrayList<L> listeners = listenersOf( evtClass );
	      
	   synchronized( listeners ) 
	   {
		   if ( !listeners.contains( listener ) ) 
		   {
			   listeners.add( listener );
		   }
	   }
	}
	
	/** 
	 * Stop sending an event class to a given listener
	 */
	public <L> void mute( Class<? extends HoopEvent<L>> evtClass, L listener) 
	{
		debug ("mute ()");
		
	 	final ArrayList<L> listeners = listenersOf( evtClass );
	   	synchronized( listeners ) 
	   	{
	   		listeners.remove( listener );
	   	}
	}
	
	/**
	 * Gets listeners for a given event class
	 */
	private <L> ArrayList<L> listenersOf(Class<? extends HoopEvent<L>> evtClass) 
	{
		debug ("listenersOf ()");
		
		synchronized ( map ) 
		{
			@SuppressWarnings("unchecked")
			final ArrayList<L> existing = map.get( evtClass );
			if (existing != null) 
			{
			   return existing;
			}
	
			final ArrayList<L> emptyList = new ArrayList<L>(5);
			map.put(evtClass, emptyList);
			return emptyList;
		}
	}
		
	/** 
	 * Notify a new event to registered listeners of this event class
	 */
	public <L> void notify( final HoopEvent<L> evt) 
	{
		debug ("notify ()");
		
		@SuppressWarnings("unchecked")
		Class<HoopEvent<L>> evtClass = (Class<HoopEvent<L>>) evt.getClass();
	
		for ( L listener : listenersOf(  evtClass ) ) 
		{
		   evt.notify(listener);
		}
	}
}
