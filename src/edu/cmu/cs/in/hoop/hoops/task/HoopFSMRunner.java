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

package edu.cmu.cs.in.hoop.hoops.task;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopControlBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;

import java.util.ArrayList;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

/**
* Note: This hoop uses: http://www.alexander-merz.com/graphviz/
*/
public class HoopFSMRunner extends HoopControlBase implements HoopInterface
{    							
	private static final long serialVersionUID = 4601746504647293496L;
	
	/**
	 *
	 */
    public HoopFSMRunner () 
    {
		setClassName ("HoopFSMRunner");
		debug ("HoopFSMRunner ()");
		
		setHoopDescription ("Load and run an FSM defined in a GraphViz file/stream");		
    }
    /**
     * 
     */
    public void reset ()
    {
    	
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		this.setMaxValues(1);
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{									
			debug ("Loading " + inData.size()+" entries ...");
			
			for (int t=0;t<inData.size();t++)
			{
				HoopKVString aKV=(HoopKVString) inData.get(t);		
		
		        try 
		        {
		            Parser p = new Parser();
		            if (p.parse(aKV.getValue())==true)
		            {
			            ArrayList<Graph> al =p.getGraphs();
			            
			            for(int i=0; i<al.size();i++) 
			            {
			            	//debug (al.get(i).toString());
			            	
			            	Graph aGraph=al.get(i);
			            	
			            	Node startNode=aGraph.getStartNode ();
			            	
			            	if (startNode!=null)
			            	{
			            		debug ("Found start node: " + startNode.getId().getId());
			            		
			            		ArrayList<String> options=getOptions (aGraph,startNode);
			            		
			            		showOptions (options);
			            		
			            		processInput (aGraph,"yes",options);
			            	}
			            	else
			            	{
			            		debug ("Error: unable to find start node");
			            	}
			            }
		            }
		            else
		            {
		            	this.setErrorString("Error parsing FSM graph");
		            	return (false);
		            }
		        } 
		        catch (Exception e) 
		        {
		        	this.setErrorString("Error parsing FSM graph: " + e.getMessage());
		            return (false);
		        }
		        
				updateProgressStatus (t,inData.size());
			}
		}	
		else
			debug ("Error: no data input hoop");
		
		debug ("Max tokens per line: " + this.getMaxValues());
		
		return (true);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopFSMRunner ());
	}
	/**
	 * 
	 */
	public ArrayList<String> getOptions (Graph aGraph,Node aNode)
	{
		debug ("getOptions ()");
		
		ArrayList<String> results=new ArrayList ();
		
		ArrayList<Edge> edges=aGraph.getEdges();
		
		for (int i=0;i<edges.size();i++)
		{
			Edge anEdge=edges.get(i);
			
			if (anEdge.getSource().getNode().getId()==aNode.getId())
			{
				debug ("Found source for edge: " + anEdge.toString());
				
				results.add(anEdge.getAttribute("label"));
			}
		}
		
		return (results);		
	}
	/**
	 * 
	 */
	public Node processInput (Graph aGraph,String anInput,ArrayList<String> anEdgeList)
	{
		debug ("processInput ()");
		
		for (int i=0;i<anEdgeList.size();i++)
		{
			//debug ("Option ["+i+"]: " + anEdgeList.get(i));
			
			String anEdge=anEdgeList.get(i);
			
			if (anEdge.equalsIgnoreCase(anInput)==true)
			{
				debug ("Choosing edge: " + anEdge);
			}
		}		
		
		return (null);
	}
	/**
	 * 
	 */
	public void showOptions (ArrayList<String> anOptionList)
	{
		debug ("showOptions ()");
		
		for (int i=0;i<anOptionList.size();i++)
		{
			debug ("Option ["+i+"]: " + anOptionList.get(i));
		}		
	}
}
