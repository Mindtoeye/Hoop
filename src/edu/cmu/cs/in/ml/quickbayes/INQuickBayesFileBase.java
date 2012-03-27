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
