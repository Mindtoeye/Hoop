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

package edu.cmu.cs.in.hoop.hoops.base;

import java.io.File;

import javax.swing.JOptionPane;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopFileTools;
import edu.cmu.cs.in.base.io.HoopVFSL;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

/**
* 
*/
public class HoopFileSaveBase extends HoopSaveBase implements HoopInterface
{    				
	private String content=null;	
	private HoopVFSL fManager=null;		
	protected HoopURISerializable URI=null;	
	private Boolean alwaysOverwrite=false;
	
	/**
	 *
	 */
    public HoopFileSaveBase () 
    {
		setClassName ("HoopFileSaveBase");
		debug ("HoopFileSaveBase ()");
		
		setHoopDescription ("Save To File");
		
		URI=new HoopURISerializable (this,"URI","");
    }
	/**
	 *
	 */
	public void setAlwaysOverwrite(Boolean alwaysOverwrite) 
	{
		this.alwaysOverwrite = alwaysOverwrite;
	}
	/**
	 *
	 */
	public Boolean getAlwaysOverwrite() 
	{
		return alwaysOverwrite;
	}
	/**
	 *
	 */	
	public void setOutputStreamPath(String outputStreamPath) 
	{
		URI.setValue (outputStreamPath);
	}
	/**
	 *
	 */	
	public String getOutputStreamPath() 
	{
		return URI.getValue();
	}	    
	/**
	 *
	 */
	public void setContent(String content) 
	{
		this.content = content;
	}
	/**
	 *
	 */
	public String getContent() 
	{
		return content;
	}    
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
				
		fManager.saveContents(URI.getValue(), content);
				
		return (true);
	}		
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopFileSaveBase ());
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
		URI.setFileExtension(fileExtension);
	}	
	/**
	 * 
	 */
	protected Boolean saveContents (String aContents)
	{
        String absPath=this.projectToFullPath (URI.getValue());
        
        if (URI.getDirsOnly()==true)
        {
        	String fullPath=absPath+"/output."+URI.getFileExtension();
        	
        	HoopFileTools fTools=new HoopFileTools ();
        	
        	String aFileName=fTools.createSequenceFilename(fullPath,this.getExecutionCount());
        	
        	File checker=new File (aFileName);
        
        	if ((checker.exists()==true) && (getAlwaysOverwrite ()==false))
        	{        		
                int result = JOptionPane.showConfirmDialog(null,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
                
                switch(result)
                {
                    case JOptionPane.YES_OPTION:
                    								setAlwaysOverwrite (true);
                               						break;
                    case JOptionPane.NO_OPTION:
                        							return (true);
                    case JOptionPane.CANCEL_OPTION:
                        							return (false);
                }                		
        	}
        	
        	return (HoopLink.fManager.saveContents (aFileName,aContents));
        }
                
        return (HoopLink.fManager.saveContents (absPath,aContents));
	}
	/**
	 * 
	 */
	protected Boolean saveContents (String aContents,int aSequence)
	{
        String absPath=this.projectToFullPath (URI.getValue());
        
        if (URI.getDirsOnly()==true)
        {
        	HoopFileTools fTools=new HoopFileTools ();
        	
        	String fullPath=absPath+"/output."+URI.getFileExtension();
        	
        	String aFileName=fTools.createSequenceFilename(fullPath,aSequence);
        	
        	File checker=new File (aFileName);
        
        	if (checker.exists()==true)
        	{        		
                int result = JOptionPane.showConfirmDialog(null,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
                
                switch(result)
                {
                    case JOptionPane.YES_OPTION:
                               						break;
                    case JOptionPane.NO_OPTION:
                        							return (true);
                    case JOptionPane.CANCEL_OPTION:
                        							return (false);
                }                		
        	}
        	
        	return (HoopLink.fManager.saveContents (aFileName,aContents));
        }
                
        return (HoopLink.fManager.saveContents (absPath,aContents));
	}	
}
