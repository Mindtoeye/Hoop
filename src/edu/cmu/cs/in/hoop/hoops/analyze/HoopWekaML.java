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

package edu.cmu.cs.in.hoop.hoops.analyze;

import java.util.ArrayList;

import edu.cmu.cs.in.base.kv.HoopKV;
import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopAnalyze;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;

import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

/**
* http://weka.wikispaces.com/Use+WEKA+in+your+Java+code
* 
* http://weka.wikispaces.com/Programmatic+Use
* 
* Just to get going this Hoop assumes that data to be classified has
* their class as either the key or a predefined value index. So in
* other words you could have a list of KV entries with a boolean as
* a key which represents the class. But you could also have a list
* of KVs where the key is an index and the class is the value found
* in column 1 (which corresponds to the primary value) Other index
* can also be used but are not recommended since data might be
* asymmetrical.
*/
public class HoopWekaML extends HoopAnalyze implements HoopInterface
{    					
	private static final long serialVersionUID = 3391350057720222671L;
	
	/**
	 *
	 */
    public HoopWekaML () 
    {
		setClassName ("HoopWekaML");
		debug ("HoopWekaML ()");
				
		removeOutPort ("KV");
		
		setHoopDescription ("Run Weka Machine Learning");
		
		String[] options = new String[1];
		options[0] = "-U";            // unpruned tree
		J48 tree = new J48();         // new instance of tree
		 
		try 
		{
			tree.setOptions(options);
		} 
		catch (Exception e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		// Declare a nominal attribute along with its values
		FastVector fvNominalVal = new FastVector(3);
		fvNominalVal.addElement("blue");
		fvNominalVal.addElement("gray");
		fvNominalVal.addElement("black");
		 
		// Declare the class attribute along with its values
		FastVector fvClassVal = new FastVector(2);
		fvClassVal.addElement("positive");
		fvClassVal.addElement("negative");
		Attribute ClassAttribute = new Attribute("theClass", fvClassVal);
		 
		// Declare two numeric attributes
		Attribute Attribute1 = new Attribute("firstNumeric");
		Attribute Attribute2 = new Attribute("secondNumeric");		 		 		 
		Attribute Attribute3 = new Attribute("aNominal", fvNominalVal);
		 		 
		// Declare the feature vector
		FastVector fvWekaAttributes = new FastVector(4);
		fvWekaAttributes.addElement(Attribute1);    
		fvWekaAttributes.addElement(Attribute2);    
		fvWekaAttributes.addElement(Attribute3);    
		fvWekaAttributes.addElement(ClassAttribute);		 
		 
		// Create an empty training set
		Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);
		 
		// Set class index
		isTrainingSet.setClassIndex(3);
		 
		try 
		{
			tree.buildClassifier(isTrainingSet);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
    }
	/**
	 *
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
			
		ArrayList <HoopKV> inData=inHoop.getData();
		
		if (inData!=null)
		{
			//StringBuffer formatted=new StringBuffer ();
			
			for (int t=0;t<inData.size();t++)
			{
				HoopKVInteger aKV=(HoopKVInteger) inData.get(t);
								
				//formatted.append(aKV.getKeyString ());
				
				ArrayList<Object> vals=aKV.getValuesRaw();
				
				for (int i=0;i<vals.size();i++)
				{
					//formatted.append(",");
					//formatted.append(vals.get(i));
				}
				
				//formatted.append("\n");
				
				updateProgressStatus (t,inData.size());
			}			
		}	
						
		return (true);				
	}	 
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopWekaML ());
	}	
}
