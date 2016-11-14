package at.ac.uibk.fiba.arunda.watermark;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

/**
 * Created by joseph on 11/14/16.
 */
public class ImageResizeTest {

    @Test
    public void test() throws Exception {
        File f = new File("src/test/resources/picts/18_06.jpg");
        InputStream in = ImageResize.createThumb(f);
        FileUtils.copyInputStreamToFile(in, new File("target/testoutput/image.thumb.jpg"));
    }

    @Test
    public void test2() throws Exception {
        File f = new File("src/test/resources/pdf/01_(7).pdf");
        InputStream in = PdfImageResize.createThumb(f);
        FileUtils.copyInputStreamToFile(in, new File("target/testoutput/pdf.thumb.jpg"));
    }


}
