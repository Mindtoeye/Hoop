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

import edu.cmu.cs.in.base.HoopRoot;
import mx.bigdata.anyobject.AnyObject;

/**
 * 
 */
public final class Stage extends HoopRoot
{
	private final int id;
  
	private final AnyObject config;
  
	/**
	 * 
	 */
	Stage(int id, AnyObject config) 
	{
		setClassName ("Stage");
		debug ("Stage ()");	  
	  
		this.id = id;
		this.config = config;
	}
	/**
	 * 
	 * @return
	 */
	public AnyObject getConfiguration() 
	{
		return config;
	}
	/**
	 * 
	 * @return
	 */
	public int getId() 
	{
		return id;
	}  
}
