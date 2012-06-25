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

import java.util.ArrayList;

import edu.cmu.cs.in.hoop.base.INHoopBase;

/** 
 * @author Martin van Velsen
 */
public class INHoopGraphManager extends INHoopBase
{	
	private INHoopBase root=null;
	private ArrayList <INHoopBase> hoops=null;
	
	/**
	 * 
	 */
	public INHoopGraphManager()
	{		
		setClassName ("INHoopGraphManager");
		debug ("INHoopGraphManager ()");
			
		hoops=new ArrayList<INHoopBase> ();
	}
	/**
	 * 
	 */
	public void reset ()
	{
		debug ("reset ()");
		
		root=null;
		hoops=new ArrayList<INHoopBase> ();
	}
	/** 
	 * @param aHoop
	 */
	public void addHoop(INHoopBase aHoop) 
	{
		if (this.root==null)
			setRoot (aHoop);
		
		hoops.add(aHoop);
	}	
	/** 
	 * @param root
	 */
	public void setRoot(INHoopBase root) 
	{
		this.root = root;
	}
	/** 
	 * @return INHoopBase
	 */
	public INHoopBase getRoot() 
	{
		return root;
	}
	/** 
	 * @param aSource
	 * @param aTarget
	 */
	public Boolean connectHoops(INHoopBase aSource,INHoopBase aTarget) 
	{
		debug ("connectHoops ()");
		
		if (aSource==null)
		{
			debug ("Error: hoop source is null");
			return (false);
		}
		
		if (aTarget==null)
		{
			debug ("Error: hoop target is null");
			return (false);
		}		
		
		if (aSource==aTarget)
		{
			setErrorString ("Can't connect a hoop to itself");
			return (false);
		}
		
		aSource.addOutHoop(aTarget);
		
		return (true);
	}	
}
