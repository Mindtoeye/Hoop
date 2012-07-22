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

import java.util.ArrayList;

import edu.cmu.cs.in.controls.HoopVisualFeature;

public class HoopVisualFeatureGroup extends HoopVisualFeature
{	
	private ArrayList <HoopVisualFeature> features=null;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
    public HoopVisualFeatureGroup () 
    {
		setClassName ("HoopVisualFeatureGroup");
		//debug ("HoopVisualFeatureGroup ()");
		setFeatures(new ArrayList<HoopVisualFeature> ());
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public HoopVisualFeatureGroup (String aText) 
    {
		setClassName ("HoopVisualFeatureGroup");
		//debug ("HoopVisualFeatureGroup ()");
		setFeatures(new ArrayList<HoopVisualFeature> ());
    }  
	/**------------------------------------------------------------------------------------
	 *
	 */    
	public void setFeatures(ArrayList <HoopVisualFeature> features) 
	{
		this.features = features;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public ArrayList <HoopVisualFeature> getFeatures() 
	{
		return features;
	}
	//-------------------------------------------------------------------------------------	
}
