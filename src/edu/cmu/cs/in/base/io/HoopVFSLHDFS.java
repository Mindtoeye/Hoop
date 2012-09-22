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

package edu.cmu.cs.in.base.io;

import java.util.ArrayList;

import edu.cmu.cs.in.base.HoopRoot;

/**
 * 
 */
public class HoopVFSLHDFS extends HoopRoot implements HoopVFSLInterface
{
	/**
	 * 
	 */
	public HoopVFSLHDFS ()
	{
		setClassName ("HoopVFSLHDFS");
		debug ("HoopVFSLHDFS ()");		
	}

	@Override
	public StringBuilder getContents() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURI() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setURI(String uRI) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isStreamOpen() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean openStream(String aFileURI) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void closeStream() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToStream(String aContents) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String loadContents(String aFileURI) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveContents(String aFileURI, String aContents) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean appendContents(String aFileURI, String aContents) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createDirectory(String aDirURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeDirectory(String aDirURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesFileExist(String aFileURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<String> listDirectoryEntries(String aDirURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> listFiles(String aDirURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getFileTime(String aPath) {
		// TODO Auto-generated method stub
		return null;
	}
}
