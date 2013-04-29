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

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.apache.uima.jcas.JCas;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.hoop.HoopPathOrderEditor;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopControlBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* 
*/
public class HoopPathChooser extends HoopControlBase implements HoopInterface
{    	
	private static final long serialVersionUID = 7488640051868262267L;
	
	private HoopPathOrderEditor orderEditor=null;
		
	/**
	 *
	 */
    public HoopPathChooser () 
    {
		setClassName ("HoopPathChooser");
		debug ("HoopPathChooser ()");
										
		setHoopDescription ("Setup and Control Paths");		
    }
	/**
	 * In any other hoop we reset all the data but since this particular
	 * hoop simply passes the data along we have to be careful and make
	 * sure we don't accidentally erase anything coming in.
	 */
    public void resetData ()
    {
    	debug ("resetData ()");
    	
    	debug ("NOP");
    }	    
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		this.setData(inHoop.getData()); // Should work as a pass-through
		this.setjCasList(inHoop.getjCasList()); // Should work as a pass-through
		
		debug ("We now have " + this.getData().size() + " entries");
								
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopPathChooser ());
	}	
	/**
	 * 
	 */
	@Override
	public JPanel getPropertiesPanel() 
	{
		debug ("getPropertiesPanel ()");
		
		if (orderEditor==null)
			orderEditor=new HoopPathOrderEditor ();
		
		orderEditor.setController(this);
				
		// Doesn't make a difference, probably because there is no vertical glue in the scrollpane
		
		orderEditor.setPreferredSize(new Dimension (150,200)); 
		
		return (orderEditor);
	}		
	/**
	 * When a graph is saved it will go through each hoop and call this
	 * method. This will in turn in the panel belonging to that hoop
	 * call a method that sets all the visual properties back into the
	 * hoop.
	 */
	public void propagateVisualProperties ()
	{
		debug ("propagateVisualProperties ()");
		
		super.propagateVisualProperties();
		
		if (orderEditor!=null)
			orderEditor.updatePaths ();
	}	
}
