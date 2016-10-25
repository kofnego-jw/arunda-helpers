package at.ac.uibk.fiba.arunda.watermark;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Test for WMApplier and PdfWatermarker
 *
 * Created by joseph on 10/11/16.
 */
public class WatermarkRectTest {

    private static WMApplier applier;

    @BeforeClass
    public static void setUp() {
        ImagePlus wm = WMConstants.WATERMARK.getWatermark();
        applier = new WMApplier(wm);
    }

    @Test
    public void test_pict() throws Exception {
        File output = new File("target/rect/test.jpg");
        ImagePlus in = ImageFileIO.readFile("test.jpg", new FileInputStream("src/test/resources/picts/18_06.jpg"), ImageFileType.JPEG);
        ImageProcessor imageProcessor = applier.applyWM(in);
        ByteArrayInputStream test = ImageFileIO.writeImage(new ImagePlus("test", imageProcessor), ImageFileType.JPEG);
        FileUtils.copyInputStreamToFile(test, output);
    }

    @Test
    public void test_pdf() throws Exception {
        File input = new File("src/test/resources/pdf/01_(7).pdf");
        File output = new File("target/rect/01_(7).pdf");
        PdfWatermarker.watermarking(input, output, applier, 200F, 0.7F);
    }

}
