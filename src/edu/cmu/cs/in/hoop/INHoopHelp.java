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

import edu.cmu.cs.in.controls.INHoopHTMLPane;
import edu.cmu.cs.in.controls.base.INEmbeddedJPanel;

/*
 * 
 */
public class INHoopHelp extends INEmbeddedJPanel 
{  
	private static final long serialVersionUID = 8470077729832249463L;

	private INHoopHTMLPane html=null;
	
	/**
	 * 
	 */
	public INHoopHelp () 
    {
		setClassName ("INHoopHelp");
		debug ("INHoopHelp ()");  
		
       	setBounds (200,25,400,400);
       	html=new INHoopHTMLPane ();
       	setContentPane(html);
    }
	/**
	 * 
	 */
	public void navigateToTopic (String aTopic)
	{
		debug ("navigateToTopic ("+aTopic+")");
		
		html.navigateTo("/assets/help/" + aTopic + ".html");
	}
}