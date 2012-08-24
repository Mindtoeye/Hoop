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

package edu.cmu.cs.in.base.kv;

import java.io.Serializable;

import org.jdom.Element;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.HoopHTML2Text;
import edu.cmu.cs.in.base.HoopRoot;

/**
 * Perhaps the most important data structure in the Hoop system is the
 * document class. Since it is derived from the Hoop KV Class we should
 * think about it as a KV object but with specialized methods and fixed
 * known member variables, such as rank and score. You can freely use 
 * this class as a KV Class and even as an entry in a KV List if so desired.
 * 
 * Our document representation class is directly derived from the Hoop KV 
 * abstract class. For now this should suffice but we might have to derive 
 * from the KV Table instead to allow for better data modeling.
 * 
 */
public class HoopKVDocument extends HoopKVClass implements HoopKVInterface, Serializable
{    						
	private static final long serialVersionUID = -239882069750434354L;
	private HoopKVInteger rank=null;
	private HoopKVFloat score=null;
	
	public HoopKVString description=null;
	public HoopKVString author=null;	
	public HoopKVString title=null;
	public HoopKVString abstr=null;
	public HoopKVString createDate=null;
	public HoopKVString modifiedDate=null;
	public HoopKVString keywords=null;
	public HoopKVString url=null;
		
	public HoopKVString additional=null; // Any other attributes that should be stored
		
	private int abstrSize=250; // 250 characters for now, CHANGE THIS TO WHOLE TERMS!
			
	/**
	 *
	 */
    public HoopKVDocument ()
    {
    	setType (HoopDataType.CLASS);
    	    	
    	description=new HoopKVString ("description","");
    	addVariable (description);
    	
    	author=new HoopKVString ("author","");
    	addVariable (author);
    	
       	title=new HoopKVString ("title","");
       	addVariable (title);
       	
    	abstr=new HoopKVString ("abstr","");
    	addVariable (abstr);
    	
    	createDate=new HoopKVString ("createDate","");
    	addVariable (createDate);
    	
    	modifiedDate=new HoopKVString ("modifiedDate","");
    	addVariable (modifiedDate);
    	
    	keywords=new HoopKVString ("keywords","");
    	addVariable (keywords);
    	
    	url=new HoopKVString ("url","");
    	addVariable (url);
    	
    	rank=new HoopKVInteger (1,"1");
    	addVariable (rank);
    	
    	score=new HoopKVFloat ((float) 1.0,"1.0");
    	addVariable (score);    	
    	
    	// Make sure we have an entry for our text
    	values.add(new String ("0"));
    	
    	// Extra stuff ...    	
    	additional=new HoopKVString ("additional","");
    	addVariable (additional);
    }
    /**
     * Notice that this is the only KV class that has a debug method. All
     * the parent classes do not derive from HoopBase since there will be
     * large amounts of them and the base class does have a decent memory
     * footprint.
     */
    private void debug (String aMessage)
    {
    	HoopRoot.debug("HoopKVDocument",aMessage);
    }
	/**
	 *
	 */    
	public long getRank() 
	{
		return rank.getKey();
	}
	/**
	 *
	 */	
	public void setRank(int aRank) 
	{
		this.rank.setKey(aRank);
	}
	/**
	 *
	 */	
	public float getScore() 
	{
		return score.getKey();
	}
	/**
	 *
	 */	
	public void setScore(float aScore) 
	{
		this.score.setKey(aScore);
	}    
	/**
	 *
	 */
	public void setDocID(String aKey) 
	{
		this.setKey (aKey);
	}    
	/**
	 *
	 */
	public String getDocID() 
	{
		return this.getKey();
	}
	/**
	 *
	 */
	public String getValue() 
	{
		return (String) (values.get(0));
	}
	/**
	 *
	 */
	public String getValue(int anIndex) 
	{
		if (anIndex>values.size())
			return ("0");
		
		return (String) (values.get(anIndex));
	}
	/**
	 *
	 */
	public String getValueAsString() 
	{
		return (String) (values.get(0));
	}
	/**
	 *
	 */
	public String getValueAsString(int anIndex) 
	{
		if (anIndex>values.size())
			return ("0");
		
		return (String) (values.get(anIndex));
	}		
	/**
	 * Insert a new value and move all the other existing values down
	 */
	public void bump(String value,String aLabel) 
	{				
		// PROCESS NEW LABEL AND MOVE THE OLD ONE UP!
		
		//String aLabel=getKVTypeValue (0);
		
		values.add (0,value);
	}	
	/**
	 *
	 */
	public void setValue(String value) 
	{
		values.set(0,value);
	}
	/**
	 *
	 */
	public void setValue(String value, int anIndex) 
	{
		if (anIndex>(values.size()-1))
		{
			// fill with bogus data up to the requested element
			
			for (int i=(values.size ()-1);i<anIndex;i++)
			{
				values.add(new String ("0"));
			}
		}
		
		values.set(anIndex,value);
	}
	/**
	 * 
	 */
	public void postProcess ()
	{
		debug ("postProcess ()");

		HoopHTML2Text parser=new HoopHTML2Text ();
		
		parser.parse(this.getValue());
		
		String cleanText=parser.getText();
		
		this.bump(cleanText,"Cleaned");
		
		if (abstr.getValue().isEmpty()==true)
		{						
			if (this.getValue().length()>abstrSize)
			{
				abstr.setValue(cleanText.substring(0,abstrSize));
			}
			else
			{
				abstr.setValue(cleanText);
			}
		}
		
		if (modifiedDate.getValue().isEmpty()==true)
		{
			modifiedDate.setValue(createDate.getValue());
		}
	}
	/**
	 * 
	 */
	public String toText ()
	{
		debug ("toText ()");
		
		StringBuffer formatter=new StringBuffer ();
		
		formatter.append("Title: " + title.getValue());
		
		formatter.append("\n\n");
		
		formatter.append("Created: " + createDate.getValue ()+"\n");
		formatter.append("Modified: " + modifiedDate.getValue ()+"\n");
		
		formatter.append("\n\n");
		formatter.append("Abstract: \n\n");
		formatter.append(abstr.getValue());
		
		formatter.append("\n\n");
		formatter.append("Text: \n\n");
		formatter.append(getValue());
		
		formatter.append("\n\n");
		
		formatter.append("Keywords: " + keywords.getValue() + "\n");
		formatter.append("URL: " + url.getValue() + "\n");
						
		return (formatter.toString());
	}
	/**
	 * 
	 */
	public String toText (int perspIndex)
	{
		debug ("toText ("+perspIndex+")");
		
		StringBuffer formatter=new StringBuffer ();
		
		formatter.append("Title: " + title.getValue());
		
		formatter.append("\n\n");
		
		formatter.append("Created: " + createDate.getValue ()+"\n");
		formatter.append("Modified: " + modifiedDate.getValue ()+"\n");
		
		formatter.append("\n\n");
		formatter.append("Abstract: \n\n");
		formatter.append(abstr.getValue());
		
		formatter.append("\n\n");
		formatter.append("Text: \n\n");
		formatter.append(getValue(perspIndex));
		
		formatter.append("\n\n");
		
		formatter.append("Keywords: " + keywords.getValue() + "\n");
		formatter.append("URL: " + url.getValue() + "\n");
						
		return (formatter.toString());
	}	
	/**
	 * 
	 */
	public void fromXML (Element aRoot)
	{
		
	}
	/**
	 * 
	 */
	public Element toXML ()
	{
		debug ("toXML ()");
		
		Element documentElement=new Element ("document");
		
		Element keyElement=new Element ("key");
		keyElement.setText(this.getKeyString());
		documentElement.addContent(keyElement);
				
		Element titleElement=new Element ("title");
		titleElement.setText(title.getValue());
		documentElement.addContent(titleElement);
		
		Element descElement=new Element ("description");
		descElement.setText(description.getValue());
		documentElement.addContent(descElement);		
		
		Element createElement=new Element ("createDate");
		createElement.setText(createDate.getValue());
		documentElement.addContent(createElement);
		
		Element modifiedElement=new Element ("modifiedDate");
		modifiedElement.setText(modifiedDate.getValue());
		documentElement.addContent(modifiedElement);		
						
		Element keywordsElement=new Element ("keywords");
		keywordsElement.setText(keywords.getValue());
		documentElement.addContent(keywordsElement);
		
		Element urlElement=new Element ("url");
		urlElement.setText(url.getValue());
		documentElement.addContent(urlElement);
		
		Element abstractElement=new Element ("abstract");
		Element contentElement=new Element ("content");
		contentElement.setText(abstr.getValue());
		abstractElement.addContent(contentElement);
		
		for (int j=0;j<abstr.getValuesRaw().size();j++)
		{
			if (j>0)
			{
				Element altAbstractElement=new Element ("original");
				altAbstractElement.setText(abstr.getValue(j));
				abstractElement.addContent(altAbstractElement);
			}
		}		
		
		documentElement.addContent(abstractElement);				
		
		Element textElement=new Element ("text");
		Element contentTElement=new Element ("content");
		contentTElement.setText(this.getValue());
		textElement.addContent(contentTElement);
				
		for (int i=0;i<this.getValuesRaw().size();i++)
		{
			if (i>0)
			{
				Element altTextElement=new Element ("original");
				altTextElement.setText(this.getValue(i));
				textElement.addContent(altTextElement);
			}
		}
		
		documentElement.addContent(textElement);
		
		return (documentElement);
	}		
}
