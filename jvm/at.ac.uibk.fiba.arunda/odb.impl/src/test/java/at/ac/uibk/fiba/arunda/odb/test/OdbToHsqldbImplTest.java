package at.ac.uibk.fiba.arunda.odb.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.junit.Test;

import at.ac.uibk.fiba.arunda.odb.api.OdbToHsqldb;
import at.ac.uibk.fiba.arunda.odb.api.impl.OdbToHsqldbImpl;

public class OdbToHsqldbImplTest {
	
	public static final File ODB_FILE = new File("src/test/resources/testdb.odb");
	
	@Test
	public void test() throws Exception {
		
		Class.forName("org.hsqldb.jdbcDriver");
		
		OdbToHsqldb convert = new OdbToHsqldbImpl();
		
		
		String jdbcUrl = convert.convertToJdbcUrl(ODB_FILE, "testMeMe");
		
		System.out.println(jdbcUrl);
		
		Connection con = DriverManager.getConnection(jdbcUrl,"sa","");
		
		Statement statement = con.createStatement();
		ResultSet results = statement.executeQuery(
				"select message.\"MessageID\", message.\"MessageContent\","
				+ "sender.\"Username\" as Sender, recipient.\"Username\" as Recipient "
				+ "from \"Messages\" as message "
				+ "join \"UserTable\" as sender on message.\"Sender\" = sender.\"UserID\" "
				+ "join \"UserTable\" as recipient on message.\"Recipient\" = recipient.\"UserID\" "
				+ "where sender.\"Username\" like '%andy%' or recipient.\"Username\"  like '%andy%'");
		ResultSetMetaData meta = results.getMetaData();
		System.out.println("Columns count: " + meta.getColumnCount());
		for (int i=1; i<=meta.getColumnCount(); i++) {
			System.out.print(meta.getColumnLabel(i) + "\t");
		}
		
		System.out.println();
		while (results.next()) {
			
			for (int i=1; i<=meta.getColumnCount();i++) {
				System.out.print(results.getString(i) + "\t");
			}
			System.out.println();
		}
		
		results.close();
		con.close();
		
		convert.deleteTemporaryFile(jdbcUrl);
		
	}

}
