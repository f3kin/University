import bagel.Image;
/**
 * Class to represent the double score special note
 * @author Finlay Ekins
 */
public class DoubleScore extends SpecialNote{
    protected static final int FRAME_DURATION = 480;
    protected final String message = "DOUBLE SCORE";
    private final Image image = new Image("res/note2x.png");

    /** The constructor for the double score special note
     * @param appearanceFrame the appearance frame of the special note
     * @param type the type of special note
     */
    public DoubleScore(int appearanceFrame, String type){
        super(appearanceFrame, type);
    }

    /** draws the image of the DoubleScore Special Note
     * @param x the horizontal value where the special note should be printed (the lane)
     */
    public void draw(int x){
        if (active) {
            image.draw(x, y);
        }
    }

    /** Getter for the DoubleScore message
     * @return String the message for printing
     */
    public String getMessage() {
        return message;
    }
}
