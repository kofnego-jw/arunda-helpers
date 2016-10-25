package at.ac.uibk.fiba.arunda.webapp.web;

import at.ac.uibk.fiba.arunda.odb.export.impl.ArundaExport;
import at.ac.uibk.fiba.arunda.watermark.ImageFileType;
import at.ac.uibk.fiba.arunda.watermark.PdfWatermarker;
import at.ac.uibk.fiba.arunda.watermark.WMApplier;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by joseph on 10/25/16.
 */
@Controller
@RequestMapping("/arunda")
public class WebEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebEndpoint.class);

    private static final float DPI = 300F;
    private static final float QUALITY = 0.3F;

    private static final String DATABASE_FILE_PREFIX = "arunda";
    private static final String DATABASE_FILE_SUFFIX = ".odb";

    private static final File TEMPDIR;
    static {
        File dir;
        try {
            File test = File.createTempFile("arunda_wm", ".tmp");
            String name = test.getName().substring(0, test.getName().indexOf("."));
            dir = new File(test.getParent(), name);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create temporary upload directory.", e);
        }
        TEMPDIR = dir;
    }

    @Autowired
    private WMApplier wmApplier;

    @Autowired
    private ArundaExport arundaExport;

    @RequestMapping(value = "/tosql", method = {RequestMethod.POST, RequestMethod.PUT})
    public void convert(@RequestParam("file") MultipartFile file, HttpServletResponse resp) {
        String name = DATABASE_FILE_PREFIX + uuid() + DATABASE_FILE_SUFFIX;
        File odb = new File(TEMPDIR, name);
        try {
            FileUtils.writeByteArrayToFile(odb, file.getBytes());
        } catch (Exception e) {
            LOGGER.error("Cannot write odb file.", e);
            write(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot write odb file: " + e.getMessage(), resp);
            return;
        }
        byte[] content;
        try {
            String text = arundaExport.execute(odb);
            content = text.getBytes("utf-8");
        } catch (Exception e) {
            LOGGER.error("Cannot convert odb file.", e);
            write(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot convert odb file: " + e.getMessage(), resp);
            return;
        }
        resp.setCharacterEncoding("UTF-8");
        String contentType = "application/sql";
        writeContent(content, contentType, "arunda.sql", resp);
    }



    @RequestMapping(value = "/stamp", method = {RequestMethod.POST, RequestMethod.PUT})
    public void stamp(@RequestParam("file") MultipartFile file, HttpServletResponse resp)  {
        if (!TEMPDIR.exists()) {
            TEMPDIR.mkdirs();
        }
        String name = file.getOriginalFilename();
        if (name==null || name.isEmpty()) {
            name = file.getName();
        }
        if (name==null || name.isEmpty()) {
            write(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot read file name.", resp);
            return;
        }
        File orig = new File(TEMPDIR, name);
        File result = new File(TEMPDIR, "result_" + uuid() + name);
        try {
            FileUtils.writeByteArrayToFile(orig, file.getBytes());
        } catch (Exception e) {
            write(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot write original file: " + e.getMessage(), resp);
            return;
        }
        String contentType;
        byte[] content;
        try {
            if (orig.getName().toLowerCase().endsWith(".pdf")) {
                content = stampingPdf(orig, result);
                contentType = "at/ac/uibk/fiba/arunda/webapp/application/pdf";
            } else {
                content = stampingPict(orig, result);
                contentType = "image/jpeg";
            }
        } catch (Exception e) {
            LOGGER.error("Cannot apply watermark.", e);
            write(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot apply watermark: " + e.getMessage(), resp);
            return;
        }
        writeContent(content, contentType, name, resp);
    }

    private void writeContent(byte[] content, String contentType, String filename, HttpServletResponse resp) {
        OutputStream os = null;
        try {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType(contentType);
            resp.setContentLength(content.length);
            resp.setHeader("Content-Disposition", "attachement; filename=" + filename);
            os = resp.getOutputStream();
            IOUtils.write(content, os);
        } catch (Exception e) {
            LOGGER.error("Cannot write to output stream.", e);
            write(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot write to outputStream: " + e.getMessage(), resp);
            return;
        } finally {
            if (os!=null) {
                try {
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    LOGGER.warn("Cannot shut down OutputStream gracefully.", e);
                }
            }
        }
    }

    private byte[] stampingPdf(File input, File output) throws Exception {
        PdfWatermarker.watermarking(input, output, wmApplier, DPI, QUALITY);
        if (!output.exists()) {
            throw new Exception("Cannot stamp, result file not existent.");
        }
        return content(output);
    }

    private byte[] stampingPict(File input, File output) throws Exception {
        wmApplier.applyWM(input, output, ImageFileType.JPEG);
        if (!output.exists()) {
            throw new Exception("Cannot stamp, result file not existent.");
        }
        return content(output);
    }

    private byte[] content(File output) throws Exception {
        byte[] content;
        try {
            content = FileUtils.readFileToByteArray(output);
        } catch (Exception e) {
            LOGGER.error("Cannot read file.", e);
            throw e;
        }
        return content;
    }

    private static void write(int statusCode, String msg, HttpServletResponse resp) {
        resp.setStatus(statusCode);
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
            writer.write(msg);
        } catch (Exception e) {
            LOGGER.error("Cannot write to HttpServletResponse.", e);
        } finally {
            if (writer!=null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    LOGGER.warn("Cannot close HttpServletWriter gracefully.", e);
                }
            }
        }
    }

    private static String uuid() {
        return UUID.randomUUID().toString().substring(0, 20);
    }

}
