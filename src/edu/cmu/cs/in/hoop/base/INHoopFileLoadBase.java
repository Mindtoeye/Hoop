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

package edu.cmu.cs.in.hoop.base;

import edu.cmu.cs.in.base.INHoopLink;
import edu.cmu.cs.in.base.io.INFileManager;
import edu.cmu.cs.in.base.kv.INKVString;
import edu.cmu.cs.in.hoop.properties.types.INHoopURISerializable;

/**
* 
*/
public class INHoopFileLoadBase extends INHoopLoadBase implements INHoopInterface
{    				
	private INFileManager fManager=null;
	private INKVString fileKV=null;
	
	//private String inputStreamPath="X:\\Echidne\\Hydra (Science)\\Development\\Hoop\\Resources\\ExampleData\\playtennis.arff";
	//private String inputStreamPath="X:\\Echidne\\Hydra (Science)\\Development\\Hoop\\Resources\\ExampleData\\MovieReviews-Full.csv";
	//private String inputStreamPath="C:\\Martin\\Echidne\\Hydra (Science)\\Development\\Hoop\\Resources\\ExampleData\\MovieReviews-Full.csv";
	
	private INHoopURISerializable URI=null;
	
	/**
	 *
	 */
    public INHoopFileLoadBase () 
    {
		setClassName ("INHoopFileLoadBase");
		debug ("INHoopFileLoadBase ()");
		
		setHoopDescription ("Load Text from a File");

		removeInPort ("KV");
		
		fManager=new INFileManager ();
		fileKV=new INKVString ();						
		addKV (fileKV);		
		
		//URI=new INHoopURISerializable (this,"File","<PROJECTPATH>/data/MovieReviews-Full.csv");
		//URI=new INHoopURISerializable (this,"File","C:\\Martin\\Echidne\\Hydra (Science)\\Development\\Hoop\\Resources\\ExampleData\\Agatha Christie - The Mysterious Affair at Styles.txt");
		URI=new INHoopURISerializable (this,"File","<PROJECTPATH>\\Resources\\ExampleData\\Agatha Christie - The Mysterious Affair at Styles.txt");
    }
	/**
	 *
	 */
	public void setContent(String content) 
	{
		fileKV.setValue(content);
	}
	/**
	 *
	 */
	public String getContent() 
	{
		return fileKV.getValue();
	}
	/**
	 *
	 */
	public Boolean runHoop (INHoopBase inHoop)
	{		
		debug ("runHoop ()");
		
		if (URI.getValue().indexOf("<PROJECTPATH>")!=-1)
		{
			if (INHoopLink.project==null)
			{
				this.setErrorString ("You need a project first for this hoop to be useful");
				return (false);
			}
		
			if (INHoopLink.project.getVirginFile()==true)
			{
				this.setErrorString ("You to save your project first for this hoop to be useful");
				return (false);
			}
		}	
		
		String contents=fManager.loadContents(INHoopLink.relativeToAbsolute(URI.getValue()));
		
		if (contents==null)
		{
			this.setErrorString(fManager.getErrorString());
			return (false);
		}	
		
		fileKV.setKeyString(fManager.getURI());
		fileKV.setValue(contents);
		
		return (true);
	}	
	/**
	 *
	 */	
	public void setInputStreamPath(String inputStreamPath) 
	{
		URI.setValue(inputStreamPath);
	}
	/**
	 *
	 */	
	public String getInputStreamPath() 
	{
		return URI.getValue();
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopFileLoadBase ());
	}	
}
