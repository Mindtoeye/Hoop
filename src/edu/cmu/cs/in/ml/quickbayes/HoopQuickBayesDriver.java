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

package edu.cmu.cs.in.ml.quickbayes;

import edu.cmu.cs.in.base.HoopRoot;
//import edu.cmu.cs.in.ml.quickbayes.HoopQuickBayesArffReader;
import edu.cmu.cs.in.ml.quickbayes.HoopQuickBayesData;

public class HoopQuickBayesDriver extends HoopRoot
{
	//private HoopQuickBayesArffReader reader=null;
	//private HoopQuickBayesCSVWriter writer=null;
	private HoopQuickBayesData data=null;
	
	private String inputFile="undefined";
	private String outputFile="undefined";
	private String operation="undefined";
	private String targetClass="undefined";
	
	public HoopQuickBayesDriver () 
	{
		 setClassName ("HoopQuickBayes");
		 debug ("HoopQuickBayesDriver ()");  
		 
		//reader=new HoopQuickBayesArffReader ();
		//writer=new HoopQuickBayesCSVWriter ();
		data=new HoopQuickBayesData ();
	}
	
	public void help ()
	{
		System.err.println ("Usage: HoopQuickBayesMain -input <arff file> -output <csv file> -operation <operation> -class <targetClass> -smoothing <on|off>");		
	}

	private void parseArgs (String [] args)
	{
		for (int i=0;i<args.length;i++)
		{
			if (args [i].equals("-input")==true)
			{
				inputFile=args [i+1];
			}
			
			if (args [i].equals("-output")==true)
			{
				outputFile=args [i+1];
			}
			
			if (args [i].equals("-operation")==true)
			{
				operation=args [i+1];
			}
			
			if (args [i].equals("-class")==true)
			{
				targetClass=args [i+1];
			}			
			
			if (args [i].equals("-smoothing")==true)
			{
				if (args [i+1].equals("on")==true)
					data.smoothing=true;
				else
					data.smoothing=false;
			}			
		}
	}	
	@SuppressWarnings("unused")

	private void loadInput ()
	{
		//reader.processInput (inputFile,data);
	}

	public void processOperation ()
	{
		
	}

	public void saveOutput ()
	{
		//writer.processOutput(outputFile,data);	
	}
	
	public void run (String [] args)
	{
		parseArgs (args);
		
		if (inputFile.equals ("undefined")==true)
		{
			help ();	
		}
		
		if (outputFile.equals ("undefined")==true)
		{
			help ();	
		}
		
		if (operation.equals ("undefined")==true)
		{
			help ();	
		}			
		
		/*
		if (reader.processInput (inputFile,data)==false)
		{
			debug ("Error, unable to read input file");
			return;
		}
		*/
		
		data.prepClassification(targetClass);
		data.classify (operation);
		//writer.processOutput(outputFile,data);	
		
		//debug ("Found instacne: " + reader.getDataItem("play",13));
	}	
}