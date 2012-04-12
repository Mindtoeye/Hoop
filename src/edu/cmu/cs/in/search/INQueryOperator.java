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

package edu.cmu.cs.in.search;

import java.util.ArrayList;

import edu.cmu.cs.in.base.INBase;
import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.search.INPositionEntry;
import edu.cmu.cs.in.search.INPositionList;

/**
*
*/
public class INQueryOperator extends INBase
{    			
	private String operator="#and"; // nice default, one of #sum,#and,#or,#near
	private int distance=1; // in case the operator is something like #near/1
	private Boolean isTerm=false;
	private Boolean isStop=false;
	private ArrayList <INQueryOperator> operators=null;
	private INPositionList positions=null;
	private Boolean valid=true;
	
	private Boolean loadFull=false;
	private Boolean loadPositions=false;
	
	private Long timeTaken=1L;
	private Long memUsed=1L;
	
	private Boolean fastLoad=true;
	
	/**
	 *
	 */
	public INQueryOperator (String anOp) 
	{
		setClassName ("INQueryOperator");
		debug ("INQueryOperator ()");	
		setInstanceName (anOp);
		operators=new ArrayList<INQueryOperator> ();
		positions=new INPositionList ();
	}	
	/**
	 *
	 */
	public INQueryOperator () 
	{
		setClassName ("INQueryOperator");
		debug ("INQueryOperator ()");		
		operators=new ArrayList<INQueryOperator> ();
		positions=new INPositionList ();
	}	
    /**
	 *
	 */	
	public void setIsStop(Boolean isStop) 
	{
		this.isStop = isStop;
	}
   /**
	 *
	 */
	public Boolean getIsStop() 
	{
		return isStop;
	}	
	/**
	 * 
	 */	
	public void setFastLoad(Boolean fastLoad) 
	{
		this.fastLoad = fastLoad;
	}
	/**
	 * 
	 */	
	public Boolean getFastLoad() 
	{
		return fastLoad;
	}		
	/**
	 * 
	 */	
	public Long getTimeTaken() 
	{
		return timeTaken;
	}
	/**
	 * 
	 */	
	public void setTimeTaken(Long timeTaken) 
	{
		this.timeTaken = timeTaken;
	}
	/**
	 * 
	 */	
	public Long getMemUsed() 
	{
		return memUsed;
	}
	/**
	 * 
	 */	
	public void setMemUsed(Long memUsed) 
	{
		this.memUsed = memUsed;
	}	
	/**
	 * 
	 */
	public void setLoadFull(Boolean loadFull) 
	{
		this.loadFull = loadFull;
	}
	/**
	 * 
	 */	
	public Boolean getLoadFull() 
	{
		return loadFull;
	}	
	/**
	 * 
	 */	
	public void setLoadPositions(Boolean loadPositions) 
	{
		this.loadPositions = loadPositions;
	}
	/**
	 * 
	 */	
	public Boolean getLoadPositions() 
	{
		return loadPositions;
	}	
	/**
	 * 
	 */
	public void setInstanceName (String aName)
	{
		super.setInstanceName(aName.toLowerCase());
		
		this.setIsTerm(true);		
		
		if (aName.equals("#and")==true)
		{
			this.setIsTerm(false);
			setOperator ("#and");
		}
			
		if (aName.equals("#or")==true)
		{
			this.setIsTerm(false);
			setOperator ("#or");
		}
		
		if (aName.equals("#sum")==true)
		{
			this.setIsTerm(false);
			setOperator ("#sum");
		}
		
		if (aName.equals("#near")==true)
		{
			this.setIsTerm(false);
			setOperator ("#near");
		}
	}
	/**
	 *
	 */
	public void reset ()
	{
		debug ("reset ()");		
		//operators=new ArrayList<INQueryOperator> ();
			
		positions=new INPositionList ();
		
		for (int i=0;i<operators.size();i++)
		{
			INQueryOperator op=operators.get(i);
			op.reset();
		}
	}
	/**
	 *
	 */
	public INQueryOperator addOperator (String anOperator)
	{
		debug ("addOperator ("+anOperator+")");
		
		INQueryOperator op=new INQueryOperator ();
		op.setInstanceName(anOperator);
								
		operators.add (op);
		
		return (op);
	}
	/**
	 * 
	 */
	public void addOperator (INQueryOperator anOperator)
	{					
		operators.add(anOperator);
	}
	/**
	 *
	 */
	public ArrayList<INQueryOperator> getOperators ()
	{
		return (operators);
	}
	/**
	 *
	 */
	public String getOperator() 
	{
		return operator;
	}
	/**
	 *
	 */
	public void setOperator(String operator) 
	{
		this.operator = operator;
	}
	/**
	 *
	 */	
	public Boolean getIsTerm() 
	{
		return isTerm;
	}
	/**
	 *
	 */	
	public void setIsTerm(Boolean isTerm) 
	{
		this.isTerm = isTerm;
	}
	/**
	 *
	 */	
	public INPositionList getPositions() 
	{
		return positions;
	}
    /**
	 *
	 */	
	public Boolean getValid() 
	{
		return valid;
	}
    /**
	 *
	 */	
	public void setValid(Boolean valid) 
	{
		this.valid = valid;
	}
    /**
	 *
	 */	
	public int getDistance() 
	{
		return distance;
	}
    /**
	 *
	 */	
	public void setDistance(int distance) 
	{
		this.distance = distance;
	}	
    /**
	 *
	 */	
	public Boolean loadPositionList ()
	{
		debug ("loadPositionsList ("+this.getInstanceName()+")");
		
		if (INHoopLink.posFiles==null)
		{
			debug ("Error: no position lists available");
			return (false);
		}
		
		Boolean found=false;
		
		for (int i=0;i<INHoopLink.posFiles.size ();i++)
		{
			String fileName=INHoopLink.posFiles.get(i);
			//if (fileName.indexOf(this.getInstanceName())!=-1)
			if (fileName.equals(this.getInstanceName())==true)
			{
				String invListURL=INHoopLink.vocabularyPath+"/"+this.getInstanceName()+".inv";
				
				debug ("Loading: " + invListURL);
						
				if (INHoopLink.fManager.doesFileExist (invListURL)==false)
				{
					debug ("File does not exist");
					setValid (false);
				}
				else
				{				
					found=true;
				
					if (fastLoad==false)
					{
						ArrayList<String> lines=INHoopLink.fManager.loadLines(invListURL);
						debug ("Loaded");
						processPositions (lines);
						lines=null; // Need to get rid of memory!!
					}
					else
					{
						debug ("Calling positions load and processing simultaneously ...");
						processPositions (invListURL);
					}
				}	
			}
		}
		
		if (found==false)
		{
			debug ("Error: positions list most likely not found in vocabulary");
		}
		
		return (found);
	}	
    /**
	 *
	 */		
	private void processPositions (ArrayList<String> lines)
	{
		debug ("processPositions (ArrayList<String>)");
		
		if (lines.size()<2)
		{
			debug ("Error, file contains less than 2 lines");
			return;
		}
		
		String header=lines.get (0);
		
		String [] split=header.split("\\s+");
				
		if (loadFull==true)
		{
			positions.setStemmTerm(split [0]);
			positions.setCollTerm(split [1]);
		}
		
		positions.setFreq(Long.parseLong(split [2]));
		positions.setN(Long.parseLong(split [3]));
		
		ArrayList<INPositionEntry> posList=positions.getPosEntries (); // At this point an empty list
		
		for (int i=1;i<lines.size();i++)
		{
			String line=lines.get(i);
			
			String [] entries=line.split("\\s+");
			
			INPositionEntry newEntry=new INPositionEntry ();
			
			for (int j=0;j<entries.length;j++)
			{				
				if (j==0)
				{
					String formatter=entries [j];
					newEntry.setDocID(Long.parseLong(formatter));
				}
				
				if (j==1)
					newEntry.setTf(Long.parseLong(entries [j]));
				
				if (j==2)
					newEntry.setDocLen(Long.parseLong(entries [j]));
				
				if (j>2)
				{
					if ((loadPositions==true) || (loadFull==true))
					{
						ArrayList<Long> pos=newEntry.getPosList();
						pos.add(Long.parseLong(entries [j]));
					}	
				}
			}
			
			posList.add(newEntry);
		}
		
		split=null;
	}
    /**
	 *
	 */		
	private void processPositions (String invListURL)
	{
		debug ("processPositions (String)");
		
		String aLine=INHoopLink.fManager.readALine(invListURL);

		int index=0;
		
		while (aLine!=null)
		{			
			//debug (aLine);
			
			if (index==0)
			{
				String [] split=aLine.split("\\s+");
				
				if (loadFull==true)
				{
					positions.setStemmTerm(split [0]);
					positions.setCollTerm(split [1]);
				}
				
				positions.setFreq(Long.parseLong(split [2]));
				positions.setN(Long.parseLong(split [3]));
			}	
						
			if (index>0)
			{	
				ArrayList<INPositionEntry> posList=positions.getPosEntries ();
				
				String [] entries=aLine.split("\\s+");
			
				INPositionEntry newEntry=new INPositionEntry ();
			
				for (int j=0;j<entries.length;j++)
				{				
					if (j==0)
					{
						String formatter=entries [j];
						newEntry.setDocID(Long.parseLong(formatter));
					}
				
					if (j==1)
						newEntry.setTf(Long.parseLong(entries [j]));
				
					if (j==2)
						newEntry.setDocLen(Long.parseLong(entries [j]));
				
					if (j>2)
					{
						if ((loadPositions==true) || (loadFull==true))
						{
							ArrayList<Long> pos=newEntry.getPosList();
							pos.add(Long.parseLong(entries [j]));
						}	
					}
				}
				
				//debug ("Entry: "+newEntry.getDocID()+" docLen: " + newEntry.getDocLen()+" tf: " + newEntry.getTf());
			
				posList.add(newEntry);
			}
			
			aLine=INHoopLink.fManager.readALine(invListURL);
			index++;
		}
		
		ArrayList<INPositionEntry> checkList=positions.getPosEntries ();
		debug ("Loaded " + checkList.size()+" entries, for: " + this.getInstanceName());
	}
}
