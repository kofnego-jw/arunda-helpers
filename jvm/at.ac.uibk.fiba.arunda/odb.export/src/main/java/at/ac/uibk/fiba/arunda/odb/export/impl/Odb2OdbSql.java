package at.ac.uibk.fiba.arunda.odb.export.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.uibk.fiba.arunda.odb.api.OdbException;
import at.ac.uibk.fiba.arunda.odb.api.OdbToHsqldb;

/**
 * Export an odbfile to hsqldb own sql
 * @author joseph
 *
 */
public class Odb2OdbSql {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Odb2OdbSql.class);
	
	protected static final String EXPORT_STATEMENT = "SCRIPT 'LOCATION';";
	
	private OdbToHsqldb odbToHsqldb;
	public void setOdbToHsqldb(OdbToHsqldb o2db) {
		this.odbToHsqldb = o2db;
	}
	
	public void exportToHsqldb(File odbFile, File outputFile) throws OdbException {
		LOGGER.debug("Convert to jdbcUrl for '{}' called.", odbFile.getAbsolutePath());
		String jdbcUrl;
		try {
			jdbcUrl = odbToHsqldb.convertToJdbcUrl(odbFile);
		} catch (IOException e) {
			LOGGER.error("IOException while converting odbfile to jdbcUrl.", e);
			throw new OdbException("Exception during conversion from odbfile to jdbcUrl.", e);
		}
		
		
		executeExport(jdbcUrl, outputFile);
		
	}
	
	protected void executeExport(String jdbcUrl, File output) throws OdbException {
		String location = output.getAbsolutePath();
		String statement = EXPORT_STATEMENT.replace("LOCATION", location);
		
		Connection con = null;
		
		ResultSet rs = null;
				
		try {
			
			LOGGER.debug("Executing statement '{}'.", statement);
			
			Class.forName("org.hsqldb.jdbcDriver");

			con = DriverManager.getConnection(jdbcUrl,
					"sa", "");
			
			Statement s = con.createStatement();

			rs = s.executeQuery(statement);
			

		} catch (Exception e) {
			LOGGER.error("Exception while exporting {} to SQL.", e);
			throw new OdbException("Exception while exporting SQL.", e);
		} finally {
			if (con!=null) {
				try {
					con.close();
				} catch (Exception e2) {
					// ignored
				}
			}
			if (rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
					// ignored
				}
			}
		}
		
		
	}

}
