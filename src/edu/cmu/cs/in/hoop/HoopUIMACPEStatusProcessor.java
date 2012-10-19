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

package edu.cmu.cs.in.hoop;

import java.util.List;

import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 * Callback Listener. Receives event notifications from CPE. Will be rebased from
 * the Hoop reporter class so that it can report performance information through
 * a network socket to the Hoop cluster monitor.
 *  
 */
public class HoopUIMACPEStatusProcessor extends HoopBase implements StatusCallbackListener 
{
	/// The CPE instance.
	private CollectionProcessingEngine mCPE;
	
	/// Start time of CPE initialization
	private long mStartTime;

	/// Start time of the processing
	private long mInitCompleteTime;
	
    int entityCount = 0;
    long size = 0;

    /**
     * 
     */
    public HoopUIMACPEStatusProcessor (CollectionProcessingEngine aCPE)
    {
		setClassName ("HoopUIMACPEStatusProcessor");
		debug ("HoopUIMACPEStatusProcessor ()");    	
		
		mCPE=aCPE;
		
		mStartTime = System.currentTimeMillis();
    }
    /**
     * Called when the initialization is completed.
     * 
     * @see org.apache.uima.collection.processing.StatusCallbackListener#initializationComplete()
     */
    public void initializationComplete() 
    {
      debug ("CPM Initialization Complete");
      
      mInitCompleteTime = System.currentTimeMillis();
    }

    /**
     * Called when the batchProcessing is completed.
     * 
     * @see org.apache.uima.collection.processing.StatusCallbackListener#batchProcessComplete()
     * 
     */
    public void batchProcessComplete() 
    {
    	debug ("Completed " + entityCount + " documents");
      
    	if (size > 0) 
    	{
    		debug ("; " + size + " characters");
    	}
            
    	long elapsedTime = System.currentTimeMillis() - mStartTime;
      
    	debug ("Time Elapsed : " + elapsedTime + " ms ");
    }

    /**
     * Called when the collection processing is completed.
     * 
     * @see org.apache.uima.collection.processing.StatusCallbackListener#collectionProcessComplete()
     */
    public void collectionProcessComplete() 
    {
    	long time = System.currentTimeMillis();
      
    	debug ("Completed " + entityCount + " documents");
      
    	if (size > 0) 
    	{
    		debug ("; " + size + " characters");
    	}
            
    	long initTime = mInitCompleteTime - mStartTime;
    	long processingTime = time - mInitCompleteTime;
    	long elapsedTime = initTime + processingTime;
      
    	debug ("Total Time Elapsed: " + elapsedTime + " ms ");
    	debug ("Initialization Time: " + initTime + " ms");
    	debug ("Processing Time: " + processingTime + " ms");

    	debug ("\n\n ------------------ PERFORMANCE REPORT ------------------\n");
    	debug (mCPE.getPerformanceReport().toString());
      
    	// stop the JVM. Otherwise main thread will still be blocked waiting for
    	// user to press Enter.
    	System.exit(1);
    }

    /**
     * Called when the CPM is paused.
     * 
     * @see org.apache.uima.collection.processing.StatusCallbackListener#paused()
     */
    public void paused() 
    {
    	debug ("Paused");
    }

    /**
     * Called when the CPM is resumed after a pause.
     * 
     * @see org.apache.uima.collection.processing.StatusCallbackListener#resumed()
     */
    public void resumed() 
    {
    	debug ("Resumed");
    }

    /**
     * Called when the CPM is stopped abruptly due to errors.
     * 
     * @see org.apache.uima.collection.processing.StatusCallbackListener#aborted()
     */
    public void aborted() 
    {
    	debug ("Aborted");
      
    	// stop the JVM. Otherwise main thread will still be blocked waiting for
    	// user to press Enter.
    	System.exit(1);
    }

    /**
     * Called when the processing of a Document is completed. <br>
     * The process status can be looked at and corresponding actions taken.
     * 
     * @param aCas
     *          CAS corresponding to the completed processing
     * @param aStatus
     *          EntityProcessStatus that holds the status of all the events for aEntity
     */
    public void entityProcessComplete(CAS aCas, EntityProcessStatus aStatus) 
    {
    	debug ("entityProcessComplete ("+aCas.getViewName()+")");
    	
    	if (aStatus.isException()) 
    	{
    		List<Exception> exceptions = aStatus.getExceptions();
    		
    		for (int i = 0; i < exceptions.size(); i++) 
    		{
    			((Throwable) exceptions.get(i)).printStackTrace();
    		}
    		
    		return;
      	}
      
      	entityCount++;
      
      	String docText = aCas.getDocumentText();
      
      	if (docText != null) 
      	{
      		size += docText.length();
      	}
    }
}
  