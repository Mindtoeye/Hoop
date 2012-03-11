/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
 * 
 */

package edu.cmu.cs.in.hoop;

import javax.swing.SwingWorker;

public class INHoopSearchThread extends SwingWorker<Integer, Integer>
{
        protected Integer doInBackground() throws Exception
        {
                // Do a time-consuming task.
                Thread.sleep(1000);
                return 42;
        }

        protected void done()
        {
                try
                {
                        //JOptionPane.showMessageDialog(f, get());
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
        }
}
