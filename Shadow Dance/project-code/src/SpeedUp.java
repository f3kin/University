import bagel.Image;
/**
 * Class to represent the speed up special note
 * @author Finlay Ekins
 */
public class SpeedUp extends SpecialNote{
    protected final String message = "SPEED UP";
    private final Image image = new Image("res/noteSpeedUp.png");
    protected SpeedUp(int appearanceFrame, String type) {
        super(appearanceFrame, type);
    }

    /** This draws the SpeedUp special note's image
     * @param x int This is the x location of the special note
     */
    public void draw(int x){
        if(active) {
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