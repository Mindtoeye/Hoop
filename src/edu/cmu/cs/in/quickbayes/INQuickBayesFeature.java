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

package edu.cmu.cs.in.quickbayes;

import java.util.ArrayList;

import edu.cmu.cs.in.base.INBase;

public class INQuickBayesFeature extends INBase
{
	public boolean majorityAttribute		=false;
	public String featureName				="undefined";
	public ArrayList <INQuickBayesAttribute> instances=null;
	
	//--------------------------------------------------------------------------------- 
	public INQuickBayesFeature () 
	{
		setClassName ("INQuickBayesFeature");
		debug ("INQuickBayesFeature ()");
		
		instances=new ArrayList<INQuickBayesAttribute> ();
	}
	//---------------------------------------------------------------------------------
	public INQuickBayesAttribute findAttribute (String an_instance)
	{
		for (int i=0;i<instances.size();i++)
		{
		 INQuickBayesAttribute tester=instances.get (i);
		 
		 //debug ("Comparing inst: " + tester.instanceName + " with: " + an_instance.toLowerCase());
		 
		 if (tester.instanceName.equals(an_instance.toLowerCase ())==true)
		 {			
			 return (tester);
		 }
		}
		
		//debug ("Inst: " + an_instance + " not found");
		return (null);
	}	
	//---------------------------------------------------------------------------------
	public boolean addAttribute (String an_instance)
	{
		INQuickBayesAttribute tester=findAttribute (an_instance);

		if (tester!=null)
		{
			//tester.countClassifier++;
			tester.instanceCount++;
			return (false);
		}
		
		tester=new INQuickBayesAttribute ();
		tester.instanceName=an_instance.toLowerCase ();
		//tester.countClassifier=1;
		tester.instanceCount=1;
		instances.add(tester);
		
		return (true);
	}
	//---------------------------------------------------------------------------------	
}
