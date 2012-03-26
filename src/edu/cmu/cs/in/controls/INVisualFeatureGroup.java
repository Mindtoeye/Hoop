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

import edu.cmu.cs.in.controls.INVisualFeature;

public class INVisualFeatureGroup extends INVisualFeature
{	
	private ArrayList <INVisualFeature> features=null;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
    public INVisualFeatureGroup () 
    {
		setClassName ("INVisualFeatureGroup");
		//debug ("INVisualFeatureGroup ()");
		setFeatures(new ArrayList<INVisualFeature> ());
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public INVisualFeatureGroup (String aText) 
    {
		setClassName ("INVisualFeatureGroup");
		//debug ("INVisualFeatureGroup ()");
		setFeatures(new ArrayList<INVisualFeature> ());
    }  
	/**------------------------------------------------------------------------------------
	 *
	 */    
	public void setFeatures(ArrayList <INVisualFeature> features) 
	{
		this.features = features;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public ArrayList <INVisualFeature> getFeatures() 
	{
		return features;
	}
	//-------------------------------------------------------------------------------------	
}
