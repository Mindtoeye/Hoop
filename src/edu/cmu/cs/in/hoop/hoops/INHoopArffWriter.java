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

package edu.cmu.cs.in.hoop.hoops;

import java.util.ArrayList;

import edu.cmu.cs.in.base.io.INFileManager;
import edu.cmu.cs.in.base.kv.INKV;
import edu.cmu.cs.in.base.kv.INKVClassificationTable;
//import edu.cmu.cs.in.base.kv.INKVInteger;
import edu.cmu.cs.in.base.kv.INKVTable;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopInterface;
import edu.cmu.cs.in.hoop.base.INHoopSaveBase;
import edu.cmu.cs.in.hoop.properties.types.INHoopStringSerializable;
import edu.cmu.cs.in.hoop.properties.types.INHoopURISerializable;

/**
* 
*/
public class INHoopArffWriter extends INHoopSaveBase implements INHoopInterface
{    			
	private INFileManager fManager=null;
	
	private INHoopURISerializable URI=null;
	private INHoopStringSerializable relation=null;
	
	/**
	 *
	 */
    public INHoopArffWriter () 
    {
		setClassName ("INHoopArffWriter");
		debug ("INHoopArffWriter ()");
				
		setHoopDescription ("Export Model in Weka format");
		
		fManager=new INFileManager ();
		
		URI=new INHoopURISerializable (this,"File","./HoopOut.arff");
		relation=new INHoopStringSerializable (this,"Relation","DefaultRelation");
    }  
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		INKVTable inModel=inHoop.getModel();
		
		if (inModel==null)
		{
			this.setErrorString("Error: no model available for export, please check to see if the linked hoop provides a model output");
			return (false);
		}
		
		if (inModel instanceof INKVClassificationTable)
		{
			debug ("Pfew the output model isn't just a table, it's a classification table");
		}
		else
		{
			this.setErrorString("Error: provided model isn't a classification based model");
			return (false);			
		}
							
		INKVClassificationTable model=(INKVClassificationTable) inModel;
		
		StringBuffer formatted=new StringBuffer ();
		
		formatted.append("@relation ");
		formatted.append(relation.getValue ());
		formatted.append("\n\n");
		
		ArrayList<String> feats=model.getFeatures();
		
		for (int i=0;i<feats.size();i++)
		{
			formatted.append("@attribute ");
			formatted.append(feats.get(i));
			formatted.append(" undef");
			formatted.append("\n");
		}
		
		formatted.append("\n");
		formatted.append("@data\n");
		
		for (int j=0;j<model.getRowCount ();j++)
		{
			ArrayList <INKV> aRow=model.getRow (j);
			
			for (int k=0;k<aRow.size();k++)
			{
				if (k<0)
					formatted.append(",");
				
				INKV aCell=aRow.get(k);
				
				formatted.append (aCell.getValueAsString());		
			}
			
			formatted.append("\n");
		}
				
		fManager.saveContents (URI.getValue(),formatted.toString());
						
		return (true);
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopCSVWriter ());
	}	
}
