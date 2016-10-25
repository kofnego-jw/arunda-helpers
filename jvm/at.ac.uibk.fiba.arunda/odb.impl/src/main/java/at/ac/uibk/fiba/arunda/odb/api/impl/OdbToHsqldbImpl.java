package at.ac.uibk.fiba.arunda.odb.api.impl;

import at.ac.uibk.fiba.arunda.odb.api.OdbException;
import at.ac.uibk.fiba.arunda.odb.api.OdbToHsqldb;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Implementation of OdbToHsqldb
 * @author totoro
 *
 */
public class OdbToHsqldbImpl implements OdbToHsqldb {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OdbToHsqldbImpl.class);
	
	private Cache<String, File> fileLocationCache;
	
	public OdbToHsqldbImpl() {
		super();
		fileLocationCache = CacheBuilder.newBuilder().build();
		LOGGER.debug("OdbToHsqldbImpl initialized.");
	}
	
	protected static File createTempDir() throws IOException {
		File tempFile = File.createTempFile("odbtohsqldb", ".temp");
		String name = tempFile.getName();
		name = name.substring(0, name.length()-5);
		return new File(tempFile.getParent(), name);
	}
	
	protected static void renameFiles(File dir, String prefix) throws IOException {
		String usedPrefix = prefix + ".";
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File now: files) {
				File to = new File(dir, usedPrefix + now.getName());
				now.renameTo(to);
			}
		}
	}
	
	@Override
	public String convertToJdbcUrl(File odbFile, String databasename)
			throws IOException, OdbException {
		File tmpDir = createTempDir();
		
		LOGGER.debug("TemporaryFile created: {}", tmpDir);
		
		try {
			Unzipper.unpack(odbFile, tmpDir);
		} catch (Exception e) {
			LOGGER.error("Cannot unzip file.", e);
			throw new OdbException("Cannot unzip file.", e);
		}
		
		LOGGER.debug("ODB File {} unpacked.", odbFile);
		
		File databaseDir = new File(tmpDir, "database");
		
		renameFiles(databaseDir, databasename);
		
		LOGGER.debug("Files of {} renamed.", odbFile);
		
		String absolutePath = databaseDir.getAbsolutePath();
		
		if (absolutePath.endsWith("/"))
			absolutePath = absolutePath.substring(0, absolutePath.length()-1);
		
		String jdbcUrl = "jdbc:hsqldb:file:" + absolutePath + "/" + databasename;
		
		fileLocationCache.put(jdbcUrl, tmpDir);
		
		return jdbcUrl;
	}
	
	@Override
	public void deleteTemporaryFile(String jdbcUrl) throws IOException,
			OdbException {
		LOGGER.debug("Deletion for url {} requested.", jdbcUrl);
		File tempDir = fileLocationCache.getIfPresent(jdbcUrl);
		if (tempDir == null)
			throw new OdbException("Cannot find the file to be deleted.");
		
		if (!FileUtils.deleteQuietly(tempDir))
			LOGGER.warn("Cannot delete temporary files: {}", tempDir);
		
	}

}
