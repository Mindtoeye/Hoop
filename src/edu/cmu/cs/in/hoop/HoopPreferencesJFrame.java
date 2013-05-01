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

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.HoopComponentSnapshot;
import edu.cmu.cs.in.controls.base.HoopJFrame;

/**
 * A frame that restores position and size from user preferences and updates the 
 * preferences upon exit.
 */
public class HoopPreferencesJFrame extends HoopJFrame
{
	private static final long serialVersionUID = -1L;

	private int inset=50;
	
	private Preferences root;
	
	/**
	 * 
	 */
	public HoopPreferencesJFrame()
	{
		super ("Hoop");
		
    	setClassName ("HoopPreferencesJFrame");
    	debug ("HoopPreferencesJFrame ()");		
    	
    	ArrayList <String> profileLocations=HoopLink.environment.getProfileLocation();
    	
    	if (profileLocations.size()==0)
    	{
    		debug ("Error: unable to figure out where the profile location should be");
    	}
    	else
    		debug ("Determined profile directory to be: " + profileLocations.get(0));    

        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        setBounds (inset,inset,screenSize.width-inset*2, screenSize.height-inset*2);    	
    	    	
    	root=Preferences.userNodeForPackage(this.getClass());
    	
    	HoopLink.preferences=root;
    	
    	int left = root.getInt("left", inset);
    	int top = root.getInt("top", inset);
    	int width = root.getInt("width", screenSize.width-inset*2);
    	int height = root.getInt("height", screenSize.height-inset*2);
    	
    	String storedProps=root.get("props","");
    	
    	if (storedProps.isEmpty()==false)
    	{
    		HoopLink.props.fromXMLString(storedProps);
    	}
    	
    	setBounds(left, top, width, height);
	}
	/**
	 * 
	 */
	protected void quit ()
	{
		if (HoopLink.project!=null)
		{
			if (HoopLink.project.getVirginFile()==false)
			{
				HoopComponentSnapshot.saveScreenShot(this,HoopLink.project.getBasePath()+"/preview.png");
			}	
		}	
		
        root.putInt("left", getX());
        root.putInt("top", getY());
        root.putInt("width", getWidth());
        root.putInt("height", getHeight());
        
        root.put("props",HoopLink.props.toXMLString());
        
        System.exit(0);		
	}
	/**
	 * http://www.catalysoft.com/articles/busyCursor.html
	 */
	protected void startWaitCursor() 
	{
		Container cp = this.getContentPane();			
	    cp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	/**
	 * http://www.catalysoft.com/articles/busyCursor.html
	 */
	protected void endWaitCursor() 
	{
		Container cp = this.getContentPane();			
	    cp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}		
}
