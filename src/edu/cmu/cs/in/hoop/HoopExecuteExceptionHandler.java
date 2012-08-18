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

import edu.cmu.cs.in.base.HoopLink;

/**
 *
 */
public class HoopExecuteExceptionHandler implements Thread.UncaughtExceptionHandler 
{
	/**
	 * 
	 */
    public void uncaughtException (Thread th, Throwable ex) 
    {
		HoopErrorPanel errorPanel=(HoopErrorPanel) HoopLink.getWindow("Errors");
		
		if (errorPanel==null)
		{			
			HoopLink.addView ("Errors",new HoopErrorPanel(),"bottom");
			errorPanel=(HoopErrorPanel) HoopLink.getWindow("Errors");
		}	
						
		HoopLink.popWindow("Errors");
		
		errorPanel.addError (th.getName(),ex.toString());				
		errorPanel.addError (th.getName (),getTrace (ex));		
    }
    /**
     * 
     */
    private String getTrace (Throwable ex)
    {
    	StringBuffer formatted=new StringBuffer ();
    	
    	StackTraceElement [] lines=ex.getStackTrace();
    	
    	for (int i=0;i<lines.length;i++)
    	{
    		if (i>0)
    		{
    			formatted.append(System.getProperty("line.separator"));
    		}	
    		
    		StackTraceElement aLine=lines [i];
    		
    		formatted.append (aLine.toString());
    	}
    	
    	return (formatted.toString());
    }
}
