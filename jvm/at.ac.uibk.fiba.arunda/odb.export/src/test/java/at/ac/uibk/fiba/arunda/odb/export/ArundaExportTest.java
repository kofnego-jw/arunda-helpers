package at.ac.uibk.fiba.arunda.odb.export;

import java.io.File;

import at.ac.uibk.fiba.arunda.odb.export.impl.OdbExportServiceImpl;
import at.ac.uibk.fiba.arunda.odb.export.impl.ArundaExport;
import org.apache.commons.io.FileUtils;

import at.ac.uibk.fiba.arunda.odb.api.impl.OdbToHsqldbImpl;
import org.junit.Assume;
import org.junit.Test;

public class ArundaExportTest {
	
	public static final File ARUNDA_FILE = new File("./src/test/resources/arunda/ArundaProjectVers2.odb");
	
	public static final File EXPORT_FILE = new File("./target/arunda.sql");


	@Test
	public void test() throws Exception {
        Assume.assumeTrue(ARUNDA_FILE.exists());
        OdbExportServiceImpl service = new OdbExportServiceImpl();
		service.setOdbToHsqldb(new OdbToHsqldbImpl());
		ArundaExport ae = new ArundaExport(service);
		String text = ae.execute(ARUNDA_FILE);
		FileUtils.write(EXPORT_FILE, text, "utf-8");
	}

}
