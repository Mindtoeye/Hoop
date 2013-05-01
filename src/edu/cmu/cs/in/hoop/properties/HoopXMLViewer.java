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

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopXMLViewer extends JTree 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DefaultTreeModel treeModel;
	HoopXMLHandler handler=null;
	
	/**
	 * 
	 */			
	public HoopXMLViewer( )
    {
		setup ();
    }
	/**
	 * 
	 */			
	public HoopXMLViewer( java.util.Hashtable<?,?> value )
    {
        super (value);
		setup ();        
    }
	/**
	 * 
	 */			
	public HoopXMLViewer( Object[] value )
    {
        super (value);
		setup ();        
    }
	/**
	 * 
	 */			
	public HoopXMLViewer( javax.swing.tree.TreeModel newModel )
    {
        super (newModel);
		setup ();        
    }
	/**
	 * 
	 */			
	public HoopXMLViewer( javax.swing.tree.TreeNode root )
    {
        super (root);
		setup ();        
    }
	/**
	 * 
	 */			
	public HoopXMLViewer( javax.swing.tree.TreeNode root, boolean asksAllowsChildren )
    {
        super (root,asksAllowsChildren);
		setup ();        
    }
	/**
	 * 
	 */			
	public HoopXMLViewer( java.util.Vector<?> value )
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
		handler=new HoopXMLHandler ();
		handler.setTree (this);
	}
    /**
	 * 
	 */
    private void debug (String aMessage)
    {
    	HoopRoot.debug ("HoopXMLViewer",aMessage);
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
