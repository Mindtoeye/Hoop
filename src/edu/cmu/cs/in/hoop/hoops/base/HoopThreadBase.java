package edu.cmu.cs.in.hoop.hoops.base;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;

public class HoopThreadBase extends HoopBaseTyped implements Runnable
{	
    public static int myCount = 0;
    protected Boolean isRunning = false;
	
	/**
	 *
	 */
	public HoopThreadBase () 
	{
		setClassName ("HoopThreadBase");
		debug ("HoopThreadBase ()");
	}
	/**
	 * 
	 * @return
	 */
	public Boolean getIsRunning() 
	{
		return isRunning;
	}	
	/**
	 * 
	 */
	protected void dataReady ()
	{
		debug ("dataReady ()");
		
		HoopLink.runner.dataReady(this);
	}
	/**
	 * 
	 */
	@Override
	public void run() 
	{
		debug ("run ()");
		
        while(HoopThreadBase.myCount <= 10)
        {
            try
            {
                debug ("Expl Thread: "+(++HoopThreadBase.myCount));
                Thread.sleep(100);
            } 
            catch (InterruptedException iex) 
            {
                debug("Exception in thread: "+iex.getMessage());
            }
        }		
	}
	
	/**
	 * Just for testing
	 * @param a
	 */
    public static void main(String a[])
    {
        HoopRoot.debug("HoopThreadBase","Starting Main Thread...");
        
        HoopThreadBase mrt = new HoopThreadBase();
        
        Thread t = new Thread(mrt);
        t.start();
    }	
}
