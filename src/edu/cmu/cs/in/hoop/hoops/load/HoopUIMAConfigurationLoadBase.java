package edu.cmu.cs.in.hoop.hoops.load;

import java.util.Scanner;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.io.HoopVFSL;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopURISerializable;

import static org.uimafit.factory.JCasFactory.createJCas;

public class HoopUIMAConfigurationLoadBase extends HoopLoadBase implements HoopInterface {

	private static final long serialVersionUID = -3882301282248283204L;
	
	public HoopURISerializable URI=null;
	//private HoopFileTools fTools=null;
	//private ArrayList <String> files=null;

	public static Scanner uimaConfigurationFileScanner;

	public static String tsdPath;

	public static String type;

	public static String fs;

	public static String aedPath;

	public static String aePath;
		
	public static String typePath;
	public static String typeFsPath;
	/**
	 *
	 */
    public HoopUIMAConfigurationLoadBase () 
    {
		setClassName ("HoopUIMAConfigurationLoadBase");
		debug ("HoopUIMAConfigurationLoadBase ()");
		
		setHoopDescription ("Load UIMA Configuration");
					
		URI=new HoopURISerializable (this,"URI","");
		
		//fTools=new HoopFileTools ();
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
    	
    	//files=null;
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
		
		uimaConfigurationFileScanner = new Scanner(contents);
		tsdPath = uimaConfigurationFileScanner.next();
		type = uimaConfigurationFileScanner.next();
		fs = uimaConfigurationFileScanner.next();
		aedPath = uimaConfigurationFileScanner.next();
		aePath = uimaConfigurationFileScanner.next();
		typePath = uimaConfigurationFileScanner.next();
		typeFsPath = uimaConfigurationFileScanner.next();
	
		try {
			JCas jCas = createJCas();
			addCas (jCas);
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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

}

