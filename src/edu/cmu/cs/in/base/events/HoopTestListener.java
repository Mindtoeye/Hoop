package edu.cmu.cs.in.base.events;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopTestListener extends HoopRoot implements HoopListenerInterface
{
	/**
	 * 
	 */
	public HoopTestListener ()
	{
		
	}
	/**
	 * 
	 * @param anInp
	 * @param anOutp
	 */
    public void processEvent(final Integer anInp, final Integer anOutp)
    {
    	debug ("processEvent ()");
    }
}
