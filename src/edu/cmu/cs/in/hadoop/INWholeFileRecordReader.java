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

package edu.cmu.cs.in.hadoop;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INStringTools;

/*
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
//import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
//import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;
*/

import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

/** 
 * @author vvelsen
 *
 */
public class INWholeFileRecordReader extends INBase implements RecordReader<LongWritable, Text> 
{
	private LongWritable key = null;
	private Text value = null;
	private long fileSize=-1;
	private InputStream inStream=null;	  
	private JobConf job=null;
	private CompressionCodecFactory compressionCodecs = null;
	private int len=0;
	private String internalKey="";

	  public INWholeFileRecordReader (JobConf aJob,InputSplit aSplit)
	  {
		  setClassName ("INWholeFileRecordReader");
		  debug ("INWholeFileRecordReader ()");		
		  
		  job=aJob;
		  
		  FileSplit split = (FileSplit) aSplit;
		    
		  //this.maxLineLength=job.getInt ("mapred.linerecordreader.maxlength",Integer.MAX_VALUE);
	    	    
		  fileSize=split.getLength();
		  
		  final Path file=split.getPath();
	    
		  createKeyFromName (file.getName());
		  
		  debug ("File/Key: " + internalKey + " with size: " + split.getLength());
	    
		  compressionCodecs = new CompressionCodecFactory (job);

		  final CompressionCodec codec = compressionCodecs.getCodec (file);

		  FileSystem fs=null;
		  try 
		  {
			fs = file.getFileSystem (job);
		  } 
		  catch (IOException e) 
		  {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		  
		  FSDataInputStream fileIn=null;
		  
		  try 
		  {
			fileIn = fs.open (split.getPath());
		  } 
		  catch (IOException e) 
		  {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
	    
		  if (codec!=null) 
		  {
			  try 
			  {
				  inStream=codec.createInputStream (fileIn);
			  } 
			  catch (IOException e) 
			  {
				  // TODO Auto-generated catch block
				  e.printStackTrace();
			  }			  
		  } 
		  else 
		  {
	      
			  inStream=fileIn;
		  }			  
	  }	  
	  /**
	   * Primarily this key generated is designed to turn document file names like
	   * the ones of the form wiki-doc10.txt into a long integer 10. The code is
	   * somewhat elaborate but this way it can extract any number from an encoded
	   * filename and not just from the wiki dataset.
	   */	  
	  private String createKeyFromName (String aFileName)
	  {
		  String cleaned=aFileName.toLowerCase();
		  
		  //debug ("createKey ("+cleaned+")");
		  		  		  		  
		  LinkedList<String> numbers=INStringTools.StringToNumbers (cleaned);
		  
		  if (numbers==null)
			  key.set(fileSize);
		  
		  internalKey=numbers.get(0);
		  
		  return (internalKey);
	  }
	  /**
	   * The current progress of the record reader through its data.
	   * 
	   * Returns:
	   * 	a number between 0.0 and 1.0 that is the fraction of the data read 
	   * Throws:
	   * 	IOException 
	   * 	InterruptedException
	   */
	  public float getProgress() 
	  {
		  //debug ("getProgress ()");
		  		  
		  return (1.0f);		    
	  }
	  /**
	   * 
	   */	  
	  public synchronized void close() throws IOException 
	  {
		  debug ("close ()");
		  
		  /*
		  if (in!=null) 
		  {
			  in.close(); 
		  }
		  */
	  }
	  /**
	   * 
	   */		  
	  @Override
	  public boolean next (LongWritable key, 
			  			   Text value) throws IOException 
	  {
		  debug ("next ()");
		  	    	    
		  byte [] buffer=new byte [(int) fileSize];
		  
		  if (inStream==null)
		  {
			  debug ("Internal error: no input stream available!");
			  return (false);
		  }
			  			
		  len=inStream.read (buffer,0,(int) fileSize);
			
		  //debug ("Actually read: " +len);
		  
		  if (len!=-1)
		  {		  
			  value.set (buffer,0,len);		  
		  }
		  else
		  {
			  return (false);
		  }
		  
		  debug ("Read the file");
		  		  		  
		  return true;
	  }
	  /**
	   * 
	   */		  
	  @Override
	  public LongWritable createKey() 
	  {
		  debug ("createKey ()");
		  
		  return (new LongWritable());
	  }
	  /**
	   * 
	   */		  
	  @Override
	  public Text createValue() 
	  {
		  debug ("createValue ()");
		  
		  //return value;
		  
		  return (new Text ());
	  }
	  /**
	   * 
	   */		  
	  @Override
	  public long getPos() throws IOException 
	  {		  
		  return (len);
	  }
	  /**
	   * 
	   */		  
	  public void setValue(Text value) 
	  {
		  this.value = value;
	  }
	  /**
	   * 
	   */	
	  public Text getValue() 
	  {
		  return value;
	  }
}
