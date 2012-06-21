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

//import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.hoop.properties.INHoopInspectable;
import edu.cmu.cs.in.stats.INPerformanceMetrics;
import edu.cmu.cs.in.stats.INStatisticsMeasure;

/**
* Here we have the basis for all the hoops. It manages incoming and
* outgoing links to other hoops. Please keep in mind that even
* though the API allows more than one incoming hoop, we currently
* restrict the functionality to only one.
*/
public class INHoopBase extends INHoopInspectable implements INHoopInterface
{    			
	//private static final long serialVersionUID = 1L;
	private ArrayList <INHoopBase> outHoops=null;	
	private ArrayList <INKV> data=null;

	/// Either one of display,load,save,transform 
	protected StringBuffer hoopCategory=null; 
	protected String hoopDescription="Undefined";
	
	/// One of: STOPPED, WAITING, RUNNING, PAUSED, ERROR
	private String executionState="STOPPED"; 
	
	private Boolean inEditor=false;
	
	private String errorString="";
	
	private INPerformanceMetrics performance=null;
	private INStatisticsMeasure stats=null;
	
	private ArrayList <String>inPorts=null;
	private ArrayList <String>outPorts=null;
	
	/**
	 *
	 */
    public INHoopBase () 
    {
		setClassName ("INHoopBase");
		debug ("INHoopBase ()");
		
		hoopCategory=new StringBuffer ();
		hoopCategory.append("root");
		
		outHoops=new ArrayList<INHoopBase> ();
		data=new ArrayList<INKV> ();
		
		inPorts=new ArrayList<String> ();
		outPorts=new ArrayList<String> ();
		
		performance=new INPerformanceMetrics ();
		
		setHoopDescription ("Abstract Hoop");
		
		inPorts.add("KV");
		outPorts.add("KV");
		outPorts.add("Stats");
				
		//generateRandomKVs ();
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
	public void setErrorString(String errorString) 
	{
		this.errorString = errorString;
	}
	/**
	 * 
	 */	
	public String getErrorString() 
	{
		return errorString;
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
    @SuppressWarnings("unused")
	private void generateRandomKVs ()
    {
    	for (int i=0;i<200;i++)
    	{
    		INKV aKV=new INKV ();
    		aKV.setKey(i);
    		aKV.setValue("A Value: " + i);
    		
    		addKV (aKV);
    	}
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
    public void addKV (String aKey,String aValue)
    {
    	INKV aKV=new INKV ();
    	aKV.setKeyString(aKey);
    	aKV.setValue(aValue);
    	
    	data.add(aKV);
    }
    /**
     * 
     */
    public void addKV (int aKey,String aValue)
    {
    	INKV aKV=new INKV ();
    	aKV.setKey(aKey);
    	aKV.setValue(aValue);
    	
    	data.add(aKV);
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
    /*
	public ArrayList <INHoopBase> getInHoops ()
	{
		return (inHoops);
	}
	*/	
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
}
