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
 * try 
 * {
 *     	// Some examples
 *     	DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
 *     	Date date = (Date)formatter.parse("01/29/02");
 * 
 *     	formatter = new SimpleDateFormat("dd-MMM-yy");
 *     	date = (Date)formatter.parse("29-Jan-02");
 * 
 *     	// Parse a date and time; see also
 *     	// Parsing the Time Using a Custom Format
 *     	formatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
 *     	date = (Date)formatter.parse("2002.01.29.08.36.33");
 * 
 *     	formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
 *     	date = (Date)formatter.parse("Tue, 29 Jan 2002 22:14:02 -0500");
 * 	} 
 * 	catch (ParseException e) 
 * 	{
 * 	
 * 	} 
 */

package edu.cmu.cs.in.base;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 */
public class HoopDateTools extends HoopRoot
{
	private String dateFormat="yyyy.MM.dd HH.mm.ss";
	
	/**
	 * 
	 */
	public HoopDateTools ()
	{
		setClassName ("HoopDateTools");
		debug ("HoopDateTools ()");
	}
	/** 
	 * @param dateFormat
	 */
	public void setDateFormat(String dateFormat) 
	{
		this.dateFormat = dateFormat;
	}
	/** 
	 * @return
	 */
	public String getDateFormat() 
	{
		return dateFormat;
	}	
	/** 
	 * @param aDate
	 * @return
	 */
	public Long StringToDate (String aDate)
	{
		debug ("StringToDate ("+aDate+")");
		
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		Date transf=null;
		
		try 
		{
			transf=formatter.parse (aDate);
		} 
		catch (ParseException e) 
		{
			//e.printStackTrace();
			
			Date date = new Date();
			debug ("Error parsing date ("+aDate+"), was expecting something like: " + formatter.format(date));
			
			return (long) (-1);
		}	
		
		//checkDateConversion (transf.getTime());
		
		return (transf.getTime());
	}	
	/** 
	 * @param aDate
	 * @return
	 */
	public Long StringToDate (String aDate,String aFormat)
	{
		debug ("StringToDate ("+aDate+")");
		
		setDateFormat (aFormat);
		
		return (StringToDate (aDate));
	}		
	/**
	 * 
	 */
	private void checkDateConversion (Long aTimeStamp)
	{
		debug ("checkDateconversion ("+aTimeStamp+")");
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		 
		Date resultdate = new Date(aTimeStamp);
		debug ("Check: " + sdf.format (resultdate));  		 		
	}
}