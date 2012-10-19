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

package edu.cmu.cs.in.hoop;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.util.XMLInputSource;

import edu.cmu.cs.in.hoop.hoops.base.HoopBase;

/**
 * Main Class that runs a Collection Processing Engine (CPE). This class reads a CPE Descriptor as a
 * command-line argument and instantiates the CPE. It also registers a callback listener with the
 * CPE, which will print progress and statistics to System.out. 
 */
public class HoopUIMACPE extends Thread 
{
  /// The CPE instance.
  private CollectionProcessingEngine mCPE;

  /**
   * Constructor for the class.
   * 
   * @param args
   *          command line arguments into the program - see class description
   */
  public HoopUIMACPE(String args[]) throws Exception 
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

	  // Start Processing
	  
	  debug ("Running CPE ...");
    
	  mCPE.process();

	  // Allow user to abort by pressing Enter
	  
	  debug ("To abort processing, type \"abort\" and press enter.");
	  
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
   * main class.
   * 
   * @param args
   *          Command line arguments - see class description
   */
  public static void main(String[] args) throws Exception 
  {
	  new HoopUIMACPE(args);
  }
}
