import bagel.Image;
/**
 * Class to represent the bomb special note
 * @author Finlay Ekins
 */
public class Bomb extends SpecialNote{
    /** Designated message when the bomb is activated
     */
    public final String message = "LANE CLEAR";
    private final Image image = new Image("res/noteBomb.PNG");

    /** Constructor for the Bomb special note
     * @param appearanceFrame the frame where the bomb should appear
     * @param type the type of special note
     */
    public Bomb(int appearanceFrame, String type) {
        super(appearanceFrame, type);
    }

    /** method to draw the image of the bomb
     * @param x the x-value of where to draw the special note (what lane)
     */
    public void draw(int x){
        if(active){
            image.draw(x, y);
        }
    }

    /** gives the Bomb's message
     * @return String the message of the special note
     */
    public String getMessage() {
        return message;
    }

}
