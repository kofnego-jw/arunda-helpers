package at.ac.uibk.fiba.arunda.watermark;

/**
 * Created by joseph on 9/13/16.
 */
public class WMConstants {

    public static final int THRESHHOLD = 10;

    public static final double ALPHA = 0.4D;

    public static final WMContainer WATERMARK;
    static {
        WMContainer test;
        try {
            test = WMContainer.createWMContainer();
        } catch (Exception e) {
            test = null;
        }
        WATERMARK = test;
    }

}
