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

package edu.cmu.cs.in.hoop;

import java.awt.Dimension;

import javax.swing.BoxLayout;

import edu.cmu.cs.in.controls.HoopHTMLPane;
import edu.cmu.cs.in.controls.base.HoopEmbeddedJPanel;


/**
 * 
 */
public class HoopWelcomePane extends HoopEmbeddedJPanel
{  
	private static final long serialVersionUID = -3473134785140861807L;

	private HoopHTMLPane html=null;
	
	/**
	 * 
	 */
	public HoopWelcomePane ()
	{
		setClassName ("HoopWelcomePane");
		debug ("HoopWelcomePane ()");
		
		this.setLayout(new BoxLayout (this,BoxLayout.Y_AXIS));
		
		html=new HoopHTMLPane ();
		html.setMinimumSize(new Dimension (100,24));
		html.setPreferredSize(new Dimension (200,200));
		html.setCommandProcessor(new HoopMainCommandProcessor ());
		
		setContentPane(html);
		
		html.navigateTo("/assets/help/welcome.html");
	}
}
