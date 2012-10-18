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
	private double graphScale=1;
	
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
	public void setGraphScale (double aScale)
	{
		graphScale=aScale;
	}
	/**
	 * 
	 * @return
	 */
	public double getGraphScale() 
	{
		return graphScale;
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
				
		if (hoops==null)
		{
			debug ("ERROR! No hoops available yet!");
			
			return (null);
		}
		
		for (int i=0;i<hoops.size();i++)
		{
			HoopBase aHoop=hoops.get(i);
			
			if (aHoop.getHoopID()==null)
			{
				debug ("ERROR! Hoop ID is null in Hoop!");
				
				return (null);				
			}
			
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
									
									newConnection.setInstanceName(connNode.getAttributeValue("name"));
									newConnection.setFromHoop(getByID (fromHoopID));
									newConnection.setToHoop(getByID(toHoopID));
									
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
		
		printHoopData (null);
		
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
	 * @return
	 */
	private Element connectionToXML (HoopConnection aConnection)
	{
		Element cElement=new Element ("connection");
		
		cElement.setAttribute("name",aConnection.getInstanceName());
		cElement.setAttribute("from",aConnection.getFromHoopID());
		cElement.setAttribute("to",aConnection.getToHoopID());	
		
		return (cElement);
	}		
	/**
	 * 
	 * @return
	 */
	/*
	private Element connectionToXML (HoopBase from,HoopBase to)
	{
		Element cElement=new Element ("connection");
		
		cElement.setAttribute("name","Edge");
		cElement.setAttribute("from",from.getHoopID());
		cElement.setAttribute("to",to.getHoopID());	
		
		return (cElement);
	}
	*/	
	/**
	 * This method will most likely go away when we explicitly model edges
	 */
	/*
	private void buildConnectionList (Element aRoot,HoopBase aChecker)
	{
		debug ("toConnectionXML ()");
		
		ArrayList<HoopBase> list=aChecker.getOutHoops();
		
		for (int j=0;j<list.size();j++)
		{
			HoopBase target=list.get(j);
			
			aRoot.addContent(connectionToXML (aChecker,target));
			
			buildConnectionList (aRoot,target);
		}				
	}
	*/
	/**
	*
	*/	
	public Element toXML() 
	{
		debug ("toXML ()");
		
		//connections=new ArrayList <HoopConnection> ();
		
		Element rootElement=super.toXML();
		
		Element hoopElement=new Element ("graph");
		
		Double scaleConverter=this.getGraphScale();
		
		hoopElement.setAttribute("scale",scaleConverter.toString());
		
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
			
		for (int t=0;t<connections.size();t++)
		{
			HoopConnection aConn=connections.get(t);
			
			connElement.addContent(connectionToXML (aConn));
		}
		
		/*
		if (graphRoot!=null)
		{			
			ArrayList<HoopBase> list=graphRoot.getOutHoops();
			
			for (int j=0;j<list.size();j++)
			{
				HoopBase target=list.get(j);
				
				connElement.addContent(connectionToXML (graphRoot,target));
				
				buildConnectionList (connElement,target);
			}
		}
		*/
				
		return (rootElement);
	}
	/**
	 * 
	 */
	public void printHoopData (HoopBase aHoop)
	{
		if (aHoop==null)
			debug ("printHoopData ()");
		
		HoopBase target=graphRoot;
		
		if (aHoop!=null)
			target=aHoop;
				
		if (target!=null)
		{			
			ArrayList<HoopBase> list=target.getOutHoops();
			
			for (int j=0;j<list.size();j++)
			{
				HoopBase test=list.get(j);
				
				System.out.println ("Hoop: " + test.getClassName() + " ("+target.getClassName()+"->"+test.getClassName()+")");
				
				printHoopData (test);
			}
		}		
	}
}
