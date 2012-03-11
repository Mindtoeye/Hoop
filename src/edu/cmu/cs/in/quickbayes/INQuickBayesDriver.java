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

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.quickbayes.INQuickBayesArffReader;
import edu.cmu.cs.in.quickbayes.INQuickBayesCSVWriter;
import edu.cmu.cs.in.quickbayes.INQuickBayesData;

public class INQuickBayesDriver extends INBase
{
	private INQuickBayesArffReader reader=null;
	private INQuickBayesCSVWriter writer=null;
	private INQuickBayesData data=null;
	
	private String inputFile="undefined";
	private String outputFile="undefined";
	private String operation="undefined";
	private String targetClass="undefined";
	
	//--------------------------------------------------------------------------------- 
	public INQuickBayesDriver () 
	{
		 setClassName ("INQuickBayes");
		 debug ("INQuickBayesDriver ()");  
		 
		reader=new INQuickBayesArffReader ();
		writer=new INQuickBayesCSVWriter ();
		data=new INQuickBayesData ();
	}
	//---------------------------------------------------------------------------------
	public void help ()
	{
		System.err.println ("Usage: INQuickBayesMain -input <arff file> -output <csv file> -operation <operation> -class <targetClass> -smoothing <on|off>");		
	}
	//---------------------------------------------------------------------------------
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
	//---------------------------------------------------------------------------------
	private void loadInput ()
	{
		reader.processInput (inputFile,data);
	}
	//---------------------------------------------------------------------------------
	public void processOperation ()
	{
		
	}
	//---------------------------------------------------------------------------------
	public void saveOutput ()
	{
	 writer.processOutput(outputFile,data);	
	}
	//---------------------------------------------------------------------------------	
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
		
		if (reader.processInput (inputFile,data)==false)
		{
			debug ("Error, unable to read input file");
			return;
		}
		data.prepClassification(targetClass);
		data.classify (operation);
		writer.processOutput(outputFile,data);	
		
		//debug ("Found instane: " + reader.getDataItem("play",13));
	}
	//---------------------------------------------------------------------------------	
}
