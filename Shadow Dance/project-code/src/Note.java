import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Class to represent normal notes
 * @author Finlay Ekins
 */
public class Note {
    private final Image image;
    private final int appearanceFrame;
    protected final int speed = 2;
    private int y = 100;
    private boolean active = false;
    private boolean completed = false;

    /** This is the constructor for the note class
     * @param dir This is the direction of the note
     * @param appearanceFrame This is the frame where the note should appear
     */
    public Note(String dir, int appearanceFrame) {
        image = new Image("res/note" + dir + ".png");
        this.appearanceFrame = appearanceFrame;
    }

    /** Shows whether the note is active
     * @return boolean This is the indicator of whether the note is active
     */
    public boolean isActive() {
        return active;
    }

    /** Checks whether a note is completed
     * @return boolean This indicates if the note is completed
     */
    public boolean isCompleted() {return completed;}

    /** This deactivates the note
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /** This updates the note's location
     * @param extra This is the extra or speed that should be added or removed from the note's speed
     */
    public void update(int extra) {
        if (active) {
            y += speed + extra;
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    /** This draws the image of the note
     * @param x This is horizontal location of the note
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    /** This checks the score of the note
     * @param input This is the input from the keyboard of the level
     * @param accuracy This is the scoring object of the level
     * @param targetHeight This is the height of press for a perfect score
     * @param relevantKey This is the key to press to trigger the note
     * @return int This is the score value of the note
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateScore(y, targetHeight, input.wasPressed(relevantKey));
            // note has been scored
            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            }
        }
        return 0;
    }

    /** This gives the height of the note
     * @return int This is the height of the note
     */
    public int getY() {
        return y;
    }
}
