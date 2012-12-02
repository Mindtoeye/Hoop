
package edu.cmu.side;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import com.yerihyo.yeritools.io.FileToolkit;
import com.yerihyo.yeritools.swing.SimpleOKDialog;
import com.yerihyo.yeritools.swing.SwingToolkit.ResultOption;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopFileTools;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;
import edu.cmu.side.simple.newui.FastListModel;
import edu.cmu.side.dataitem.TrainingResultInterface;
import edu.cmu.side.export.ARFFExporter;
import edu.cmu.side.export.CSVExporter;
import edu.cmu.side.plugin.PluginManager;
import edu.cmu.side.plugin.SIDEPlugin;
import edu.cmu.side.simple.SimpleDocumentList;
import edu.cmu.side.simple.SimplePredictionResult;
import edu.cmu.side.simple.SimpleTrainingResult;
import edu.cmu.side.simple.feature.FeatureTable;
import edu.cmu.side.simple.newui.SimpleWorkbenchPanel;
import edu.cmu.side.simple.newui.features.FeatureTableListPanel;
import edu.cmu.side.simple.newui.machinelearning.ModelListPanel;
import edu.cmu.side.simple.newui.prediction.PredictionFileSelectPanel;

public class SimpleWorkbench extends HoopEmbeddedJPanel
{
	private static final long serialVersionUID = -1774294491405977680L;

	/**
	 * Static fields that everything else references for file lookup
	 */
	public static File rootFolder = null;
	public static File HoopFolder = null;
	
	static public String PLATFORM_FILE_SEPARATOR = System.getProperty("file.separator");
	static public String BASE_PATH = null;
	static public File PLUGIN_FOLDER = null;
	
	public static File dataFolder = null;
	public static File stopwordsFolder = null;
	public static File csvFolder = null;
	public static File toolkitsFolder = null;
	public static File savedFolder = null;
	
	public static PluginManager pluginManager = null;
	
	static List<FeatureTable> featureTables = null;
	static List<TrainingResultInterface> trainingResults = null;
	static List<SimplePredictionResult> predictionResults = null;
		
	public static SimpleWorkbenchPanel workbench=null;
	
	public SimpleWorkbench()
	{
		setClassName ("SimpleWorkbench");
		debug ("SimpleWorkbench ()");		
		
		setup ();
		
		workbench = new SimpleWorkbenchPanel();		
		workbench.actionPerformed(new ActionEvent(this,1,"plugins"));
		
		JScrollPane SIDEContainer=new JScrollPane (workbench);
		
		setContentPane (SIDEContainer);
	}	
	/**
	 * 
	 */
	public void setup ()
	{
		debug ("setup ()");
		
		PLATFORM_FILE_SEPARATOR = System.getProperty("file.separator");
		
		rootFolder = new File(HoopLink.project.getBasePath());
		HoopFolder = new File(System.getProperty("user.dir")+PLATFORM_FILE_SEPARATOR);
				
		BASE_PATH = rootFolder.getAbsolutePath()+ PLATFORM_FILE_SEPARATOR;
		
		PLUGIN_FOLDER = new File(HoopFolder, "plugins"+ PLATFORM_FILE_SEPARATOR + "SIDE");
		
		HoopFileTools fileTools=new HoopFileTools ();
				
		debug ("SIDE base path: " + BASE_PATH);
		debug ("SIDE application path: " + HoopFolder);
		debug ("SIDE plugin path: " + PLUGIN_FOLDER);
						
		fileTools.createDirectory(rootFolder + PLATFORM_FILE_SEPARATOR + "data");
		
		dataFolder = new File(rootFolder, "data");
				
		stopwordsFolder = new File(dataFolder, "stopwords");
				
		csvFolder = dataFolder;
		toolkitsFolder = new File(HoopFolder, "toolkits");
		
		fileTools.createDirectory(rootFolder + PLATFORM_FILE_SEPARATOR + "saved");
		
		savedFolder = new File(BASE_PATH, "saved");
		
		pluginManager = new PluginManager(PLUGIN_FOLDER);
		
		featureTables = new ArrayList<FeatureTable>();
		trainingResults = new ArrayList<TrainingResultInterface>();
		predictionResults = new ArrayList<SimplePredictionResult>();		
	}
	/**
	 * 
	 */
	public static SIDEPlugin[] getPluginsByType(String type)
	{
		return pluginManager.getSIDEPluginArrayByType(type);
	}
	/**
	 * 
	 */	
	public static void addFeatureTable(FeatureTable table)
	{
		featureTables.add(table);
	}
	/**
	 * 
	 */	
	public static void addTrainingResult(TrainingResultInterface result)
	{
		trainingResults.add(result);
	}
	/**
	 * 
	 */	
	public static void addPredictionResult(SimplePredictionResult result){
		predictionResults.add(result);
	}
	/**
	 * 
	 */	
	public static List<FeatureTable> getFeatureTables()
	{
		return featureTables;
	}
	/**
	 * 
	 */	
	public static List<TrainingResultInterface> getTrainingResults()
	{
		return trainingResults;
	}
	/**
	 * 
	 */	
	public static List<SimplePredictionResult> getPredictionResults()
	{
		return predictionResults;
	}
	/**
	 * 
	 */	
	public static void clearTrainingResults()
	{
		trainingResults = new ArrayList<TrainingResultInterface>();
		workbench.actionPerformed(null);
	}
	/**
	 * 
	 */	
	public static void clearFeatureTables()
	{
		featureTables = new ArrayList<FeatureTable>();
		workbench.actionPerformed(null);
	}
	/**
	 * 
	 */	
	public static void removeTrainingResult(int i)
	{
		trainingResults.remove(i);
		workbench.actionPerformed(null);
	}
	/**
	 * 
	 */	
	public static void removeFeatureTable(int i)
	{
		featureTables.remove(i);
		workbench.actionPerformed(null);
	}
	
	
	
	
	/**
	 * ActionListener for adding CSV files to SimpleListManagerPanel.
	 * @author emayfiel
	 *
	 */
	public static class FileAddActionListener implements ActionListener 
	{
		private Component parentComponent;
		private FastListModel model;
		private JFileChooser chooser = new JFileChooser(csvFolder);

		public FileAddActionListener(Component parentComponent, FastListModel model){
			this.parentComponent = parentComponent;
			this.model = model;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			chooser.setFileFilter(FileToolkit
					.createExtensionListFileFilter(new String[] { "csv" }, true));
			chooser.setMultiSelectionEnabled(true);
			int result = chooser.showOpenDialog(parentComponent);
			if (result != JFileChooser.APPROVE_OPTION) {
				return;
			}
			FastListModel fileListModel = model;
			fileListModel.addAll(chooser.getSelectedFiles());
		}
	}
	
	
	
	
	/**
	 * 
	 */	
	public static class FeatureTableSaveListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent ae){
			JFileChooser chooser = new JFileChooser();
			FeatureTable ft = FeatureTableListPanel.getSelectedFeatureTable();
			chooser.setCurrentDirectory(savedFolder);
			chooser.setMultiSelectionEnabled(false);
			chooser.setSelectedFile(new File(ft.getTableName() + ".ser"));
			int result = chooser.showSaveDialog(null);
			if (result != JFileChooser.APPROVE_OPTION) {
				return;
			}
			try{
				ft.serialize(chooser.getSelectedFile());
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 
	 */
	public static class FeatureTableLoadListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent ae){
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(savedFolder);
			chooser.setMultiSelectionEnabled(false);
			int result = chooser.showOpenDialog(null);
			if (result != JFileChooser.APPROVE_OPTION) {
				return;
			}
			try{
				featureTables.add(new FeatureTable(new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()))));
				workbench.actionPerformed(null);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 
	 */
	public static class FeatureTableExportListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			JComboBox combo = new JComboBox();
			combo.addItem("ARFF");
			combo.addItem("CSV");
			JPanel pane = new JPanel();
			pane.add(combo);
			ResultOption option = SimpleOKDialog.show(null, "export to...", pane);
			if(option == ResultOption.APPROVE_OPTION)
			{
				final String exportFormat = combo.getSelectedItem().toString();
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileFilter()
		        {

					@Override
					public boolean accept(File file)
					{
						return file.getPath().endsWith("."+exportFormat.toLowerCase());
					}

					@Override
					public String getDescription()
					{
						// TODO Auto-generated method stub
						return exportFormat+" Files";
					}
		        	
		        });
				
				FeatureTable ft = FeatureTableListPanel.getSelectedFeatureTable();
				chooser.setCurrentDirectory(SimpleWorkbench.dataFolder);
				chooser.setMultiSelectionEnabled(false);
				chooser.setSelectedFile(new File(ft.getTableName() + "." + combo.getSelectedItem().toString().toLowerCase()));
				int result = chooser.showDialog(null, "Export to "+exportFormat);
				if (result != JFileChooser.APPROVE_OPTION) {
					return;
				}
				if(exportFormat.equals("CSV")){
					CSVExporter.exportToCSV(ft, chooser.getSelectedFile());
				}
				else if(exportFormat.equals("ARFF")){
					ARFFExporter.export(ft, chooser.getSelectedFile());
				}
			}
		}
	}
	
	/**
	 * 
	 */	
	public static class TrainingResultSaveListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent ae){
			JFileChooser chooser = new JFileChooser();
			SimpleTrainingResult tr = ModelListPanel.getSelectedTrainingResult();
			chooser.setCurrentDirectory(savedFolder);
			chooser.setMultiSelectionEnabled(false);
			chooser.setSelectedFile(new File(tr.toString() + ".ser"));
			int result = chooser.showSaveDialog(null);
			if (result != JFileChooser.APPROVE_OPTION) {
				return;
			}
			try{
				tr.serialize(chooser.getSelectedFile());
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 
	 */	
	public static class TrainingResultLoadListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent ae){
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(savedFolder);
			chooser.setMultiSelectionEnabled(false);
			int result = chooser.showOpenDialog(null);
			if (result != JFileChooser.APPROVE_OPTION) {
				return;
			}
			try{
				trainingResults.add(new SimpleTrainingResult(chooser.getSelectedFile()));
				workbench.actionPerformed(null);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 
	 */	
	public static class PredictionResultSaveListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent ae){
			JFileChooser chooser = new JFileChooser();
			SimpleDocumentList output = PredictionFileSelectPanel.getPredictionDocuments();
			chooser.setCurrentDirectory(savedFolder);
			chooser.setMultiSelectionEnabled(false);
			chooser.setSelectedFile(new File(output.getCurrentAnnotation() + ".csv"));
			int result = chooser.showSaveDialog(null);
			if (result != JFileChooser.APPROVE_OPTION) {
				return;
			}
			try{
				BufferedWriter out = new BufferedWriter(new FileWriter(chooser.getSelectedFile()));
				out.write(output.toCSVString());
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * 
	 */	
	public static SimpleWorkbenchPanel getWorkbench()
	{
		return workbench;
	}
}
