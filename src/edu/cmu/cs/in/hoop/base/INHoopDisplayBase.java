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

package edu.cmu.cs.in.hoop.base;

import java.util.ArrayList;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.kv.INKV;
import edu.cmu.cs.in.controls.INSentenceWall;
import edu.cmu.cs.in.controls.INVisualSentence;
import edu.cmu.cs.in.hoop.INHoopConsole;

/**
* 
*/
public class INHoopDisplayBase extends INHoopBase implements INHoopInterface
{    	
	private INHoopConsole viewer=null;
	
	private INSentenceWall wall=null;
	
	/**
	 *
	 */
    public INHoopDisplayBase () 
    {
		setClassName ("INHoopDisplayBase");
		debug ("INHoopDisplayBase ()");
		setHoopCategory ("Display");
		
		setHoopDescription ("Shows Hoop Results");			
    }
	/**
	 *
	 */
	public void setViewer(INHoopConsole viewer) 
	{
		this.viewer = viewer;
	}
	/**
	 *
	 */
	public INHoopConsole getViewer() 
	{
		return viewer;
	}
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		if (getInEditor()==false)
		{
			this.setErrorString("Error: this hoop can only be run from within the editor");
			return (false);
		}
		
		wall=(INSentenceWall) INHoopLink.getWindow("Sentence Wall");
		if (wall==null)
		{
			INHoopLink.addView("Sentence Wall",new INSentenceWall (),"right");
			wall=(INSentenceWall) INHoopLink.getWindow("Sentence Wall");			
		}		
			
		ArrayList <INVisualSentence> sData=new ArrayList<INVisualSentence> ();
		
		ArrayList <INKV> inData=inHoop.getData();
		
		if (inData!=null)
		{			
			for (int t=0;t<inData.size();t++)
			{
				INKV aKV=inData.get(t);
								
				INVisualSentence aSentence=new INVisualSentence ();
				aSentence.buildUnigramFeatureList((String) aKV.getValue(),"Unigrams");
				sData.add(aSentence);
			}		
		}	
		
		wall.assignData (sData);
					
		return (true);
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopDisplayBase ());
	}	
}
