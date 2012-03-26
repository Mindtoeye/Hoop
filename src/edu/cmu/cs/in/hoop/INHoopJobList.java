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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.cmu.cs.in.controls.INJFeatureList;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;
//import edu.cmu.cs.in.controls.base.INJInternalFrame;

/**
 * 
 */
public class INHoopJobList extends INEmbeddedJPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private INJFeatureList jobList=null;
	
	/**
	 * 
	 */
	public INHoopJobList ()  
    {
    	//super("Hadoop Job List", true, true, true, true);
    	
		setClassName ("INHoopJobList");
		debug ("INHoopJobList ()");    	
    	
		jobList=new INJFeatureList ();
		jobList.setLabel("Selected Jobs");
		jobList.setMinimumSize(new Dimension (150,60));
		jobList.setPreferredSize(new Dimension (150,150));
		jobList.setMaximumSize(new Dimension (5000,150));		
		
		setContentPane (jobList);
		setSize (425,300);
		setLocation (75,75);		
    }
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		
	}  
}