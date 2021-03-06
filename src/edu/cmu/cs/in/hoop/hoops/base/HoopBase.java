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

package edu.cmu.cs.in.hoop.hoops.base;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JPanel;

import org.apache.uima.jcas.JCas;
import org.jdom.Element;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVTable;
import edu.cmu.cs.in.controls.HoopProgressPainter;
//import edu.cmu.cs.in.hoop.HoopStatisticsPanel;
import edu.cmu.cs.in.hoop.editor.HoopProbe;
import edu.cmu.cs.in.hoop.editor.HoopVisualRepresentation;
import edu.cmu.cs.in.hoop.execute.HoopExecuteInEditor;
import edu.cmu.cs.in.hoop.properties.HoopVisualProperties;
import edu.cmu.cs.in.hoop.properties.types.HoopSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;
import edu.cmu.cs.in.stats.HoopEmission;
import edu.cmu.cs.in.stats.HoopPerformanceMeasure;
import edu.cmu.cs.in.stats.HoopSampleMeasure;
import edu.cmu.cs.in.stats.HoopStatisticsMeasure;

/**
* Here we have the basis for all the hoops. It manages incoming and
* outgoing links to other hoops. Please keep in mind that even
* though the API allows more than one incoming hoop, we currently
* restrict the functionality to only one.
*/
public class HoopBase extends HoopThreadBase implements HoopInterface, Serializable
{    			
	private static final long serialVersionUID = 6430720253230678737L;

    public HoopStringSerializable batchSize=null; 
    protected Integer bSize=-1;
	
	private Object graphCellReference=null;
	
	private Boolean active=true;
	
	protected ArrayList <HoopBase> outHoops=null;	
	private ArrayList <HoopKV> data=null;
	private ArrayList <HoopKV> trash=null;
	private HoopKVTable model=null;	
	
	/// Either one of display,load,save,transform,task,visualize 
	protected StringBuffer hoopCategory=null; 
	protected String hoopDescription="Undefined";
	
	/// One of: STOPPED, WAITING, RUNNING, PAUSED, ERROR
	private String executionState="STOPPED"; 
		
	private HoopPerformanceMeasure performance=null;
	private HoopStatisticsMeasure stats=null;
	private HoopVisualRepresentation visualizer=null;
	
	private ArrayList <String>inPorts=null;
	private ArrayList <String>outPorts=null;
		
	private int maxValues=1;
	private int executionCount=0;
	private String helpTopic="hooptemplate";
	private String hoopID="";
	private Boolean done=false;
	private Boolean inEditor=false;
	
	private Boolean breakBefore=false;
	private Boolean breakAfter=false;
	
	//Cas code
	private ArrayList <JCas> jCasList = null;
	
	// Probe support
	
	private ArrayList<HoopProbe> probeList=new ArrayList<HoopProbe> ();
	
		
	/**
	 *
	 */
    public HoopBase () 
    {
		setClassName ("HoopBase");
		debug ("HoopBase ()");
				
    	batchSize=new HoopStringSerializable (this,"batchSize","1");  

		hoopID=UUID.randomUUID().toString();
		
		hoopCategory=new StringBuffer ();
		hoopCategory.append("root");
		
		outHoops=new ArrayList<HoopBase> ();
		data=new ArrayList <HoopKV> ();
		trash=new ArrayList <HoopKV> ();
    	//initialize list of jCas
    	jCasList = new ArrayList<JCas>();
				
		inPorts=new ArrayList<String> ();
		outPorts=new ArrayList<String> ();
				
		performance=new HoopPerformanceMeasure ();
				
		setHoopDescription ("Abstract Hoop");
		setInstanceName(HoopLink.hoopInstanceIndex.toString());
		HoopLink.hoopInstanceIndex++;
		
		addInPort ("KV");
		
		addOutPort ("KV");
		addOutPort ("CAS");
		//addOutPort ("Model");
    }
    /**
     * 
     */
    public void clearProbes ()
    {
    	probeList=new ArrayList<HoopProbe> ();
    }
    /**
     * 
     */
    public void addProbe (HoopProbe aProbe)
    {
    	debug ("addProbe ()");
    	
    	probeList.add(aProbe);
    }
    /**
     * 
     */
    public void removeProbe (HoopProbe aProbe)
    {
    	debug ("removeProbe ()");
    	
    	probeList.remove(aProbe);
    }
    /**
     * 
     */
    public void updateProbes ()
    {
    	for (int i=0;i<probeList.size();i++)
    	{
    		HoopProbe aProbe=probeList.get(i);
    		
    		aProbe.update(this);
    	}
    }
    /**
     * 
     */
	@Override
	public void prep() 
	{
		debug ("prep ()");

		if (batchSize.getPropValue().isEmpty()==true)
		{
			bSize=1;
		}
		else
			bSize=Integer.parseInt(batchSize.getPropValue());		
	}    
	/**
	 * 
	 */
    public void reset ()
    {
    	debug ("reset ()");
    	    	
    	//bSize=-1;
    	
    	resetData ();
    	
    	setDone (false);
    	setExecutionCount(0);
    	setExecutionState ("STOPPED");
    }	
    /**
     * 
     */
    public HoopPerformanceMeasure getPerformanceMetrics ()
    {
    	return (performance);
    }
    /**
     * 
     */
    protected String getProjectPath ()
    {
    	if (HoopLink.project==null)
    		return (".");
    		
    	return (HoopLink.project.getBasePath());
    }
    /**
     * 
     */    
	public int getExecutionCount() 
	{
		return executionCount;
	}
	/**
	 * 
	 */
	public void incExecutionCount ()
	{
		this.executionCount++;
	}
    /**
     * 
     */	
	public Boolean getDone() 
	{
		return done;
	}
    /**
     * 
     */	
	public void setDone(Boolean done) 
	{
		this.done = done;
	}	
    /**
     * 
     */	
	public void setExecutionCount(int executionCount) 
	{
		this.executionCount = executionCount;
	}    
    /**
     * 
     */    
	public void setActive(Boolean active) 
	{
		this.active = active;
	}
    /**
     * 
     */	
	public Boolean getActive() 
	{
		return active;
	}    
    /**
     * 
     */    
	public void setHoopID(String hoopID) 
	{
		this.hoopID = hoopID;
	}
    /**
     * 
     */	
	public String getHoopID() 
	{
		return hoopID;
	}    
    /**
     * 
     */    
	public void setVisualizer(HoopVisualRepresentation visualizer) 
	{
		this.visualizer = visualizer;				
	}
    /**
     * 
     */	
	public HoopVisualRepresentation getVisualizer() 
	{
		return visualizer;
	}
	/**
	 * 
	 */
	protected Boolean inPortExists (String aPort)
	{
		for (int i=0;i<inPorts.size();i++)
		{
			String port=inPorts.get(i);
			
			if (port.toLowerCase().equals(aPort.toLowerCase())==true)
				return (true);
		}
		
		return (false);
	}
	/**
	 * 
	 */
	protected Boolean outPortExists (String aPort)
	{
		for (int i=0;i<outPorts.size();i++)
		{
			String port=outPorts.get(i);
			
			if (port.toLowerCase().equals(aPort.toLowerCase())==true)
				return (true);
		}
		
		return (false);
	}	
    /**
     * 
     */
    protected void removeInPort (String aPort)
    {
    	inPorts.remove(aPort);
    }
    /**
     * 
     */
    protected void addInPort (String aPort)
    {
    	inPorts.add(aPort);	
    }
    /**
     * 
     */
    protected void removeOutPort (String aPort)
    {
    	outPorts.remove(aPort);
    }    
    /**
     * 
     */
    protected void addOutPort (String aPort)
    {
    	outPorts.add(aPort);	
    }
    /**
     * 
     */
    public ArrayList <String> getInPorts ()
    {
    	return (inPorts);
    }
    /**
     * 
     */
    public ArrayList <String> getOutPorts ()
    {
    	return (outPorts);
    }    
	/**
	 * 
	 */    
	public void setStats(HoopStatisticsMeasure stats) 
	{
		this.stats = stats;
	}
	/**
	 * 
	 */	
	public HoopStatisticsMeasure getStats() 
	{
		return stats;
	}        
	/**
	 * 
	 */    
	public void setInEditor(Boolean inEditor) 
	{
		this.inEditor = inEditor;
	}
	/**
	 * 
	 */	
	public Boolean getInEditor() 
	{
		return inEditor;
	}    
	/**
	 * 
	 */    
	public String getExecutionState() 
	{
		return executionState;
	}
	/**
	 * 
	 */
	public void blink ()
	{
		if (visualizer!=null)
		{
			visualizer.blink();
		}
	}
    /**
     * 
     */
    public HoopEmission emit ()
    {
    	return (new HoopEmission (1.0,0.0,1.0));
    }
	/**
	 * 
	 */	
	public void setExecutionState(String executionState) 
	{
		this.executionState = executionState;
		
		if (visualizer!=null)
		{
			visualizer.setState(executionState);
		}
	}  
	/**
    * This method guarantees that garbage collection is
    * done unlike <code>{@link System#gc()}</code>
    */
	public void gc() 
	{
		debug ("gc ()");
		
		Object obj = new Object();
		WeakReference<Object> ref = new WeakReference<Object>(obj);
	    obj = null;
	    while(ref.get() != null) 
	    {
	       System.gc();
	    }
	    
	    // Or:
	    
	    /*
		Runtime r = Runtime.getRuntime();
		r.gc();
		*/
	}
	/**
	 * Perhaps the most vital method of the entire Hoop system is the resetData
	 * function. This call will first call garbage collection on both the old
	 * data and the trash data, after which it will create new instances of
	 * both elements. Although not implemented yet, this method call would be
	 * the point where old data is serialized to disk before reset and garbage
	 * collected. This way any Hoop can go back into the entire history of
	 * the pipeline.
	 */
    public void resetData ()
    {
    	debug ("resetData ()");
    	
    	persistData ();
    	
    	data=null;
    	trash=null;
    	jCasList = null;
    	
    	gc();
    	
    	data=new ArrayList<HoopKV> ();
    	trash=new ArrayList<HoopKV> ();
    
    	//initialize list of jCas
    	jCasList = new ArrayList<JCas>();
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		// Implement in child class!
				
		return (true);
	}
	/**
	 * 
	 */
	public void stop ()
	{
		debug ("stop ()");
		
		// Implement in child class!
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopBase ());
	}    
    /**
     * 
     */
    protected void persistData ()
    {
    	debug ("persistData ()");
    	    	
    }
    /**
     * 
     */
    public void processKV (HoopKV aKV)
    {
    	debug ("processKV ("+(data.size()+1)+" -> "+bSize+")");
    
    	addKV (aKV);
    	
    	blink ();
    	
    	propagateVisualProperties ();
    	
    	if (data.size()>=bSize)
    	{
    		HoopLink.runner.dataReady(this);
    		
    		resetData();
    	}
    }
    /**
     * 
     */
    public void addKV (HoopKV aKV)
    {    	
    	data.add(aKV);
    	
    	//debug ("Data size: " + data.size());
    }  
    
    /**
     * Add Cas
     */
    public void addCas (JCas tempJCas)
    {    	
    	jCasList.add(tempJCas);
    }  
    /**
     * 
     */
    public void toss (HoopKV aKV)
    {
    	trash.add(aKV);
    }
    /**
     * BE VERY CAREFUL WITH THIS ONE! Currently only used by
     * HoopPathChooser
     */
    public void setData (ArrayList<HoopKV> aList)
    {
    	data=aList;
    }
	/**
	 *
	 */    
	public ArrayList <HoopKV> getData() 
	{
		return data;
	}

	/**
	 * Get Cas List
	 * @return
	 */
	public ArrayList<JCas> getjCasList() {
		return jCasList;
	}
	
    /**
     * BE VERY CAREFUL WITH THIS ONE! Currently only used by
     * HoopPathChooser
     */
	public void setjCasList(ArrayList<JCas> jCasList) {
		this.jCasList = jCasList;
	}
	
	/**
	 *
	 */    
	public ArrayList <HoopKV> getTrash() 
	{
		return trash;
	}
	/**
	 * 
	 */
	public HoopKV getKVFromKey (String aKey)
	{
		debug ("getKVFromKey ("+aKey+")");
		
		for (int i=0;i<data.size();i++)
		{
			HoopKV aKV=data.get(i);
			
			if (aKV.getKeyString().toLowerCase().equals(aKey)==true)
			{
				return (aKV);
			}
		}
		
		return (null);
	}
	/**
	 * 
	 */
	public Object getValueFromName (HoopKV aKV,String aLabel)	
	{
		for (int i=0;i<getTypesSize ();i++)
		{
			String aType=getKVTypeName (i);
			
			if (aType.equalsIgnoreCase(aLabel)==true)
			{
				return (aKV.getValue(i));
			}
		}
		
		return (null);
	}
	/**
	 * 
	 */
	public void setValueByName (HoopKV aKV,Object aValue,String aLabel)
	{
		for (int i=0;i<getTypesSize ();i++)
		{
			String aType=getKVTypeName (i);
			
			if (aType.equalsIgnoreCase(aLabel)==true)
			{
				aKV.setValue(aValue, i);
			}
		}		
	}
	/**
	 * 
	 */
	/*
	public HoopKV getKVFromKey (int aKey)
	{
		for (int i=0;i<data.size();i++)
		{
			HoopKV aKV=data.get(i);
			
			if (aKV.getKey ()==aKey)
			{
				return (aKV);
			}
		}
		
		return (null);
	}
	*/	
	/**
	 *
	 */	
	public ArrayList <HoopBase> getOutHoops ()
	{
		return (outHoops);
	}
	/**
	 *
	 */
	public void addOutHoop (HoopBase aHoop)
	{
		debug ("addOutHoop ()");
		
		outHoops.add(aHoop);
	}
	/**
	 *
	 */	
	public void setHoopCategory(String aCategory) 
	{
		this.hoopCategory.append("."+aCategory);
	}
	/**
	 *
	 */	
	public String getHoopCategory() 
	{
		return hoopCategory.toString();
	}	
	/**
	 *
	 */	
	public void setHoopDescription(String hoopDescription) 
	{
		this.hoopDescription = hoopDescription;
	}
	/**
	 *
	 */	
	public String getHoopDescription() 
	{
		return hoopDescription;
	}	
	/**
	 *
	 */
	public Boolean runHoopInternal (HoopBase inHoop)
	{	
		debug ("runHoopInternal ()");
			
		if ((inHoop==null) && (this.getClassName().equalsIgnoreCase("HoopStart")==true))
		{
			debug ("The incoming hoop is null but the hoop is the start state, so we're ok");
			return (true);
		}
		
		HoopExecuteInEditor execution=(HoopExecuteInEditor) HoopLink.runner;
		
    	performance.reset();		
		performance.setMarker ("start");
		
		debug ("Processing data from: " + inHoop.getClassName());
		
		// Assume we're done unless the child hoop says we're not
		this.setDone(true); 
		
		if (inPortExists ("KV")==true)
		{
			ArrayList <HoopKV> inData=inHoop.getData();
				
			if (inData!=null)
			{			
				if ((inData.size()>0) && (execution!=null))
				{					
					if (execution.getExecuteSpeed ()==HoopExecuteInEditor.SPEED_SLOW)
					{					
						HoopVisualRepresentation viz=this.getVisualizer();
					
						HoopProgressPainter progress=viz.getProgressPainter();
					
						progress.setLevels(0,inData.size());
					}	
				}	
			}	
			else
			{
				this.setErrorString("Error: no data found in incoming hoop");
				return (false);
			}					
		}	
		
		if (inPortExists ("CAS")==true)
		{
			ArrayList <JCas> inJCas=inHoop.getjCasList();
			
			if (inJCas!=null)
			{			
				if (inJCas.size()>0)
				{
					debug ("We have JCas data");
				}
			}	
		}
		
		setExecutionState ("RUNNING");
		
		propagateVisualProperties ();
	
		Boolean result=runHoop (inHoop);
		
		if (result==true)
		{	
			incExecutionCount ();
		}	
		
		setExecutionState ("STOPPED");
				
		performance.closeMarker (this.getClassName());
		
		//Long metric=performance.getYValue ();
		
		performance.printMetrics ();
		
		HoopSampleMeasure sm = new HoopSampleMeasure();
		sm.setYValue(performance.getYValue());
		sm.setXValue((long)executionCount);
		sm.setOpen(false);
		HoopLink.timeTakenByHoops.add(sm);
		
		//>-----------------------------------------------------------
		
		/*
		HoopStatisticsPanel statsPanel=null;
		
		if(HoopLink.getWindow("Statistics")!=null)
		{
			statsPanel=(HoopStatisticsPanel) HoopLink.getWindow("Statistics");
		}
		else
		{
			statsPanel=new HoopStatisticsPanel ();
			HoopLink.addView ("Statistics",statsPanel,HoopLink.bottom);
		}
				
    	statsPanel.appendString("\n"+getPerformanceMetrics().getMetrics());
    	*/
    	
    	//>-----------------------------------------------------------
    	
		int count =0;
		
    	for(int i =0;i<data.size();i++)
    	{
    		count = count + data.get(i).getValueSize();
    	}
    	
    	HoopLink.dataSizeForHoop.add(count+"");
    	
    	//propagateVisualProperties ();
		
		//debug ("Hoop executed in: " + metric+"ms");
				
		debug ("runHoopInternal () Done");
		
		return (result);
	}
	/**
	 * 
	 */
	@Override
	public JPanel getPropertiesPanel() 
	{
		// To be implemented by child class
		return null;
	}
	/**
	 * 
	 */	
	public void setMaxValues(int maxValues) 
	{
		this.maxValues = maxValues;
	}
	/**
	 * 
	 */	
	public int getMaxValues() 
	{
		return maxValues;
	}
	/**
	 * 
	 */
	public void fromXML(String aStream) 
	{
		debug ("fromXML ()");
		
	}
	/**
	 * 
	 */
	public void fromXML(Element anElement) 
	{
		debug ("fromXML ()");
		
		if (anElement.getName().equals("hoop")==false)
		{
			debug ("Error: the top level hoop element isn't called 'hoop', instead it's: " + anElement.getName());
			return;
		}
		
		this.setHoopID(anElement.getAttributeValue("id"));
		
		if (anElement.getAttribute("name")!=null)
		{
			this.setInstanceName(anElement.getAttributeValue("name"));
			
			Integer tester=Integer.parseInt(anElement.getAttributeValue ("name"));
		
			if (tester>HoopLink.hoopInstanceIndex)
				HoopLink.hoopInstanceIndex=tester;
		}	
		
		if (anElement.getAttribute("active")!=null)
		{
			this.setActive(Boolean.parseBoolean(anElement.getAttributeValue ("active")));
		}
		
		List <?> hoopList=anElement.getChildren ();
		
		for (int i = 0; i < hoopList.size(); i++) 
		{
			Element node = (Element) hoopList.get(i);
			
			if (node.getName().equals("visual")==true)
			{
				debug ("Assigning visual information to hoop ...");
				
				Integer xmlX=Integer.parseInt(node.getAttributeValue("x"));
				Integer xmlY=Integer.parseInt(node.getAttributeValue("y"));
				Integer xmlWidth=Integer.parseInt(node.getAttributeValue("width"));
				Integer xmlHeight=Integer.parseInt(node.getAttributeValue("height"));
				
				debug ("Hoop visuals: " + xmlX +"," +xmlY + " with: " + xmlWidth + "," + xmlHeight);
				
				HoopVisualProperties vizProps=this.getVisualProperties();
				
				vizProps.setX(xmlX);
				vizProps.setY(xmlY);
				vizProps.setWidth(xmlWidth);
				vizProps.setHeight(xmlHeight);
			}
			
			if (node.getName().equals("properties")==true)
			{
				debug ("Assigning visual information to hoop ...");
				
				List <?> propList=node.getChildren ();
				
				for (int j=0; j<propList.size(); j++) 
				{
					Element propNode = (Element) propList.get(j);
					
					if (propNode.getName().equals ("serializable")==true)
					{
						String targetInstance=propNode.getAttributeValue ("instance");
						if (targetInstance!=null)
						{
							debug ("Configuring serialized property " + targetInstance + " from xml ...");
							
							HoopSerializable prop=props.getProperty(targetInstance);
							if (prop!=null)
							{
								prop.fromXML(propNode);
							}
							else
							{
								debug ("Error: unable to find property instance '" + targetInstance + "' in hoop");
								//listProperties ();
							}	
						}
					}
				}
			}
		}	
	}
	/**
	 * 
	 */
	public Element toXML ()
	{
		debug ("toXML ()");
		
		Element hoopElement=new Element ("hoop");
		
		hoopElement.setAttribute("id",this.getHoopID());
		hoopElement.setAttribute("class",this.getClassName());
		hoopElement.setAttribute("name",this.getInstanceName());
		hoopElement.setAttribute("active",this.getActive().toString());
		
		HoopVisualProperties vizProps=this.getVisualProperties();
		
		Element visualElement=new Element ("visual");			
		visualElement.setAttribute("x",String.format("%d",vizProps.getX()));
		visualElement.setAttribute("y",String.format("%d",vizProps.getY()));		
		visualElement.setAttribute("width",String.format("%d",vizProps.getWidth()));
		visualElement.setAttribute("height",String.format("%d",vizProps.getHeight()));
		hoopElement.addContent(visualElement);		
		
		Element propertiesElement=new Element ("properties");							
		hoopElement.addContent(propertiesElement);		
		
		ArrayList <HoopSerializable> props=getProperties ();
		
		for (int i=0;i<props.size();i++)
		{
			HoopSerializable prop=props.get(i);
			
			if (prop.getEnabled()==true)
			{
				propertiesElement.addContent(prop.toXML());
			}	
		}		
				
		return (hoopElement);
	}			
	/**
	 * 
	 */
	public String getHelpTopic() 
	{
		return helpTopic;
	}
	/**
	 * 
	 */
	public void setHelpTopic(String helpTopic) 
	{
		this.helpTopic = helpTopic;
	}	
	/**
	 * 
	 */
	public void showHelp ()
	{
		debug ("showHelp ()");
		
		HoopLink.showHelpTopic (helpTopic);
	}
	/*
	 * 
	 */
	public HoopKVTable getModel() 
	{
		return model;
	}
	/** 
	 * @return
	 */
	public Object getGraphCellReference() 
	{
		return graphCellReference;
	}
	/** 
	 * @param graphCellReference
	 */
	public void setGraphCellReference(Object graphCellReference) 
	{
		this.graphCellReference = graphCellReference;
	}
	/**
	 * When a graph is saved it will go through each hoop and call this
	 * method. This will in turn in the panel belonging to that hoop
	 * call a method that sets all the visual properties back into the
	 * hoop.
	 */
	public void propagateVisualProperties ()
	{
		debug ("propagateVisualProperties ()");
		
		if (visualizer!=null)
		{	
			visualizer.propagateVisualProperties();
		}	
	}
	/**
	 * 
	 */
	public void listProperties ()
	{
		debug ("listProperties ()");
				
		ArrayList <HoopSerializable> props=getProperties ();
		
		for (int i=0;i<props.size();i++)
		{
			HoopSerializable prop=props.get(i);
				
			debug ("Property ("+prop.getClassName()+") " + prop.getInstanceName()+" -> "+prop.typeToString () + ":"+ prop.getValue());
		}								
	}				
	/**
	 * 
	 */
	protected void updateProgressStatus (int anIndex,int aTotal)
	{
		//debug("updateProgressStatus ("+anIndex+","+aTotal+")");
		
		StringBuffer aStatus=new StringBuffer ();
		
		aStatus.append (" R: ");
		aStatus.append (anIndex);
		aStatus.append (" out of ");
		aStatus.append (aTotal);
		
		getVisualizer ().setExecutionInfo (aStatus.toString ());
				
		HoopProgressPainter progress=getVisualizer ().getProgressPainter();
		
		progress.setLevels(anIndex,aTotal);		
	}
	/**
	 * 
	 * @return
	 */
	public Boolean getBreakBefore() 
	{
		return breakBefore;
	}
	/**
	 * 
	 * @param breakBefore
	 */
	public void setBreakBefore(Boolean breakBefore) 
	{
		this.breakBefore = breakBefore;
	}
	/**
	 * 
	 * @return
	 */
	public Boolean getBreakAfter() 
	{
		return breakAfter;
	}
	/**
	 * 
	 * @param breakAfter
	 */
	public void setBreakAfter(Boolean breakAfter) 
	{
		this.breakAfter = breakAfter;
	}
}
