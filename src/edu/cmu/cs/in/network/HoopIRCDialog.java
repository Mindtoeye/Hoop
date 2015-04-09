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
 
 * Notes:
 * 
 *  Try: http://www.freenode.net/irc_servers.shtml
 */

package edu.cmu.cs.in.network;

import java.io.IOException;

import org.jibble.pircbot.*;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;

/** 
 * @author vvelsen
 */
public class HoopIRCDialog extends PircBot
{
	private Boolean executing=false;
	private HoopInterface charge=null;
	
    public HoopIRCDialog(String anOWner) 
    {
        this.setName(anOWner);
    }		
	/**
	 * 
	 * @param aMessage
	 */
	protected void debug (String aMessage)
	{
		HoopRoot.debug("HoopIRCDialogBase",aMessage);
	}	
	/**
	 * 
	 */	
	public HoopInterface getCharge() 
	{
		return charge;
	}
	/**
	 * 
	 */	
	public void setCharge(HoopInterface charge) 
	{
		this.charge = charge;
	}		
	/**
	 * 
	 */
	public void execute (String ircURL,String auth,String aChannel,Integer aPort)
	{
		debug ("execute ()");
				
        setVerbose(true);

        try 
        {
        	connect(ircURL,aPort,auth);
		} 
        catch (NickAlreadyInUseException e) 
        {
        	debug ("NickAlreadyInUseException: " + e.getMessage());
			setExecuting(false);
			return;
		} 
        catch (IOException e) 
        {
        	debug ("IOException: " + e.getMessage());
			setExecuting(false);
			return;
		} 
        catch (IrcException e) 
        {
        	debug ("IrcException: " + e.getMessage());
			setExecuting(false);
			return;
		}
        
        debug ("Joining channel(s) ...");
        
        String [] channels=aChannel.split(",");
        
        for (int i=0;i<channels.length;i++)
        {
        	joinChannel ("#"+channels [i]);
        }	
        
        //sendMessage("#"+aChannel,"*[MindtoEye bot online]*");
        
        setExecuting(true);
	}
	/**
	 * 
	 */
	/*
	protected void handleLine(String line)
	{
		debug ("handleLine ()");
		
		HoopRoot.consoleOut ("Line: " + line);
	}
	*/
	/**
	 * 
	 */
    public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) 
    {
    	debug ("onNotice ()");
    	
    	HoopRoot.consoleOut ("("+sourceNick+") " + notice);
    }	
	/**
	 * 
	 */
    public void onMessage(String channel,
    					  String sender,
    					  String login, 
    					  String hostname, 
    					  String message) 
    {
    	debug ("onMessage ()");
    	
    	/*
    	if (message.equalsIgnoreCase("!mind")==true)
    	{
   			//sendMessage(channel, sender + ": You got botted");
    		debug ("COMMAND DETECTED!");
    	}
    	else
    	{
    		debug ("Toss: ["+sender+"] " + message);
    	}
    	*/
    	
    	HoopRoot.consoleOut ("["+channel+"] "+sender + ": " + message);
    	
    	if (charge!=null)
    	{
    		charge.blink ();
    	}
    }
	/**
	 * 
	 * @return
	 */
	public Boolean getExecuting() 
	{
		return executing;
	}
	/**
	 * 
	 * @param executing
	 */
	public void setExecuting(Boolean executing) 
	{
		this.executing = executing;
	}
    /**
     * 
     * @param args
     */
	public static void main(String[] args) 
	{
		HoopIRCDialog bot=new HoopIRCDialog ("MindtoEye");
		bot.execute("irc.twitch.tv","xxxxxx","tehmorag",6667);
	}
}
