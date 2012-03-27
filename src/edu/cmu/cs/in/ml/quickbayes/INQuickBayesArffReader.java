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

import java.io.IOException;
import java.util.ArrayList;

import edu.cmu.cs.in.ml.quickbayes.INQuickBayesFileBase;

public class INQuickBayesArffReader extends INQuickBayesFileBase
{
	INQuickBayesData dataGrid=null;
	
	/**
	*
	*/	
	public INQuickBayesArffReader () 
	{
		super ();
		
		setClassName ("INQuickBayesArffReader");
		debug ("INQuickBayesArffReader ()");		
	}
	//---------------------------------------------------------------------------------
	public void addFeature (String a_feature)
	{
		//debug ("Adding feature: " + a_feature);
		INQuickBayesFeature feature=new INQuickBayesFeature ();
		feature.featureName=a_feature.toLowerCase ();
		dataGrid.features.add (feature);
	}
	//---------------------------------------------------------------------------------
	public void addFeatureRaw (String a_feature)
	{
		String parsed[]=a_feature.split(" ");
		if (parsed.length>1)
			addFeature (parsed [1]);
	}
	//---------------------------------------------------------------------------------
	public void addInstanceString (String a_row,int rowCounter)
	{
		//debug ("Adding instance string: " + a_row);
		String instances[]=a_row.split(",");
		
		ArrayList<String> row=new ArrayList <String> ();
		
		for (int i=0;i<instances.length;i++)
		{
			//debug ("Adding instance: " + instances [i]);
			row.add(instances [i]);
		}
		
		dataGrid.data.add(row);
	}
	//---------------------------------------------------------------------------------
	public boolean processInput (String a_file,INQuickBayesData a_grid)
	{
		dataGrid=a_grid;
		
		StringBuffer raw=null;
		
		try 
		{
			raw=load (a_file);
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return (false);
		}
		
		String split[]=raw.toString ().split("\\n");
		
		boolean markData=false;
		dataGrid.nrRows=0;
		
		for (int i=0;i<split.length;i++)
		{
			if (markData==false)
			{
				if (split [i].indexOf("@attribute")!=-1)
					addFeatureRaw (split [i]);
			
				if (split [i].indexOf("@data")!=-1)
				{
					markData=true;
					dataGrid.nrFeatures=dataGrid.features.size();
				}
			}
			else
			{
				addInstanceString (split [i],dataGrid.nrRows);
				dataGrid.nrRows++;
			}
		}
		
		return (true);
	}
	//---------------------------------------------------------------------------------	
}


