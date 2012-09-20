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

package edu.cmu.cs.in.controls.dialogs;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopGenericProgressdialog extends HoopRoot implements PropertyChangeListener 
{
    private ProgressMonitor progressMonitor=null;
    private CopyFiles operation=null;
    private Component dialogParent=null;
    
    /**
     * 
     */
    public HoopGenericProgressdialog (Component aParent) 
    {
		setClassName ("HoopGenericProgressdialog");
		debug ("HoopGenericProgressdialog ()");
		
		dialogParent=aParent;
    }
    /**
     * 
     */
    public void copyFiles (String fromPath,String toPath,File [] aFileSet) 
    {
        // make sure there are files to copy
        File srcDir = new File(fromPath);
        
        if (srcDir.exists() && (srcDir.listFiles() != null && srcDir.listFiles().length > 0)) 
        {
            // set up the destination directory
            File destDir = new File (toPath);
            
            // create the progress monitor
            progressMonitor = new ProgressMonitor(dialogParent,
                                                  "Operation in progress...",
                                                  "",
                                                  0,
                                                  100);
            progressMonitor.setProgress(0);

            // schedule the copy files operation for execution on a background thread
            operation = new CopyFiles (srcDir, destDir,aFileSet);
            
            // add ProgressMonitorExample as a listener on CopyFiles;
            // of specific interest is the bound property progress
            
            operation.addPropertyChangeListener(this);
            
            operation.execute();            
        } 
        else 
        {
            alert ("The sample application needs files to copy."
                   + " Please add some files to the in directory"
                   + " located at the project root.");
        }
    }
    /**
     * executes in event dispatch thread
     */    
    public void propertyChange (PropertyChangeEvent event) 
	{
        // if the operation is finished or has been canceled by
        // the user, take appropriate action
        if (progressMonitor.isCanceled()) 
		{
            operation.cancel(true);
        } 
		else if (event.getPropertyName().equals("progress")) 
		{		
            // get the % complete from the progress event
            // and set it on the progress monitor
            int progress = ((Integer)event.getNewValue()).intValue();
            progressMonitor.setProgress(progress);            
        }        
    }
 
    
    
    /**
     * 
     */
    class CopyFiles extends SwingWorker<Void, CopyData> 
	{
        private static final int PROGRESS_CHECKPOINT = 10000;
        private File srcDir;
        private File destDir;
        private File[] files=null;
        
        /** 
         * @param src
         * @param dest
         */
        CopyFiles (File src,
        		   File dest,
        		   File [] aFileSet) 
		{
            this.srcDir = src;
            this.destDir = dest;
            this.files=aFileSet;
        }
        /**
         * perform time-consuming copy task in the worker thread
         */
        @Override
        public Void doInBackground() 
		{
            int progress = 0;
            
            // initialize bound property progress (inherited from SwingWorker)
            setProgress(0);
            
            // get the files to be copied from the source directory
            if (files==null)
            	files = srcDir.listFiles();
            
            // determine the scope of the task
            long totalBytes = calcTotalBytes(files);
            long bytesCopied = 0;
            
            while (progress < 100 && !isCancelled()) 
			{
                // copy the files to the destination directory
                for (File f : files) 
                {
                    File destFile = new File(destDir, f.getName());
                    long previousLen = 0;
                    
                    try 
                    {
                        InputStream in = new FileInputStream(f);
                        OutputStream out = new FileOutputStream(destFile);                    
                        byte[] buf = new byte[1024];
                        int counter = 0;
                        int len;
                        
                        while ((len = in.read(buf)) > 0) 
                        {
                            out.write(buf, 0, len);
                            counter += len;
                            bytesCopied += (destFile.length() - previousLen);
                            previousLen = destFile.length();
                            
                            if (counter > PROGRESS_CHECKPOINT || bytesCopied == totalBytes) 
                            {
                                // get % complete for the task
                                progress = (int)((100 * bytesCopied) / totalBytes);
                                counter = 0;
                                CopyData current = new CopyData(progress, f.getName(),
                                                                getTotalKiloBytes(totalBytes),
                                                                getKiloBytesCopied(bytesCopied));

                                // set new value on bound property
                                // progress and fire property change event
                                setProgress(progress);
                                
                                // publish current progress data for copy task
                                publish(current);
                            }
                        }
                        
                        in.close();
                        out.close();
                    } 
                    catch (IOException e) 
                    {
                        e.printStackTrace();
                    }
                }
            }
            
            return null;
        }
        /**
         * process copy task progress data in the event dispatch thread
         */ 
        @Override
        public void process (List<CopyData> data) 
		{
            if(isCancelled()) 
            { 
            	return; 
            }
            
            CopyData update  = new CopyData(0, "", 0, 0);
			
            for (CopyData d : data) 
			{
                // progress updates may be batched, so get the most recent
                if (d.getKiloBytesCopied() > update.getKiloBytesCopied()) 
				{
                    update = d;
                }
            }
            
            // update the progress monitor's status note with the
            // latest progress data from the copy operation, and
            // additionally append the note to the console
            String progressNote = update.getKiloBytesCopied() + " of " 
                                  + update.getTotalKiloBytes() + " kb copied.";
            
            String fileNameNote = "Now copying " + update.getFileName();
            
            if (update.getProgress() < 100) 
            {
                progressMonitor.setNote(progressNote + " " + fileNameNote);
                //console.append(progressNote + "\n" + fileNameNote + "\n");
            } 
            else 
            {
                progressMonitor.setNote(progressNote);
                //console.append(progressNote + "\n");
            }           
        }
        
        /**
         * perform final updates in the event dispatch thread
         */
        @Override
        public void done() 
        {
            try 
            {
                // call get() to tell us whether the operation completed or 
                // was canceled; we don't do anything with this result
                Void result = get();
                
                //console.append("Copy operation completed.\n");                
            } 
            catch (InterruptedException e) 
            {
                
            } 
            catch (CancellationException e) 
            {
                // get() throws CancellationException if background task was canceled
                //console.append("Copy operation canceled.\n");
            } 
            catch (ExecutionException e) 
            {
                //console.append("Exception occurred: " + e.getCause());
            }
            
            // reset the example app
            //copyButton.setEnabled(true);
            progressMonitor.setProgress(0);            
        }
        /** 
         * @param files
         * @return
         */
        private long calcTotalBytes(File[] files) 
        {
            long tmpCount = 0;
            
            for (File f : files) 
            {
                tmpCount += f.length();
            }
            
            return tmpCount;
        }
        /** 
         * @param totalBytes
         * @return
         */
        private long getTotalKiloBytes(long totalBytes) 
        {
            return Math.round(totalBytes / 1024);
        }
        /** 
         * @param bytesCopied
         * @return
         */
        private long getKiloBytesCopied(long bytesCopied) 
        {
            return Math.round(bytesCopied / 1024);
        }
    }
    
    /**
     * 
     */
    class CopyData 
    {
        private int progress;
        private String fileName;
        private long totalKiloBytes;
        private long kiloBytesCopied;
        
        /**
         * 
         */
        CopyData(int progress, String fileName, long totalKiloBytes, long kiloBytesCopied) 
        {
            this.progress = progress;
            this.fileName = fileName;
            this.totalKiloBytes = totalKiloBytes;
            this.kiloBytesCopied = kiloBytesCopied;
        }
        /**
         * 
         */
        int getProgress() 
        {
            return progress;
        }
        /**
         * 
         */        
        String getFileName() 
        {
            return fileName;
        }
        /**
         * 
         */
        long getTotalKiloBytes() 
        {
            return totalKiloBytes;
        }
        /**
         * 
         */
        long getKiloBytesCopied() 
        {
            return kiloBytesCopied;
        }
    }        
}
