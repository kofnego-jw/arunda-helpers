package at.ac.uibk.fiba.arunda.odb.export.impl;

import at.ac.uibk.fiba.arunda.odb.export.OdbExportFormat;
import at.ac.uibk.fiba.arunda.odb.export.OdbExportService;
import org.apache.commons.io.IOUtils;

import java.io.File;

/**
 * Created by joseph on 10/25/16.
 */
public class ArundaExport {

    private static final String ARUNDA_DROP = "/arunda/arundaDroptable.sql";

    private File arundaFile;

    private final OdbExportService service;

    public ArundaExport(OdbExportService service) {
        super();
        this.service = service;
    }


    public File getArundaFile() {
        return arundaFile;
    }

    public String execute(File arundaFile) throws Exception {
        this.arundaFile = arundaFile;
        return execute();
    }

    private String execute() throws Exception {

        String sql = service.convertOdb(arundaFile, OdbExportFormat.MYSQL_SQL);

        String drop = IOUtils.toString(getClass().getResourceAsStream(ARUNDA_DROP), "utf-8");

        return drop + sql;

    }
}
