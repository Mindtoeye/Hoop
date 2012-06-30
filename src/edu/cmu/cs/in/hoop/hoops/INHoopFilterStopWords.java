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

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.kv.INKV;
import edu.cmu.cs.in.base.kv.INKVInteger;
import edu.cmu.cs.in.hoop.INHoopStopWordEditor;
import edu.cmu.cs.in.hoop.base.INHoopBase;
import edu.cmu.cs.in.hoop.base.INHoopInterface;
import edu.cmu.cs.in.hoop.base.INHoopTransformBase;

/**
* 
*/
public class INHoopFilterStopWords extends INHoopTransformBase implements INHoopInterface
{    	
	private INHoopStopWordEditor propPanel=null;
	
	/**
	 *
	 */
    public INHoopFilterStopWords () 
    {
		setClassName ("INHoopFilterStopWords");
		debug ("INHoopFilterStopWords ()");
				
		setHoopDescription ("Remove stopwords from input KVs");		
    }
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		ArrayList <INKV> inData=inHoop.getData();
		if (inData!=null)
		{					
			for (int i=0;i<inData.size();i++)
			{
				INKVInteger aKV=(INKVInteger) inData.get(i);
				
				Boolean isStop=false;
				
				for (int j=0;j<INHoopLink.stops.length;j++)
				{				
					if (aKV.getValue().toLowerCase().equals(INHoopLink.stops [j])==true)
					{
						isStop=true;
					}	
				}	
				
				if (isStop==false)
				{
					addKV (new INKVInteger (i,aKV.getValue()));
				}	
			}						
		}
		else
			return (false);		
				
		return (true);
	}	 
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopFilterStopWords ());
	}
	/**
	 * 
	 */
	@Override
	public JPanel getPropertiesPanel() 
	{
		debug ("getPropertiesPanel ()");
		
		if (propPanel==null)
			propPanel=new INHoopStopWordEditor ();
		
		// Doesn't make a difference, probably because there is no vertical glue in the scrollpane
		
		propPanel.setPreferredSize(new Dimension (150,200)); 
		
		return (propPanel);
	}	
}
