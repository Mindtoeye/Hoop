/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
 * 
 */
package edu.cmu.cs.in.quickbayes;

import java.util.ArrayList;
import java.util.regex.Pattern;

import edu.cmu.cs.in.base.INDataCollection;
import edu.cmu.cs.in.controls.INVisualSentinetFeature;

public class INSentinetReader extends INCSVReader
{	
	private INDataCollection dataSet=null;
	
	private StringBuffer dumpCollector=null;
	
	/**------------------------------------------------------------------------------------
	 *
	 */ 
	public INSentinetReader (INDataCollection aSet) 
	{
		super ();
		
		setClassName ("INSentinetReader");
		debug ("INSentinetReader ()"); 		
		
		dataSet=aSet;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public void assignData (INDataCollection aSet)
	{
		dataSet=aSet;
	}
	/**------------------------------------------------------------------------------------
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
	/**------------------------------------------------------------------------------------
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
	/**------------------------------------------------------------------------------------
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
	/**------------------------------------------------------------------------------------
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
	//---------------------------------------------------------------------------------
}
