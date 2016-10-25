package at.ac.uibk.fiba.arunda.watermark;

/**
 * Exception wrapper class
 * @author joseph
 *
 */
public class WatermarkException extends Exception {

    private static final long serialVersionUID = 201505071018L;

    /**
     * Message
     */
    private String message;

    public WatermarkException() {
        this.message = "";
    }

    public WatermarkException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
