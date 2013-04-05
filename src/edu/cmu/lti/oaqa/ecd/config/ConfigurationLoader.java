/*
 *  Copyright 2012 Carnegie Mellon University
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package edu.cmu.lti.oaqa.ecd.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import mx.bigdata.anyobject.AnyObject;
import mx.bigdata.anyobject.impl.SnakeYAMLLoader;

import com.google.common.io.CharStreams;

import edu.cmu.cs.in.base.HoopLink;
import edu.cmu.cs.in.base.HoopRoot;

/** 
 * Reworked by Martin van Velsen
 */
public class ConfigurationLoader extends HoopRoot
{
	/**
	 * 
	 */
	public ConfigurationLoader ()
	{
		setClassName ("ConfigurationLoader");
		debug ("ConfigurationLoader ()");		
	}	
	/**
	 * I've reworked this method to allow normal path specifications. For example
	 * you will find in java apps or Eclipse launch files this:
	 * 
	 * <stringAttribute key="org.eclipse.jdt.launching.PROGRAM_ARGUMENTS" value="helloqa.helloqa"/>
	 * 
	 * What this means is that the driver should load:
	 *
	 * helloqa.helloqa.yaml
	 * 
	 * However if someone already specified the path with yaml it will fail. Hence the
	 * upgrade below. The method also allows specification of regular full paths such
	 * as:
	 * 
	 * helloqa/helloqa.yaml
	 * 
	 * @param resource
	 * @param fullpath
	 * @return
	 */
	public static String getResourceLocation(String resource) 
	{
		HoopRoot.debug ("ConfigurationLoader","getResourceLocation ()");
		
		resource.replace ("\\", File.pathSeparator);
		
		if (resource.indexOf("/")==-1) // There are no regular path separators
		{
			String parsed = resource.replace (".", File.pathSeparator);
			
			if (parsed.indexOf(".yaml")==-1)			
				return (parsed + ".yaml");
			
			return (parsed);
		}	
	
	
		return (resource);
	}
	/**
	 * 
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public static AnyObject load(String resource) throws IOException 
	{
		HoopRoot.debug ("ConfigurationLoader","load ("+resource+")");
		
		String resourceLocation = getResourceLocation(HoopLink.relativeToAbsolute(resource));
				
		InputStream in=HoopLink.fManager.openInputStream(resourceLocation);
		
		if (in == null) 
		{
			throw new FileNotFoundException (resourceLocation + " is not found");
		}
		try 
		{
			return SnakeYAMLLoader.getInstance().load (in);
		} 
		finally 
		{
			in.close();
		}		
	}
	/**
	 * 
	 * @param resource
	 * @return
	 * @throws IOException
	 */
	public static String getString(String resource) throws IOException 
	{
		HoopRoot.debug ("ConfigurationLoader","getString ("+resource+")");
		
		String resourceLocation = ConfigurationLoader.getResourceLocation(HoopLink.relativeToAbsolute(resource));
		InputStream in = ConfigurationLoader.class.getResourceAsStream(resourceLocation);
		
		try 
		{
			Reader reader = new InputStreamReader(in);
			return CharStreams.toString(reader);
		} 
		finally 
		{
			in.close();
		}
	}
}
