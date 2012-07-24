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

package edu.cmu.cs.in.hoop.hoops.load;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;

import java.util.ArrayList;

import edu.cmu.cs.in.base.kv.HoopKVInteger;
import edu.cmu.cs.in.hoop.hoops.base.HoopBase;
import edu.cmu.cs.in.hoop.hoops.base.HoopInterface;
import edu.cmu.cs.in.hoop.hoops.base.HoopLoadBase;
import edu.cmu.cs.in.hoop.properties.types.HoopStringSerializable;

/**
 * 
 */
public class HoopMySQLReader extends HoopLoadBase implements HoopInterface
{
	public	String driver = "com.mysql.jdbc.Driver";
    public	String driverAlias="mysql";
    public	String protocol ="composite";
    
    public	HoopStringSerializable username=null;
    public	HoopStringSerializable password=null;
    public	HoopStringSerializable dbName=null;
    public	HoopStringSerializable dbServer=null;
    
    public	HoopStringSerializable query=null;
    public	HoopStringSerializable queryColumns=null;
    
    private Connection connection = null;
    
    private ArrayList <String> tables=null;
    
    /**
     * 
     */
    public HoopMySQLReader ()
    {
		setClassName ("HoopMySQLReader");
		debug ("HoopMySQLReader ()");
		
		setHoopDescription ("Load KVs from a MySQL Database");

		removeInPort ("KV");
    	
    	username=new HoopStringSerializable (this,"username","root");
    	password=new HoopStringSerializable (this,"password","");
    	dbName=new HoopStringSerializable (this,"dbName","default");
    	dbServer=new HoopStringSerializable (this,"dbServer","127.0.0.1");
    	    	
    	query=new HoopStringSerializable (this,"query","");
    	queryColumns=new HoopStringSerializable (this,"queryColumns","");
    }
    /**
     * 
     */
	public String getUsername() 
	{
		return username.getValue();
	}
    /**
     * 
     */	
	public void setUsername(String aValue) 
	{
		this.username.setValue(aValue);
	}
    /**
     * 
     */	
	public String getPassword() 
	{
		return password.getValue();
	}
    /**
     * 
     */	
	public void setPassword(String aValue) 
	{
		this.password.setValue (aValue);
	}    
	/**
	 * 
	 * @return
	 */
	public String getDbName() 
	{
		return dbName.getValue();
	}
	/**
	 * 
	 */
	public void setDbName(String aValue) 
	{
		this.dbName.setValue(aValue);
	}
	/**
	 * 
	 */
	public Connection getConnection() 
	{
		return connection;
	}
	/**
	 * 
	 */
	public void setConnection(Connection conn) 
	{
		this.connection = conn;
	}	
    /**
     * Loads the appropriate JDBC driver for this environment/framework. For
     * example, if we are in an embedded environment, we load Derby's
     * embedded Driver, <code>org.apache.derby.jdbc.EmbeddedDriver</code>.
     * 
     * The JDBC driver is loaded by loading its class.
     * If you are using JDBC 4.0 (Java SE 6) or newer, JDBC drivers may
     * be automatically loaded, making this code optional.
     * 
     * In an embedded environment, this will also start up the Derby
     * engine (though not any databases), since it is not already
     * running. In a client environment, the Derby engine is being run
     * by the network server framework.
     * 
     * In an embedded environment, any static Derby system properties
     * must be set before loading the driver to take effect.
     */
    private Boolean loadDriver() 
    {
    	debug ("loadDriver ()");
    	
    	protocol="jdbc:"+driverAlias+"://"+dbServer.getValue()+":3306/mysql";
    	
        try 
        {
            Class.forName(driver).newInstance();
            debug ("Loaded the appropriate driver");
        } 
        catch (ClassNotFoundException cnfe) 
        {
            debug ("Unable to load the JDBC driver " + driver +". Please check your CLASSPATH.");
            this.setErrorString("Unable to load the JDBC driver " + driver +". Please check your CLASSPATH.");
            cnfe.printStackTrace(System.err);
            return (false);
        } 
        catch (InstantiationException ie) 
        {
            debug ("Unable to instantiate the JDBC driver " + driver);
            this.setErrorString("Unable to instantiate the JDBC driver " + driver);
            ie.printStackTrace(System.err);
            return (false);
        } 
        catch (IllegalAccessException iae) 
        {
            debug ("Not allowed to access the JDBC driver " + driver);
            this.setErrorString("Not allowed to access the JDBC driver " + driver);
            iae.printStackTrace(System.err);
            return (false);
        }
        
        return (true);
    }
	/**
	 * http://dev.mysql.com/doc/refman/5.0/en/connector-j-reference-configuration-properties.html
	 */
	private Boolean connect ()
	{
		debug ("connect ()");
		
		//protocol.setValue("jdbc:"+driverAlias+"://"+dbServer.getValue()+":3306/"+dbName.getValue());
		
		protocol="jdbc:"+driverAlias+"://"+dbServer.getValue()+":3306/mysql";
		
        try 
        {
			connection = DriverManager.getConnection (protocol,username.getValue(),password.getValue());
		} 
        catch (SQLException e) 
        {
        	this.setErrorString("Error connecting to database");
			e.printStackTrace();
			return (false);
		}

        debug ("Connected to and created database " + dbName.getValue());
        
        return (true);
	}    
	/**
	 * http://www.kitebird.com/articles/jdbc.html
	 */
	public Boolean runHoop (HoopBase inHoop)
	{		
		debug ("runHoop ()");
			
		if (dbName.getValue().isEmpty()==true)
		{
			this.setErrorString("Please provide a database name");
			return (false);
		}
		
		if (username.getValue().isEmpty()==true)
		{
			this.setErrorString("Please provide a username for the database");
			return (false);
		}
		
		if (password.getValue().isEmpty()==true)
		{
			this.setErrorString("Please provide a password for the username");
			return (false);
		}		
		
		if (loadDriver()==false)
		{
			return (false);
		}
		
		if (connect ()==false)
		{
			return (false);
		}
		
		if (getTables ()!=null)
		{
			for (int i=0;i<tables.size();i++)
			{			
				HoopKVInteger sentenceKV=new HoopKVInteger ();
				sentenceKV.setKey(i+1);
				sentenceKV.setValue(tables.get(i));
	
				addKV (sentenceKV);
			}	
		}
		
		runQuery ();
		
		close ();
		
		return (true);
	}	
	/**
	 * TABLE_CAT String => table catalog (may be null)
	 * TABLE_SCHEM String => table schema (may be null)
	 * TABLE_NAME String => table name
	 * TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
	 * REMARKS String => explanatory comment on the table
	 * TYPE_CAT String => the types catalog (may be null)
	 * TYPE_SCHEM String => the types schema (may be null)
	 * TYPE_NAME String => type name (may be null)
	 * SELF_REFERENCHoopG_COL_NAME String => name of the designated "identifier" column of a typed table (may be null)
	 * REF_GENERATION String => specifies how values in SELF_REFERENCHoopG_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null) 
	 */
	private ArrayList <String> getTables ()
	{
		debug ("getTables ()");
		
		tables=new ArrayList<String> ();
		
		DatabaseMetaData metadata=null;
		
		try 
		{
			metadata = connection.getMetaData();
		} 
		catch (SQLException e) 
		{
			//e.printStackTrace();
			printSQLException (e);
			this.setErrorString("Error getting metadata from database");
			return (null);
		}
		
		ResultSet tableSet=null;
		
		try 
		{
			tableSet=metadata.getTables(null, null, null, new String[]{"TABLE"});
		} 
		catch (SQLException e) 
		{
			printSQLException (e);
			return (null);
		}
				
		try 
		{
			while (tableSet.next()) 
			{
				String tableType=null;
				
				try 
				{
					tableType = tableSet.getString("TABLE_NAME");
					
					tables.add(tableType);
				} 
				catch (SQLException e) 
				{
					printSQLException (e);
				}
				
				debug (" " + tableType);
			}
		} 
		catch (SQLException e1) 
		{
			printSQLException (e1);
			return (null);
		}		
		
		try 
		{
			tableSet.close();
		} 
		catch (SQLException e) 
		{		
			printSQLException (e);
			return (null);
		}
				
		return (tables);
	}
	/**
	 * http://www.kitebird.com/articles/jdbc.html
	 * 
	 * For example: s.executeQuery ("SELECT id, name, category FROM animal");
	 */
	private Boolean runQuery ()
	{
		debug ("runQuery ()");
		
		if (connection==null)
		{
			this.setErrorString("Error: no connection to database yet");
			return (false);
		}
		
		if (query.getValue().isEmpty()==true)
		{
			debug ("Error: no SQL query provided");
			return (false);
		}
		
		if (queryColumns.getValue().isEmpty()==true)
		{
			debug ("Error: no target columns provided for SQL query");
			return (false);
		}		
		
		Statement s=null;
		
		try 
		{
			s = connection.createStatement ();
		} 
		catch (SQLException e) 
		{
			printSQLException (e);
		}
		
		try 
		{
			s.executeQuery (query.getValue());
		} 
		catch (SQLException e) 
		{
			printSQLException (e);
		}
		
		ResultSet rs=null;
		
		try 
		{
			rs = s.getResultSet ();
		} 
		catch (SQLException e1) 
		{		
			printSQLException (e1);
		}
		
		int count = 0;
		
		try 
		{
			while (rs.next ())
			{
				int idVal = rs.getInt ("id");
				String nameVal = rs.getString ("name");
				String catVal = rs.getString ("category");
				System.out.println ("id = " + idVal + ", name = " + nameVal + ", category = " + catVal);
				++count;
			}
		} 
		catch (SQLException e1) 
		{		
			printSQLException (e1);
		}

		try 
		{
			rs.close ();
		} 
		catch (SQLException e) 
		{		
			printSQLException (e);
		}
		
		try 
		{
			s.close ();
		} 
		catch (SQLException e) 
		{
			printSQLException (e);
		}
		
		debug (count + " rows were retrieved");
		
		return (true);
	}
	/**
	 * 
	 */
	private Boolean close ()
	{
		debug ("close ()");
		
		if (connection!=null)
		{
			try 
			{
				connection.close();
			} 
			catch (SQLException e) 
			{
				this.setErrorString("Error: Unable to close connection to database");
				printSQLException (e);
				return (false);
			}
		}
		
		return (true);
	}
	/**
	 * 
	 */
	public HoopBase copy ()
	{
		return (new HoopMySQLReader ());
	}		    
    /**
     * Prints details of an SQLException chain to <code>System.err</code>.
     * Details included are SQL State, Error code, Exception message.
     *
     * @param e the SQLException from which to print details.
     */
    protected void printSQLException (SQLException e)
    {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
    	
        while (e != null)
        {
            debug ("----- SQLException -----");
            debug ("  SQL State:  " + e.getSQLState());
            debug ("  Error Code: " + e.getErrorCode());
            debug ("  Message:    " + e.getMessage());
            
            this.setErrorString("SQL State: " + e.getSQLState() + ", Error Code: " + e.getErrorCode()+ ", Message: " + e.getMessage());
            
            // for stack traces, refer to derby.log or uncomment this:
            e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }	   
}
