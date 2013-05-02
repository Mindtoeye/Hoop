import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import edu.cmu.cs.in.base.HoopLink;

import java.awt.FontMetrics;

/**
 * 
 */
public class HoopGUITests 
{
	private JFrame frame = new JFrame();
	private JTabbedPane tabbedPane =null;
	private JButton addTabButton = null;
	private ImageIcon closeXIcon = null;
	private Dimension closeButtonSize;
	private int tabCounter = 0;

	public HoopGUITests() 
	{
		loadImageIcons ();
	  
		tabbedPane = new JTabbedPane();

		addTabButton = new JButton("Add Tab");

		closeXIcon = HoopLink.getImageByName("gtk-close.png");	  
	  
		tabbedPane.setUI(new BasicTabbedPaneUI() 
		{
			@Override
			protected int calculateTabWidth (int tabPlacement, 
											 int tabIndex,
											 FontMetrics metrics) 
			{				
		    	System.out.println("calculateTabWidth ()");
		    	
		    	System.out.println ("Total width " + tabbedPane.getWidth() + " for " + tabbedPane.getTabCount() + " tabs");
		    	
		    	int tabWidth=Math.round(tabbedPane.getWidth()/tabbedPane.getTabCount())-4;
		    	
				return tabWidth; // the width of the tab
			}
		});
	  
		addTabButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				add();
			}
		});
    
		closeButtonSize = new Dimension(closeXIcon.getIconWidth() + 2, closeXIcon.getIconHeight() + 2);

		frame.add(tabbedPane, BorderLayout.CENTER);
		frame.add(addTabButton, BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setMinimumSize(new Dimension(300, 300));
		frame.setVisible(true);
	}
	/**
	 * 
	 */
	private void loadImageIcons ()
	{
		//debug ("loadImageIcons ()");
  	
		java.net.URL imgURL=null;
		String loadPath="";
    		        	
		HoopLink.imageIcons=new ImageIcon [HoopLink.imgURLs.length];

		for (int i=0;i<HoopLink.imgURLs.length;i++)
		{
			loadPath="/assets/images/"+HoopLink.imgURLs [i];
			imgURL=getClass().getResource(loadPath);

			if (imgURL!=null) 
			{
				HoopLink.imageIcons [i]=new ImageIcon(imgURL,HoopLink.imgURLs [i]);
				//debug ("Loaded: " + loadPath);
			}
			//else 
			//	debug ("Unable to load image ("+loadPath+") icon from jar");    		
		}
  	
		HoopLink.icon=HoopLink.getImageByName("machine.png");
		HoopLink.linkIcon=HoopLink.getImageByName("link.jpg");
		HoopLink.unlinkIcon=HoopLink.getImageByName("broken.jpg");
	}  
	/**
	 * 
	 */
	public void add() 
	{
		final JPanel content = new JPanel();
		JPanel tab = new JPanel();
		
		BoxLayout boxLayout = new BoxLayout(tab, BoxLayout.X_AXIS);
		tab.setLayout(boxLayout);
		
		tab.setBorder (BorderFactory.createLineBorder(Color.green));
		tab.setOpaque(false);

		JLabel tabLabel = new JLabel("Tab " + (++tabCounter));

		JButton tabCloseButton = new JButton(closeXIcon);
		tabCloseButton.setOpaque(false);
		tabCloseButton.setPreferredSize(closeButtonSize);
		tabCloseButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int closeTabNumber = tabbedPane.indexOfComponent(content);
				tabbedPane.removeTabAt(closeTabNumber);
			}
		});

		tab.add(tabLabel, BorderLayout.WEST);
		tab.add(tabCloseButton, BorderLayout.EAST);

		tabbedPane.addTab(null, content);
		tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, tab);
	}
	/**
	 * 
	 */
  	public static void main(String[] args) 
  	{
  		// run the HoopLink constructor; We need this to have a global settings registry
  		@SuppressWarnings("unused")
		HoopLink link = new HoopLink();
  	
  		HoopGUITests main = new HoopGUITests();
  	}
}
