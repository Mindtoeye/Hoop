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

package edu.cmu.cs.in.hoop.visualizers;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

//import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.collections.StoredSortedMap;

import com.toedter.calendar.JDateChooser;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopBerkeleyDB;
import edu.cmu.cs.in.base.io.HoopBerkeleyDBInstance;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;

/**
 * 
 */
public class HoopBackingDBInspector extends HoopEmbeddedJPanel implements MouseListener, ActionListener 
{
	private static final long serialVersionUID = -1353911499968570785L;
	
	private SimpleDateFormat df = new SimpleDateFormat ("EEE d MMM yyyy HH:mm:ss.SSS Z");
	
	private JList coreList=null;
	private JList tableList=null;
	private HoopBerkeleyDB driver=null;
	private JTextArea dbData=null;
	private JTable vizTable=null;
	
    private String[] dbheader = new String[] {"Index","Date", "Entry"};
    private String[][] dbdata = new String[][] {};	
    
    private DefaultTableModel model = null;    
	private HoopBerkeleyDBInstance dummyDB=null;		    
	private StoredSortedMap<String, String> dummyMap=null;		    
	private SortedMap<String, String> dummyMapRanged=null;	
	private Object[] dummyObjectMap=null;
    
    private Boolean showDates=false; 
    
    private JCheckBox toggleDateFormat=null;    
    private JTextField status=null;
    private JTextField statusBar=null;
    private JTextField tableInfo=null;
    private JTextField minRange=null;
    private JTextField maxRange=null;    
    private JButton setRange=null;    
    private JButton setDateRange=null;
    private JButton previousSet=null;
    private JButton nextSet=null;
    
    private JButton exportSet=null;
    
    private JRadioButton exportAllButton=null;
    private JRadioButton exportSelectedButton=null;    
    
    private Boolean exportAll=false;
    
    private JDateChooser startDate=null;
    private JDateChooser endDate=null;
        
    public static final int BYRANGE = 1;
    public static final int BYDATE = 2;
    
    private int sortby=BYRANGE;
    
	/**
	 * 
	 */	
	public HoopBackingDBInspector ()
	{	    
	    Border border=BorderFactory.createLineBorder(Color.black);
	    
	    JTabbedPane tabbedPane = new JTabbedPane();
	    	    
		this.setContentPane(tabbedPane);
		
	    JPanel panel=new JPanel ();
		
		tabbedPane.addTab("Data Manager",null,panel,"Load, edit and export data");
		
		panel.setLayout(new BoxLayout (panel,BoxLayout.Y_AXIS));
			    
		coreList = new JList ();
		coreList.setMinimumSize (new Dimension (100,50));
		coreList.addMouseListener (this);
		coreList.setFont(new Font("Dialog", 1, 10));
	    JScrollPane posScrollList=new JScrollPane (coreList);
	    	    	    	    
	    Box bottomContainer=new Box (BoxLayout.Y_AXIS);
	    	    
	    Box buttonBar=new Box (BoxLayout.X_AXIS);
	    buttonBar.setMinimumSize(new Dimension (50,20));
	    //buttonBar.setMaximumSize(new Dimension (50000,20));
	    
	    toggleDateFormat=new JCheckBox ();
	    toggleDateFormat.setText("Show Date/Raw");
	    toggleDateFormat.setFont(new Font("Dialog", 1, 10));
	    toggleDateFormat.addActionListener(this);
	    buttonBar.add(toggleDateFormat);
	    
	    buttonBar.add(new JSeparator(SwingConstants.VERTICAL));
	    
	    minRange=new JTextField ();
	    minRange.setText("0");
	    minRange.setFont(new Font("Dialog", 1, 10));
	    minRange.setMinimumSize(new Dimension (50,18));
	    minRange.setPreferredSize(new Dimension (50,18));
	    buttonBar.add(minRange);
	    	    	    	    
	    maxRange=new JTextField ();
	    maxRange.setText("100");
	    maxRange.setFont(new Font("Dialog", 1, 10));
	    maxRange.setMinimumSize(new Dimension (50,20));
	    maxRange.setPreferredSize(new Dimension (50,20));
	    buttonBar.add(maxRange);
	    	    
	    setRange=new JButton ();
	    setRange.setMargin(new Insets(1,1,1,1));
	    setRange.setText("Set");
	    setRange.setFont(new Font("Dialog", 1, 10));
	    setRange.setMinimumSize(new Dimension (60,20));
	    setRange.setPreferredSize(new Dimension (60,20));
	    setRange.addActionListener(this);
	    buttonBar.add(setRange);	    
	    
	    previousSet=new JButton ();
	    previousSet.setMargin(new Insets(1,1,1,1));
	    previousSet.setText("Previous");
	    previousSet.setFont(new Font("Dialog", 1, 10));
	    previousSet.setMinimumSize(new Dimension (60,20));
	    previousSet.setPreferredSize(new Dimension (60,20));
	    previousSet.addActionListener(this);
	    buttonBar.add(previousSet);
	    
	    nextSet=new JButton ();
	    nextSet.setMargin(new Insets(1,1,1,1));  
	    nextSet.setText("Next");
	    nextSet.setFont(new Font("Dialog", 1, 10));
	    nextSet.setMinimumSize(new Dimension (60,20));
	    nextSet.setPreferredSize(new Dimension (60,20));
	    nextSet.addActionListener(this);
	    buttonBar.add(nextSet);	    
	    
	    buttonBar.add(new JSeparator(SwingConstants.VERTICAL));
	    
	    startDate=new JDateChooser(new Date());
	    startDate.setFont(new Font("Dialog", 1, 10));
	    startDate.setMinimumSize(new Dimension (100,20));
	    startDate.setPreferredSize(new Dimension (100,20));
	    buttonBar.add (startDate);

	    endDate=new JDateChooser(new Date());
	    endDate.setFont(new Font("Dialog", 1, 10));
	    endDate.setMinimumSize(new Dimension (100,20));
	    endDate.setPreferredSize(new Dimension (100,20));
	    buttonBar.add(endDate);	    
	    
	    setDateRange=new JButton ();
	    setDateRange.setMargin(new Insets(1,1,1,1));
	    setDateRange.setText("Set Dates");
	    setDateRange.setFont(new Font("Dialog", 1, 10));
	    setDateRange.setMinimumSize(new Dimension (60,20));
	    setDateRange.setPreferredSize(new Dimension (60,20));
	    setDateRange.addActionListener(this);
	    buttonBar.add(setDateRange);
	    
	    buttonBar.add(new JSeparator(SwingConstants.VERTICAL));
	    	    	    
	    status=new JTextField ();
	    status.setText("Status: OK");
	    status.setEditable(false);
	    status.setBorder(border);
	    status.setFont(new Font("Dialog", 1, 10));
	    status.setMinimumSize(new Dimension (20,20));
	    status.setPreferredSize(new Dimension (100,20));
	    buttonBar.add(status);	    
	    
	    model = new DefaultTableModel(dbdata,dbheader);
	    
	    vizTable=new JTable (model);
		vizTable.setMinimumSize(new Dimension (100,50));
		vizTable.setFont(new Font("Dialog", 1, 10));
		vizTable.getColumnModel().getColumn(0).setPreferredWidth(27);
		vizTable.getColumnModel().getColumn(1).setPreferredWidth(120);

	    JScrollPane tableScrollList = new JScrollPane (vizTable);
	    tableScrollList.setMinimumSize(new Dimension (50,50));
	    //tableScrollList.setMaximumSize(new Dimension (5000,5000));
	    
	    Box toolBar=new Box (BoxLayout.X_AXIS);
	    toolBar.setMinimumSize(new Dimension (50,20));
	    //toolBar.setMaximumSize(new Dimension (50000,20));
	    
	    exportAllButton = new JRadioButton();
	    exportAllButton.setText("All");
	    exportAllButton.setFont(new Font("Dialog", 1, 10));
	    exportAllButton.addActionListener(this);
	    toolBar.add(exportAllButton);
	    
	    exportSelectedButton = new JRadioButton();
	    exportSelectedButton.setText("Selected");
	    exportSelectedButton.setFont(new Font("Dialog", 1, 10));
	    exportSelectedButton.setSelected(true);
	    exportSelectedButton.addActionListener(this);
	    toolBar.add(exportSelectedButton);

	    //Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(exportAllButton);
	    group.add(exportSelectedButton);
	    	    	   	    	    
	    exportSet=new JButton ();
	    exportSet.setMargin(new Insets(1,1,1,1));  
	    exportSet.setText("Export ...");
	    exportSet.setFont(new Font("Dialog", 1, 10));
	    exportSet.setMinimumSize(new Dimension (70,20));
	    exportSet.setPreferredSize(new Dimension (70,20));
	    exportSet.addActionListener(this);
	    toolBar.add(exportSet);
	    
	    JButton importSet=new JButton ();
	    importSet.setMargin(new Insets(1,1,1,1));  
	    importSet.setText("Import ...");
	    importSet.setFont(new Font("Dialog", 1, 10));
	    importSet.setMinimumSize(new Dimension (70,20));
	    importSet.setPreferredSize(new Dimension (70,20));
	    importSet.addActionListener(this);
	    toolBar.add(importSet);	    
	    	    
	    JTextField filler2=new JTextField ();
	    filler2.setText("Status: OK");
	    filler2.setEditable(false);
	    filler2.setBorder(border);
	    filler2.setFont(new Font("Dialog", 1, 10));
	    filler2.setMinimumSize(new Dimension (20,18));
	    filler2.setPreferredSize(new Dimension (100,20));
	    toolBar.add(filler2);
	    	    	    
	    tableInfo=new JTextField ();
	    tableInfo.setText("");
	    tableInfo.setEditable(false);
	    tableInfo.setBorder(BorderFactory.createLoweredBevelBorder());
	    tableInfo.setFont(new Font("Dialog", 1, 10));
	    tableInfo.setMinimumSize(new Dimension (20,20));
	    tableInfo.setPreferredSize(new Dimension (100,20));
	    //tableInfo.setMaximumSize(new Dimension (100,20));
	    
	    bottomContainer.add (buttonBar);
	    bottomContainer.add (tableInfo);
	    bottomContainer.add (tableScrollList);
	    bottomContainer.add (toolBar);
	    
	    dbData=new JTextArea ();
	    dbData.setFont(new Font("Courier", 1, 10));	    
	    JScrollPane dbDataScroller = new JScrollPane (dbData);
	    	    
	    JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,dbDataScroller, bottomContainer);
	    rightSplitPane.setOneTouchExpandable(true);
	    rightSplitPane.setDividerLocation(150);
	    	    	    
	    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,posScrollList, rightSplitPane);
	    splitPane.setOneTouchExpandable(true);
	    splitPane.setDividerLocation(150);	    
	    splitPane.setMinimumSize(new Dimension (20,20));
	    //splitPane.setMaximumSize(new Dimension (50000,50000));	    
		splitPane.setBorder(border);
	    
	    panel.add(splitPane);
	    
 	    
	    JPanel genPanel=new JPanel ();
		
		tabbedPane.addTab("Data Generator",null,genPanel,"Generate simulated data");
		
		genPanel.setLayout(new BoxLayout (genPanel,BoxLayout.Y_AXIS));
				
	    driver=new HoopBerkeleyDB ();
	    driver.setDbDir (getProjectPath ()+"/system/documents");
	    driver.startDBService ("Documents");
	    
	    getDBInfo ();
	}
    /**
     * 
     */
    protected String getProjectPath ()
    {
    	if (HoopLink.project!=null)
    		return (".");
    		
    	return (HoopLink.project.getBasePath());
    }	
	/**
	 * 
	 */
	private void showMessage (String aMessage)
	{
		debug (aMessage);
		JOptionPane.showMessageDialog(this,aMessage);
	}	
	/**
	 * 
	 */		
	private void reset ()
	{
		minRange.setText("0");
		maxRange.setText("100");			
		sortby=BYRANGE;			
		tableInfo.setText ("");
	}	
	/**
	 * 
	 */	
	public Boolean getExportAll() 
	{
		return exportAll;
	}
	/**
	 * 
	 */	
	public void setExportAll(Boolean exportAll) 
	{
		this.exportAll = exportAll;
	}	
	/**
	 * 
	 */	
	private void getDBInfo ()
	{
		debug ("getDBInfo ()");
		
		if (driver==null)
			return;
		
		dbData.setText(driver.getStatus ());
		
		ArrayList<String> dbs=driver.getDatabases ();
		
		coreList.setModel(modelFromList(dbs));		
	}
	/**
	 *
	 */	
	public DefaultListModel modelFromList(ArrayList<String> dbs)
	{
		debug ("modelFromArray ()");
		
		DefaultListModel mdl=new DefaultListModel ();
		
		for (int i=0;i<dbs.size();i++)
		{
			mdl.addElement(dbs.get(i));
		}
		
		return (mdl);
	}
	/**
	 *
	 */	
	public DefaultListModel modelFromMap(Map<String, String> aMap)
	{
		debug ("modelFromArray ()");
		
		DefaultListModel mdl=new DefaultListModel ();
						
        Iterator<Map.Entry<String, String>> iter=aMap.entrySet().iterator();
                        
        while (iter.hasNext()) 
        {
            Map.Entry<String, String> entry = iter.next();
            mdl.addElement(entry.getKey().toString());
        }    			
		
		return (mdl);
	} 
	/**
	 *
	 */	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if (e.getClickCount()==2)
		{			
			reset ();
			
			int index = coreList.locationToIndex(e.getPoint());
			ListModel dlm = coreList.getModel();
			Object item = dlm.getElementAt(index);;
			coreList.ensureIndexIsVisible(index);
			
			debug ("Loading database: " + item);
				
			//sessionDB=null;
			//memoryDB=null;
			dummyDB=null;
			
			//sessionMap=null;
			//memoryMap=null;
			dummyMap=null;
			
			HoopBerkeleyDBInstance inst=null;
			
			try 
			{
				inst = driver.accessDB (item.toString());
			} 
			catch (Exception e1) 
			{
				debug ("Error accessing or creating database: " + item.toString());
				e1.printStackTrace();
			}
			
			if (inst!=null)
			{								
				if (item.toString().indexOf("-TSDummy")!=-1)
				{
					debug ("Mapping dummy db ...");
					
					try 
					{
						dummyDB=driver.accessDB(item.toString());
					} 
					catch (Exception e1) 
					{
						debug ("Error accessing or creating dummy data " + item.toString());
						e1.printStackTrace();
					}
					
					if (dummyDB!=null)
					{
						StringBinding keyBinding = new StringBinding();
						StringBinding valueBinding = new StringBinding();

						dummyMap=new StoredSortedMap<String, String> (dummyDB.getDB (), keyBinding, valueBinding, true);
					}
					else
						debug ("Error: unable to access (dummy) database");
				}							
				
				refreshTable ();		
				
				/*
				if (sessionMap!=null)
				{
					@SuppressWarnings("unchecked")
					java.util.Map.Entry<String, HoopTSSession> entry = (java.util.Map.Entry<String, HoopTSSession>) sessionObjectMap[0];
					Long fromKey = entry.getKey();					
					
					startDate.setDate(new Date (fromKey));
					
					entry = (Entry<String, HoopTSSession>) sessionObjectMap[sessionObjectMap.length-1];
					Long toKey = entry.getKey();					
					
					endDate.setDate(new Date (toKey));
					
					tableInfo.setText("Table date range is from \""+df.format(fromKey)+"\" to \""+df.format(toKey)+"\"");
				}
				
				if (memoryMap!=null)
				{
					@SuppressWarnings("unchecked")
					java.util.Map.Entry<String, HoopTSMemory> entry = (java.util.Map.Entry<String, HoopTSMemory>) memoryObjectMap[0];
					Long fromKey = entry.getKey();					
					
					startDate.setDate(new Date (fromKey));
					
					entry = (Entry<String, HoopTSMemory>) memoryObjectMap[memoryObjectMap.length-1];
					Long toKey = entry.getKey();					
					
					endDate.setDate(new Date (toKey));
					
					tableInfo.setText("Table date range is from \""+df.format(fromKey)+"\" to \""+df.format(toKey)+"\"");
				}
				*/				
			}
			else
			{
				debug ("Error: unable to obtain handle to database: "+item.toString ());
				if (statusBar!=null)
					statusBar.setText("Error: unable to obtain handle to database: "+item.toString ());
			}
		}		
	}
	/**
	 * 
	 */
	private void refreshTable ()
	{
		debug ("refreshTable ()");
				
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		createRange ();
							
		if (dummyMap!=null)
		{		
			if (statusBar!=null)
				statusBar.setText("Creating Dummy Map from DB ...");
			
			status.setText ("Table size: " + dummyMap.entrySet().size());
			
			Long start=Long.parseLong(minRange.getText());
			Long range=Long.parseLong(maxRange.getText());
			
			if (dummyMap.entrySet ().size ()<(start+range))
			{
			    minRange.setEnabled(false);
			    maxRange.setEnabled(false);
			    setRange.setEnabled(false);    
			    previousSet.setEnabled(false);
			    nextSet.setEnabled(false);
			}
			else
			{
			    minRange.setEnabled(true);
			    maxRange.setEnabled(true);
			    setRange.setEnabled(true);
			    previousSet.setEnabled(true);
			    nextSet.setEnabled(true);				
			}
			
			// First remove everything we already have in the table
			
			model.setRowCount(0);
						
			mapToTable (dummyMapRanged,model);
			dummyArrayToTable (dummyObjectMap,model);
			
			if (statusBar!=null)
				statusBar.setText("Map available");
		}	
				
		vizTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		vizTable.getColumnModel().getColumn(0).setWidth(27);
		vizTable.getColumnModel().getColumn(1).setWidth(120);
				
		this.setCursor(Cursor.getDefaultCursor());
	}
	/**
	 * 
	 */
	private void dummyMapToTable (Map<String, String> aMap,DefaultTableModel aModel)
	{
		debug ("dMapToTable ("+aMap.entrySet().size()+")");
		
		//Long start=Long.parseLong(minRange.getText());
		Long range=Long.parseLong(maxRange.getText());
		
        Iterator<Map.Entry<String, String>> iter=aMap.entrySet().iterator();
        
        int counter=0;
        
        debug ("Transforming data ...");
        
        while ((iter.hasNext()) && (counter<range)) 
        {
            Map.Entry<String, String> entry = iter.next();
            
            //Map.Entry entry = (Map.Entry) iter.next();
            
            if (entry!=null)
            {            
            	//System.out.print(".");
            	
            	if (showDates==true)
            	{
            		Date shower=new Date (entry.getKey());
            		String aDateString=df.format(shower);
            
            		aModel.insertRow(vizTable.getRowCount(),new Object[]{aDateString,entry.getValue().toString()});
            	}
            	else
            		aModel.insertRow(vizTable.getRowCount(),new Object[]{entry.getKey ().toString (),entry.getValue().toString()});
            }
            else
            	debug ("Error, unable to get memory entry from map");
            
            counter++;
        }    		
        
        debug ("memoryMapToTable () Done");
	}		
	/**
	 * 
	 */
	private void mapToTable (Map<String, String> aMap,DefaultTableModel aModel)
	{
		debug ("mapToTable ()");
		
		//Long start=Long.parseLong(minRange.getText());
		Long range=Long.parseLong(maxRange.getText());
		
        Iterator<Map.Entry<String, String>> iter=aMap.entrySet().iterator();
        
        int counter=0;
        
        while ((iter.hasNext()) && (counter<range)) 
        {
            Map.Entry<String, String> entry = iter.next();
            
            if (showDates==true)
            {
            	Date shower=new Date (entry.getKey());
            	String aDateString=df.format(shower);
            
            	aModel.insertRow(vizTable.getRowCount(),new Object[]{aDateString,entry.getValue().toString()});
            }
            else
            	aModel.insertRow(vizTable.getRowCount(),new Object[]{entry.getKey ().toString (),entry.getValue().toString()});
            
            counter++;
        }    								
	}	
	/**
	 * 
	 */
	private void dummyArrayToTable (Object [] anArray,DefaultTableModel aModel)
	{
		debug ("dummyArrayToTable ()");
		
		Long start=Long.parseLong(minRange.getText());
		Long range=Long.parseLong(maxRange.getText());
				
		for (Long i=start;i<range;i++)
		{
			int mappedIndex=i.intValue(); // !!! For large databases this might get out of range!
			
			@SuppressWarnings("unchecked")
			java.util.Map.Entry<String, String> entry = (java.util.Map.Entry<String, String>) anArray[mappedIndex];
			String key = entry.getKey();
			String value = entry.getValue();
						
			aModel.insertRow(vizTable.getRowCount(),new Object[]{key,value});
			
			/*
            if (showDates==true)
            {
            	Date shower=new Date (key);
            	String aDateString=df.format(shower);
	            
            	aModel.insertRow(vizTable.getRowCount(),new Object[]{aDateString,value});
            }
            else
            	aModel.insertRow(vizTable.getRowCount(),new Object[]{key.toString(),value});
            */
		}
	}		
	/**
	 *
	 */	
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		//debug ("mouseEntered ()");		
	}
	/**
	 *
	 */	
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		//debug ("mouseExited ()");		
	}
	/**
	 *
	 */	
	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		//debug ("mousePressed ()");		
	}
	/**
	 *
	 */	
	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		//debug ("mouseReleased ()");		
	}
	/**
	 *
	 */	
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
		//String act=event.getActionCommand();
		
		if (event.getSource ()==toggleDateFormat)
		{		
			if (toggleDateFormat.isSelected()==true)
				showDates=true;
			else
				showDates=false;
				
			refreshTable ();
			
			return;
		}
		
		if (event.getSource()==setRange)
		{
			sortby=BYRANGE;
			
			createRange ();
			
			refreshTable ();
			
			return;
		}
		
		if (event.getSource()==setDateRange)
		{			
			sortby=BYDATE;
			
			refreshTable ();
			
			return;
		}		
		
		if (event.getSource()==exportAllButton)
		{
			setExportAll(true);
			return;
		}
		
		if (event.getSource()==exportSelectedButton)
		{
			setExportAll(false);
			return;
		}			
		
		if (event.getSource()==exportSet)
		{
			/*
			HoopDBFileFilter filter1 = new HoopDBFileFilter("Delimited spreadsheet files", new String[] { "CSV" });
			
			final JFileChooser fc = new JFileChooser();
			fc.setFileFilter(filter1);
			
			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
				File file = fc.getSelectedFile();

				debug ("Opening: " + file.getName() + ".");
				
				exportData (file.getAbsolutePath());
	        } 
			else 
			{
				debug ("Open command cancelled by user.");
			}
			*/		
			
			return;
		}
		
		if (event.getSource ()==setRange)
		{
			//Long start=Long.parseLong(minRange.getText());
			//Long range=Long.parseLong(maxRange.getText());
						
			refreshTable ();
			
			return;
		}
		
		if (event.getSource ()==previousSet)
		{
			Long start=Long.parseLong(minRange.getText());
			Long range=Long.parseLong(maxRange.getText());
			
			start-=100;
			range-=100;
			
			minRange.setText (start.toString ());
			maxRange.setText (range.toString ());
			
			refreshTable ();
			
			return;
		}
		
		if (event.getSource ()==nextSet)
		{
			Long start=Long.parseLong(minRange.getText());
			Long range=Long.parseLong(maxRange.getText());
			
			start+=100;
			range+=100;
			
			minRange.setText (start.toString ());
			maxRange.setText (range.toString ());
			
			refreshTable ();
			
			return;
		}		
	}
	/**
	* Returns a view of the portion of this sorted map whose keys range from fromKey, inclusive, 
	* to toKey, exclusive. (If fromKey and toKey are equal, the returned sorted map is empty.) 
	* The returned sorted map is backed by this sorted map, so changes in the returned sorted 
	* map are reflected in this sorted map, and vice-versa. The returned Map supports all 
	* optional map operations that this sorted map supports.
	* 
	* The map returned by this method will throw an IllegalArgumentException if the user attempts 
	* to insert a key outside the specified range.
	* 
	* Note: this method always returns a half-open range (which includes its low endpoint but not 
	* its high endpoint). If you need a closed range (which includes both endpoints), and the key 
	* type allows for calculation of the successor a given key, merely request the subrange from 
	* lowEndpoint to successor(highEndpoint). For example, suppose that m is a map whose keys are 
	* strings. The following idiom obtains a view containing all of the key-value mappings in m 
	* whose keys are between low and high, inclusive:
	* 
	* Map sub = m.subMap(low, high+"\0");
	* 
	* A similarly technique can be used to generate an open range (which contains neither endpoint). 
	* The following idiom obtains a view containing all of the key-value mappings in m whose keys 
	* are between low and high, exclusive:
	* 
	* Map sub = m.subMap(low+"\0", high);
	*/
	private void createRange ()
	{
		debug ("createRange ()");
		
		/*
		Long minTest=Long.parseLong(minRange.getText());
		Long maxTest=Long.parseLong(maxRange.getText());
		
		if (minTest>maxTest)
		{
			showMessage ("Error: maximum range needs to be larger than minimum range");			
			return;
		}
		
		if (minTest==maxTest)
		{
			showMessage ("Error: Start index is equal to end index");
			return;
		}			
		
		if (minTest<0)
		{
			showMessage ("Error: Can't enter negative numbers");
			return;
		}
				
		if (dummyMap!=null)
		{
			if (maxTest>dummyMap.size())
			{
				debug ("Error: max range is larger than dataset");
				return;
			}
			
			try
			{
				dummyMapRanged=dummyMap.subMap (minTest,maxTest);
			}
			catch (IllegalArgumentException e)
			{
				showMessage ("One of the range indexes is out of bounds");
				return;
			}
			
			dummyObjectMap=dummyMap.entrySet().toArray();
		}
		*/
								
		debug ("createRange () Done");
	}
	/** 
	 * @param aFile
	 */
	private void exportData (String aFile)
	{
		debug ("exportData ()");
		
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
		String fileFormatted=aFile;
		
		if (aFile.toLowerCase().indexOf("csv")==-1)
			fileFormatted=aFile+".csv";
				
		if (HoopLink.fManager.openStream(fileFormatted)==false)
		{
			debug ("Error opening file for writing");
			this.setCursor(Cursor.getDefaultCursor());
			return;
		}
		
		if (exportAll==true)
		{
			/*
			if (sessionMap!=null)
			{
				HoopTSSession headerGenerator=new HoopTSSession ();
				fManager.writeToStream("Time,");
				fManager.writeToStream(headerGenerator.toCSVHeader());
				
				Iterator<Map.Entry<String, HoopTSSession>> iter=sessionMap.entrySet().iterator();
	        	        	
				while (iter.hasNext()) 
				{
					Map.Entry<String, HoopTSSession> entry = iter.next();
	            
					if (entry!=null)
					{
						if (showDates==true)
						{
							Date shower=new Date (entry.getKey());
							String aDateString=df.format(shower);
	            		
							fManager.writeToStream(aDateString+","+entry.getValue().toCSV());	            
						}
						else
						{
							fManager.writeToStream(entry.getKey ().toString ()+","+entry.getValue().toCSV());
						}	
						
						fManager.writeToStream("\n");
					}
					else
						debug ("Error getting session from map");	            	        
				}
			}
			*/
			
			/*
			if (memoryMap!=null)
			{		
				HoopTSMemory headerGenerator=new HoopTSMemory ();
				fManager.writeToStream("Time,");
				fManager.writeToStream(headerGenerator.toCSVHeader());				
				
				Iterator<Map.Entry<String, HoopTSMemory>> iter=memoryMap.entrySet().iterator();
	        	        	
				while (iter.hasNext()) 
				{
					Map.Entry<String, HoopTSMemory> entry = iter.next();
	            
					if (entry!=null)
					{
						if (showDates==true)
						{
							Date shower=new Date (entry.getKey());
							String aDateString=df.format(shower);
	            		
							fManager.writeToStream(aDateString+","+entry.getValue().toCSV());	            
						}
						else
						{
							fManager.writeToStream(entry.getKey ().toString ()+","+entry.getValue().toCSV());
						}
						
						fManager.writeToStream("\n");
					}
					else
						debug ("Error getting session from map");
				}
			}
			*/			
		}
		else
		{
			Long minTest=Long.parseLong(minRange.getText());
			Long maxTest=Long.parseLong(maxRange.getText());
		}
		
		HoopLink.fManager.closeStream();
		
		this.setCursor(Cursor.getDefaultCursor());
	}	
	/**
	 * 
	 */
	public void handleCloseEvent ()
	{
		debug ("handleCloseEvent ()");
		
	}
	/**
	 *
	 */	
	public void updateContents() 
	{
		debug ("updateContents ()");
			
		getDBInfo ();
	}
	/**
	 *
	 */	
	public void updateSize() 
	{
		debug ("updateSize ()");
		
	}		
}
