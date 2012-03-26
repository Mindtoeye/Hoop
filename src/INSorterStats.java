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

import java.util.ArrayList;
//import java.util.Collections;
import java.util.Comparator;
//import java.util.Hashtable;
//import java.util.Iterator;
import java.util.Map;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INFileManager;
import edu.cmu.cs.in.base.INKV;
import edu.cmu.cs.in.base.INLink;
import edu.cmu.cs.in.base.INPositionsBase;

public class INSorterStats extends INBase
{    		
	public static Boolean getTop=false;
	public static int topEntries=25;
	public static Boolean getSelected=false;			
	public static String [] selectedEntries={"Age","Information","Largescale","Retrieval","Web"};
	public static Boolean getShowRare=false;
	public static int rareEntries=5;
	
	static class MyComparator implements Comparator
	{
		public int compare(Object obj1, Object obj2)
		{
			int result=0;Map.Entry e1 = (Map.Entry)obj1 ;

			Map.Entry e2 = (Map.Entry)obj2 ;//Sort based on values.

			Integer value1 = (Integer)e1.getValue();
			Integer value2 = (Integer)e2.getValue();

			if	(value1.compareTo(value2)==0)
			{
				String word1=(String)e1.getKey();
				String word2=(String)e2.getKey();

				//Sort String in an alphabetical order
				result=word1.compareToIgnoreCase(word2);
			} 
			else
			{
				//Sort values in a descending order
				result=value2.compareTo( value1 );
			}

			return result;
		}

	}
	
	
	/**
	 *
	 */
    public INSorterStats () 
    {
		setClassName ("INSorterStats");
		debug ("INSorterStats ()");						
    }  
    /**
	 *
	 */
    private static void usage ()
    {
    	System.out.println ("Usage: choose either one of:");
    	System.out.println (" 1) Locally analyzing wiki files");
    	System.out.println (" 2) Running the MapReduce task on hadoop");
    	System.out.println (" 3) Post processing/analyzing hadoop output");
    	System.out.println (" ");    	
    	System.out.println ("Usage 1): -classpath INMain.jar INMain <options>");
    	System.out.println (" <options>:");    	
    	System.out.println ("	-stopwords yes/no (Default: yes)");
    	System.out.println ("	-stemming yes/no (Default: yes)");    	
    	System.out.println ("	-cleanoutput yes/no (Default: no");    	
    	System.out.println ("	-datapath <path>");
    	System.out.println ("	-outputpath <path>");    	    	
    	System.out.println ("Example:");    	
    	System.out.println (" java -classpath INMain.jar INMain -cleanoutput yes -datapath /Root/DataSet/Wiki-10 -outputpath Root/Results/");    	
    	System.out.println (" ");    	
    	System.out.println ("Usage 2): -classpath INMain.jar INMain -hadoop <options>");
    	System.out.println (" <options>:");
    	System.out.println ("	-dbglocal yes/no (Default: no)");    	
    	System.out.println ("	-stopwords yes/no (Default: yes)");
    	System.out.println ("	-stemming yes/no (Default: yes)");     	    	
    	System.out.println ("	-datapath <path>");
    	System.out.println ("	-outputpath <path>");    	
    	System.out.println ("Example:");    	
    	System.out.println (" java -classpath INMain.jar INMain -datapath /user/hduser/Wiki-10 -outputpath /user/hduser/output");    	
    	System.out.println (" ");
    	System.out.println ("Usage 3): -classpath INMain.jar INSorterStats <options>");
    	System.out.println (" <options>:");
    	System.out.println ("   -inputfile <path-to-file");
    	System.out.println ("	-gettop yes/no (Default: no) Shows the top 25 frequencies for the selected input");    	
    	System.out.println ("	-getselected yes/no (Default: no) Uses an internal table of selected words to show frequences for (see homework)");
    	System.out.println ("	-getshowrare yes/no (Default: no) For the frequencies 1,2,3,4,5 create output files using the output path and list the terms for each");    	
    	System.out.println (" ");    	
    	System.out.println ("Example:");    	
    	System.out.println (" java -classpath INMain.jar INSorterStats -getshowrare yes -inputfile Root/Results/MapReduced-60k.txt -outputpath Root/Results/");
    }    
    /**
	 *
	 */
    private static Boolean parseArgs (String args [])
    {
    	INBase.debug ("INSorterStats","parseArgs ()");
    	
    	if (args.length<3)
    	{    		
    		return (false);
    	}    	
    	
        for (int i = 0; i < args.length; i++)
        {        	        	
        	if (args [i].compareTo ("-inputfile")==0)
        	{
        		INLink.datapath=args [i+1];
        	}
        	
        	if (args [i].compareTo ("-outputpath")==0)
        	{
        		INLink.outputpath=args [i+1];
        	}        	        	        	
        	
        	
        	if (args [i].compareTo ("-gettop")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			INSorterStats.getTop=true;
        		else
        			INSorterStats.getTop=false;
        	}
        	
        	if (args [i].compareTo ("-getselected")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			INSorterStats.getSelected=true;
        		else
        			INSorterStats.getSelected=false;
        	}
        	
        	if (args [i].compareTo ("-getshowrare")==0)
        	{
        		if (args [i+1].equals("yes")==true)
        			INSorterStats.getShowRare=true;
        		else
        			INSorterStats.getShowRare=false;
        	}        	
        }    	    	
        
        return (true);
    }
    /**
	 *
	 */
    public static void main(String args[]) throws Exception
	{
    	INBase.debug ("INSorterStats","main ()");
    	
    	if (parseArgs (args)==false)
    	{
    		usage ();
    		return;
    	}    	
    	
        //Get the jvm heap size.
        long heapSize = Runtime.getRuntime().totalMemory();
 
        //Print the jvm heap size.
        INBase.debug ("INSorterStats","Heap Size = " + heapSize);    	
    	
    	@SuppressWarnings("unused")
		INLink link = new INLink(); // run the INLink constructor; We need this to have a global settings registry
    	
    	parseArgs (args);
        
        INBase.debug ("INSorterStats","Starting system ...");
        
        INFileManager fManager=new INFileManager ();
        ArrayList<String> lines=fManager.loadLines(INLink.datapath);
        
        INPositionsBase fPositions = new INPositionsBase ();
        fPositions.fromLines (lines);
        
        if (INSorterStats.getTop==true)
        {
        	fPositions.sortEntries();
        	
            ArrayList<INKV> results=fPositions.getTop(25);
            
            for (int i=0;i<results.size();i++)
            {
            	INKV kv=results.get(i);
            	INBase.debug ("INSorterStats","Key: " + kv.getKeyString()+", value: " + kv.getValue()); 
            }        	
        }
                
        if (INSorterStats.getSelected==true)
        {
        	ArrayList<INKV> results=fPositions.getSorted();
        	
        	for (int j=0;j<selectedEntries.length;j++)
        	{        	
        		String target=selectedEntries [j];
        		
        		for (int i=0;i<results.size();i++)
        		{
        			INKV kv=results.get(i);
        			if (kv.getKeyString().equals (target.toLowerCase())==true)
        			{
        				INBase.debug ("INSorterStats","Value for " + target + " is: " + kv.getValue ());
        			}
        		}
        	}	
        }
        
        if (INSorterStats.getShowRare==true)        
        {
        	for (int t=0;t<INSorterStats.rareEntries;t++)
        	{        	
        		int srch=t+1;
        		Integer val=srch;
        		String retr=val.toString();
        		
        		INBase.debug ("INSorterStats","Retrieving entries with frequency: " + retr);
        		
        		fManager.saveContents(INLink.outputpath+"/rare-"+(t+1)+".txt",fPositions.getValuesFormatted (retr));
        	}
        }
	}
}
