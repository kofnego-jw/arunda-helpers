package at.ac.uibk.fiba.arunda.watermark;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by joseph on 9/14/16.
 */
public class PdfWatermarker {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfWatermarker.class);

    public static void watermarking(File origPdf, File resultPdf, WMApplier applier,
                                    float docDpi, float quality) throws Exception {

        float qual = quality < 0 || quality > 1 ? 0.8F : quality;

        LOGGER.info("Starting converting {} to {}.", origPdf, resultPdf);

        PDDocument orig = PDDocument.load(origPdf);

        PDFRenderer renderer = new PDFRenderer(orig);

        int width, height;

        try {
            BufferedImage bufferedImage = renderer.renderImageWithDPI(0, docDpi);
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
        } catch (Exception e) {
            LOGGER.error("Cannot convert, cancelling...", e);
            orig.close();
            return;
        }

        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(resultPdf));

        Document resultDoc = new Document(new Rectangle(width, height));
        PdfWriter writer = PdfWriter.getInstance(resultDoc, os);

        resultDoc.open();

        int totalPages = orig.getNumberOfPages();

        for (int n=0; n<totalPages; n++) {
            LOGGER.debug("Rendering page {} of {}.", n, totalPages);
            BufferedImage bImage = renderer.renderImageWithDPI(n, docDpi);
            ImagePlus ip = new ImagePlus("page" + n, bImage);
            ImageProcessor marked = applier.applyWM(ip);
            resultDoc.setPageSize(new Rectangle(marked.getWidth(), marked.getHeight()));
            resultDoc.newPage();
            Image image = Image.getInstance(writer, marked.getBufferedImage(), qual);
            image.setAbsolutePosition(0,0);
            resultDoc.add(image);
        }

        resultDoc.close();
        writer.close();

        orig.close();

    }

}
