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

import java.io.*;

import edu.cmu.cs.in.base.INBase;

public class INQuickBayesFileBase extends INBase
{  
private String filePath="undefined";

//--------------------------------------------------------------------------------- 
public INQuickBayesFileBase () 
{
	//super ();
	
	setClassName ("INQuickBayesFileBase");
	debug ("INQuickBayesFileBase ()");	
}
//--------------------------------------------------------------------------------- 
public StringBuffer load (String a_path) throws IOException 
{
	debug ("load ("+a_path+")");

	StringBuffer sb=new StringBuffer ();
	
	File f = new File(a_path);      
    FileReader fr = new FileReader(f);
    BufferedReader br = new BufferedReader(fr);
    
    String eachLine = br.readLine();
    
    while(eachLine != null) 
    {
        sb.append(eachLine);
        sb.append("\n");
        
        eachLine = br.readLine();
    } 	

    return (sb);
} 
//--------------------------------------------------------------------------------- 
public boolean save (StringBuffer buffer) 
{
	debug ("save ()"); 

	if (filePath.equals ("undefined")==true)
	{
		debug ("Error: no output path/file provided");
		return (false);
	} 

	return (saveAs (filePath,buffer));	
} 
//--------------------------------------------------------------------------------- 
public boolean saveAs (String a_path,StringBuffer buffer)
{
	debug ("save_as ("+a_path+")");

	String formatted=buffer.toString ();

	//debug (formatted);

	try
	{
		FileOutputStream profileOut=new FileOutputStream (a_path);
		PrintStream profileStream=new PrintStream (profileOut);
		       
		profileStream.println (formatted);
		profileStream.flush ();      
		profileStream.close ();
	}
	catch (Exception e)
	{   
		System.err.println ("Error writing to file");
		debug ("Error writing to file");
		return (false);
	}    

	filePath=a_path;

	return (true);
}  
//---------------------------------------------------------------------------------  
} 
