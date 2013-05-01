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

package edu.cmu.cs.in.controls.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import edu.cmu.cs.in.controls.base.HoopJDialog;

/**
 * 
 */
public class HoopEnvironmentInspector extends HoopJDialog implements ActionListener 
{	
	private static final long serialVersionUID = -3861766664588933762L;
	
	private JList propList=null;
	private JScrollPane scroller=null;
	//private DefaultListModel model=null;

	/**
     * 
     */
    public HoopEnvironmentInspector (JFrame frame, boolean modal) 
	{
		super (HoopJDialog.OK,frame, modal,"Environment Inspector");
		
		setClassName ("HoopEnvironmentInspector");
		debug ("HoopEnvironmentInspector ()");				

		JPanel contentFrame=getFrame ();
		
		//DefaultListModel model = new DefaultListModel();
		DefaultListModel model = fillPropList ();
		//model.addElement("Hello World");
		
		propList=new JList (model);
		propList.setOpaque(true);
		propList.setMinimumSize(new Dimension (200,200));
		//propList.setBackground(new Color (220,220,220));
		
		scroller=new JScrollPane (propList);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				
		contentFrame.add(scroller);		
    }      
    /**
     * 
     */
    public DefaultListModel fillPropList ()
    {
    	debug ("fillPropList ()");
    	
		Properties systemProps = System.getProperties();
		Set<Entry<Object, Object>> sets = systemProps.entrySet();
		Map<String,String> env=System.getenv();    	
    	
		DefaultListModel model = new DefaultListModel();
		
		for (Entry<Object,Object> entry : sets) 
		{
			StringBuffer formatter=new StringBuffer ();
			
			formatter.append("(JP) "); 
			
			formatter.append(entry.getKey().toString());
			formatter.append ("=");
			formatter.append(entry.getValue().toString());
			
			debug (formatter.toString());
			
			model.addElement(formatter.toString());			
		}    	
				
		debug ("Environment properties: ");
		
        for (String envName : env.keySet()) 
        {        	
			StringBuffer formatter=new StringBuffer ();
			
			formatter.append("(EP) "); 
			
			formatter.append(envName);
			formatter.append ("=");
			formatter.append(env.get(envName));
			
			debug (formatter.toString());
			
			model.addElement(formatter.toString());
        }
        
        debug ("VM properties: ");
        
        RuntimeMXBean RuntimemxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = RuntimemxBean.getInputArguments();
        
        for (int t=0;t<arguments.size();t++) 
        {        	
			StringBuffer formatter=new StringBuffer ();
			
			formatter.append("(VM) "); 		
			formatter.append(arguments.get(t));
			
			debug (formatter.toString());
			
			model.addElement(formatter.toString());
        }
				
        //propList.setModel(model);
        
        //debug ("A total of " + propList.getModel().getSize() + " entries");
        
        return (model);
    }
} 