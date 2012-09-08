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

package edu.cmu.cs.in.base;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.base.io.HoopFileManager;
import edu.cmu.cs.in.hoop.HoopConsoleInterface;
import edu.cmu.cs.in.hoop.HoopVersion;

/**
*
*/
public class HoopBaseLink extends HoopRoot
{    		    	
	public static int debugLine=0;
	
	public static String dateFormat="yyyy-MM-dd HH:mm:ss.SSS";
	
	public static boolean useHadoop=false;
	public static boolean casefold=true;
	public static boolean stopwords=true;	
	public static boolean stemming=true;
	public static boolean cleanoutput=false;
	public static boolean dbglocal=false;
	public static boolean nodebug=false;
	public static int minstemsize=4;
	
	public static HoopConsoleInterface console=null;
	
	public static boolean postonly=false;
	
	public static String shardtype="alphabetical"; // One of: docid|alphabetical|docsize
	public static long splitsize=10;
	public static int nrshards=1;
	public static int shardcount=0;
	public static String shardcreate="hdfs";
	
	public static String task="none";
	
	public static String datapath=".";
	public static String outputpath=".";
	
	public static String monitorHost="10.0.0.245";
	//public static String monitorHost="augustus.pslc.cs.cmu.edu";
	public static int monitorPort=8080;
			 							 	
	public static HoopEnvironment environment=null;
	public static HoopFileManager fManager=null;	
			
	public static String crossDomainPolicy = "<?xml version=\"1.0\"?>\n" +
	"<!DOCTYPE cross-domain-policy SYSTEM \"http://www.macromedia.com/xml/dtds/cross-domain-policy.dtd\">\n" + 
	"<cross-domain-policy>\n" + 
	"<allow-access-from domain=\"*\" to-ports=\"*\" />\n" +
	"</cross-domain-policy>\0";	
	
	/**
	 *
	 */
    public HoopBaseLink () 
    {
		setClassName ("HoopBaseLink");
		debug ("HoopBaseLink ()");
		
		// This runs the code that sets a public static variable to the date the jar was compiled on
		@SuppressWarnings("unused")
		HoopVersion versionGenerator=new HoopVersion ();
							
		environment=new HoopEnvironment ();
		fManager=new HoopFileManager ();				
    }      
}
