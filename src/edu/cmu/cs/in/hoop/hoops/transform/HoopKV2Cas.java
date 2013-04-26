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

package edu.cmu.cs.in.hoop.hoops.transform;

import static org.uimafit.factory.JCasFactory.createJCas;

import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;

/**
* 
*/
public class HoopKV2Cas extends HoopTransformBase
{    	
	private JCas jCas = null;
	
	/**
	 *
	 */
    public HoopKV2Cas () 
    {
		setClassName ("HoopKV2Cas");
		debug ("HoopKV2Cas ()");
											
		setHoopDescription ("Generate Cas from KV values");		
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		for (int i=0;i<inData.size();i++)
		{
			HoopKV aKV=inData.get(i);
			try {
				jCas = createJCas();
				jCas.setDocumentText(aKV.getValueAsString());
				addCas(jCas);
			} catch (UIMAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
				
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopKV2Cas ());
	}		
}
