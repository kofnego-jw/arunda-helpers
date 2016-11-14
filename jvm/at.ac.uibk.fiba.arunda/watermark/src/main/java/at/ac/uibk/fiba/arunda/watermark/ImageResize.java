package at.ac.uibk.fiba.arunda.watermark;

import ij.ImagePlus;
import ij.io.FileInfo;
import ij.process.ImageProcessor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by joseph on 11/14/16.
 */
public class ImageResize {

    private static final float DEST_WIDTH = 225;
    private static final float DEST_HEIGHT = 300;

    public static InputStream createThumb(File f) throws Exception {
        if (f==null || !f.exists() || f.isDirectory() || !f.canRead()) {
            throw new Exception("Cannot process null file.");
        }
        ImagePlus ip = ImageFileIO.readFile(f.getName(), new BufferedInputStream(new FileInputStream(f)), ImageFileType.guessTypeByName(f.getName()));
        return resize(ip);
    }

    public static InputStream resize(ImagePlus ip) throws Exception {
        float ratioByWidth = DEST_WIDTH / ( (float)ip.getWidth());
        float ratioByHeight = DEST_HEIGHT / ( (float)ip.getHeight());
        float ratio = ratioByWidth > ratioByHeight ? ratioByWidth : ratioByHeight;
        int destWidth = (int) (ip.getWidth() * ratio);
        int destHeight = (int) (ip.getHeight() * ratio);
        ImageProcessor resized = ip.getProcessor().resize(destWidth, destHeight, true);
        return toJpeg(resized, ip.getFileInfo());
    }


    public static InputStream toJpeg(ImageProcessor ip, FileInfo fi) throws Exception {
        String filename = fi==null ? "thumb.jpg" : fi.fileName;
        ImagePlus imageP = new ImagePlus(filename, ip);
        return ImageFileIO.writeImage(imageP, ImageFileType.JPEG);
    }

}
