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

package edu.cmu.cs.in.hoop.project;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;


import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopConnection;

/** 
 * @author Martin van Velsen
 */
public class HoopGraphFile extends HoopProjectFile
{
	private HoopBase graphRoot=null;
	private ArrayList <HoopBase> hoops=null;
	private ArrayList <HoopConnection> connections=null;
	
	/**
	 * 
	 */
	public HoopGraphFile ()
	{
		setClassName ("HoopGraphFile");
		debug ("HoopGraphFile ()");
		
		hoops=new ArrayList<HoopBase> ();
		connections=new ArrayList <HoopConnection> ();
	}		
	/**
	 * 
	 */
	public void reset ()
	{
		debug ("reset ()");
		
		graphRoot=null;
		hoops=new ArrayList<HoopBase> ();	
		connections=new ArrayList <HoopConnection> ();
	}
	/** 
	 * @return
	 */
	public ArrayList <HoopConnection> getHoopConnections() 
	{
		return connections;
	}		
	/**
	 * 
	 */
	public void setHoops(ArrayList <HoopBase> hoops) 
	{
		this.hoops = hoops;
	}
	/** 
	 * @return
	 */
	public ArrayList <HoopBase> getHoops() 
	{
		return hoops;
	}	
	/** 
	 * @param graphRoot
	 */
	public void setGraphRoot(HoopBase graphRoot) 
	{
		this.graphRoot = graphRoot;
	}
	/** 
	 * @return HoopBase
	 */
	public HoopBase getGraphRoot() 
	{
		return graphRoot;
	}	
	/**
	 * 
	 */
	public HoopBase getByID (String anID)
	{
		debug ("getByID ("+anID+")");
				
		for (int i=0;i<hoops.size();i++)
		{
			HoopBase aHoop=hoops.get(i);
			if (aHoop.getHoopID().equals(anID)==true)
			{
				return (aHoop);
			}
		}
		
		return (null);
	}
	/**
	 * 
	 */
	private HoopBase hoopFactory (String aHoopClass)
	{
		debug ("hoopFactory ("+aHoopClass+")");
		
		return (HoopLink.hoopManager.instantiate(aHoopClass));
	}
	/**
	*
	*/	
	public Boolean fromXML(Element root) 
	{
		debug ("fromXML ("+root.getName()+")");
				
		reset ();

		List <?> graphElements = root.getChildren();
		 
		if (graphElements.isEmpty()==true)
		{
			debug ("Error: graph element not found in graph");
			return (false);
		}	
		
		for (int t=0;t<graphElements.size();t++) 
		{
			Element graphElement = (Element) graphElements.get(t);
											
			if (graphElement.getName().equals("graph")==true)
			{		
				debug ("Looking at element: " + graphElement.getName());
				
				List <?> hoopsList = graphElement.getChildren();
				
				if (hoopsList.isEmpty()==false)
				{
					//debug ("Parsing 'hoop' ("+hoopsList.size()+") elements in graph file");
			
					for (int i=0; i<hoopsList.size(); i++)
					{
						Element hoopNode = (Element) hoopsList.get(i);
					
						//>------------------------------------------------------------
						
						if (hoopNode.getName().equals("hoops")==true)
						{											
							List <?> hoopEntries=hoopNode.getChildren();
							
							debug ("Parsing "+hoopEntries.size()+" hoop element(s)");
							
							for (int j=0; j<hoopEntries.size(); j++)
							{
								Element hoopEntry = (Element) hoopEntries.get(j);
							
								if (hoopEntry.getName ().equals("hoop")==true)
								{
									debug ("Found a hoop node ...");
									
									HoopBase aHoop=hoopFactory (hoopEntry.getAttributeValue("class"));
									if (aHoop!=null)
									{
										aHoop.fromXML(hoopEntry);
										hoops.add(aHoop);
									
										if (graphRoot==null)
										{
											graphRoot=aHoop;
										}
									}
									else
										debug ("Error: unable to find hoop " + hoopNode.getAttributeValue("class") + " in list of registered hoops");
								}
								else
									debug ("Error: element name isn't 'hoop', instead it's: " + hoopEntry.getName());								
							}								
						}

						//>------------------------------------------------------------
						
						
						if (hoopNode.getName().equals("connections")==true)
						{						
							debug ("Parsing connections element");
							
							List <?> connectionsList = hoopNode.getChildren();
							 
							if (connectionsList.isEmpty()==false)
							{
								debug ("Parsing 'connections' element in graph file");
							
								for (int w = 0; w < connectionsList.size(); w++) 
								{
									Element connNode = (Element) connectionsList.get(w);
						   
									String fromHoopID=connNode.getAttributeValue("from");
									String toHoopID=connNode.getAttributeValue("to");
									
									debug ("Found connection from: " + fromHoopID + " to: " + toHoopID);
									
									HoopConnection newConnection=new HoopConnection ();
									
									newConnection.setFromHoopID(fromHoopID);
									newConnection.setToHoopID(toHoopID);
									
									connections.add(newConnection);
								}
							}							
						}
						
						//>------------------------------------------------------------
					}
				}
			}				
		}	
		
		processConnections ();
		
		return (true);
	}	
	/**
	 * 
	 */
	private void processConnections ()
	{
		debug ("processConnections ()");
		
		for (int i=0;i<connections.size();i++)
		{
			HoopConnection aConnection=connections.get(i);
			
			HoopBase fromHoop=getByID(aConnection.getFromHoopID());
			HoopBase toHoop=getByID(aConnection.getToHoopID());
			
			if (fromHoop!=null)
			{
				if (toHoop!=null)
				{
					debug ("Making connection: " + fromHoop.getClassName() + " -> " + toHoop.getClassName());
					
					fromHoop.addOutHoop(toHoop);
				}
				else
					debug ("Error: can't find 'to' hoop");
			}
			else
				debug ("Error: can't find 'from' hoop");
		}
	}
	/**
	 * 
	 */
	private void toConnectionXML (Element aRoot,HoopBase aChecker)
	{
		debug ("toConnectionXML ()");
		
		ArrayList<HoopBase> list=aChecker.getOutHoops();
		
		for (int j=0;j<list.size();j++)
		{
			HoopBase target=list.get(j);
			
			aRoot.addContent(connectionToXML (aChecker,target));
			
			toConnectionXML (aRoot,target);
		}		
		
	}
	/**
	*
	*/	
	public Element toXML() 
	{
		debug ("toXML ()");
		
		Element rootElement=super.toXML();
		
		Element hoopElement=new Element ("graph");
		
		rootElement.setContent(hoopElement);
			
		Element hoopsElement=new Element ("hoops");
		hoopElement.addContent(hoopsElement);
							
		for (int i=0;i<hoops.size();i++)
		{
			HoopBase aHoop=hoops.get(i);
			aHoop.propagateVisualProperties();
			hoopsElement.addContent (aHoop.toXML());
		}
		
		Element connElement=new Element ("connections");
		hoopElement.addContent(connElement);
				
		if (graphRoot!=null)
		{			
			ArrayList<HoopBase> list=graphRoot.getOutHoops();
			
			for (int j=0;j<list.size();j++)
			{
				HoopBase target=list.get(j);
				
				connElement.addContent(connectionToXML (graphRoot,target));
				
				toConnectionXML (connElement,target);
			}
		}
				
		return (rootElement);
	}
	/**
	 * 
	 * @return
	 */
	private Element connectionToXML (HoopBase from,HoopBase to)
	{
		Element cElement=new Element ("connection");
		
		cElement.setAttribute("from",from.getHoopID());
		cElement.setAttribute("to",to.getHoopID());	
		
		return (cElement);
	}
}
