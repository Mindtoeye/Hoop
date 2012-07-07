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

import edu.cmu.cs.in.base.INHoopLink;

/** 
 * @author Martin van Velsen
 */
public class INHoopStopWords extends INHoopProjectFile
{
	//private ArrayList <String> wordList=null;
	
	/**
	 * 
	 */
	public INHoopStopWords ()
	{
		setClassName ("INHoopStopWords");
		debug ("INHoopStopWords ()");
		
		//wordList=new ArrayList<String> ();
		
		this.setInstanceName("stopwords.xml");
	}	
	/**
	*
	*/	
	public String toXML() 
	{
		debug ("toXML ()");
	
		StringBuffer formatted=new StringBuffer ();
		formatted.append (super.toXML());
		
		formatted.append("<entries>\n");
		
		for (int i=0;i<INHoopLink.stops.length;i++)
		{
			String stopEntry=INHoopLink.stops [i];
			
			formatted.append("<stopword name=\""+stopEntry+"\" />\n");
		}
		
		formatted.append("</entries>\n");
		
		return (formatted.toString());
	}		
}
