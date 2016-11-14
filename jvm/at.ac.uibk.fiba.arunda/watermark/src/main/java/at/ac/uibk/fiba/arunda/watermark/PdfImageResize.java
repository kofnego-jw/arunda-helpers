package at.ac.uibk.fiba.arunda.watermark;

import ij.ImagePlus;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

/**
 * Created by joseph on 11/14/16.
 */
public class PdfImageResize {

    private static final float DOC_DPI = 100;

    public static InputStream createThumb(File pdfFile) throws Exception {
        if (pdfFile==null || pdfFile.isDirectory() || !pdfFile.canRead()) {
            throw new Exception("Cannot create thumb out of null file.");
        }
        PDDocument orig = PDDocument.load(pdfFile);

        PDFRenderer renderer = new PDFRenderer(orig);

        try {
            BufferedImage bufferedImage = renderer.renderImageWithDPI(0, 100);
            ImagePlus ip = new ImagePlus(pdfFile.getName(), bufferedImage);
            return ImageResize.resize(ip);
        } finally {
            if (orig!=null) {
                orig.close();
            }
        }
    }

}
