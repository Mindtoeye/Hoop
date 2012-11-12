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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;

import edu.cmu.cs.in.controls.base.HoopJDialog;

/**
 * 
 */
public class HoopEnvironmentInspector extends HoopJDialog implements ActionListener 
{	
	private static final long serialVersionUID = -3861766664588933762L;
	
	private JList propList=null;
	
	private DefaultListModel model=null;

	/**
     * 
     */
    public HoopEnvironmentInspector (JFrame frame, boolean modal) 
	{
		super (frame, modal,"Environment Inspector");
		
		setClassName ("HoopEnvironmentInspector");
		debug ("HoopEnvironmentInspector ()");				

		JPanel contentFrame=this.getFrame();

		model = new DefaultListModel();
		
		propList=new JList (model);
		propList.setOpaque(true);
		propList.setBackground(new Color (220,220,220));
		
		JScrollPane scroller=new JScrollPane (propList);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		Properties systemProps = System.getProperties();
		Set<Entry<Object, Object>> sets = systemProps.entrySet();
		Map<String,String> env=System.getenv();
		
		model = new DefaultListModel();
		
		for (Entry<Object,Object> entry : sets) 
		{
			StringBuffer formatter=new StringBuffer ();
			
			formatter.append("(JP) "); 
			
			formatter.append(entry.getKey().toString());
			formatter.append ("=");
			formatter.append(entry.getValue().toString());
			
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
			
			model.addElement(formatter.toString());        	
        }
				
        propList.setModel(model);
        
		contentFrame.add(scroller);
    }      
} 