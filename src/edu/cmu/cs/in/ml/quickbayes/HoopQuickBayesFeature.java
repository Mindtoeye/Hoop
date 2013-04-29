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

package edu.cmu.cs.in.ml.quickbayes;

import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopQuickBayesFeature extends HoopRoot
{
	public boolean majorityAttribute		=false;
	public String featureName				="undefined";
	public ArrayList <HoopQuickBayesAttribute> instances=null;
	
	/**
	 *  
	 */
	public HoopQuickBayesFeature () 
	{
		setClassName ("HoopQuickBayesFeature");
		debug ("HoopQuickBayesFeature ()");
		
		instances=new ArrayList<HoopQuickBayesAttribute> ();
	}
	/**
	 *  
	 */	
	public HoopQuickBayesAttribute findAttribute (String an_instance)
	{
		for (int i=0;i<instances.size();i++)
		{
		 HoopQuickBayesAttribute tester=instances.get (i);
		 
		 //debug ("Comparing inst: " + tester.instanceName + " with: " + an_instance.toLowerCase());
		 
		 if (tester.instanceName.equals(an_instance.toLowerCase ())==true)
		 {			
			 return (tester);
		 }
		}
		
		//debug ("Inst: " + an_instance + " not found");
		return (null);
	}	
	/**
	 *  
	 */
	public boolean addAttribute (String an_instance)
	{
		HoopQuickBayesAttribute tester=findAttribute (an_instance);

		if (tester!=null)
		{
			//tester.countClassifier++;
			tester.instanceCount++;
			return (false);
		}
		
		tester=new HoopQuickBayesAttribute ();
		tester.instanceName=an_instance.toLowerCase ();
		//tester.countClassifier=1;
		tester.instanceCount=1;
		instances.add(tester);
		
		return (true);
	}		
}
