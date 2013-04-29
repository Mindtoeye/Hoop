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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * http://thysmichels.com/2012/02/12/working-with-hdfs-java-example/
 * 
 * Prerequisite for using the code in Eclipse is that you download and 
 * add the following jars to your project libraries:
 * 
 *   hadoop-core-0.20.2.jar
 *   commons-logging-*.jar
 *   
 */
public class HoopVFSLHDFS extends HoopRoot implements HoopVFSLInterface
{
	//change this to string arg in main
	public static final String inputfile = "hdfsinput.txt";
	public static final String inputmsg = "Count the amount of words in this sentence!\n";
	
	/**
	 * 
	 */
	public HoopVFSLHDFS ()
	{
		setClassName ("HoopVFSLHDFS");
		debug ("HoopVFSLHDFS ()");		
	}

	@Override
	public StringBuilder getContents() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURI() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setURI(String uRI) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isStreamOpen() 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public Writer getOutputStream() 
	{
		debug ("getOutputStream ()");
				
		return null;
	}

	@Override
	public boolean openStream(String aFileURI) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void closeStream() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToStream(String aContents) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String loadContents(String aFileURI) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveContents(String aFileURI, String aContents) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean appendContents(String aFileURI, String aContents) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createDirectory(String aDirURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeDirectory(String aDirURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesFileExist(String aFileURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<String> listDirectoryEntries(String aDirURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> listFiles(String aDirURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFileTime(String aPath) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @param args
	 */
	public static void main(String [] args) throws IOException 
	{
		// Create a default hadoop configuration
		Configuration config = new Configuration();
              // Parse created config to the HDFS 
		FileSystem fs = FileSystem.get(config);
		// Specifies a new file in HDFS.
		Path filenamePath = new Path(inputfile);
		
		try
		{
                      // if the file already exists delete it.
			if (fs.exists(filenamePath))
			{
				//remove the file
				fs.delete(filenamePath, true);
			}
		
              //FSOutputStream to write the inputmsg into the HDFS file 
		FSDataOutputStream fin = fs.create(filenamePath);
		fin.writeUTF(inputmsg);
		fin.close();
		
              //FSInputStream to read out of the filenamePath file
		FSDataInputStream fout = fs.open(filenamePath);
		String msgIn = fout.readUTF();
              //Print to screen
		System.out.println(msgIn);
		fout.close();
		}
		catch (IOException ioe)
		{
			System.err.println("IOException during operation " + ioe.toString());
			System.exit(1);
		}
	}

	@Override
	public OutputStream getOutputStreamBinary() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean openStreamBinary(String aFileURI) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public InputStream openInputStream(String aFileURI) {
		// TODO Auto-generated method stub
		return null;
	}	
}
