package at.ac.uibk.fiba.arunda.odb.export.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.ac.uibk.fiba.arunda.odb.api.OdbException;

/**
 * Conversion Class, takes a HSQLDB SQL Statements and converts it to MYSQL SQL statements.
 * @author joseph
 *
 */
public class OdbSql2MysqlSql {
	
	/**
	 * Convert a whole input of HSQLDB SQL Statements to Mysql SQL statements
	 * @param input
	 * @return
	 * @throws OdbException
	 */
	public String convert(String input) throws OdbException {
		
		// to lines
		
		String[] lines = input.split("\\r?\\n");
		
		// Add ';'
		
		String prelist = Stream.of(lines)
				.map(l -> conv(l))
				.collect(Collectors.joining("\n"));
		
		List<String> list = Arrays.asList(prelist.split("\\n"));
		
		List<String> alterStatements = list.stream().filter(l -> l.startsWith("ALTER "))
				.collect(Collectors.toList());
		
		List<String> otherStatements = 
				list.stream().filter(l -> !l.startsWith("ALTER "))
				.collect(Collectors.toList());
		
		List<String> together = new ArrayList<>(list.size());
		together.addAll(otherStatements);
		together.addAll(alterStatements);
		return together.stream().collect(Collectors.joining("\n"));
	}
	
	/**
	 * Convert a line 
	 * @param l
	 * @return
	 */
	protected static String conv(String l) {
		
		
		if (l==null || l.isEmpty())
			return "";
		
		l = l + ";";
		
		// If the line is one of HSQLDB own lines, eliminate
		// All SET, CREATE USER, CREATE SCHEMA, GRANT statements
		
		if (shouldEliminate(l))
			return "# " + l;
		
		// Change the table statements
		
		if (l.startsWith("CREATE ")) {
			l = eliminateCached(l);
			l = changeToAutoIncrement(l);
			l = changePrimaryKey(l);
			l = changeConstraint(l);
		}
		
		
		if (l.startsWith("ALTER TABLE")) {
			l = changeAutoIncrement(l);
		}
		
		// change the unicodes
		
		l = returnToUnicode(l);
		
		l = convertQuoteSign(l);
		
		return l;
	}
	
	/**
	 * @param l
	 * @return "ALTER COLUMN RESTART WITH..." to ALTER TABLE MODIFY...
	 */
	protected static String changeAutoIncrement(String l) {
		Pattern pat = Pattern.compile("ALTER TABLE \"([^\"]+)\" ALTER COLUMN \"([^\"]+)\" RESTART WITH (\\d+)");
		Matcher mat = pat.matcher(l);
		if (mat.find()) {
			return "ALTER TABLE `" + mat.group(1)
					+ "` MODIFY `" + mat.group(2) 
					+ "` INTEGER NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=" + mat.group(3)
					+ ";";
		}
		return l;
	}
	
	/**
	 * 
	 * @param l
	 * @return l, with double quotation mark replaced with `
	 */
	protected static String convertQuoteSign(String l) {
		return l.replace('\"', '`');
	}
	
	/**
	 * 
	 * @param l
	 * @return l replace \\uXXXX to the corresponding unicode characters
	 */
	protected static String returnToUnicode(String l) {
		Pattern pat = Pattern.compile("(?<!\\\\)\\\\u(.{4})");
		Matcher mat = pat.matcher(l);
		StringBuffer sb = new StringBuffer();
		while (mat.find()) {
			mat.appendReplacement(sb, changeToUnicode(mat.group(1)));
		}
		mat.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * Change a 4 byte code (e.g. "4bfc") to its corresponding Unicode character
	 * @param code
	 * @return
	 */
	protected static String changeToUnicode(String code) {
		
		int charcode = Integer.parseInt(code, 16);
		
		return Character.toString((char) charcode);
	}
	
	/**
	 * 
	 * @param l
	 * @return l with no CONSTRAINT FOREIGN KEY... but an additional "ALTER TABLE" statement
	 */
	protected static String changeConstraint(String l) {
		Pattern tableNamePat = Pattern.compile("CREATE TABLE \"([^ (]+)\"");
		Matcher matName = tableNamePat.matcher(l);
		String tableName = matName.find() ? matName.group(1) : "";
		if (tableName.isEmpty()) return l;
		
		
		Pattern pat = Pattern.compile(",\\s*CONSTRAINT ([^ ]+) "
				+ "FOREIGN KEY\\s*\\(\"([^\"]+)\"\\) "
				+ "REFERENCES \"([^\"]+)\"\\(\"([^\"]+)\"\\)");
		Matcher mat = pat.matcher(l);
		StringBuffer sb = new StringBuffer();
		StringBuilder add = new StringBuilder();
		
		while(mat.find()) {
			mat.appendReplacement(sb, "");
			
			add.append("\n")
				.append("ALTER TABLE `").append(tableName).append("` ")
				.append("ADD CONSTRAINT `").append(mat.group(1)).append("` ")
				.append("FOREIGN KEY(`").append(mat.group(2)).append("`) ")
				.append("REFERENCES `").append(mat.group(3)).append("`(`")
				.append(mat.group(4)).append("`);");
			
		}
		mat.appendTail(sb);
		
		
		return sb.toString() + add.toString();
	}
	
	/**
	 * 
	 * @param l
	 * @return l with no "PRIMARY KEY" but with an additional "ALTER TABLE" statement
	 */
	protected static String changePrimaryKey(String l) {
		Pattern tableNamePat = Pattern.compile("CREATE TABLE \"([^ (]+)\"");
		Matcher matName = tableNamePat.matcher(l);
		String tableName = matName.find() ? matName.group(1) : "";
		if (tableName.isEmpty()) return l;
		
		
		Pattern pat = Pattern.compile("\"([^\"]+)\"([^,\"]*)PRIMARY KEY([^,]*)");
		Matcher mat = pat.matcher(l);
		StringBuffer sb = new StringBuffer();
		StringBuilder add = new StringBuilder();
		while (mat.find()) {
			mat.appendReplacement(sb, "\"$1\"$2 $3");
			add.append("\n");
			add.append("ALTER TABLE `").append(tableName)
				.append("` ADD PRIMARY KEY(`").append(mat.group(1)).append("`);");
		}
		mat.appendTail(sb);
		return sb.toString() + add.toString();
	}
	
	/**
	 * 
	 * @param l
	 * @return l with " GENERATED BY DEFAULT AS... " deleted
	 */
	protected static String changeToAutoIncrement(String l) {
		
		// The start value can be ignored. There is an alter table statement anyway.
		if (l.contains(" GENERATED BY DEFAULT AS IDENTITY(START WITH ")) {
			Pattern pat = Pattern.compile("\\sGENERATED BY DEFAULT AS IDENTITY\\(START WITH (\\d+)\\)\\s");
			Matcher mat = pat.matcher(l);
			StringBuffer sb = new StringBuffer();
			List<Integer> startValue = new ArrayList<>();
			while (mat.find()) {
				mat.appendReplacement(sb, " ");
				startValue.add(Integer.parseInt(mat.group(1)));
			}
			mat.appendTail(sb);
			return sb.toString();
		}
		return l;
	}
	
	/**
	 * 
	 * @param l
	 * @return l replace "CREATE CACHED" with "CREATE "
	 */
	protected static String eliminateCached(String l) {
		return l.replaceFirst("CREATE CACHED ", "CREATE ");
	}
	
	
	/**
	 * 
	 * @param l a HSQLDB SQL statement
	 * @return true if the line should be eliminated
	 */
	protected static boolean shouldEliminate(String l) {
		String t = l.toLowerCase();
		if (t.startsWith("set ")) return true;
		if (t.startsWith("grant ")) return true;
		if (t.startsWith("create schema ")) return true;
		if (t.startsWith("create user ")) return true;
		return false;
	}

	
	
}
