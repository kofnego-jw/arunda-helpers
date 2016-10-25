package at.ac.uibk.fiba.arunda.odb.export;

import java.io.File;

import at.ac.uibk.fiba.arunda.odb.export.impl.OdbSql2MysqlSql;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;


public class TranslatorTest {
	
	public static final File ODBSQL = new File("./src/test/resources/s2_orig.sql");
	
	public static final File OUTPUT = new File("./src/test/resources/s2_conv.sql");
	
	static OdbSql2MysqlSql conv;
	
	@BeforeClass
	public static void setUpConverter() {
		conv = new OdbSql2MysqlSql();
	}
	
	@Test
	public void test02() throws Exception {
		String test = FileUtils.readFileToString(ODBSQL, "utf-8");
		String converted = conv.convert(test);
		System.out.println(converted);
		FileUtils.write(OUTPUT, converted, "utf-8");
	}
	

}
