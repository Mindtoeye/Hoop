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
package edu.cmu.cs.in.controls;

import java.util.*;

import org.pushingpixels.flamingo.api.bcb.*;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * http://stackoverflow.com/questions/5355977/how-to-create-the-flamingo-ribbon-menu
 */
public class HoopBreadCrumbBar extends JBreadcrumbBar<String> 
{
	private static final long serialVersionUID = -326728608485980803L;
	
	/**
	 * 
	 * @param callback
	 */
	public HoopBreadCrumbBar(BreadcrumbBarCallBack<String> callback) 
	{
		super(callback);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopRoot.debug("HoopBreadCrumbBar",aMessage);
	}
	/**
	 * 
	 */
	public void setPath (ArrayList<String> aCrumList)
	{
		debug ("setPath ()");
	
		ArrayList<BreadcrumbItem<String>> path = new ArrayList<BreadcrumbItem<String>>();
		
		for (int i=0;i<aCrumList.size();i++)
		{		
			String aTerm=aCrumList.get(i);
			
			debug ("Adding " + aTerm + " to crumb path ...");
			
			BreadcrumbItem<String> bci = new BreadcrumbItem<String>(aTerm);
			//bci.setIcon(fsv.getSystemIcon(dir));
			path.add(bci);
		}
		
		this.setPath(path);
	}	
}
