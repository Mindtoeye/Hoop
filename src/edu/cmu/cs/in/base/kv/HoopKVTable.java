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

import java.io.Serializable;
import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopDataType;

/**
* Primarily meant for machine learning analsysis, but using key/value pairs,
* we present the KV table datastructure. There are two ways to think about
* this class. You can think about it as a table of key/value pairs, where
* each cell is an HoopKV object. Or you can think about it as a table where
* the columns are features and the rows are attributes. Currently the table
* isn't specifically designed for machine learning or classification and
* you should therefore use HoopKVClassificationTable if you want to get a table
* with specific ML support.
* 
* Since the HoopKV class can store more than one value if so desired we can
* turn the HoopKVTable class into a three dimensional data structure. When
* using the table class directly for that purpose please be careful to see
* how the class works internally since it would not be too difficult to
* go against the internal api for data access.
* 
* Note: this table class does not provide any support for named headers.
* Instead you should used the HoopKVClassificationTable class.
*/
public class HoopKVTable extends HoopKV implements Serializable
{    			
	private static final long serialVersionUID = 2636076327925667312L;
	private ArrayList <ArrayList <HoopKV>>data=null;
	private int maxRowLength=1; // technically not true but at least not 0
	
	/**
	 *
	 */
    public HoopKVTable () 
    {
    	setDataType (HoopDataType.TABLE);
    	data=new ArrayList<ArrayList <HoopKV>> ();
    }
    /**
     * 
     */
    public int getRowSize ()
    {
    	return (maxRowLength);
    }
    /**
     * 
     */
    public int getRowCount ()
    {
    	return (data.size());
    }
    /**
     * 
     */
    public ArrayList <HoopKV> getRow (int aRow)
    {
		ArrayList <HoopKV> row=(ArrayList <HoopKV>) data.get (aRow);
		return (row);
    }
    /**
     * CURRENTLY THIS COULD BE VERY SLOW!!!
     */
    public ArrayList <HoopKV> getColumn (int aCol)
    {
    	ArrayList <HoopKV> aSlice=new ArrayList<HoopKV> ();
    	
    	for (int i=0;i<data.size();i++)
    	{
    		ArrayList <HoopKV> aRow=data.get(i);
    		
    		aSlice.add(aRow.get(aCol));
    	}
    	
    	return (aSlice);
    }
    /**
     * 
     */
    public HoopKV getCell (int aCol,int aRow)
    {
		ArrayList <HoopKV> row=(ArrayList <HoopKV>) data.get (aRow);
		return (row.get (aCol));
    }
    /**
     * 
     */
    public void addRow (ArrayList <HoopKV> aRow)
    {
    	maxRowLength=aRow.size();
    	data.add(aRow);
    }
}
