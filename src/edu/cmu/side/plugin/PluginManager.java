package edu.cmu.side.plugin;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import oracle.xml.parser.v2.XMLDocument;

import org.w3c.dom.Element;

import com.mysterion.xml.XMLBoss;
import com.yerihyo.yeritools.collections.MapToolkit.ListValueMap;
import com.yerihyo.yeritools.debug.YeriDebug;
import com.yerihyo.yeritools.io.FileToolkit;
import com.yerihyo.yeritools.text.StringToolkit;
import com.yerihyo.yeritools.xml.XMLToolkit;

import edu.cmu.cs.in.base.HoopRoot;

public class PluginManager extends HoopRoot 
{	
	private ListValueMap<String,PluginWrapper> pluginTypeMap = new ListValueMap<String,PluginWrapper>();
	private String errorMessage;	
	public ListValueMap<String,PluginWrapper> getPluginTypeMap(){ return pluginTypeMap; }
	
	/**
	 * 
	 */
	public PluginManager ()
	{
		setClassName ("PluginManager");
		debug ("PluginManager ()");		
	}
	/**
	 * 
	 */
	public PluginManager(File rootFolder) 
	{
		setClassName ("PluginManager");
		debug ("PluginManager ()");		
		
		// Traverse the directory, building a PluginCollection for each folder
		// that we find
		try
		{
			YeriDebug.ASSERT(rootFolder.isDirectory());
		} 
		catch(RuntimeException re)
		{
			System.err.println(rootFolder.toString());
		}
		
		StringBuilder errorStringBuilder = new StringBuilder();
		
		this.pluginTypeMap = createPluginTypeMap(rootFolder, errorStringBuilder);
		
		errorMessage = errorStringBuilder.toString();
	}
	/**
	 * 
	 */
	public String getErrorMessage() 
	{
		return errorMessage;
	}	
	/**
	 * 
	 */
	public Collection<PluginWrapper> getAllPlugins() 
	{
		return pluginTypeMap.valueElements();
	}
	/**
	 * 
	 */
	public Set<String> getPluginTypes()
	{
		return pluginTypeMap.keySet();
	}
	/**
	 * 
	 * @param pluginClassName
	 * @return
	 */
	public PluginWrapper getPluginWrapperByPluginClassName(String pluginClassName) 
	{
		debug ("getPluginWrapperByPluginClassName ()");
		
		PluginWrapper result = null;

		for (Iterator<PluginWrapper> iTemp = getAllPlugins().iterator(); iTemp.hasNext();) {
			PluginWrapper pTemp = (PluginWrapper) iTemp.next();
			if (pTemp.getConfigMap().get(PluginWrapper.CLASSNAME).equalsIgnoreCase(pluginClassName)) {
				result = pTemp;
				break;
			}
		}
		return result;
	}
	/**
	 * 
	 * @param type
	 * @return
	 */
	public List<SIDEPlugin> getPluginCollectionByType(String type) 
	{
		debug ("getPluginCollectionByType ()");
		
		List<SIDEPlugin> pluginList = new ArrayList<SIDEPlugin>();
		for(PluginWrapper pluginWrapper : getPluginWrapperCollectionByType(type)){
			SIDEPlugin sidePlugin = pluginWrapper.getSIDEPlugin();
			pluginList.add(sidePlugin);
		}
		return pluginList;
	}
	/**
	 * 
	 * @param type
	 * @return
	 */
	public List<PluginWrapper> getPluginWrapperCollectionByType(String type)
	{
		debug ("getPluginWrapperCollectionByType ()");
		
		List<PluginWrapper> pluginWrapperList = new ArrayList<PluginWrapper>();
		
		for(String key : pluginTypeMap.keySet()){
			if(!key.startsWith(type)){ continue; }
			pluginWrapperList.addAll(pluginTypeMap.get(key));
		}
		return pluginWrapperList;
	}
	
	public SIDEPlugin[] getSIDEPluginArrayByType(String type)
	{
		debug ("getSIDEPluginArrayByType ()");
		
		List<SIDEPlugin> sidePluginList = new ArrayList<SIDEPlugin>();
		
		Collection<PluginWrapper> pluginWrapperCollection = pluginTypeMap.get(type);
		for(PluginWrapper pluginWrapper : pluginWrapperCollection){
			SIDEPlugin sidePlugin = pluginWrapper.getSIDEPlugin();
			sidePluginList.add(sidePlugin);
		}
		return sidePluginList.toArray(new SIDEPlugin[0]);
	}

	public void addPluginWrapper(PluginWrapper pluginWrapper) 
	{
		debug ("addPluginWrapper ()");
		
		pluginTypeMap.add(pluginWrapper.getType(), pluginWrapper);
	}

	public String toString() 
	{
		return "PluginWrapper Manager";
	}
	
	private static Collection<PluginWrapper> createPluginOfFolder(File rootFolder, StringBuilder errorComment)
	{
		HoopRoot.debug ("PluginManager","createPluginOfFolder ()");
		
		List<PluginWrapper> pluginList = new ArrayList<PluginWrapper>();
		
		// config file
		File configFile = new File(rootFolder, "config.xml");
		
		if (!configFile.exists()) 
		{ 
			HoopRoot.debug ("PluginManager","Error: plugin descriptor does not exist in: " + rootFolder);
			return null; 
		}
		
		XMLDocument config = null;
		
		try 
		{
			config = XMLBoss.XMLFromFile(configFile);
		} 
		catch (IOException e) 
		{
			errorComment.append(e.getMessage());
			return null;
		}
		
		if (config == null) 
		{
			errorComment.append("The xmiFile '").append(FileToolkit.getAbsoluteCanonicalPath(configFile)).append("' has an invalid internal structure.");
			return null;
		}
		
		Element root = config.getDocumentElement();
		
		for (Element element : XMLToolkit.getChildElements(root)) 
		{
			PluginWrapper pluginWrapper = new PluginWrapper(rootFolder,element);
			
			try 
			{
				pluginWrapper.fromXML(element);
			} 
			catch (Exception e) 
			{
				errorComment.append(e.getMessage()).append(StringToolkit.newLine());
			}
			
			pluginList.add(pluginWrapper);
		}
		return pluginList;
	}

	protected static ListValueMap<String,PluginWrapper> createPluginTypeMap(File rootFolder, StringBuilder errorComment) 
	{
		HoopRoot.debug ("PluginManager","createPluginTypeMap ()");
		
		YeriDebug.ASSERT(rootFolder.isDirectory());
		
		ListValueMap<String,PluginWrapper> pluginTypeMap = new ListValueMap<String,PluginWrapper>();

		// config file
		Collection<? extends PluginWrapper> pluginCollection = createPluginOfFolder(rootFolder, errorComment);
		if(pluginCollection!=null)
		{
			for(PluginWrapper pluginWrapper : pluginCollection)
			{
				pluginTypeMap.add(pluginWrapper.getType(), pluginWrapper);
			}
		}
		
		// for recursive call onfolders
		
		File[] fileArray = rootFolder.listFiles();
		for(File file : fileArray)
		{
			if(!file.isDirectory()){ continue; }
			ListValueMap<String,PluginWrapper> childPluginTypeMap = createPluginTypeMap(file, errorComment);
			pluginTypeMap.putAll(childPluginTypeMap);
		}
		return pluginTypeMap;
	}	
}
