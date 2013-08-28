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

package edu.cmu.cs.in.hoop.hoops.transform;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.jdom.Document;
import org.jdom.Element;

import edu.cmu.cs.in.base.HoopXMLBase;
import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.controls.HoopXPathFeatureEditor;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopTransformBase;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
* http://www.ibm.com/developerworks/library/x-javaxpathapi/index.html
*/
public class HoopXML2Features extends HoopTransformBase implements HoopInterface
{    	
	private static final long serialVersionUID = -3153758968460781524L;

	private ArrayList <HoopStringSerializable>features=null;
	
	private HoopXPathFeatureEditor featureEditor=null;
	
	/**
	 *
	 */
    public HoopXML2Features () 
    {
		setClassName ("HoopXML2Features");
		debug ("HoopXML2Features ()");
										
		setHoopDescription ("Parse Text as XML and extract Features");			
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{			
			if (inData.size()==0)
			{
				this.setErrorString("Error: data size is 0");
				return (false);
			}
			
			for (int t=0;t<inData.size();t++)
			{
				HoopKV aKV=inData.get(t);

				String aFullText=aKV.getValueAsString();
					
				if (aFullText.indexOf("<?xml")==-1) // Quick check
				{
					this.setErrorString("Error: loaded text does not contain xml signature");
					return (false);
				}
				else
				{
					HoopXMLBase xmlTools=new HoopXMLBase ();
					xmlTools.loadXMLFromString (aFullText);
					
					Document root=xmlTools.getDoc();
				
					for (int i=0;i<features.size();i++)
					{
						HoopStringSerializable aFeature=features.get(i);
					
						try
						{
							XPath xPath = XPathFactory.newInstance().newXPath();
							//Node node = (Node) xPath.evaluate("/Request/@name", root, XPathConstants.NODE);
							//Node node = (Node) xPath.evaluate(aFeature.getName(), root, XPathConstants.NODE);
							Element node =(Element) xPath.evaluate(aFeature.getName(), root, XPathConstants.NODE);
							//aFeature.setValue(node.getNodeValue());						
							aFeature.setValue(node.getText());
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}					
					}
				}	
				
				updateProgressStatus (t,inData.size());
			}		
		}		
		else
		{
			this.setErrorString("Error: no data found in incoming hoop");
			return (false);
		}	
						
		return (true);
	}	
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopXML2Features ());
	}		
	/**
	 * 
	 */
	@Override
	public JPanel getPropertiesPanel() 
	{
		debug ("getPropertiesPanel ()");
		
		if (featureEditor==null)
			featureEditor=new HoopXPathFeatureEditor (this);
		
		// Doesn't make a difference, probably because there is no vertical glue in the scrollpane
		
		featureEditor.setPreferredSize(new Dimension (150,200)); 
		
		return (featureEditor);
	}		
}
