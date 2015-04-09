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

package edu.cmu.cs.in.hoop.hoops.load;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopIntegerSerializable;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;
import edu.cmu.cs.in.network.HoopIRCDialog;

/** 
 * @author vvelsen
 */
public class HoopIRCReader extends HoopLoadBase implements HoopInterface
{
	private static final long serialVersionUID = 3535942236927045758L;
	
	private HoopStringSerializable ircOwner=null;
	private HoopStringSerializable ircURL=null;	
	private HoopStringSerializable ircAuth=null;	
	private HoopStringSerializable ircChannel=null;
	private HoopIntegerSerializable ircPort=null;
	
	private HoopIRCDialog bot=null;
	
	/**
	 *
	 */
	public HoopIRCReader () 
	{
		setClassName ("HoopIRCReader");
		debug ("HoopIRCReader ()");
		
		setHoopDescription ("Connect to IRC services and stream text data in");
				
		ircOwner=new HoopStringSerializable (this,"ircOwner","MindtoEye");
		ircURL=new HoopStringSerializable (this,"twitchURLBase","irc.twitch.tv");
		ircAuth=new HoopStringSerializable (this,"ircAuth","xxxxxx");
		ircChannel=new HoopStringSerializable (this,"ircChannel","giantwaffle");
		ircPort=new HoopIntegerSerializable (this,"ircPort",6667);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopIRCReader ());
	}
	/**
	 * 
	 */
    public void reset ()
    {
    	debug ("reset ()");
    
    	super.reset ();    
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		if (bot==null)
		{
			debug ("Instantiating bot ...");
			bot=new HoopIRCDialog (ircOwner.getValue());
			bot.setCharge(this);
		}
			
		if (bot.getExecuting ()==false)
		{
			debug ("Executing ...");
			
			bot.execute(ircURL.getValue(),ircAuth.getValue(),ircChannel.getValue(),ircPort.getPropValue());
		}
		else
		{
			debug ("Already executing, bump");
		}
	
		return (true);
	}
	/**
	 * 
	 */
	public void stop ()
	{
		debug ("stop ()");
				
		bot.quitServer();
		//bot.disconnect();
		
		bot.setExecuting (false);
		
		debug ("IRC connection closed");
	}	
}
