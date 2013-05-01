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

package edu.cmu.cs.in.controls;

import java.awt.Color;
import java.util.HashMap;
//import java.util.HashSet;

import edu.cmu.cs.in.base.HoopRoot;

/**
 *
 */
public class HoopVisualClass extends HoopRoot
{
	private String name="";
	private int classIndex=0;
	private HashMap <String,HoopVisualFeature>uniqueFeatures=null;
	private Color color=Color.BLACK;
	
	/**
	 *
	 */
    public HoopVisualClass () 
    {
		setClassName ("HoopVisualClass");
		//debug ("HoopVisualClass ()");	
		uniqueFeatures=new HashMap<String,HoopVisualFeature> ();
    }
	/**
	 *
	 */
    public void addFeature (HoopVisualFeature aFeature)
    {
    	//debug ("addFeature ("+aFeature.text+")");
    	
    	uniqueFeatures.put(aFeature.text,aFeature);
    }
	/**
	 *
	 */
    public HashMap<String,HoopVisualFeature> getFeatures ()
    {
    	return (uniqueFeatures);
    }
	/**
	 *
	 */    
	public void setName(String name) 
	{
		this.name=name;
	}
	/**
	 *
	 */
	public String getName() 
	{
		return name;
	}
	/**
	 *
	 */	
	public void setClassIndex(int classIndex) 
	{
		this.classIndex=classIndex;
	}
	/**
	 *
	 */	
	public int getClassIndex() 
	{
		return classIndex;
	}
	/**
	 *
	 */		
	public void setColor(Color color) 
	{
		this.color = color;
	}
	/**
	 *
	 */	
	public Color getColor() 
	{
		return color;
	}
}
