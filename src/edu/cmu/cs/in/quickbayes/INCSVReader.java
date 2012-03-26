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

package edu.cmu.cs.in.quickbayes;

import java.util.ArrayList;

import edu.cmu.cs.in.base.INBase;

public class INCSVReader extends INBase 
{
	public INQuickBayesData dataGrid=null;
	public String mode="TAB"; // TAB,COMMA,DASH
	
	/**
	 *
	 */ 
	public INCSVReader () 
	{
		super ();
		
		setClassName ("INCSVReader");
		debug ("INCSVReader ()"); 		
	}
	/**
	 *
	 */
	public void addFeature (String a_feature)
	{
		debug ("Adding feature: " + a_feature);
		INQuickBayesFeature feature=new INQuickBayesFeature ();
		feature.featureName=a_feature.toLowerCase ();
		dataGrid.features.add (feature);
	}
	/**
	 *
	 */
	public void addFeatureRaw (String a_feature)
	{
		String parsed[]=a_feature.split(" ");
		if (parsed.length>1)
			addFeature (parsed [1]);
	}
	/**
	 *
	 */
	public void addInstanceStringTab (String entries[],int rowCounter)
	{
		debug ("addInstanceStringTab ()");
				
		ArrayList<String> row=new ArrayList <String> ();
		
		for (int i=0;i<entries.length;i++)
		{
			debug ("Adding instance: " + entries [i]);
			row.add (entries [i]);
		}
		
		dataGrid.data.add (row);
	}	
	/**
	 *
	 */
	public void addInstanceString (String a_row,int rowCounter)
	{
		debug ("Adding instance string: " + a_row);
		String instances[]=a_row.split(",");
		
		ArrayList<String> row=new ArrayList <String> ();
		
		for (int i=0;i<instances.length;i++)
		{
			debug ("Adding (selected) instance: " + instances [i]);
			row.add(instances [i]);	
		}
		
		dataGrid.data.add(row);
	}	
	/**
	 *
	 */
	public boolean processInputTab (String a_file,INQuickBayesData a_grid)
	{
		debug ("processInput ()");
		
		dataGrid=a_grid;
								
		debug ("Processing data ...");
		
		String split[]=a_file.toString ().split("\\n");
				
		int index=0;
		
		for (int i=0;i<split.length;i++)
		{
			//debug ("Looking at line: " + i + ": "+ split [i]);
					
			String entries[]=split [i].split("\\t");
		 
			if (index==0)
			{
				for (int j=0;j<entries.length;j++)
				{
					addFeature (entries [j]);
				}
			}
			else
			{
				addInstanceStringTab (entries,index);
			}
					 
			index++;
		}
		
		return (true);
	}	
	/**
	 * This method is for now exclusively dedicated to loading a file of a specific format
	 * that has the features Date, Text and Class. Please use processInputTab for any other
	 * input loading and processing.
	 */
	public boolean processInput (String a_file,INQuickBayesData a_grid)
	{
		debug ("processInput ()");
		
		dataGrid=a_grid;
				
		addFeature ("Date");
		addFeature ("Text");
		addFeature ("Class");		
				
		debug ("Processing data ...");
		
		String split[]=a_file.toString ().split("\\n");
				
		int index=0;
		
		for (int i=0;i<split.length;i++)
		{
		 String entries[]=split [i].split("\\t");
		 
		 addInstanceString (entries [1]+",undefined,"+entries [8],index);
		 
		 index++;
		}
		
		return (true);
	}	
	//---------------------------------------------------------------------------------
}
