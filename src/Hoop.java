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

import java.awt.*;
import javax.swing.*;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.HoopMainFrame;

/** 
 * @author vvelsen
 *
 */
public class Hoop 
{
	public static JFrame frame=null;
	
	/**
	 *
	 */	
    public static void main( String[] args ) 
    {
    	/*
    	 * Tell Swing that we need Popup Menus to be heavyweight. The Java 3D
    	 * window is a heavyweight window – that is, the window is a native
    	 * window, and therefore any windows that must overlap it must also be
    	 * native. Our menu items will be dropped down in front of the
    	 * Java 3D Canvas3D so they must be created as heavyweight windows.
    	 */
    	//JPopupMenu.setDefaultLightWeightPopupEnabled (false);
    	  
    	// run the HoopLink constructor; We need this to have a global settings registry
    	@SuppressWarnings("unused")
		HoopLink link = new HoopLink();     	    	
   	
    	HoopRoot.debug ("Hoop","main ()");
    	
        UIManager.put ("swing.boldMetal", Boolean.FALSE);
        JDialog.setDefaultLookAndFeelDecorated (true);
        JFrame.setDefaultLookAndFeelDecorated (true);
        Toolkit.getDefaultToolkit().setDynamicLayout (true);

        System.setProperty ("sun.awt.noerasebackground","true");
        
        try 
        {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} 
        catch (ClassNotFoundException e1) 
        {
        	HoopRoot.debug ("HoopQueryTest","UIManager: setLookAndFeel, ClassNotFoundException");
			return;
		} 
        catch (InstantiationException e1) 
        {
        	HoopRoot.debug ("HoopQueryTest","UIManager: setLookAndFeel, InstantiationException");
			return;
		} 
        catch (IllegalAccessException e1) 
        {
        	HoopRoot.debug ("HoopQueryTest","UIManager: setLookAndFeel, IllegalAccessException");
			return;
		} 
        catch (UnsupportedLookAndFeelException e1) 
        {
        	HoopRoot.debug ("HoopQueryTest","UIManager: setLookAndFeel, UnsupportedLookAndFeelException");
			return;
		}           
        
        frame=new HoopMainFrame ();
        frame.setResizable(true);
        
        SwingUtilities.invokeLater (new Runnable() 
        {
            public void run() 
            {
            	// Implemented this part according to Swing coding standards. More information
            	// can be found at: http://java.sun.com/developer/technicalArticles/javase/swingworker/
            	frame.setVisible(true);
            }
         });        
    }
}
