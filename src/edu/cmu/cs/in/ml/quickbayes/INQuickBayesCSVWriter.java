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

import edu.cmu.cs.in.ml.quickbayes.INQuickBayesFileBase;

public class INQuickBayesCSVWriter extends INQuickBayesFileBase 
{
	/**
	 * 
	 */
	public INQuickBayesCSVWriter () 
	{
		super ();
		
		setClassName ("INQuickBayesCSVWriter");
		debug ("INQuickBayesCSVWriter ()");		
	}
	/**
	 * 
	 */
	public void processOutput (String a_file,INQuickBayesData data)
	{
	 StringBuffer formatted=new StringBuffer ();
	 
	 for (int i=0;i<data.features.size();i++)
	 {
		 if (i!=0)
			 formatted.append ("\t"); 
		 		 
		 formatted.append(data.features.get(i).featureName);
	 }
	 
	 formatted.append("\n");
	 
	 for (int j=0;j<data.data.size();j++)
	 {
		 if (j!=0)
			 formatted.append("\n");
		 
		 ArrayList<String> temp=data.data.get (j);
		 
		 for (int k=0;k<temp.size();k++)
		 {
			 if (k!=0)
				 formatted.append("\t");
			 
			 formatted.append(temp.get (k));
		 }		 
	 }	 
	 
	 saveAs (a_file,formatted);
	}
}
