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
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import edu.cmu.cs.in.HoopApplicationInstanceListener;
import edu.cmu.cs.in.HoopApplicationManager;
import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopClassLoader;
import edu.cmu.cs.in.base.io.HoopFileManager;
import edu.cmu.cs.in.base.io.HoopPluginClassProxy;
//import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.controls.base.HoopViewInterface;
import edu.cmu.cs.in.hoop.HoopMainFrame;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/** 
 * See here the main entry point for the Hoop application. Only two
 * tasks are encapsulated here. First of all this class manages the
 * main frame that holds all the visual tools and editors. Second,
 * it starts up all the low level abstract tools and class instances.
 * For example the most important object is created right at the
 * start, which is the HoopLink class. The HoopLink object, of which
 * there is only one, can be thought of as a global registory 
 * containing pointers to objects that need to be persistent and
 * accessible to every class that needs it for the duration of the
 * application's execution cycle.
 */
public class Hoop 
{
	public static JFrame frame=null;
	
	/**
	 * 
	 */
	public static void loadPlugins ()
	{
		HoopRoot.debug ("Hoop","loadPlugins ()");
		
		HoopFileManager fTools=new HoopFileManager ();
		
		// Currently the code assumes the plugins directory exists, CHANGE THIS!
		
		ArrayList<String> fileList=fTools.listFiles ("./plugins");
		
		for (int i=0;i<fileList.size();i++)
		{
			String aFile=fileList.get(i);
			
			if (aFile.toLowerCase().indexOf(".jar")!=-1)
			{
				HoopRoot.debug ("Hoop","["+i+"] Found potential plugin jar ("+aFile+"), examining ...");
				
				HoopClassLoader loader=new HoopClassLoader ();
				
				//                      Load Hoops First
				//>-------------------------------------------------------------------------------------
				
				ArrayList<HoopPluginClassProxy> classes=loader.loadHoopInterfaces("./plugins/"+aFile,false);
				
				if (classes!=null)
				{
					HoopRoot.debug ("Hoop","Found " + classes.size() + " classes");
					
					for (int cl=0;cl<classes.size();cl++)
					{					
						HoopPluginClassProxy proxy=classes.get(cl);
						Class hoopClass=proxy.reference;
						
						if (hoopClass!=null)
						{
							HoopRoot.debug ("Hoop","Trying to instantiate interface: " + proxy.classType);
							
							try 
							{
								HoopRoot.debug ("Hoop","Attempting to create instance ...");
								Object object=hoopClass.newInstance();
								HoopRoot.debug ("Hoop","We have an instance, post processing ...");
								
								HoopRoot.debug ("Hoop","Processing plugin class with type: " + proxy.classType);
								
								if (proxy.classType.equalsIgnoreCase("HoopInterface")==true)
								{
									HoopRoot.debug ("Hoop","Adding Hoop plugin: " + proxy.classType);
									
									HoopLink.hoopManager.addTemplate((HoopBase) object);
								}	
								
								if (proxy.classType.equalsIgnoreCase("HoopViewInterface")==true)
								{
									HoopRoot.debug ("Hoop","Adding Hoop plugin: " + proxy.classType);
									
									HoopLink.windowsPlugins.add((HoopViewInterface) object);
								}									
							} 
							catch (InstantiationException e) 
							{						
								e.printStackTrace();
							} 
							catch (IllegalAccessException e) 
							{						
								e.printStackTrace();
							}
						}
						else
							HoopRoot.debug ("Hoop","Error: hoop class is null in list");
					}					
				}
				else
					HoopRoot.debug ("Hoop","Error loading jar");
								
				//>-------------------------------------------------------------------------------------
			}
		}
	}
	/**
	 *
	 */	
    public static void main( String[] args ) 
    {
    	if (!HoopApplicationManager.registerInstance()) 
    	{
    		// instance already running.
    		HoopRoot.debug ("Hoop","Another instance of Hoop is already running. Exiting...");
    		System.exit(0);
    	}
            
    	HoopApplicationManager.setApplicationInstanceListener(new HoopApplicationInstanceListener() 
    	{
    		public void newInstanceCreated() 
    		{
    			HoopRoot.debug ("Hoop","New instance detected...");
    			//this is where your handler code goes...
    		}
    	});
    	    	
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
   	
    	loadPlugins ();
    	
    	//System.exit(1);
    	
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
          
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();  
        defaults.put("Separator.foreground", new ColorUIResource(100,100,100));  
        
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
