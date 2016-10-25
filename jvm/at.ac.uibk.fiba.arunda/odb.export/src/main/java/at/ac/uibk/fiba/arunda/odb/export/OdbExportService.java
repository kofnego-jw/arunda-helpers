/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.ac.uibk.fiba.arunda.odb.export;

import java.io.File;

import at.ac.uibk.fiba.arunda.odb.api.OdbException;

/**
 * Interface for exporting ODB to another SQL format
 * @author joseph
 *
 */
public interface OdbExportService {
	
	/**
	 * 
	 * NB: For larger databases one also needs more memory...
	 * 
	 * @param odbFile
	 * @param format
	 * @return A String full of SQL-Statements, containing the database.
	 * @throws OdbException
	 */
	public String convertOdb(File odbFile, OdbExportFormat format) throws OdbException;


}