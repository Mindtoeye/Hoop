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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/** 
 * @author Martin van Velsen
 */
public class HoopExecuteInEditor extends HoopExecute implements ActionListener
{			
	/**
	 *
	 */
	public HoopExecuteInEditor () 
	{
		setClassName ("HoopExecuteInEditor");
		debug ("HoopExecuteInEditor ()");		
		
		this.setInEditor(true);
	}	
	/**
	 * 
	 */
	protected void showError (String aClass,String anError)
	{
		debug ("showError ()");
		
		debug ("Hoop: " + aClass + ", Error: " + anError);
		
		HoopErrorPanel errorPanel=(HoopErrorPanel) HoopLink.getWindow("Errors");
		
		if (errorPanel==null)
		{
			debug ("No error panel yet, creating ...");
			
			HoopLink.addView ("Errors",new HoopErrorPanel(),"bottom");
			errorPanel=(HoopErrorPanel) HoopLink.getWindow("Errors");
		}	
				
		HoopLink.popWindow("Errors");
		
		errorPanel.addError (aClass,anError);		
	}	
	/**
	 * 
	 */
	protected void updateDependencies ()
	{
		debug ("updateDependencies ()");
		
		HoopProjectPanel projectPane=(HoopProjectPanel) HoopLink.getWindow("Project");
       	if (projectPane!=null)
       	{
       		projectPane.refresh();
       	}		
       	
		HoopDocumentList docList=(HoopDocumentList) HoopLink.getWindow("Document List");
		
		if (docList!=null)
			docList.updateContents();
	}	
	/**
	 * 
	 */
	protected Boolean execute (HoopBase aParent,HoopBase aRoot)
	{
		debug ("execute ()");
				
		HoopGraphEditor editor=(HoopGraphEditor) HoopLink.getWindow("Hoop Editor");
		
		if (editor!=null)
		{
			//editor.setEnabled(false);
			editor.setLocked(true);
		}	
		
		HoopTreeList hoopWindow=(HoopTreeList) HoopLink.getWindow("Hoop List");
		
		if (hoopWindow!=null)
		{
			//hoopWindow.setEnabled(false);
			hoopWindow.setLocked(true);
		}
		
		Boolean result=super.execute(aParent, aRoot);
		
		if (editor!=null)
		{
			//editor.setEnabled(false);
			editor.setLocked(false);
		}	
				
		if (hoopWindow!=null)
		{
			//hoopWindow.setEnabled(false);
			hoopWindow.setLocked(false);
		}
				
		return (result);
	}
	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) 
	{		
		debug ("actionPerformed ("+event.getActionCommand()+")");
		
		String act=event.getActionCommand();
		
		if (act.equalsIgnoreCase("stop")==true)
		{
			JButton button = (JButton)event.getSource();
			stop ();
			button.setEnabled(false);
		}
		
		if (act.equalsIgnoreCase("run")==true)
		{
			//JButton button = (JButton)event.getSource();
			//button.setEnabled(false);
		}
		
		if (act.equalsIgnoreCase("runN")==true)
		{
			//JButton button = (JButton)event.getSource();
			//button.setEnabled(false);
		}
		
		if (act.equalsIgnoreCase("runForever")==true)
		{
			//JButton button = (JButton)event.getSource();
			//button.setEnabled(false);
		}				
	}	
}

