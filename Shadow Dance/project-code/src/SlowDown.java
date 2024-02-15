import bagel.Image;
/**
 * Class to represent the slow down special note
 * @author Finlay Ekins
 */
public class SlowDown extends SpecialNote{
    protected final String message = "SLOW DOWN";
    private final Image image = new Image("res/noteSlowDown.png");

    /** This is the constructor for the slow down special note
     * @param appearanceFrame This is the frame at which the special note appears
     * @param type This is the type of special note
     */
    public SlowDown(int appearanceFrame, String type) {
        super(appearanceFrame, type);
    }
    /** This draws the SlowDown special note's image
     * @param x int This is the x location of the special note
     */
    public void draw(int x){
        if (active) {
            image.draw(x, y);
        }
    }
    /** This gives the notes message
     * @return String This is the message required for printing of the note
     */
    public String getMessage() {
        return message;
    }

}