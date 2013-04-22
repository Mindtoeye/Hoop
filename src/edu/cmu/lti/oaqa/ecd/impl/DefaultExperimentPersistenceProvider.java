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

package edu.cmu.lti.oaqa.ecd.impl;

import java.util.Set;

/**
 * We may have to implement our own persistence provider here to make sure it works within
 * the Hoop environment and to make sure it works on a cluster running the Hoop assumptions
 * for distributed KV/JCAS propagation
 */
public final class DefaultExperimentPersistenceProvider extends AbstractExperimentPersistenceProvider 
{  
	/**
	 * 
	 */
	@Override
	public void insertExperiment(String id,
								 String name,
								 String author,
								 String configuration,
								 String resource) throws Exception 
	{
		// Do nothing
	}
	/**
	 * 
	 */
	@Override
	public void updateExperimentMeta(String experimentId, int size) 
	{
		// Do nothing
	}
	/**
	 * 
	 */
	@Override
	public void updateExperimentMeta(String experimentId, int size, Set<Integer> topics) 
	{
		// TODO Auto-generated method stub    
	}
}
