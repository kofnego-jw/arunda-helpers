package at.ac.uibk.fiba.arunda.odb.export.impl;

import at.ac.uibk.fiba.arunda.odb.api.OdbException;
import at.ac.uibk.fiba.arunda.odb.api.OdbToHsqldb;
import at.ac.uibk.fiba.arunda.odb.export.OdbExportService;
import at.ac.uibk.fiba.arunda.odb.export.OdbExportFormat;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class OdbExportServiceImpl implements OdbExportService {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OdbExportServiceImpl.class);
	
	private static final String TEMPFILE_PREFIX = "odbexport";
	private static final String TEMPFILE_SUFFIX = ".sql";
	
	private OdbSql2MysqlSql toMysqlConverter = new OdbSql2MysqlSql();
	
	private Odb2OdbSql odb2odbsql;
	
	public void setOdbToHsqldb(OdbToHsqldb o2db) {
		if (this.odb2odbsql == null) 
			this.odb2odbsql = new Odb2OdbSql();
		this.odb2odbsql.setOdbToHsqldb(o2db);
	}

	protected File createTempFile() throws IOException {
		return File.createTempFile(TEMPFILE_PREFIX, TEMPFILE_SUFFIX);
	}
	
	@Override
	public String convertOdb(File odbFile, OdbExportFormat format)
			throws OdbException {
		
		LOGGER.debug("Convert {} to SQL.");
		
		File hsqldbSql;
		try {
			hsqldbSql = createTempFile();
			hsqldbSql.delete();
		} catch (Exception e) {
			LOGGER.error("Cannot create temporary file.", e);
			throw new OdbException("Cannot create temporary file.", e);
		}
		
		this.odb2odbsql.exportToHsqldb(odbFile, hsqldbSql);
		
		String hSql;
		try {
			hSql = FileUtils.readFileToString(hsqldbSql, "UTF-8");
		} catch (Exception e) {
			LOGGER.error("Cannot read from hsqldb export file.", e);
			throw new OdbException("Cannot read from hsqldb export file.", e);
		}
		
		if (format==OdbExportFormat.HSQLDB_SQL)
			return hSql;
		
		return this.toMysqlConverter.convert(hSql);
	}
	
}
