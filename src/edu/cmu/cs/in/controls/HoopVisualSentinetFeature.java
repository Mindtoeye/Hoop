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

package edu.cmu.cs.in.controls;

import java.text.DecimalFormat;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class HoopVisualSentinetFeature extends HoopVisualFeature implements HoopVisualFeatureBase
{	
	private float posSentiment=(float) 0.0;
	private float negSentiment=(float) 0.0;
	
	/**------------------------------------------------------------------------------------
	 *
	 */
    public HoopVisualSentinetFeature () 
    {
		setClassName ("HoopVisualSentinetFeature");
		//debug ("HoopVisualSentinetFeature ()");		
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public HoopVisualSentinetFeature (String aText) 
    {
		setClassName ("HoopVisualSentinetFeature");
		//debug ("HoopVisualSentinetFeature ()");
		
		text=aText;
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public Boolean isPositive ()
    {
    	if (posSentiment>negSentiment)
    		return (true);
    	
    	return (false);
    }
	/**------------------------------------------------------------------------------------
	 *
	 */
    public Boolean isNegative ()
    {
    	if (posSentiment<negSentiment)
    		return (true);
   	
    	return (false);
    }    
	/**------------------------------------------------------------------------------------
	 *
	 */
    public HoopVisualSentinetFeature (String aText,float aPos,float aNeg) 
    {
		setClassName ("HoopVisualSentinetFeature ("+aText+","+aPos+","+aNeg+")");
		//debug ("HoopVisualSentinetFeature ()");
		
		text=aText;
		posSentiment=aPos;
		negSentiment=aNeg;
    }     
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public String toString()
	{
		StringBuffer formatter=new StringBuffer ();
		formatter.append(text);
		formatter.append (" p(");
		formatter.append(new DecimalFormat("#.##").format(posSentiment));
		formatter.append (") n(");
		formatter.append(new DecimalFormat("#.##").format(negSentiment));
		formatter.append(")");
		return formatter.toString();
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void setPosSentiment(float posSentiment) 
	{
		this.posSentiment = posSentiment;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public float getPosSentiment() 
	{
		return posSentiment;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void setNegSentiment(float negSentiment) 
	{
		this.negSentiment = negSentiment;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public float getNegSentiment() 
	{
		return negSentiment;
	}
	/**------------------------------------------------------------------------------------
	 *
	 */	
	public void fromXML (Node aNode)
	{
		 if (aNode.getNodeName ().equals ("feature")==true)
		 {
	    	 NamedNodeMap attr=(NamedNodeMap) aNode.getAttributes();
            for (int t=0;t<attr.getLength();t++) 
            {
                Attr attrib=(Attr)attr.item(t);
                if (attrib.getName ().equals ("name")==true)
                {
               	 debug ("Processing feature: " + attrib.getValue ());                                    	 
                }
            }    
		 }	
	}	
	/**------------------------------------------------------------------------------------
	 *
	 */
	public String toXML ()
	{
		StringBuffer formatter=new StringBuffer ();
		formatter.append("<feature name=\"");
		formatter.append(text);
		formatter.append("\" selected=\"");
		if (isSelected()==true)
			formatter.append("true");
		else
			formatter.append("false");		
		formatter.append ("\" pos=\"");
		formatter.append(new DecimalFormat("#.##").format(posSentiment));
		formatter.append ("\" neg=\"");
		formatter.append(new DecimalFormat("#.##").format(negSentiment));
		formatter.append("\" />");
		return (formatter.toString());
	}	
	//-------------------------------------------------------------------------------------	
}
