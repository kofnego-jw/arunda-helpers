package at.ac.uibk.fiba.arunda.watermark;

import ij.ImagePlus;

import java.io.InputStream;

/**
 * Created by joseph on 9/13/16.
 */
public class WMContainer {

    private static final String RESOURCE_PATH = "/watermarks/arunda_caption3.png";

    private final ImagePlus watermark;

    private WMContainer (ImagePlus watermark) {
        this.watermark = watermark;
    }

    public ImagePlus getWatermark() {
        return watermark;
    }

    public static WMContainer createWMContainer() throws Exception {
        InputStream stream = WMContainer.class.getResourceAsStream(RESOURCE_PATH);
        ImagePlus wm;
        try {
            wm = ImageFileIO.readFile("arunda_caption3.png", stream, ImageFileType.PNG);
        } catch (Exception e) {
            throw e;
        }
        return new WMContainer(wm);
    }
}
