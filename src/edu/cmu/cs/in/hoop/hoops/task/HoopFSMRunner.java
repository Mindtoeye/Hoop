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

package edu.cmu.cs.in.hoop.hoops.task;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopControlBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;

import java.util.ArrayList;

import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;

/**
* Note: This hoop uses: http://www.alexander-merz.com/graphviz/
*/
public class HoopFSMRunner extends HoopControlBase implements HoopInterface
{    							
	private static final long serialVersionUID = 4601746504647293496L;
	
	/**
	 *
	 */
    public HoopFSMRunner () 
    {
		setClassName ("HoopFSMRunner");
		debug ("HoopFSMRunner ()");
		
		setHoopDescription ("Load and run an FSM defined in a GraphViz file/stream");		
    }
    /**
     * 
     */
    public void reset ()
    {
    	
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		this.setMaxValues(1);
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{									
			debug ("Loading " + inData.size()+" entries ...");
			
			for (int t=0;t<inData.size();t++)
			{
				HoopKVString aKV=(HoopKVString) inData.get(t);		
		
		        try 
		        {
		            Parser p = new Parser();
		            Boolean b = p.parse(aKV.getValue());
		            
		            ArrayList<Graph> al =p.getGraphs();
		            
		            for(int i=0; i<al.size();i++) 
		            {
		            	debug (al.get(i).toString());
		            }                                       
		        } 
		        catch (Exception e) 
		        {
		            e.printStackTrace();
		            return (false);
		        }
		        
				updateProgressStatus (t,inData.size());
			}
		}	
		else
			debug ("Error: no data input hoop");
		
		debug ("Max tokens per line: " + this.getMaxValues());
		
		return (true);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopFSMRunner ());
	}
}
