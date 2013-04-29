package edu.cmu.cs.in.hoop.hoops.load;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.jdom.Element;

import edu.cmu.cs.in.base.HoopDataType;
import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopFileTools;
import edu.cmu.cs.in.base.io.HoopVFSL;
import edu.cmu.cs.in.base.kv.HoopKVString;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

public class HoopUIMAConfigurationLoadBase extends HoopLoadBase implements HoopInterface {

	private static final long serialVersionUID = -3882301282248283204L;
	
	public HoopURISerializable URI=null;
	private HoopFileTools fTools=null;
	private ArrayList <String> files=null;
		
	/**
	 *
	 */
    public HoopUIMAConfigurationLoadBase () 
    {
		setClassName ("HoopUIMAConfigurationLoadBase");
		debug ("HoopUIMAConfigurationLoadBase ()");
		
		setHoopDescription ("Load UIMA Configuration");
					
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

	private Boolean processSingleFile (String aPath)
	{
		debug ("processSingleFile ("+aPath+")");
		
		String contents=HoopLink.fManager.loadContents(aPath);
		
		if (contents==null)
		{
			this.setErrorString(HoopLink.fManager.getErrorString());
			return (false);
		}
		
//		HoopKVString fileKV=new HoopKVString ();
//	
//		Long stringStamp=HoopLink.fManager.getFileTime(aPath);
//		File namer=new File (aPath);
//				
//		fileKV.setKey (fTools.getFileTimeString(stringStamp));
//		fileKV.setValue (contents);
//		fileKV.add (HoopLink.fManager.getURI());	
//		fileKV.add (namer.getName());
//		fileKV.add (fTools.getFileTimeString(stringStamp)); // Created
//		fileKV.add (fTools.getFileTimeString(stringStamp)); // Modified
//		fileKV.add ("unknown"); // Owner
//		
//		/*
//		UserPrincipal owner = Files.getOwner(path);
//		String username = owner.getName();
//		*/
//							
//		addKV (fileKV);
		
		return (true);
	}

	@Override
	public Boolean runHoop(HoopBase inHoop) {
		debug ("runHoop ()");
		
		/*
		if (loadPrep ()==false)
			return (false);
		*/
		
		debug ("Loading UIMA config: " +URI.getValue());
			
				
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
		
		
		
		return (true);
	}

	@Override
	public HoopBase copy() {
		return new HoopUIMAConfigurationLoadBase();
	}

	@Override
	public JPanel getPropertiesPanel() 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
