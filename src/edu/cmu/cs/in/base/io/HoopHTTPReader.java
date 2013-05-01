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

package edu.cmu.cs.in.base.io;

import java.awt.Component;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.cmu.cs.in.base.HoopRoot;

public class HoopHTTPReader extends HoopRoot
{	
	private InputStream binaryIn=null;
	private byte[] binaryData=null;
	private String binaryLocation=null;
		
	/**
	 *
	 */
	public HoopHTTPReader ()
	{
    	setClassName ("HoopHTTPReader");
    	debug ("HoopHTTPReader ()");		
	}
	/**
	 *
	 */
    public String loadURL (String address)  throws MalformedURLException, IOException 
    {
    	debug ("loadURL ("+address+")");
    	
        URL url = new URL(address);
        
        StringBuffer total=new StringBuffer ();
        
        URLConnection conn =url.openConnection();
        conn.setDoInput(true);
        try 
        {
        	conn.connect();
        }
        catch (Exception e)
        {
        	debug ("cannot obtain: " + address);
        	return ("");
        }
        
        try 
        {
        	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        	String inputLine;

        	while ((inputLine = in.readLine()) != null)
        	{
        		//System.out.println(inputLine);
        		total.append(inputLine);
        	}
        	
        	in.close();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	return ("");
        }        
                
        return (total.toString());
    }
	/**
	 *
	 */
    public Image fetchImage (String address, Component c) throws MalformedURLException, IOException 
    {
    	debug ("fetchImage ("+address+")");
    	
        URL url = new URL(address);
        return c.createImage((java.awt.image.ImageProducer)url.getContent());
    }
	/**
	 *
	 */
    public boolean sendData (String address,String aData)  throws MalformedURLException, IOException 
    {
    	debug ("sendData ("+address+")");
    	
    	try 
    	{
    	    URL url = new URL(address);
    	    URLConnection conn = url.openConnection();
    	    conn.setDoOutput(true);
    	    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
    	    wr.write(aData);
    	    wr.flush();

    	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

    	    @SuppressWarnings("unused")
			String line=null;
    	    
    	    while ((line = rd.readLine()) != null) 
    	    {
    	        // Process line...
    	    }
    	    wr.close();
    	    rd.close();
    	} 
    	catch (Exception e) 
    	{
    		
    	}
    	    	
    	return (true);
    }
	/**
	 *
	 */	
	public void getHTTPBinaryFile (String aURL,String aLocation) throws Exception
	{
		debug ("getHTTPBinaryFile ("+aURL+")");
		
		URL u = new URL(aURL);
	    URLConnection uc = u.openConnection();

	    String contentType = uc.getContentType();
	    int contentLength = uc.getContentLength();
	    
	    if (contentType.startsWith ("text/") || contentLength == -1) 
	    {
	      throw new IOException ("This is not a binary file.");
	    }
	    
	    InputStream raw = uc.getInputStream();
	    InputStream in = new BufferedInputStream(raw);
	    byte[] data = new byte [contentLength];
	    int bytesRead = 0;
	    int offset = 0;
	    
	    while (offset < contentLength) 
	    {	    	    		    	
	    	bytesRead = in.read(data, offset, data.length - offset);
	    	
	    	if (bytesRead == -1)
	    		break;
	    	
	    	offset += bytesRead;	    			    	    	
	    }
	    	    
	    in.close();

	    if (offset != contentLength) 
	    {
	      throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
	    }

	    FileOutputStream out = new FileOutputStream(aLocation);
	    out.write(data);
	    out.flush();
	    out.close();	    	  
	}     
	/** 
	 * @return
	 */
	public byte[] getData ()
	{
		return (binaryData);
	}
}
