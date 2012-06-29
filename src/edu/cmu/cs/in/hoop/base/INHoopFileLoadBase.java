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

import edu.cmu.cs.in.base.io.INFileManager;
import edu.cmu.cs.in.base.INKVInteger;
import edu.cmu.cs.in.base.INKVString;

/**
* 
*/
public class INHoopFileLoadBase extends INHoopLoadBase implements INHoopInterface
{    				
	private INFileManager fManager=null;
	private INKVString fileKV=null;
	private String inputStreamPath="X:\\Echidne\\Hydra (Science)\\Development\\Hoop\\Resources\\ExampleData\\playtennis.arff";
	//private String inputStreamPath="X:\\Echidne\\Hydra (Science)\\Development\\Hoop\\Resources\\ExampleData\\MovieReviews-Full.csv";
	//private String inputStreamPath="C:\\Martin\\Echidne\\Hydra (Science)\\Development\\Hoop\\Resources\\ExampleData\\MovieReviews-Full.csv";
	
	/**
	 *
	 */
    public INHoopFileLoadBase () 
    {
		setClassName ("INHoopFileLoadBase");
		debug ("INHoopFileLoadBase ()");
		
		setHoopDescription ("Load From File");

		removeInPort ("KV");
		
		fManager=new INFileManager ();
		fileKV=new INKVString ();						
		addKV (fileKV);		
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
								
		String contents=fManager.loadContents(inputStreamPath);
		
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
		this.inputStreamPath = inputStreamPath;
	}
	/**
	 *
	 */	
	public String getInputStreamPath() 
	{
		return inputStreamPath;
	}	
	/**
	 * 
	 */
	public INHoopBase copy ()
	{
		return (new INHoopFileLoadBase ());
	}	
}
