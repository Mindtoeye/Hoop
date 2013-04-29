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

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;

/**
 * http://www.experts-exchange.com/Programming/Languages/Java/A_351-Custom-File-Filtering-Using-Java-File-Choosers.html
 * http://stackoverflow.com/questions/4096433/making-jfilechooser-show-image-thumbnails
 */
public class HoopJFileChooser extends JFileChooser implements ActionListener, PropertyChangeListener 
{
	private static final long serialVersionUID = 7100156718902543535L;
	
    /** All preview icons will be this width and height */
    private static final int ICON_SIZE = 16;

    /** This blank icon will be used while previews are loading */
    private static final Image LOADING_IMAGE = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_ARGB);

    /** Edit this to determine what file types will be previewed. */
    private final Pattern imageFilePattern = Pattern.compile(".+?\\.(png|jpe?g|gif|tiff?)$", Pattern.CASE_INSENSITIVE);

    /** Use a weak hash map to cache images until the next garbage collection (saves memory) */
    private final Map imageCache = new WeakHashMap();
        
    private class ThumbnailView extends FileView 
    {
        /** This thread pool is where the thumnnail icon loaders run */
        private final ExecutorService executor = Executors.newCachedThreadPool();

        public Icon getIcon(File file) 
        {
            if (!imageFilePattern.matcher(file.getName()).matches()) 
            {
                return null;
            }

            // Our cache makes browsing back and forth lightning-fast! :D
            synchronized (imageCache) 
            {
                ImageIcon icon = (ImageIcon) imageCache.get(file);

                if (icon == null) {
                    // Create a new icon with the default image
                    icon = new ImageIcon(LOADING_IMAGE);

                    // Add to the cache
                    imageCache.put(file, icon);

                    // Submit a new task to load the image and update the icon
                    executor.submit(new ThumbnailIconLoader(icon, file));
                }

                return icon;
            }
        }
    }    
    
    private class ThumbnailIconLoader implements Runnable 
    {
        private final ImageIcon icon;
        private final File file;

        /** 
         * @param i
         * @param f
         */
        public ThumbnailIconLoader(ImageIcon i, File f) 
        {
            icon = i;
            file = f;
        }
        /**
         * 
         */
        public void run() 
        {
            System.out.println("Loading image: " + file);

            // Load and scale the image down, then replace the icon's old image with the new one.
            ImageIcon newIcon = new ImageIcon(file.getAbsolutePath());
            Image img = newIcon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            icon.setImage(img);

            // Repaint the dialog so we see the new icon.
            SwingUtilities.invokeLater(new Runnable() {public void run() {repaint();}});
        }
    }    

	/**
	 * Constructs a JFileChooser pointing to the user's default directory.
	 */
	public HoopJFileChooser()
	{
		debug ("HoopJFileChooser()");
		
		String lastPath=HoopLink.preferences.get ("lastDir","");
		
		if (lastPath.isEmpty()==false)
		{
			this.setCurrentDirectory(new File (lastPath));
		}
		
		this.addActionListener(this);
		this.addPropertyChangeListener(this);
	}
	/**
	 * Constructs a JFileChooser using the given File as the path.
	 */
	public HoopJFileChooser(File currentDirectory)
	{
		super (currentDirectory);
		
		debug ("HoopJFileChooser(File currentDirectory)");

		HoopLink.preferences.put("lastDir",currentDirectory.getAbsolutePath());
		
		this.addActionListener(this);
		this.addPropertyChangeListener(this);
	}
	/**
	 * Constructs a JFileChooser using the given current directory and FileSystemView.
	 */
	public HoopJFileChooser(File currentDirectory, FileSystemView fsv)
    {
		super (currentDirectory,fsv);
		
		debug ("HoopJFileChooser(File currentDirectory, FileSystemView fsv)");
		
		HoopLink.preferences.put("lastDir",currentDirectory.getAbsolutePath());
		
		this.addActionListener(this);
		this.addPropertyChangeListener(this);
    } 
	/**
	 * Constructs a JFileChooser using the given FileSystemView.
	 */
	public HoopJFileChooser(FileSystemView fsv)
	{
		super (fsv);
		
		debug ("HoopJFileChooser(FileSystemView fsv)");
		
		String lastPath=HoopLink.preferences.get ("lastDir","");
		
		if (lastPath.isEmpty()==false)
		{
			this.setCurrentDirectory(new File (lastPath));
		}		
		
		this.addActionListener(this);
		this.addPropertyChangeListener(this);
	}
	/**
	 * Constructs a JFileChooser using the given path.
	 */
	public HoopJFileChooser(String currentDirectoryPath)
	{
		super (currentDirectoryPath);
		
		debug ("HoopJFileChooser(String currentDirectoryPath)");
		
		HoopLink.preferences.put("lastDir",currentDirectoryPath);
		
		this.addActionListener(this);
		this.addPropertyChangeListener(this);
	}
	/**
	 * Constructs a JFileChooser using the given current directory path and FileSystemView.
	 */
	public HoopJFileChooser(String currentDirectoryPath, FileSystemView fsv)
	{
		super (currentDirectoryPath,fsv);
		
		debug ("HoopJFileChooser(String currentDirectoryPath, FileSystemView fsv)");
		
		HoopLink.preferences.put("lastDir",currentDirectoryPath);
		
		this.addActionListener(this);
		this.addPropertyChangeListener(this);
	}	
	/**
	 * 
	 */
	protected void debug (String aMessage)
	{
		HoopRoot.debug("HoopJFileChooser",aMessage);
	}	
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		debug ("actionPerformed ("+e.getActionCommand()+")");
		
		if (e.getActionCommand().equals("ApproveSelection")==true)
		{
			HoopLink.preferences.put("lastDir",this.getCurrentDirectory().getAbsolutePath());
		}
	}
	/**
	 * 
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		debug ("propertyChange ("+evt.getPropertyName()+")");
		
    	if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(evt.getPropertyName())==true) 
    	{
             File file = (File) evt.getNewValue();
             if (file != null) 
             {
            	 debug ("Directory: " + file.getName());
             }
    	}
    	
    	if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(evt.getPropertyName())==true) 
    	{
             File file = (File) evt.getNewValue();
             if (file != null) 
             {
            	 debug ("File: " + file.getName());
             }
    	}		
	}	
	
    {
        // This initializer block is always executed after any constructor call.
        setFileView(new ThumbnailView());
    }		
}
