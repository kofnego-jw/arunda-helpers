package at.ac.uibk.fiba.arunda.odb.export;

import java.io.File;

import at.ac.uibk.fiba.arunda.odb.export.impl.OdbExportServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.uibk.fiba.arunda.odb.api.OdbToHsqldb;
import at.ac.uibk.fiba.arunda.odb.api.impl.OdbToHsqldbImpl;

public class OdbExportServiceTest {
	
	private static final File ODB_FILE = new File("./src/test/resources/arunda/ArundaProjectVers2.odb");
	
	private static final File HSQL_SQLFILE = new File("./src/test/resources/hsqldb.sql");
	
	private static final File MYSQL_SQLFILE = new File("./src/test/resources/mysql.sql");
	
	private static OdbExportService service;
	
	@BeforeClass
	public static void setupService() throws Exception {
		Assume.assumeTrue(ODB_FILE.exists());
		OdbExportServiceImpl impl = new OdbExportServiceImpl();
		OdbToHsqldb o2db = new OdbToHsqldbImpl();
		impl.setOdbToHsqldb(o2db);
		service = impl;
	}
	
	@Test
	public void test() throws Exception {
		String h = service.convertOdb(ODB_FILE, OdbExportFormat.HSQLDB_SQL);
		FileUtils.write(HSQL_SQLFILE, h, "utf-8");
	}
	
	@Test
	public void test2() throws Exception {
		String m = service.convertOdb(ODB_FILE, OdbExportFormat.MYSQL_SQL);
		FileUtils.write(MYSQL_SQLFILE, m, "utf-8");
	}
	

}
