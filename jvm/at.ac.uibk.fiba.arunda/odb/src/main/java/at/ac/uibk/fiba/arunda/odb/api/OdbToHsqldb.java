package at.ac.uibk.fiba.arunda.odb.api;

import java.io.File;
import java.io.IOException;

/**
 * Converts an ODB Database to a HSQLDB database.
 * @author joseph
 *
 */
public interface OdbToHsqldb {
	
	/**
	 * uses the filename (stripping .odb) as databasename
	 * @param odbFile
	 * @return a jdbc connection url
	 * @throws IOException
	 * @throws OdbException
	 */
	default public String convertToJdbcUrl(File odbFile) throws IOException, OdbException {
		String name = odbFile.getName();
		if (name.toLowerCase().endsWith(".odb")) 
			name = name.substring(0, name.length()-4);
		return convertToJdbcUrl(odbFile, odbFile.getName());
	}
	/**
	 * The odbfile will be converted to a HSQLDB database with the methods described in
	 * http://programmaremobile.blogspot.co.at/2009/01/java-and-openoffice-base-db-through.html
	 * 
	 * @param odbFile
	 * @param databasename
	 * @return an JDBC-URL ("jdbc:hsqldb:file:....") without username and password information.
	 * @throws IOException if any IOException is thrown
	 * @throws OdbException if any other Exception if thrown, e.g. if the file isn't an ODBFile
	 */
	public String convertToJdbcUrl(File odbFile, String databasename) throws IOException, OdbException;
	
	/**
	 * Deletes the files.
	 * @param jdbcUrl
	 * @throws IOException
	 * @throws OdbException
	 */
	public void deleteTemporaryFile(String jdbcUrl) throws IOException, OdbException;

}
