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
	
	private int abstrSize=250; // 250 characters for now, CHANGE THIS TO WHOLE TERMS!
			
	/**
	 *
	 */
    public HoopKVDocument ()
    {
    	setType (HoopDataType.CLASS);
    	    	
    	description=new HoopKVString ("description","undefined");
    	addVariable (description);
    	
    	author=new HoopKVString ("author","undefined");
    	addVariable (author);
    	
       	title=new HoopKVString ("title","undefined");
       	addVariable (title);
       	
    	abstr=new HoopKVString ("abstr","undefined");
    	addVariable (abstr);
    	
    	createDate=new HoopKVString ("createDate","undefined");
    	addVariable (createDate);
    	
    	modifiedDate=new HoopKVString ("modifiedDate","undefined");
    	addVariable (modifiedDate);
    	
    	keywords=new HoopKVString ("keywords","undefined");
    	addVariable (keywords);
    	
    	url=new HoopKVString ("url","undefined");
    	addVariable (url);
    	
    	rank=new HoopKVInteger (1,"1");
    	addVariable (rank);
    	
    	score=new HoopKVFloat ((float) 1.0,"1.0");
    	addVariable (score);    	
    	
    	// Make sure we have an entry for our text
    	values.add(new String ("0"));
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
		
		if (abstr.getValue().isEmpty()==true)
		{
			HoopHTML2Text parser=new HoopHTML2Text ();
						
			if (this.getValue().length()>abstrSize)
			{
				parser.parse(this.getValue().substring(0,abstrSize));
				
				abstr.setValue(parser.getText());
			}
			else
			{
				parser.parse(this.getValue());
				
				abstr.setValue(parser.getText());
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
		
		return (formatter.toString());
	}
}
