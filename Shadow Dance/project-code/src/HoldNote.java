import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * Class for dealing with hold notes
 * @author Finlay Ekins
 */
public class HoldNote {

    private static final int HEIGHT_OFFSET = 82;
    private final Image image;
    private final int appearanceFrame;
    private final double speed = 2;
    private int y = 24;
    private boolean active = false;
    private boolean holdStarted = false;
    private boolean completed = false;

    /** Constructor for the hold note
     * @param dir This is the direction of the hold note
     * @param appearanceFrame This is the frame that the hold note first appears
     */

    public HoldNote(String dir, int appearanceFrame) {
        image = new Image("res/holdNote" + dir + ".png");
        this.appearanceFrame = appearanceFrame;
    }

    /**
     * @return boolean This indicates if a Hold note is active or not
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @return This indicates whether a hold note has been completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /** This deactivates a hold note and marks it as completed
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /** Sets the start of the hold
     */
    public void startHold() {
        holdStarted = true;
    }

    /** Updates the hold note
     * @param extra This adds to the speed of the note depending on whether a scoring Special Note has been activated
     */
    public void update(int extra) {
        //increment the speed
        if (active) {
            y += speed + extra;
        }
        //hold note is still active
        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    /** draws the hold note image
     * @param x the lane x-value for the hold note
     */
    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    /** Scores the hold note, once at the start and once at the end (press and release)
     * @param input This is the input press of the keyboard
     * @param accuracy This is the scoring attribute
     * @param targetHeight This is the desired height of press and release
     * @param relevantKey This is the key that should be pressed to trigger the hold note
     * @return int This is the score of the hold note
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {

        if (isActive() && !holdStarted) {
            int score = accuracy.evaluateScore(getBottomHeight(), targetHeight, input.wasPressed(relevantKey));
            if (score == Accuracy.MISS_SCORE) {
                deactivate();
                return score;
            } else if (score != Accuracy.NOT_SCORED) {
                startHold();
                return score;
            }
        } else if (isActive() && holdStarted) {

            int score = accuracy.evaluateScore(getTopHeight(), targetHeight, input.wasReleased(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            } else if (input.wasReleased(relevantKey)) {
                deactivate();
                accuracy.setAccuracy(Accuracy.MISS);
                return Accuracy.MISS_SCORE;
            }
        }

        return 0;
    }

    /** returns the height of the bottom of the hold note
     * @return int This is the height of the bottom of the hold note
     */
    private int getBottomHeight() {
        return y + HEIGHT_OFFSET;
    }

    /** returns the top height of the hold note
     * @return int This is the top height of the hold note
     */
    private int getTopHeight() {
        return y - HEIGHT_OFFSET;
    }
}
