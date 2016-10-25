package at.ac.uibk.fiba.arunda.odb.export;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import at.ac.uibk.fiba.arunda.odb.api.impl.OdbToHsqldbImpl;
import at.ac.uibk.fiba.arunda.odb.export.impl.Odb2OdbSql;

public class OdbExportSqlTest {
	
	private static final File ODB_FILE = new File("./src/test/resources/testdb3.odb");
	
	private static final File OUTPUT_FILE = new File("./src/test/resources/hsqldbout.sql");
	
	@Before
	public void deleteFile() throws Exception {
		if (OUTPUT_FILE.exists())
			OUTPUT_FILE.delete();
	}
	
	@Test
	public void test() throws Exception {
		OdbToHsqldbImpl o2hsqldb = new OdbToHsqldbImpl();
		Odb2OdbSql o = new Odb2OdbSql();
		o.setOdbToHsqldb(o2hsqldb);
		o.exportToHsqldb(ODB_FILE, OUTPUT_FILE);
	}

}
