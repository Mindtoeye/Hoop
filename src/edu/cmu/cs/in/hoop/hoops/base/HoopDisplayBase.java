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

package edu.cmu.cs.in.hoop.hoops.base;

import edu.cmu.cs.in.controls.HoopSentenceWall;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;

/**
* 
*/
public class HoopDisplayBase extends HoopBase implements HoopInterface
{    	
	private static final long serialVersionUID = 1120526232074210733L;

	private HoopEmbeddedJPanel viewer=null;
	
	private HoopSentenceWall wall=null;
	
	/**
	 *
	 */
    public HoopDisplayBase () 
    {
		setClassName ("HoopDisplayBase");
		debug ("HoopDisplayBase ()");
		setHoopCategory ("Display");
		
		setHoopDescription ("Shows Hoop Results");			
    }
	/**
	 *
	 */
	public void setViewer(HoopEmbeddedJPanel viewer) 
	{
		this.viewer = viewer;
	}
	/**
	 *
	 */
	public HoopEmbeddedJPanel getViewer() 
	{
		return viewer;
	}
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		if (getInEditor()==false)
		{
			this.setErrorString("Error: this hoop can only be run from within the editor");
			return (false);
		}
		
		/*
		wall=(HoopSentenceWall) HoopLink.getWindow("Sentence Wall");
		if (wall==null)
		{
			HoopLink.addView("Sentence Wall",new HoopSentenceWall (),"right");
			wall=(HoopSentenceWall) HoopLink.getWindow("Sentence Wall");			
		}		
			
		ArrayList <HoopVisualSentence> sData=new ArrayList<HoopVisualSentence> ();
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{			
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);
								
				HoopVisualSentence aSentence=new HoopVisualSentence ();
				aSentence.buildUnigramFeatureList((String) aKV.getValue(),"Unigrams");
				sData.add(aSentence);
			}		
		}	
		
		wall.assignData (sData);
		*/
					
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopDisplayBase ());
	}	
}
