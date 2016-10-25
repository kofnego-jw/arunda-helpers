package at.ac.uibk.fiba.arunda.watermark;

import ij.ImagePlus;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by joseph on 10/11/16.
 */
public class WMApplier {

    private static final float START_X = 0.75F;

    private static final float START_Y = 0.80F;

    private static final float SIZE_X = 0.20F;

    private static final float SIZE_Y = 0.20F;

    private final float WM_RATIO;

    private final ImagePlus watermark;

    public WMApplier(ImagePlus watermark) {
        this.watermark = watermark;
        WM_RATIO = (float) watermark.getWidth() / (float) watermark.getHeight();
    }

    public void applyWM(File imageFile, File output, ImageFileType type) throws Exception {
        ImagePlus orig = ImageFileIO.readFile(imageFile.getName(), new BufferedInputStream(new FileInputStream(imageFile)), type);
        ImageProcessor result = applyWM(orig);
        InputStream in = ImageFileIO.writeImage(new ImagePlus(imageFile.getName(), result), type);
        FileUtils.copyInputStreamToFile(in, output);
    }

    public ImageProcessor applyWM(ImagePlus orig) {
        ColorProcessor proc = orig.flatten().getProcessor().convertToRGB().convertToColorProcessor();
        int expectedX = (int)(proc.getWidth() * SIZE_X);
        int yByUseX = (int) ((float)expectedX / WM_RATIO);

        int expectedY = (int) (proc.getHeight() * SIZE_Y);
        int xByUseY = (int) ((float)expectedY * WM_RATIO);

        int wmWidth, wmHeight;

        if (expectedX > xByUseY) {
            wmWidth = xByUseY;
            wmHeight = expectedY;
        } else {
            wmWidth = expectedX;
            wmHeight = yByUseX;
        }

        int startx = (int)(proc.getWidth() * START_X);
        int starty = (int)(proc.getHeight() * START_Y);

        ColorProcessor wmResized = watermark.flatten().getProcessor()
                .resize(wmWidth, wmHeight, true).convertToColorProcessor();

//        System.out.println("X, Y, startx, starty: " + wmWidth + "," + wmHeight + "," + startx + "," + starty);

        for (int i=0; i<wmResized.getWidth(); i++) {
            for (int j=0; j<wmResized.getHeight(); j++) {
                Color watermarkColor = wmResized.getColor(i, j);
                if (underThreshHold(watermarkColor)) {
                    continue;
                }
                int x = startx + i;
                int y = starty + j;
                Color originalColor = proc.getColor(x, y);
                Color merged = mergeColor(originalColor, watermarkColor);
                proc.set(x, y, merged.getRGB());
            }

        }

        return proc;

    }



    private static boolean underThreshHold(Color c) {
        if (c.getRed() < WMConstants.THRESHHOLD &&
                c.getGreen() < WMConstants.THRESHHOLD &&
                c.getBlue() < WMConstants.THRESHHOLD) return true;
        return false;
    }

    protected static Color mergeColor(Color origC, Color wmC) {
        float oR = origC.getRed();
        float oG = origC.getGreen();
        float oB = origC.getBlue();

        float wR = wmC.getRed();
        float wG = wmC.getGreen();
        float wB = wmC.getBlue();

        int r, g, b;

        r = (int) (oR * (1 - WMConstants.ALPHA) + wR * WMConstants.ALPHA);
        g = (int) (oG * (1 - WMConstants.ALPHA) + wG * WMConstants.ALPHA);
        b = (int) (oB * (1 - WMConstants.ALPHA) + wB * WMConstants.ALPHA);

        if (r<0) r = 0;
        if (g<0) g = 0;
        if (b<0) b = 0;
        if (r>255) r = 255;
        if (g>255) g = 255;
        if (b>255) b = 255;

        return new Color(r, g, b);
    }
}
