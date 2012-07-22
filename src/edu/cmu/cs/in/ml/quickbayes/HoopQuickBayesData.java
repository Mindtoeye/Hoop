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

package edu.cmu.cs.in.ml.quickbayes;

import java.text.NumberFormat;
import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.ml.quickbayes.HoopQuickBayesAttribute;
import edu.cmu.cs.in.ml.quickbayes.HoopQuickBayesFeature;

/** 
 * @author vvelsen
 */
public class HoopQuickBayesData extends HoopRoot
{
	public ArrayList<HoopQuickBayesFeature>	features=null;
	public ArrayList<ArrayList>data=null;
	public ArrayList<HoopQuickBayesClassify> classifiers=null;
	
	public HoopQuickBayesFeature targetFeature=null;
	public HoopQuickBayesAttribute	majorityAttribute=null;
	//public HoopQuickBayesAttribute minorityAttribute=null;
	
	public boolean	smoothing=false;
	
	int nrFeatures=-1;
	int nrRows=-1;
	
	float prior=0;
	float priorInv=0;
	int majorityTotal=0;
	int majorityCount=0;
	
	/**
	 * 
	 */
	public HoopQuickBayesData () 
	{
		setClassName ("HoopQuickBayesData");
		debug ("HoopQuickBayesData ()");
		
		features=new ArrayList<HoopQuickBayesFeature> ();
		data=new ArrayList<ArrayList> ();
		classifiers=new ArrayList<HoopQuickBayesClassify> ();
	}
	/**
	 * 
	 */
	public HoopQuickBayesFeature getFeature (String a_feature)
	{
		for (int i=0;i<features.size ();i++)
		{
			HoopQuickBayesFeature feature=features.get (i);
			
			if (feature.featureName.equals (a_feature.toLowerCase ())==true)
				return (feature);
		}
		
		return (null);
	}	
	/**
	 * 
	 */
	public int getFeatureIndex (String a_feature)
	{
		for (int i=0;i<features.size ();i++)
		{
			HoopQuickBayesFeature feature=features.get (i);
			
			if (feature.featureName.equals (a_feature.toLowerCase ())==true)
				return (i);
		}
		
		return (-1);
	}
	/**
	 * 
	 */
	public String getDataItem (String a_feature,int a_row)
	{
		int featureIndex=getFeatureIndex (a_feature);
		if (featureIndex==-1)
			return ("undefined");
		
		ArrayList <String> row=(ArrayList) data.get (a_row);
		return (String) (row.get (featureIndex));
	}
	/**
	 * 
	 */
	public void dumpFeatures ()
	{
		debug ("dumpFeatures ()");
		
		 for (int i=0;i<features.size ();i++)
		 {
			 HoopQuickBayesFeature feature=features.get (i);
			
			 if (feature.majorityAttribute==false)
			 {
				 ArrayList instances=feature.instances;
			 
				 for (int j=0;j<instances.size();j++)
				 {
					 HoopQuickBayesAttribute inst=(HoopQuickBayesAttribute) instances.get(j);
					 debug ("Likelihood (majority) for attribute \'"+inst.instanceName + "\' in feature \'" + feature.featureName + "\' is: " + inst.getLikelihoodString() + " and (minority): " + inst.getLikelihoodStringInv());					 
				 }
			 }
		 }	 		
	}
	/**
	 * 
	 */
	private void calculateSmoothing ()
	{
		 for (int i=0;i<features.size ();i++)
		 {
			 HoopQuickBayesFeature feature=features.get (i);
			
			 //if (feature.majorityAttribute==false)
			 //{
				 ArrayList instances=feature.instances;
			 
				 for (int j=0;j<instances.size();j++)
				 {
					 HoopQuickBayesAttribute inst=(HoopQuickBayesAttribute) instances.get(j);
					 inst.countClassifier++;
					 inst.instanceCount+=2;
				 }
			 //}	 
		 }	 				
	}
	/**
	 * 
	 */
	public void prepClassification (String a_class)
	{
	 debug ("prepClassification ()");
	 
	 debug ("Preparing data for class: \'" + a_class + "\'");
	 
	 debug ("Generating unique instance count per instance ...");
	 
	 for (int i=0;i<features.size ();i++)
	 {
		 HoopQuickBayesFeature feature=features.get (i);
		
		 for (int j=0;j<data.size();j++)
		 {
			 feature.addAttribute(getDataItem (feature.featureName,j));
		 }
	 }	 
	 		 		 
	 debug ("Calculating prior likelihood ...");	 
	 
	 HoopQuickBayesFeature tester=getFeature (a_class);
	 if (tester==null)
	 {
		 debug ("Unable to find class feature in data");
		 return;
	 }
	 
	 targetFeature=tester;
	 targetFeature.majorityAttribute=true;
	 
	 debug ("Finding majority attribute in class feature ...");
	 
	 majorityCount=0;
	 	 
	 majorityAttribute=null;
	 	 
	 for (int k=0;k<targetFeature.instances.size();k++)
	 {
		 HoopQuickBayesAttribute target=targetFeature.instances.get(k);
		 
		 majorityTotal+=target.instanceCount;
		 
		 if (target.instanceCount>majorityCount)
		 {
			 majorityCount=target.instanceCount;
			 majorityAttribute=target;
			 majorityAttribute.majorityAttribute=true;
		 }
	 }
	 
	 if (majorityAttribute==null)
	 {
		 debug ("Internal error: no target feature found in data");
		 return;
	 }
	 
	 prior=((float) majorityCount / (float) majorityTotal);
	 priorInv=((float) (majorityTotal-majorityCount) / (float) majorityTotal);
	 	 
	 debug ("Target class has count " + majorityCount + " instances with \'" + majorityAttribute.instanceName + "\' out of " + majorityTotal + " is: " + prior + " inv: " + priorInv);
 	 	 
	 debug ("Generating likelihoods for remaining features and their instances...");	 
	 
	 int indexer=0;
	 
	 for (int l=0;l<data.size();l++)
	 {
		 String correctInstance=getDataItem (a_class,l);
		 if (correctInstance.equals(majorityAttribute.instanceName)==true)
		 {
			 //debug ("Processing attributes ["+indexer+"] on line ["+l+"] for: " + a_class + "="+majorityAttribute.instanceName);
			 
			 for (int m=0;m<features.size();m++)
			 {
				 HoopQuickBayesFeature feat=features.get (m);
				if (feat.majorityAttribute==false)
				{
				 String upper=getDataItem (feat.featureName,l);
				 HoopQuickBayesAttribute inst=feat.findAttribute(upper);
				 inst.countClassifier++;
				}
			 }
			 
			 indexer++; 
		 }
	 }	 
	 
	 debug ("Before smoothing ...");
	 
	 dumpFeatures ();
	 
	 if (smoothing==true)
	 {
		 debug ("Applying smoothing ...");
		 calculateSmoothing ();
	 }	 
	 
	 dumpFeatures ();	 
	}
	/**
	 * 
	 */
	private void addClassify (String a_feature,String an_attribute)
	{
		HoopQuickBayesClassify newClassify		=new HoopQuickBayesClassify ();
		newClassify.featureName		=a_feature.toLowerCase ();
		newClassify.attributeName	=an_attribute.toLowerCase ();
		classifiers.add (newClassify);
	}
	/**
	 * 
	 */
	public void classify (String an_operation)
	{
		debug ("classify ("+an_operation+")");
	 
		String separator[]=an_operation.split(",");
	 
		for (int i=0;i<separator.length;i++)
		{
			String fseparator []=separator [i].split ("=");
		 
			addClassify (fseparator [0],fseparator [1]);		 
		}
	 
		//>------------------------------------------------------------------------
	 
		debug ("Running classification for majority attribute in class feature...");
	 	 
		for (int j=0;j<classifiers.size();j++)
		{
			HoopQuickBayesClassify cl=classifiers.get(j);
			HoopQuickBayesFeature feature=getFeature (cl.featureName);
			if (feature!=null)
			{
				HoopQuickBayesAttribute inst=feature.findAttribute (cl.attributeName);
				if (inst!=null)
				{				 
					cl.value=inst.getLikelihood();
					cl.valueString=inst.getLikelihoodString();
				}
				else
				{
					cl.value=(float) 1.0;
					cl.valueString="missing";
					debug ("Internal error: unable to find attribute " + cl.attributeName + " in feature: " + cl.featureName);
				}
			}
			else
				debug ("Internal error: unable to find feature: " + cl.featureName + "in data");
		}
	 
		StringBuffer formatted=new StringBuffer ();
	 
		float total=0;
	 
		for (int k=0;k<classifiers.size();k++)
		{
			HoopQuickBayesClassify cl=classifiers.get(k);
			formatted.append("(");
			formatted.append(cl.valueString);
			formatted.append(") * ");
		 
			if (k==0)
				total=cl.value;
			else
				total=total*cl.value;
		}
	 	 
		formatted.append("prior: (");
		formatted.append (majorityCount + "/" + majorityTotal);
		formatted.append(")");
	 
		total=(total*prior);
	 
		debug ("Likelihood: " + formatted.toString() + " = " + total + " for majority attribute: " + majorityAttribute.instanceName + " in feature: " + targetFeature.featureName);
	 
		//>------------------------------------------------------------------------
	 
		debug ("Running classification for majority attribute in class feature...");
	 	 
		for (int l=0;l<classifiers.size();l++)
		{
			HoopQuickBayesClassify cl=classifiers.get(l);
			HoopQuickBayesFeature feature=getFeature (cl.featureName);
			
			if (feature!=null)
			{
				HoopQuickBayesAttribute inst=feature.findAttribute (cl.attributeName);
				if (inst!=null)
				{				
					cl.value=inst.getLikelihood();
					cl.valueString=inst.getLikelihoodString(); 
				}
				else
				{
					cl.value=(float) 1.0;
					cl.valueString="missing";				 
					debug ("Internal error: unable to find attribute " + cl.attributeName + " in feature: " + cl.featureName);
				}
			}
			else
				debug ("Internal error: unable to find feature: " + cl.featureName + "in data");
		}
	 
		formatted=new StringBuffer ();
	 
		float totalInv=0;
	 
		for (int m=0;m<classifiers.size();m++)
		{
			HoopQuickBayesClassify cl=classifiers.get(m);
			formatted.append("(");
			formatted.append(cl.valueString);
			formatted.append(") * ");
		 
			if (m==0)
				total=cl.value;
			else
				total=total*cl.value;
		}
	 	 
		formatted.append("prior: (");
		formatted.append ((majorityTotal-majorityCount) + "/" + majorityTotal);
		formatted.append(")");
	 
		totalInv=(total*priorInv);
	 
		debug ("Likelihood: " + formatted.toString() + " = " + total + " for minority attribute in feature: " + targetFeature.featureName);
	 
		NumberFormat nf = NumberFormat.getPercentInstance();
     	 
		debug ("Normalized majority: " + (nf.format(total/(total+totalInv))));
		debug ("Normalized minority: " + (nf.format(totalInv/(totalInv+total))));	 	 	 
	}		
}
