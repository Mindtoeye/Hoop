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

package edu.cmu.cs.in.hoop.base;

import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JPanel;

import org.w3c.dom.Element;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.kv.INKV;
import edu.cmu.cs.in.hoop.editor.INHoopVisualRepresentation;
import edu.cmu.cs.in.hoop.properties.types.INHoopSerializable;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
import edu.cmu.cs.in.stats.INStatisticsMeasure;

/**
* Here we have the basis for all the hoops. It manages incoming and
* outgoing links to other hoops. Please keep in mind that even
* though the API allows more than one incoming hoop, we currently
* restrict the functionality to only one.
*/
public class INHoopBase extends INHoopBaseTyped implements INHoopInterface
{    			
	private ArrayList <INHoopBase> outHoops=null;	
	private ArrayList <INKV> data=null;

	private String hoopID="";
	
	/// Either one of display,load,save,transform 
	protected StringBuffer hoopCategory=null; 
	protected String hoopDescription="Undefined";
	
	/// One of: STOPPED, WAITING, RUNNING, PAUSED, ERROR
	private String executionState="STOPPED"; 
	
	private Boolean inEditor=false;
		
	private INPerformanceMetrics performance=null;
	private INStatisticsMeasure stats=null;
	
	private ArrayList <String>inPorts=null;
	private ArrayList <String>outPorts=null;
	
	private INHoopVisualRepresentation visualizer=null;
	
	private int maxValues=1;
	
	private String helpTopic="hooptemplate";
		
	/**
	 *
	 */
    public INHoopBase () 
    {
		setClassName ("INHoopBase");
		debug ("INHoopBase ()");

		hoopID=UUID.randomUUID().toString();
		
		hoopCategory=new StringBuffer ();
		hoopCategory.append("root");
		
		outHoops=new ArrayList<INHoopBase> ();
		data=new ArrayList <INKV> ();
				
		inPorts=new ArrayList<String> ();
		outPorts=new ArrayList<String> ();
				
		performance=new INPerformanceMetrics ();
				
		setHoopDescription ("Abstract Hoop");
		
		addInPort ("KV");
		addOutPort ("KV");
		addOutPort ("Stats");				
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
	public void setVisualizer(INHoopVisualRepresentation visualizer) 
	{
		this.visualizer = visualizer;
	}
    /**
     * 
     */	
	public INHoopVisualRepresentation getVisualizer() 
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
	public void setStats(INStatisticsMeasure stats) 
	{
		this.stats = stats;
	}
	/**
	 * 
	 */	
	public INStatisticsMeasure getStats() 
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
	public void setExecutionState(String executionState) 
	{
		this.executionState = executionState;
	}    
	/**
	 * 
	 */
    public void reset ()
    {
    	debug ("reset ()");
    	
    	data=new ArrayList<INKV> ();
    	
    	setExecutionState ("STOPPED");
    }   
    /**
     * 
     */
    public void addKV (INKV aKV)
    {    	
    	data.add(aKV);
    }    
	/**
	 *
	 */    
	public ArrayList <INKV> getData() 
	{
		return data;
	}    
	/**
	 *
	 */	
	public ArrayList <INHoopBase> getOutHoops ()
	{
		return (outHoops);
	}
	/**
	 *
	 */
	public void addOutHoop (INHoopBase aHoop)
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
	public Boolean runHoopInternal (INHoopBase inHoop)
	{	
		performance.setMarker ("start");
		
		if (inPortExists ("KV")==true)
		{
			if (inHoop==null)
			{
				setExecutionState ("ERROR");
				this.setErrorString("Error: incoming hoop object is null!");
				return (false);
			}
			else
			{
				ArrayList <INKV> inData=inHoop.getData();
				
				if (inData!=null)
				{			
					if (inData.size()==0)
					{
						this.setErrorString("Error: data size is 0");
						return (false);
					}
				}	
				else
				{
					this.setErrorString("Error: no data found in incoming hoop");
					return (false);
				}					
			}
		}	
		
		setExecutionState ("RUNNING");
	
		Boolean result=runHoop (inHoop);
		
		setExecutionState ("STOPPED");
		
		Long metric=performance.getOutPoint ();
		
		debug ("Hoop executed in: " + metric+"ms");
				
		return (result);
	}	
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		// Implement in child class!
				
		return (true);
	}
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopBase ());
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
	@Override
	public void fromXML(String aStream) 
	{
		debug ("fromXML ()");
		
	}
	/**
	 * 
	 */
	@Override
	public void fromXMLElement(Element anElement) 
	{
		debug ("fromXMLElement ()");
		
	}
	/**
	 * 
	 */
	public String toXML ()
	{
		debug ("toXML ()");
		
		StringBuffer formatted=new StringBuffer ();
		
		formatted.append("<hoop id="+this.getHoopID()+" class="+this.getClassName()+">\n");
		
		formatted.append("<properties>\n");
		
		ArrayList <INHoopSerializable> props=getProperties ();
		
		for (int i=0;i<props.size();i++)
		{
			INHoopSerializable prop=props.get(i);
			
			formatted.append(prop.toXML());
			formatted.append("\n");
		}
		
		formatted.append("</properties>\n");
		
		formatted.append("</hoop>\n");
		
		return (formatted.toString());
	}
	/*
	public Element toStringElement() 
	{
		debug ("toStringElement ()");
		
		Element newElement=getClassElement ();

		Element selectionElement=new Element ("selection");
		selectionElement.setText(getName());						
		newElement.addContent(selectionElement);
		
		Element actionElement=new Element ("action");
		actionElement.setText(action);						
		newElement.addContent(actionElement);		
		
		Element argElement=new Element ("internalArguments");
		newElement.addContent(argElement);		
		
		for (int i=0;i<arguments.size();i++)
		{
			CTATArgument arg=arguments.get(i);
			
			Element inputElement=new Element ("value");
			inputElement.setAttribute("fmt",arg.getFormat());
			inputElement.setAttribute("type",arg.getType ());
			inputElement.setText(arg.getValue ());		
			argElement.addContent(inputElement);			
		}
				
		return(newElement);
	}
	*/		
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
		
		INHoopLink.showHelpTopic (helpTopic);
	}
}
