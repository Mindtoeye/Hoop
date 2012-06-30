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

import org.w3c.dom.Element;

import edu.cmu.cs.in.hoop.base.INHoopBase;

/** 
 * @author Martin van Velsen
 */
public class INHoopGraphFile extends INHoopProjectFile
{
	private INHoopBase graphRoot=null;
	
	/**
	 * 
	 */
	public INHoopGraphFile ()
	{
		setClassName ("INHoopGraphFile");
		debug ("INHoopGraphFile ()");
		
	}
	/** 
	 * @param graphRoot
	 */
	public void setGraphRoot(INHoopBase graphRoot) 
	{
		this.graphRoot = graphRoot;
	}
	/** 
	 * @return INHoopBase
	 */
	public INHoopBase getGraphRoot() 
	{
		return graphRoot;
	}		
	/**
	*
	*/	
	public Boolean fromXML(Element root) 
	{
		debug ("fromXML ()");
				
		graphRoot=new INHoopBase ();
		graphRoot.fromXML(root);
		
		return (true);
	}	
	/**
	*
	*/	
	public String toXML() 
	{
		debug ("toXML ()");
	
		StringBuffer formatted=new StringBuffer ();
		formatted.append (super.toXML());
		
		formatted.append (graphRoot.toXML());
		
		return (formatted.toString());
	}	
}
