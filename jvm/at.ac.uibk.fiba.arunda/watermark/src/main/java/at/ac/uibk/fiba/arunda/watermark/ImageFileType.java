package at.ac.uibk.fiba.arunda.watermark;

/**
 * Managed file types
 * @author joseph
 *
 */
public enum ImageFileType {

    /**
     * GIF
     */
    GIF("gif"),
    /**
     * PNG
     */
    PNG("png"),
    /**
     * JPEG
     */
    JPEG("jpg"),
    /**
     * TIFF
     */
    TIFF("tif");

    private String suffix;

    ImageFileType(String ending) {
        this.suffix = ending;
    }

    /**
     *
     * @return default suffix without '.'
     */
    public String getSuffix() {
        return this.suffix;
    }

    /**
     * Guess the filetype by name.
     * @param name
     * @return the guessed filetype or null if it cannot determine the filetype by name.
     */
    public static ImageFileType guessTypeByName(String name) {

        if (name.contains(".")) {
            String ext = name.substring(name.lastIndexOf(".") + 1)
                    .toLowerCase();
            switch (ext) {
                case "jpg":
                case "jpeg":
                    return JPEG;
                case "gif":
                    return GIF;
                case "tiff":
                case "tif":
                    return TIFF;
                case "png":
                    return PNG;
            }
        }
        return null;

    }

}
