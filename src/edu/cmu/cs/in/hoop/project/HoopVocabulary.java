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

package edu.cmu.cs.in.hoop.project;

import java.util.ArrayList;

import org.jdom.Element;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.kv.HoopKVString;

/** 
 * @author Martin van Velsen
 */
public class HoopVocabulary extends HoopProjectFile
{
	//private Boolean skipHeader=false;
	private String mode="TAB"; // TAB,COMMA,DASH
	private ArrayList <HoopKVString> wordList=null;
	
	/**
	 * 
	 */
	public HoopVocabulary ()
	{
		setClassName ("HoopVocabulary");
		debug ("HoopVocabulary ()");
		
		wordList=new ArrayList<HoopKVString> ();
		
		this.setInstanceName("vocabulary.xml");
		
		//loadDefaults ();
	}
	/**
	 * 
	 */
	public String getEntryDescription (HoopKVString anEntry)
	{
		return (anEntry.getValue(1));
	}
	/**
	 * 
	 */
	public Boolean getEntrySelected (HoopKVString anEntry)
	{
		return (Boolean.parseBoolean(anEntry.getValue(2)));
	}
	/**
	 * 
	 */
	public String getEntryPOS (HoopKVString anEntry)
	{
		return (anEntry.getValue(3));
	}		
	/**
	 * 
	 */
	public HoopKVString addEntry (String anEntry)
	{
		debug ("addEntry ()");
		
		HoopKVString newEntry=new HoopKVString (anEntry,anEntry.toLowerCase());
		newEntry.setValue("Description",1);
		newEntry.setValue("TRUE",2);
		newEntry.setValue("NN",3); // Defaults to a nominative noun
		
		wordList.add(newEntry);	
		
		return (newEntry);
	}
	/**
	 * 
	 */
	public HoopKVString addEntry (String anEntry,String aDescription)
	{
		debug ("addEntry ()");
		
		HoopKVString newEntry=new HoopKVString (anEntry,anEntry.toLowerCase());
		newEntry.setValue(aDescription,1);
		newEntry.setValue("TRUE",2);
		newEntry.setValue("NN",3); // Defaults to a nominative noun
		
		wordList.add(newEntry);
		
		return (newEntry);
	}	
	/**
	 * 
	 */
	public void loadDefaults ()
	{
		debug ("loadDefaults ()");
		
		for (int i=0;i<HoopLink.stops.length;i++)
		{
			String stopEntry=HoopLink.stops [i];
			
			addEntry (stopEntry);
		}	
	}
	/**
	 * 
	 */
	public Boolean fromTextFile (String aFile)
	{
		debug ("fromTextFile ("+aFile+")");
		
		String data=HoopLink.fManager.loadContents(aFile);
		
		String split[]=data.split("\\n");
		
		debug ("CSV data contains " + split.length + " rows");
		
		for (int i=0;i<split.length;i++)
		{			
			String entries[]=null;
			
			// Lickety split ...
						
			if (mode.equals ("TAB")==true)
				entries=split [i].split("\\t");
			else
			{
				if (mode.equals ("COMMA")==true)
					entries=split [i].split("(?<!\\\\),");
			}

			// See if we can create some sensible data from all of this ...
					
			debug ("Storing " + entries.length + " attributes for entry "+i+" ...");	
				
			HoopKVString anEntry=new HoopKVString ();
			
			anEntry.setValue("Description",1);
			anEntry.setValue("TRUE",2);
			anEntry.setValue("NN",3); // Defaults to a nominative noun
			
			for (int j=0;j<entries.length;j++)
			{
				if (j==0)
				{
					anEntry.setKeyString(entries [j]);
				}
				else
				{
					anEntry.setValue(entries [j],j);
				}
			}				
			
			wordList.add(anEntry);
		}		
		
		return (true);
	}
	/**
	*
	*/	
	public Boolean fromXML(Element root) 
	{
		debug ("fromXML ()");
				
		
		
		return (true);
	}		
	/**
	*
	*/	
	public Element toXML() 
	{
		debug ("toXML ()");
		
		Element rootElement=super.toXML();
	
		Element baseElement=new Element ("entries");		
		rootElement.addContent(baseElement);
				
		for (int i=0;i<wordList.size();i++)
		{
			HoopKVString anEntry=wordList.get(i);
			
			Element stopElement=new Element ("entry");
			
			stopElement.setAttribute("name",anEntry.getKeyString());
			stopElement.setAttribute("selected",this.getEntrySelected(anEntry).toString());
			stopElement.setAttribute("POS",this.getEntryPOS(anEntry));
			stopElement.setText(this.getEntryDescription(anEntry));
			
			baseElement.addContent(stopElement);
		}
				
		return (rootElement);
	}			
}
