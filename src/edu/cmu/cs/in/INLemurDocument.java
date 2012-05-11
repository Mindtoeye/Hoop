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

package edu.cmu.cs.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.cmu.cs.in.search.INDocument;

/**
*
*/
public class INLemurDocument extends INDocument
{		
	/**
	 *
	 */
    public INLemurDocument () 
    {
		setClassName ("INLemurDocument");
		debug ("INLemurDocument ()");		
    }
	/**
	 * Only accessible from within the CMU domain!
	 * http://boston.lti.cs.cmu.edu/lemur.edu/cgiinterface.php
	 */
    public void loadDocument ()
    {
    	debug ("loadDocument ()");
    	
    	// create the URL and embed the parameters to the CGI
    	URL theURL=null;
    	
		try 
		{
			theURL = new URL("http://boston.lti.cs.cmu.edu/Services/search/clueweb09_wikipedia_15p/lemur.cgi?i="+this.getDocID ());
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

    	// create the password string by taking the username
    	// and password and seperating them by a colon
    	String passwordString="11441:11441";

    	// base-64 encode the username/password string
    	String encodedPassword=new sun.misc.BASE64Encoder().encode(passwordString.getBytes());

    	// create the URL connection
    	URLConnection urlConnection=null;
    	
		try 
		{
			urlConnection = theURL.openConnection();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

    	// set the basic authentication for the URL connection
    	urlConnection.setRequestProperty("Authorization", "Basic " + encodedPassword);

    	// Read in the content
    	InputStream content=null;
    	
		try 
		{
			content = (InputStream)urlConnection.getInputStream();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
    	BufferedReader in=new BufferedReader (new InputStreamReader (content));

    	String line;
    	
    	try 
    	{
			while ((line = in.readLine()) != null) 
			{
			  debug (line);
			}
		} 
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
    	
    	try 
    	{
			in.close();
		} 
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
}
