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

package edu.cmu.cs.in.base.io;

import java.util.ArrayList;
import java.util.regex.Pattern;

import edu.cmu.cs.in.base.INDataCollection;
import edu.cmu.cs.in.controls.INVisualSentinetFeature;
import edu.cmu.cs.in.ml.quickbayes.INQuickBayesData;

public class INSentinetReader extends INCSVReader
{	
	private INDataCollection dataSet=null;
	
	private StringBuffer dumpCollector=null;
	
	/**
	 *
	 */ 
	public INSentinetReader (INDataCollection aSet) 
	{
		super ();
		
		setClassName ("INSentinetReader");
		debug ("INSentinetReader ()"); 		
		
		dataSet=aSet;
	}
	/**
	 *
	 */
	public void assignData (INDataCollection aSet)
	{
		dataSet=aSet;
	}
	/**
	 *
	 */	
    public boolean isInteger (String input)  
    {  
       try  
       {  
          Integer.parseInt (input);  
          return true;  
       }  
       catch (Exception e)  
       {  
          return false;  
       }  
    }  	
	/**
	 *
	 */
	public void addInstanceStringTab (String entries[],int rowCounter)
	{
		//debug ("addInstanceStringTab ()");
		
		Pattern p=Pattern.compile("^[A-Za-z0-9]+$");
						
		String posNumber="0";
		String negNumber="0";
		String termString="";
				
		for (int i=0;i<entries.length;i++)
		{
			if (i==2)
				posNumber=entries [i];
			
			if (i==3)
				negNumber=entries [i];
			
			if (i==4)
				termString=entries [i];						
		}

		if ((posNumber.equals("0")==true) && (negNumber.equals ("0")==true))
		{
			//debug ("Not adding entry that doesn't have an effect");
		}
		else
		{			
			String atomic []=termString.split("\\s+");
			
			for (int j=0;j<atomic.length;j++)
			{
				String raw []=atomic [j].split("[#]");
				
				String term="";
				
				if (raw==null)
				{
					debug ("Internal error: entry string does not contain #, skipping ...");
					term=atomic [j];
				}
				else
					term=raw [0];
							
				//if ((p.matcher(term).matches()==true) || (term.length()==1))
				//if (p.matcher (term).matches()==false)
				if ((isInteger (term)==true) || (term.length()==1))
				{					
					debug ("Term " + term + " is alphanumeric or has a length of 1, in either case skip over it");
				}
				else
				{
					ArrayList<String> row=new ArrayList <String> ();
					row.add (posNumber);
					row.add (negNumber);					
					row.add (term);
				
					//debug ("Adding: " + posNumber + "," + negNumber + "," + term);
					dumpCollector.append("Adding: " + posNumber + "," + negNumber + "," + term + "\n");
				
					dataGrid.data.add (row);
					dataSet.sents.add (new INVisualSentinetFeature (term,new Float (posNumber),new Float (negNumber)));
				}	
			}			
		}			
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
			if ((i==2) || (i==3) || (i==4))
			{
				debug ("Adding (selected) instance: " + instances [i]);
				row.add(instances [i]);
			}	
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
		
		dumpCollector=new StringBuffer ();
		
		String split[]=a_file.toString ().split("\\n");
				
		int index=0;
		
		for (int i=0;i<split.length;i++)
		{
			String entries[]=split [i].split("\\t");
		 
			if (index==0)
			{
				for (int j=0;j<entries.length;j++)
				{
					if ((j==2) || (j==3) || (j==4))
					{
						addFeature (entries [j]);
					}	
				}
			}
			else
			{
				addInstanceStringTab (entries,index);
			}
					 
			index++;
		}
		
		dumpDebugData (dumpCollector.toString());
		
		return (true);
	}		
}
