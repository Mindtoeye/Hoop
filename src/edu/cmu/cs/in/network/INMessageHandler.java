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

package edu.cmu.cs.in.network;

import org.w3c.dom.Element;

/**
*
*/
public interface INMessageHandler
{    						
	/**
	 *
	 */
    public void handleIncomingData (int ID,String data); 
      
	/**
	 *
	 */
    public void handleIncomingXML (int ID,Element root);    
}
