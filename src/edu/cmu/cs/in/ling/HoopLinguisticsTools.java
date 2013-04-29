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

package edu.cmu.cs.in.ling;

import edu.cmu.cs.in.base.HoopRoot;

/*
 * 
 */
public class HoopLinguisticsTools extends HoopRoot
{
	/**
	 * 
	 */
	public HoopLinguisticsTools ()
	{
		setClassName ("HoopXMLFileViewer");
		debug ("HoopXMLFileViewer ()");  		
	}	
	/**
	 * http://en.wikipedia.org/wiki/Levenshtein_distance
	 * 
	 *	Levenshtein distance (editDistance) is a measure of the similarity between two strings,
	 *	The distance is the number of deletions, insertions, or substitutions required to
	 *	transform p_source into p_target.
	 *
	 *	@param p_source The source string.
	 *
	 *	@param p_target The target string.
	 *
	 *	@returns uint
	 *
	 */
	public int editDistance(String p_source,String p_target) 
	{
	    int m=p_source.length();
	    int n=p_target.length();
	    int[][]d=new int[m+1][n+1];
	    
	    for(int i=0;i<=m;i++)
	    {
	      d[i][0]=i;
	    }
	    
	    for(int j=0;j<=n;j++)
	    {
	      d[0][j]=j;
	    }
	    
	    for(int j=1;j<=n;j++)
	    {
	    	for(int i=1;i<=m;i++)
	    	{
	    		if(p_source.charAt(i-1)==p_target.charAt(j-1))
	    		{
	    			d[i][j]=d[i-1][j-1];
	    		}
	    		else
	    		{
	    			d[i][j]=min((d[i-1][j]+1),(d[i][j-1]+1),(d[i-1][j-1]+1));
	    		}
	    	}
	    }
	    
	    return(d[m][n]);
	}
	/**
	 * 
	 */
	public static int min(int a,int b,int c)
	{
	    return(Math.min(Math.min(a,b),c));
	}
}
