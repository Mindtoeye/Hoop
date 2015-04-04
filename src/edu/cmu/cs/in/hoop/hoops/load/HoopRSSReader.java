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

package edu.cmu.cs.in.hoop.hoops.load;

import java.net.MalformedURLException;
import java.net.URL;  

import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  

import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList; 

import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

public class HoopRSSReader  extends HoopLoadBase implements HoopInterface
{
	private static final long serialVersionUID = -5875084779709243214L;
	
	private HoopStringSerializable rssURL=null;
	
	/**
	 *
	 */
	public HoopRSSReader () 
	{
		setClassName ("HoopRSSReader");
		debug ("HoopRSSReader ()");
		
		setHoopDescription ("Connect to RSS feed provider(s) and stream text data in");	
		
		rssURL=new HoopStringSerializable (this,"rssURL","http://feeds.reuters.com/reuters/businessNews");
	}  	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopRSSReader ());
	}
	/**
	 * 
	 */
	public void reset ()
	{
		debug ("reset ()");
   
		super.reset ();    
	}
	/**
	 * Example feeds: http://www.reuters.com/tools/rss
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");

		URL rssPointer=null;

		try 
		{
			rssPointer = new URL (rssURL.getValue());
		} 
		catch (MalformedURLException e1) 
		{
			this.setErrorString("MalformedURLException: " + e1.getMessage());
			return (false);
		}
        
		this.resetData();
		
		try 
		{  
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
			Document doc = builder.parse(rssPointer.openStream());  
  
			NodeList items = doc.getElementsByTagName("item");  
  
			for (int i = 0; i < items.getLength(); i++) 
			{  
				Element item = (Element)items.item(i);  
				debug ("("+getValue(item, "title")+") "+ getValue(item, "description"));  
								
				HoopKVString fileKV=new HoopKVString ();
				
				fileKV.setKey (getValue(item, "title"));
				fileKV.setValue (getValue(item, "description"));
				fileKV.add (getValue(item, "link"));
				
				addKV (fileKV);
			}  
		} 
		catch (Exception e) 
		{  
			e.printStackTrace();  
		}  

		return (true);
	}
	/**
	 * 
	 */
	public void stop ()
	{
		debug ("stop ()");

	}
	/**
	 * 
	 * @param parent
	 * @param nodeName
	 * @return
	 */
	public String getValue(Element parent, String nodeName) 
	{  
		return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();  
	}
}
