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

package edu.cmu.cs.in.base.kv;

import java.util.ArrayList;

/**
 * Implements a table based model for use by machine learning algorithms.
 * We build on the data organization of the INKVTable and we add the
 * notion of named features that correspond to each of the columns in the
 * table structure. By default column 0 or the first feature is assumed
 * to be the class, unless otherwise specified.
 */
public class INKVClassificationTable extends INKVTable
{    	
	private int classColumn=0;
	
	private ArrayList <String> features=null;	
	
	/**
	 *
	 */
    public INKVClassificationTable () 
    {
    	features=new ArrayList<String> ();    	
    }
	/**
	 *
	 */
    public INKVClassificationTable (String [] colHeaders) 
    {    	
    	features=new ArrayList<String> ();
    	
    	for (int i=0;i<colHeaders.length;i++)
    	{
    		features.add(colHeaders [i]);
    	}    	
    }
    /**
     * CURRENTLY THIS COULD BE VERY SLOW!!!
     */
    public ArrayList <INKV> getColumn (String aCol)
    {
    	for (int i=0;i<features.size();i++)
    	{
    		String feat=features.get(i);
    		
    		if (feat.toLowerCase().equals(aCol)==true)
    		{
    			return (getColumn (i));
    		}
    	}
    	
    	return (null);
    }
    /**
     * 
     */
	public int getClassColumn() 
	{
		return classColumn;
	}
	/**
	 * 
	 */
	public void setClassColumn(int classColumn) 
	{
		this.classColumn = classColumn;
	}	
    /**
     * 
     */
	public ArrayList <String> getFeatures() 
	{
		return features;
	}	
}
