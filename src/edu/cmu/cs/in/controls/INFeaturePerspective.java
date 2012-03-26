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

import edu.cmu.cs.in.base.INBase;

public class INFeaturePerspective  extends INBase
{		
	private ArrayList <INVisualFeature> features=null;
	private String perspectiveName="";
	
	/**------------------------------------------------------------------------------------
	 *
	 */
	public INFeaturePerspective () 
	{
		setClassName ("INFeaturePerspective");
		//debug ("INFeaturePerspective ()");	
		
		setFeatures(new ArrayList<INVisualFeature> ());		
	}
	/**------------------------------------------------------------------------------------
	 *
	 */
	public INFeaturePerspective (String aName) 
	{
		setClassName ("INFeaturePerspective");
		//debug ("INFeaturePerspective ()");	
		
		setPerspectiveName (aName);
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
	/**------------------------------------------------------------------------------------
	 *
	 */
	public INVisualFeature getFeature (String aFeature) 
	{
		return (features.get(features.indexOf(aFeature)));
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */		
	public void setPerspectiveName(String perspectiveName) 
	{
		this.perspectiveName = perspectiveName;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public String getPerspectiveName() 
	{
		return perspectiveName;
	}
	//-------------------------------------------------------------------------------------	
}

