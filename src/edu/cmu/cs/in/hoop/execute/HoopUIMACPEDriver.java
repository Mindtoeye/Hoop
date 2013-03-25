/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package edu.cmu.cs.in.hoop.execute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 * Main Class that runs a Collection Processing Engine (CPE). This class reads a 
 * CPE Descriptor as a command-line argument and instantiates the CPE. It also 
 * registers a callback listener with the CPE, which will print progress and 
 * statistics to System.out. 
 */
public class HoopUIMACPEDriver extends Thread 
{
	private CollectionProcessingEngine mCPE=null;
	private Boolean externalCPEStop=false;

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public HoopUIMACPEDriver() 
	{
		debug ("HoopUIMACPE ()");
	}
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public HoopUIMACPEDriver(String args[]) throws Exception 
	{
		debug ("HoopUIMACPE ()");
	  
		// check command line args
		if (args.length < 1) 
		{
			printUsageMessage();
			System.exit(1);
		}

		// parse CPE descriptor
		debug ("Parsing CPE Descriptor");
	  
		CpeDescription cpeDesc = UIMAFramework.getXMLParser().parseCpeDescription(new XMLInputSource(args[0]));
		// instantiate CPE
	  
		debug ("Instantiating CPE");
	  
		mCPE = UIMAFramework.produceCollectionProcessingEngine(cpeDesc);

		// Create and register a Status Callback Listener
		mCPE.addStatusCallbackListener (new HoopUIMACPEStatusProcessor (mCPE));
	  
		debug ("Running CPE ...");
    
		mCPE.process();
	  
		while (true) 
		{
			String line = new BufferedReader(new InputStreamReader(System.in)).readLine();
      
			if ("abort".equals(line) && mCPE.isProcessing()) 
			{
				debug ("Aborting...");
				mCPE.stop();
				break;
			}
		}
	}
	/**
	 *
	 */
	private void debug (String aMessage)
	{
		HoopBase.debug ("HoopUIMACPE",aMessage);
	}
	/**
     * 
     */
	private static void printUsageMessage() 
	{
		System.out.println(" Arguments to the program are as follows : \n" + "args[0] : path to CPE descriptor file");
	}
	/**
	 *
	 */
	public Boolean runCPE (String anInputFile)
	{
		debug ("runCPE ("+anInputFile+")");
	  
		XMLInputSource aSource=null;
	  
		try 
		{
			aSource = new XMLInputSource(anInputFile);
		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return (false);
		}
	  
		CpeDescription cpeDesc=null;
	  
		try 
		{
			cpeDesc = UIMAFramework.getXMLParser().parseCpeDescription(aSource);
		} 
		catch (InvalidXMLException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return (false);		  
		}
	  
		debug ("Instantiating CPE ...");
	  
		try 
		{
			mCPE = UIMAFramework.produceCollectionProcessingEngine(cpeDesc);
		} 
		catch (ResourceInitializationException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return (false);		  
		}

		// Create and register a Status Callback Listener
		mCPE.addStatusCallbackListener (new HoopUIMACPEStatusProcessor (mCPE));

		// Start Processing
	  
		debug ("Running CPE ...");
    
		try 
		{
			mCPE.process();
		} 
		catch (ResourceInitializationException e) 
		{		
			e.printStackTrace();
			return (false);
		}
	  
		while (true) 
		{      
			if ((externalCPEStop==true) && (mCPE.isProcessing()==true)) 
			{
				debug ("Aborting...");
				mCPE.stop();
				break;
			}
		}
	  
		return (true);
	}
	/**
	 * main class.
	 * 
	 * @param args
	 *       	Command line arguments - see class description
	 */
	public static void main(String[] args) throws Exception 
	{
		new HoopUIMACPEDriver(args);
	}
	/**
	 *
	 * @return
	 */
	public Boolean getExternalCPEStop() 
	{
		return externalCPEStop;
	}
	/**
	 *
	 * @param externalCPEStop
	 */
	public void setExternalCPEStop(Boolean externalCPEStop) 
	{
		this.externalCPEStop = externalCPEStop;
	}
}
