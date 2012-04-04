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

package edu.cmu.cs.in.hoop.properties;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import edu.cmu.cs.in.base.INBase;

/**
 * 
 */
public class INHoopXMLViewer extends JTree 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DefaultTreeModel treeModel;
	INHoopXMLHandler handler=null;
	
	/**
	 * 
	 */			
	public INHoopXMLViewer( )
    {
		setup ();
    }
	/**
	 * 
	 */			
	public INHoopXMLViewer( java.util.Hashtable<?,?> value )
    {
        super (value);
		setup ();        
    }
	/**
	 * 
	 */			
	public INHoopXMLViewer( Object[] value )
    {
        super (value);
		setup ();        
    }
	/**
	 * 
	 */			
	public INHoopXMLViewer( javax.swing.tree.TreeModel newModel )
    {
        super (newModel);
		setup ();        
    }
	/**
	 * 
	 */			
	public INHoopXMLViewer( javax.swing.tree.TreeNode root )
    {
        super (root);
		setup ();        
    }
	/**
	 * 
	 */			
	public INHoopXMLViewer( javax.swing.tree.TreeNode root, boolean asksAllowsChildren )
    {
        super (root,asksAllowsChildren);
		setup ();        
    }
	/**
	 * 
	 */			
	public INHoopXMLViewer( java.util.Vector<?> value )
    {
        super (value);
		setup ();        
    }		
	/**
	 * 
	 */			
	public void setup ()
	{
		treeModel=new DefaultTreeModel (null);		
		this.setModel (treeModel);
		handler=new INHoopXMLHandler ();
		handler.setTree (this);
	}
    /**
	 * 
	 */
    private void debug (String aMessage)
    {
    	INBase.debug ("INHoopXMLViewer",aMessage);
    }	
	/**
	 * 
	 */			
	public void setXML (String aStream)
	{		
		debug ("setXML ()");
		
		handler.xmlSetUp(aStream);
		expandAll ();
	}
	/**
	 * 
	 */	
	public void expandAll() 
	{
		int row = 0;
		
		while (row < this.getRowCount()) 
		{
			this.expandRow(row);
			row++;
		}
	}	
}
