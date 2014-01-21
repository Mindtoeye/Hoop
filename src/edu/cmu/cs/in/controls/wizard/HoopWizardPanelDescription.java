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

package edu.cmu.cs.in.controls.wizard;

import java.util.ArrayList;

import javax.swing.JPanel;

import edu.cmu.cs.in.base.HoopRoot;

/** 
 *
 */
public class HoopWizardPanelDescription extends HoopRoot
{	
	private String panelLabel="Undefined";
	private ArrayList <JPanel> alternatives=new ArrayList<JPanel> ();
	private int panelIndex=0;
	
	/**
	 * 
	 */
	public String getPanelLabel() 
	{
		return panelLabel;
	}
	/**
	 * 
	 */	
	public void setPanelLabel(String panelLabel) 
	{
		this.panelLabel = panelLabel;
	}
	/**
	 * 
	 */	
	public JPanel getPanelContent() 
	{
		debug ("getPanelContent ("+alternatives.size()+"->"+panelIndex+")");
		
		return alternatives.get(panelIndex);
	}
	/**
	 * 
	 */	
	public void setPanelContent(JPanel panelContent) 
	{
		alternatives.add(panelContent);		
	}
	/**
	 * 
	 */
	public ArrayList<JPanel> getAlternatives ()
	{
		return (alternatives);
	}
	/**
	 * 
	 */
	public int getPanelIndex() 
	{
		return panelIndex;
	}
	/**
	 * 
	 * @param panelIndex
	 */
	public void setPanelIndex(int panelIndex) 
	{
		this.panelIndex = panelIndex;
	}
}
