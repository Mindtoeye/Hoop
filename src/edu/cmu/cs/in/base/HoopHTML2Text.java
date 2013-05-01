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

package edu.cmu.cs.in.base;

import java.io.*;

import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

/**
 * Based on: http://stackoverflow.com/questions/240546/removing-html-from-a-java-string
 */
public class HoopHTML2Text extends HTMLEditorKit.ParserCallback 
{
    private StringBuffer s=null;
    private Reader in=null;
    private String text=null;

    /**
     * 
     */
    public HoopHTML2Text() 
    {
    	
    }
    /** 
     * @param in
     * @throws IOException
     */
    public Boolean parse (String aText)
    {
        s = new StringBuffer();        
        in=new StringReader (aText);
        
        ParserDelegator delegator = new ParserDelegator();
        // the third parameter is TRUE to ignore charset directive
        try 
        {
			delegator.parse (in, this, Boolean.TRUE);
		} 
        catch (IOException e) 
        {		
			e.printStackTrace();
			return (false);
		}
        
        return (true);
    }    
    /** 
     * @param in
     * @throws IOException
     */
    public Boolean parse (Reader aReader)
    {
    	in=aReader;
    	
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();
        
        // the third parameter is TRUE to ignore charset directive
        
        try 
        {
			delegator.parse (in, this, Boolean.TRUE);
		} 
        catch (IOException e) 
        {		
			e.printStackTrace();
			return (false);
		}
        
        return (true);
    }
    /**
     * 
     */
	public void setText(String text) 
	{
		this.text = text;
	}    
    /**
     * 
     */
    public void handleText(char[] text, int pos) 
    {
        s.append(text);
    }
    /**
     * 
     */
    public String getText() 
    {
    	if (in!=null)
    	{
    		try 
    		{
				in.close();
			} 
    		catch (IOException e) 
    		{			
				e.printStackTrace();
			}
    		
    		text=s.toString();
    	}
    	
        return text;
    }
    /**
     * 
     */
    /*
    public static void main(String[] args) 
    {
        try 
        {
            // the HTML to convert
            FileReader in = new FileReader("java-new.html");
            HoopHTML2Text parser = new HoopHTML2Text();
            parser.parse(in);
            in.close();
            
            System.out.println(parser.getText());
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    */
}
