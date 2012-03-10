/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 * This source set uses code written for other various graduate 
 * courses and is part of a larger research effort in the field of
 * interactive narrative (IN). Courses that have contribute
 * to this source base are:
 * 
 * 	05-834 Applied Machine Learning
 *  11-719 Computational Models of Discourse Analysis
 *  11-741 Information Retrieval
 * 
 */

package edu.cmu.cs.in.controls;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import edu.cmu.cs.in.base.INFeatureMatrixBase;
  
class INJCheckListItem extends JCheckBox implements ListCellRenderer 
{
	private static final long serialVersionUID = 1L;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
	private void debug (String aMessage)
	{
		INFeatureMatrixBase.debug ("INJCheckListItem",aMessage);	
	}		
	/**------------------------------------------------------------------------------------
	 * 
	 */	
	public INJCheckListItem () 
	{
		setBackground (UIManager.getColor("List.textBackground"));
		setForeground (UIManager.getColor("List.textForeground"));
		
		debug ("INJCheckList ()");
	}
	/**------------------------------------------------------------------------------------
	 * 
	 */
	public Component getListCellRendererComponent  (JList listBox, 
													Object obj, 
													int currentindex, 
													boolean isChecked, 
													boolean hasFocus) 
	{
		if (obj==null)
		{
			debug ("Internal error: object is null");
			return this;
		}	
		
		setEnabled(listBox.isEnabled());
		setSelected(((INVisualFeature)obj).isSelected());
		setFont(listBox.getFont());
		setBackground(listBox.getBackground());
		setForeground(listBox.getForeground());
		setText (obj.toString());
		return this;
	}
	//-------------------------------------------------------------------------------------	
}
