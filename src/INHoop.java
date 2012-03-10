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

import java.awt.*;
import javax.swing.*;

import edu.cmu.cs.in.base.INFeatureMatrixBase;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.hoop.INHoopFrame;

/** 
 * @author vvelsen
 *
 */
public class INHoop 
{
	/**
	 *
	 */	
    public static void main( String[] args ) 
    {
    	@SuppressWarnings("unused")
		INLink link = new INLink(); // run the INLink constructor; We need this to have a global settings registry    	    	
   	
    	INFeatureMatrixBase.debug ("INQueryTest","main ()");
    	
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
        	INFeatureMatrixBase.debug ("INQueryTest","UIManager: setLookAndFeel, ClassNotFoundException");
			return;
		} 
        catch (InstantiationException e1) 
        {
        	INFeatureMatrixBase.debug ("INQueryTest","UIManager: setLookAndFeel, InstantiationException");
			return;
		} 
        catch (IllegalAccessException e1) 
        {
        	INFeatureMatrixBase.debug ("INQueryTest","UIManager: setLookAndFeel, IllegalAccessException");
			return;
		} 
        catch (UnsupportedLookAndFeelException e1) 
        {
        	INFeatureMatrixBase.debug ("INQueryTest","UIManager: setLookAndFeel, UnsupportedLookAndFeelException");
			return;
		}           
        
        JFrame frame = new INHoopFrame ();
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
