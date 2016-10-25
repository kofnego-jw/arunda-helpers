package at.ac.uibk.fiba.arunda.watermark;

import ij.ImagePlus;
import ij.io.FileInfo;
import ij.io.FileOpener;
import ij.io.TiffDecoder;
import ij.io.TiffEncoder;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;


/**
 * Helper class for loading and saving pictures
 * @author joseph
 *
 */
public class ImageFileIO {

    /**
     * Writes an image ip with the format outputType to an inputStream.
     * @param ip
     * @param outputType
     * @return
     * @throws WatermarkException
     */
    public static ByteArrayInputStream writeImage(ImagePlus ip, ImageFileType outputType)
            throws WatermarkException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            switch (outputType) {
                case TIFF:
                    saveTiff(ip, os);
                    break;
                case GIF:
                    saveUseImageIO("gif", ip, os);
                    break;
                case PNG:
                    saveUseImageIO("png", ip, os);
                    break;
                case JPEG:
                default:
                    saveUseImageIO("jpg", ip, os);
            }
        } catch (Exception e) {
            throw new WatermarkException("Cannot save image to outputStream: " + e.getMessage());
        }
        return new ByteArrayInputStream(os.toByteArray());
    }

    /**
     * Reads a file
     * @param name
     * @param in
     * @param type
     * @return
     * @throws WatermarkException
     */
    public static ImagePlus readFile(String name, InputStream in,
                                     ImageFileType type) throws WatermarkException {
        try {
            switch (type) {
                case PNG:
                    return loadUseImageIO(in, name);
                case TIFF:
                    return loadTiff(in, name);
                case JPEG:
                case GIF:
                default:
                    return loadUseToolkit(in, name);
            }
        } catch (Exception e) {
            throw new WatermarkException("Cannot read inputStream with type "
                    + type.name() + ": " + e.getMessage());
        }
    }

    /**
     * Use this method for PNG
     * @param in
     * @param name
     * @return ImagePlus
     * @throws IOException
     */
    protected static ImagePlus loadUseImageIO(InputStream in, String name)
            throws IOException {
        BufferedImage bi = ImageIO.read(in);
        return new ImagePlus(name, bi);
    }

    /**
     * Use this method for TIFF, copied code from ImageJ
     * @param in
     * @param name
     * @return
     * @throws IOException
     */
    protected static ImagePlus loadTiff(InputStream in, String name)
            throws IOException {
        TiffDecoder td = new TiffDecoder(in, name);
        FileInfo[] info = td.getTiffInfo();
        ImagePlus imp = null;
        FileOpener fo = new FileOpener(info[0]);
        imp = fo.open(false);
        return imp;
    }

    /**
     * Use toolkit, for JPEG and GIF
     * @param in
     * @param name
     * @return
     * @throws IOException
     */
    protected static ImagePlus loadUseToolkit(InputStream in, String name)
            throws IOException {
        Image i = Toolkit.getDefaultToolkit().createImage(
                IOUtils.toByteArray(in));
        return new ImagePlus(name, i);
    }

    /**
     * Save use ImageIO
     * @param format
     * @param ip
     * @param os
     * @throws IOException
     */
    protected static void saveUseImageIO(String format, ImagePlus ip, OutputStream os) throws IOException {
        if (!ImageIO.write(ip.getBufferedImage(), format, os))
            throw new IOException("Cannot write image to outputstream.");
    }

    /**
     * Save TIFF
     * @param ip
     * @param os
     * @throws IOException
     */
    protected static void saveTiff(ImagePlus ip, OutputStream os) throws IOException {
        FileInfo fi = ip.getFileInfo();
        TiffEncoder te = new TiffEncoder(fi);
        te.write(os);
    }

}
