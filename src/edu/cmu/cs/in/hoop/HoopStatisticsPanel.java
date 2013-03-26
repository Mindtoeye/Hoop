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
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.cmu.cs.in.base.HoopFixedSizeQueue;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.controls.HoopButtonBox;
import edu.cmu.cs.in.controls.HoopControlTools;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.cs.in.hoop.visualizers.HoopScatterPlot;
//import edu.cmu.cs.in.controls.base.HoopJInternalFrame;
import edu.cmu.cs.in.stats.HoopPerformanceMeasure;
import edu.cmu.cs.in.stats.HoopSampleDataSet;
import edu.cmu.cs.in.stats.HoopSampleMeasure;

/** 
 * 
 */
public class HoopStatisticsPanel extends HoopEmbeddedJPanel implements ActionListener, ItemListener
{	
	private static final long serialVersionUID = -1;

	private JTextArea console=null;
	private int consoleSize=200; // Only show 200 lines
	private HoopFixedSizeQueue <String>consoleData=null;
	
	private JButton clearButton=null;
	private JButton saveButton=null;	
	private JTextField maxLines=null;
	private JButton setButton=null;
	private JButton inButton=null;
	private JButton outButton=null;	
	
	private HoopButtonBox buttonBox=null;
	
	private HoopScatterPlot plotter=null;
	
	private int fontSize=8;
	
    private JComboBox <String>datasetChooser=null;
    
    private JButton generateData=null;
    private int genIndex=0;
	
	/**
	 * 
	 */	
	public HoopStatisticsPanel()
	{
		super (HoopLink.getImageByName("graph-icon2.gif"));
		
		setClassName ("HoopStatisticsPanel");
		debug ("HoopStatisticsPanel ()");
		
		consoleData=new HoopFixedSizeQueue<String>(consoleSize);
		
		Box mainBox = new Box (BoxLayout.Y_AXIS);
		
		// Create controls first
		
	   	buttonBox=new HoopButtonBox ();
	   	buttonBox.setMinimumSize(new Dimension (100,24));
	   	buttonBox.setPreferredSize(new Dimension (200,24));
	   	buttonBox.setMaximumSize(new Dimension (2000,24));		
						
	   	clearButton=HoopControlTools.makeNavigationButton ("clear","Clear",HoopLink.imageIcons [8]);
		clearButton.addActionListener(this);
		
		saveButton=HoopControlTools.makeNavigationButton ("save","Save",HoopLink.imageIcons [19]);
		saveButton.addActionListener(this);
		
		maxLines=new JTextField ();
		maxLines.setText(String.format("%d",consoleSize));
		maxLines.setFont(new Font("Courier",1,fontSize));
		maxLines.setPreferredSize(new Dimension (40,25));
		maxLines.setMaximumSize(new Dimension (40,25));
		
		setButton=HoopControlTools.makeNavigationButton ("set","Set",HoopLink.imageIcons [22]);
		setButton.addActionListener(this);
		
		inButton=HoopControlTools.makeNavigationButton ("in","in",HoopLink.imageIcons [72]);
		inButton.addActionListener(this);
		
		outButton=HoopControlTools.makeNavigationButton ("out","Out",HoopLink.imageIcons [73]);
		outButton.addActionListener(this);
		
		datasetChooser = new JComboBox();
		datasetChooser.setFont(new Font("Dialog",1,10));
		datasetChooser.setPreferredSize(new Dimension (150,20));
		datasetChooser.setMaximumSize(new Dimension (150,20));	   
		datasetChooser.addItemListener (this);
		
		generateData=HoopControlTools.makeNavigationButton ("generate","Generate",null);
		generateData.setText("Generate");
		generateData.setPreferredSize(new Dimension (100,22));
		generateData.addActionListener(this);
						
		buttonBox.addComponent(datasetChooser);		
		
		buttonBox.addComponent(clearButton);
		buttonBox.addComponent(saveButton);
		buttonBox.addComponent(maxLines);
		buttonBox.addComponent(setButton);
		buttonBox.addComponent(inButton);
		buttonBox.addComponent(outButton);
		buttonBox.addComponent(generateData);
								
		console=new JTextArea ();
		console.setEditable (false);
	    console.setFont(new Font("Courier",1,8));
		console.setMinimumSize(new Dimension (50,150));
						
		JScrollPane consoleContainer = new JScrollPane (console);
		consoleContainer.setMinimumSize(new Dimension (50,50));
		consoleContainer.setPreferredSize(new Dimension (500,50));
							
		JTabbedPane tabbedPane = new JTabbedPane();
		
		tabbedPane.addTab("Console",null,consoleContainer,"Raw input from external sources");
		
		plotter=new HoopScatterPlot ();
		
		tabbedPane.addTab("Plotter",null,plotter,"Statistics graph visualization");
		
		mainBox.add(buttonBox);
		mainBox.add(tabbedPane);
				
		setContentPane (mainBox);
		
		addDataSet ("Cluster Performance");
	}
	/**
	 *
	 */
	public void setData (HoopSampleDataSet aData,String aLabel)
	{
		debug ("setData ("+aLabel+")");
		
		plotter.setData(aData);
			
		addDataSet (aLabel);
	}	
	/**
	 * 
	 */	
	public int getConsoleSize() 
	{
		return consoleSize;
	}
	/**
	 * 
	 */	
	public void setConsoleSize(int consoleSize) 
	{
		this.consoleSize = consoleSize;
	}	
	/**
	 * 
	 */		
	public void appendString (String aMessage)
	{		
		consoleData.add (aMessage);
		
		if (console!=null)
		{
			console.setText(""); // Reset
	
			//System.out.println ("Console size: " + consoleData.size ());
			
			for (int i=0;i<consoleData.size();i++)
			{			
				String aString=consoleData.get(i);
				console.append(aString);
			}
			
			// Scroll to bottom
			console.setCaretPosition(console.getDocument().getLength());
		}	
	}
	/**
	 * 
	 */	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ()");
		
		//String act=event.getActionCommand();
		JButton button = (JButton)event.getSource();		

		if (button==clearButton)
		{
			console.setText("");
		}
		
		if (button==saveButton)
		{
			
		}		
		
		if (button==setButton)
		{
			
		}
		
		if (button==generateData)
		{			
			generateDataSet ();
		}
		
		if (button==inButton)
		{
			fontSize++;
			
			if (fontSize>23)
				fontSize=23;
			
			console.setFont(new Font("Courier",1,fontSize));
		}
		
		if (button==outButton)
		{
			fontSize--;
			
			if (fontSize<1)
				fontSize=1;
			
			console.setFont(new Font("Courier",1,fontSize));			
		}							
	}
	/**
	 * 
	 */
	protected void processClose ()
	{
		debug ("processClose ()");				
	}	
	/**
	 * 
	 */
	public void addDataSet (String aLabel)
	{
		debug ("addDataSet ("+aLabel+")");
				
		datasetChooser.addItem(aLabel);
	}
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		debug ("itemStateChanged ()");
		
	}
	/**
	 * 
	 */
	private void generateDataSet ()
	{
		HoopSampleDataSet aSet=new HoopSampleDataSet ("Generated"+genIndex);
		
		ArrayList<HoopSampleMeasure> aData=aSet.getDataSet();
		
		Random randomGenerator = new Random();
		
		for (int i=0;i<1000;i++)
		{
			HoopSampleMeasure aMeasure=new HoopSampleMeasure ();
			
			long randomLong = nextLong (randomGenerator,500);
			
			aMeasure.setXValue(i);
			aMeasure.setMeasure(randomLong);
			aMeasure.setOpen(false);
			
			aData.add(aMeasure);
		}
		
		plotter.setData(aSet);
		
		addDataSet ("Generated"+genIndex);
		
		genIndex++;
	}
	/**
	 * 
	 * @param rng
	 * @param n
	 * @return
	 */
	long nextLong(Random rng, long n) 
	{
	   // error checking and 2^x checking removed for simplicity.
	   long bits, val;
	   
	   do 
	   {
		   bits = (rng.nextLong() << 1) >>> 1;
		   val = bits % n;
	   } while (bits-val+(n-1) < 0L);
	   
	   return val;
	}	
}
