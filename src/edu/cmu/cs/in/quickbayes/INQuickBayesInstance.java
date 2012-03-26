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

import edu.cmu.cs.in.base.INBase;

public class INQuickBayesInstance extends INBase
{
	public String instanceName="undefined";
	public int countClassifier=0;	
	public int instanceCount  =0;
			
	//--------------------------------------------------------------------------------- 
	public INQuickBayesInstance () 
	{
		setClassName ("INQuickBayesInstance");
		debug ("INQuickBayesInstance ()");		
	}
	//---------------------------------------------------------------------------------
	public String getLikelihoodString ()
	{
		StringBuffer likelihood=new StringBuffer ();
		
		if (instanceCount==0)
			return ("0");
		
		likelihood.append(countClassifier);
		likelihood.append ("/");
		likelihood.append(instanceCount);
		
		return (likelihood.toString());
	}
	//---------------------------------------------------------------------------------
	public float getLikelihood ()
	{		
		if (instanceCount==0)
			return (0);
		
		return ((float) countClassifier / (float) instanceCount);
	}	
	//---------------------------------------------------------------------------------	
}
