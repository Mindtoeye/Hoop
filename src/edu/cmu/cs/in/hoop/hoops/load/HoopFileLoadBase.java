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

import static org.uimafit.factory.JCasFactory.createJCas;

import java.io.File;
import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
//import java.util.Random;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopFileTools;
import edu.cmu.cs.in.base.io.HoopVFSL;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

/**
* 
*/
public class HoopFileLoadBase extends HoopLoadBase implements HoopInterface
{    				
	private static final long serialVersionUID = -3882301282248283204L;
			
	public HoopURISerializable URI=null;
	private HoopFileTools fTools=null;
	private ArrayList <String> files=null;
		
	/**
	 *
	 */
    public HoopFileLoadBase () 
    {
		setClassName ("HoopFileLoadBase");
		debug ("HoopFileLoadBase ()");
		
		setHoopDescription ("Load Text File(s)");
					
		URI=new HoopURISerializable (this,"URI","");
		
		fTools=new HoopFileTools ();
    }
	/**
	 * 
	 */
	public String getFileExtension() 
	{
		return URI.getFileExtension();
	}
	/**
	 * 
	 */	
	public void setFileExtension(String fileExtension) 
	{
		this.setFileExtension(fileExtension);
	}    
	/**
	 * 
	 */
    public void reset ()
    {
    	debug ("reset ()");
    
    	super.reset ();
    	
    	files=null;
    }	
	/**
	 * 
	 */
	private Boolean loadPrep ()
	{
		debug ("loadPrep ()");
		
		//this.resetData();
		
		setKVType (0,HoopDataType.LONG,"Timestamp"); // KEY !!!!!!!!
		setKVType (1,HoopDataType.STRING,"Content");
		setKVType (2,HoopDataType.STRING,"URL");
		setKVType (3,HoopDataType.STRING,"File Name");
		setKVType (4,HoopDataType.STRING,"Created");
		setKVType (5,HoopDataType.STRING,"Modified");
		setKVType (6,HoopDataType.STRING,"Author");
		
		if (URI.getValue().indexOf("<PROJECTPATH>")!=-1)
		{
			if (HoopLink.project==null)
			{
				this.setErrorString ("You need a project first for this hoop to be useful");
				return (false);
			}
		
			if (HoopLink.project.getVirginFile()==true)
			{
				this.setErrorString ("You to save your project first for this hoop to be useful");
				return (false);
			}
		}	
		
		return (true);
	}
	/**
	 * 
	 */
	private void prepFileListing ()
	{
		debug ("prepFileListing ()");
				
		if (files==null)
		{			
			files=new ArrayList<String> ();
			
			ArrayList <String> tempList=HoopLink.fManager.listFiles(HoopVFSL.relativeToAbsolute(URI.getValue()));
															
			for (int i=0;i<tempList.size();i++)
			{
				String testEntry=tempList.get(i);
				
				// Filter unwanted entries ...
				
				if ((testEntry.equals(".")==false) && (testEntry.equals("..")==false))
				{
					File finalTest=new File (testEntry);
					
					if (finalTest.isDirectory()==false)
					{
						files.add(testEntry);
					}
				}
			}
														
			debug ("files.size (): " + files.size() + " < + batchSize.getPropValue(): " + batchSize.getPropValue());
		
			int actualSize=files.size();
			
			calculateIndexingSizes (actualSize);
		}		
	}
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		if (loadPrep ()==false)
			return (false);
				
		if (URI.getDirsOnly()==false)
		{				
			debug ("Processing single file ...");
			
			if (processSingleFile (HoopVFSL.relativeToAbsolute(URI.getValue()))==false)
				return (false);
			else
			{
				getVisualizer ().setExecutionInfo (" R: 1 out of 1");				
				return (true);
			}
		}
		
		if (URI.getDirsOnly()==true)
		{
			prepFileListing ();
			
			this.resetData();
			
			bCount=0;
			
			if (mode.getValue().equals("LINEAR")==true)
			{			
				while ((checkLoopDone ()==false) && (checkDone ()==false))	
				{				
					if (loadDataObject (loadIndex)==false)
						return (false);
												
					loadIndex++; // Update total index
					bCount++; // Update batch count
				}
			}
			else
			{
				while ((checkLoopDone ()==false) && (checkDone ()==false))		
				{				
					if (loadDataObject (getSample (originalSize))==false)
						return (false);
												
					loadIndex++; // Update total index
					bCount++; // Update batch count
				}
			}
			
			updateProgressStatus (loadIndex,loadMax);
			
			if (checkDone ()==false)
				this.setDone(false);			
		}
		
		return (true);
	}
	/**
	 * 
	 */
	protected boolean loadDataObject (int anIndex)
	{
		debug ("loadDataObject ("+anIndex+")");
		
		String nextFile=files.get(anIndex);
		
		if (processSingleFile (HoopVFSL.relativeToAbsolute(URI.getValue())+"/"+nextFile)==false)
		{
			return (false);
		}		
		
		return (true);
	}
	/**
	 * 
	 */
	private Boolean processSingleFile (String aPath)
	{
		debug ("processSingleFile ("+aPath+")");
		
		String contents=HoopLink.fManager.loadContents(aPath);
		
		if (contents==null)
		{
			this.setErrorString(HoopLink.fManager.getErrorString());
			return (false);
		}
		
		HoopKVString fileKV=new HoopKVString ();
	
		Long stringStamp=HoopLink.fManager.getFileTime(aPath);
		File namer=new File (aPath);
				
		fileKV.setKey (fTools.getFileTimeString(stringStamp));
		fileKV.setValue (contents);
		fileKV.add (HoopLink.fManager.getURI());	
		fileKV.add (namer.getName());
		fileKV.add (fTools.getFileTimeString(stringStamp)); // Created
		fileKV.add (fTools.getFileTimeString(stringStamp)); // Modified
		fileKV.add ("unknown"); // Owner
		
		/*
		UserPrincipal owner = Files.getOwner(path);
		String username = owner.getName();
		*/
							
		addKV (fileKV);
		
		//cas code
		try {
			JCas jCas = createJCas();
			jCas.setDocumentText(contents);
			addCas (jCas);
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (true);
	}
	/**
	 *
	 */	
	public void setInputStreamPath(String inputStreamPath) 
	{
		URI.setValue(inputStreamPath);
	}
	/**
	 *
	 */	
	public String getInputStreamPath() 
	{
		return URI.getValue();
	}	
	/**
	 * 
	 */
	public void showFiles ()
	{
		debug ("showFiles ()");
		
		for (int i=0;i<files.size();i++)
		{
			String aFile=files.get(i);
			
			debug ("File: " + aFile);
		}
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopFileLoadBase ());
	}		
}
