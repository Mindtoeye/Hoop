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

import java.util.ArrayList;
import java.util.List;

public class HoopSimpleFeatureMaker extends HoopRoot implements HoopFeatureMaker
{	
	/**
	 *
	 */
	public HoopSimpleFeatureMaker () 
	{
		setClassName ("HoopFeatureMakerBase");
		debug ("HoopFeatureMakerBase ()");
	}
	/**
	 *
	 */
	private Boolean runExclusionFilters (String aSource)
	{
		//debug ("runExclusionFilters ()");
		
		ArrayList<HoopFilterBase> fList=getFilters ();
		if (fList.size()==0)
			return (true);
		
		for (int j=0;j<fList.size();j++)
		{
			HoopFilterBase filter=fList.get(j);
			
			if (filter.getNoMore()==true)
			{
				return (false);
			}
			
			if (filter.evaluate (aSource)==false)
				return (false);
		}
		
		return (true);
	}
	/**
	 *
	 */
	private String runCleanFilters (String aSource)
	{
		//debug ("runCleanFilters ()");
		
		ArrayList<HoopFilterBase> fList=getFilters ();
		
		if (fList.size()==0)
		{
			return (aSource);
		}	
		
		for (int j=0;j<fList.size();j++)
		{
			HoopFilterBase filter=fList.get(j);
						
			aSource=filter.clean (aSource);
			if (aSource==null) // One of the filters detected that this entire term is invalid
				return (null);
		}
		
		return (aSource);
	}	
	/**
	 *
	 */
	private List<String> runCheckFilters (List<String> rawTokens)
	{
		List<String> out = new ArrayList<String>();
		
		ArrayList<HoopFilterBase> fList=getFilters ();
		
		if (fList.size()==0)
		{
			return (rawTokens);
		}	
		
		for (int j=0;j<fList.size();j++)
		{
			HoopFilterBase filter=fList.get(j);
			
			for (int t=0;t<rawTokens.size();t++)
			{
				String token=rawTokens.get(t);
				if (filter.check(token)==true)// In other words: keep
				{
					//debug ("Keeping: " + token);
					out.add(new String (token));
				}
				//else
				//	debug ("Throwing out: " + token);
			}
		}		
		
		return (out);
	}
	/**
	 *
	 */	
	public List<String> unigramTokenizeBasic (String aSource)
	{
		debug ("unigramTokenizeBasic (String)");
		
		String cleaned=aSource;
				
		ArrayList<String> out = new ArrayList<String>();
		
		if (aSource == null || (aSource.trim().length() == 0))
			return out;
		
		if (cleaned!=null)
		{
			String [] split=cleaned.split("\\s+");
		
			for (int i = 0; i < split.length; i++)
			{
	
				out.add(split [i]);
			}
		}	
		
		return (out);				
	}		
	/**
	 *
	 */	
	public List<String> unigramTokenize (String aSource)
	{
		debug ("unigramTokenize (String)");
		
		String cleaned=aSource;
				
		ArrayList<String> out = new ArrayList<String>();
		
		if (aSource == null || (aSource.trim().length() == 0))
			return out;
		
		if (runExclusionFilters (aSource)==false)
			return out;
		
		cleaned=runCleanFilters (aSource);
		if (cleaned!=null)
		{
			cleaned = cleaned.trim().toLowerCase();

			String [] split=cleaned.split("\\s+");
		
			for (int i = 0; i < split.length; i++)
			{
				if (HoopLink.casefold==true)
					out.add(split [i].toLowerCase());
				else
					out.add(split [i]);
			}
		}	
		
		return (runCheckFilters (out));				
	}	
	/**
	 *
	 */	
	public List<String> unigramTokenize (List<String> oldTokens,String aSource)
	{
		//debug ("unigramTokenize (List<String>,String)");
				
		String cleaned=aSource;		
		
		if (oldTokens==null)
		{
			debug ("Internal error: null token list!");
			return (null);
		}	
		
		if ((aSource) == null || (aSource.trim().length() == 0))
		{
			//debug ("Found empty string!");
			return oldTokens;
		}	
		
		if (runExclusionFilters (aSource)==false)
			return oldTokens;		
				
		cleaned=runCleanFilters (aSource);		

		if (cleaned!=null)
		{
			cleaned = cleaned.trim().toLowerCase();
		
			String [] split=cleaned.split("\\s+");
		
			//debug ("Split into "+ split.length + " raw tokens");
		
			for(int i = 0; i < split.length; i++)
			{
				if (HoopLink.casefold==true)
					oldTokens.add(split [i].toLowerCase());
				else
					oldTokens.add(split [i]);
			}
		}	
		
		return (runCheckFilters (oldTokens));							
	}		
	/**
	 *
	 */	
	public List<String> unigramTokenize (String aSource, int n)
	{
		//debug ("unigramTokenize (String,int)");
	
		return (unigramTokenize (aSource));
		
		/*
		List<String> out = new ArrayList<String>();
		
		if (aSource == null || (aSource.trim().length() == 0))
			return out;
		
		aSource = aSource.trim().toLowerCase();
		String[] split = aSource.split("([\\W|_&&[^'-<>]])");
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < split.length; i++)
		{
			sb.setLength(0);
			
			for(int j = 0; j < n; j++)
			{
				if(i+j < split.length)
					sb.append(split[i+j]); sb.append("_");
			}
			
			String toAdd = sb.toString();
			
			if(toAdd.length() > 1)
			{
				if (HoopLink.casefold==true)
					out.add(toAdd.toLowerCase());
				else
					out.add(toAdd);
			}
		}
		return out;
		*/		
	}	
}

