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

import org.jdom.Element;

import edu.cmu.cs.in.base.HoopLink;

/** 
 * @author Martin van Velsen
 */
public class HoopStopWords extends HoopProjectFile
{	
	/**
	 * 
	 */
	public HoopStopWords ()
	{
		setClassName ("HoopStopWords");
		debug ("HoopStopWords ()");
				
		this.setInstanceName("stopwords.xml");
	}	
	/**
	*
	*/	
	public Element toXML() 
	{
		debug ("toXML ()");
		
		Element rootElement=super.toXML();
	
		Element baseElement=new Element ("entries");		
		rootElement.addContent(baseElement);
				
		for (int i=0;i<HoopLink.stops.length;i++)
		{
			String stopEntry=HoopLink.stops [i];
			
			Element stopElement=new Element ("stopword");
			
			stopElement.setAttribute("name",stopEntry);
			stopElement.setAttribute("selected","true");
						
			baseElement.addContent(stopElement);
		}
				
		return (rootElement);
	}		
}
