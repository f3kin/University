import bagel.Font;
import bagel.Window;

/**
 * Class for dealing with accuracy of pressing the notes
 * @author Finlay Ekins
 */
public class Accuracy {
    /**
     * Score values for each different not scoring possibility
     */
    public static final int PERFECT_SCORE = 10;
    public static final int GOOD_SCORE = 5;
    public static final int BAD_SCORE = -1;
    public static final int MISS_SCORE = -5;
    public static final int NOT_SCORED = 0;
    public static final int SPECIAL_DONE = -100;
    public static final int SPECIAL_ACTIVE = -200;
    /**
     * Score messages for each different note scoring possibility
     */
    public static final String PERFECT = "PERFECT";
    public static final String GOOD = "GOOD";
    public static final String BAD = "BAD";
    public static final String MISS = "MISS";
    private static final int PERFECT_RADIUS = 15;
    private static final int GOOD_RADIUS = 50;
    private static final int BAD_RADIUS = 100;
    private static final int SPECIAL_DIST = 50;
    private static final int MISS_RADIUS = 200;
    private static final Font ACCURACY_FONT = new Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private String currAccuracy = null;
    private int frameCount = 0;

    /** sets the accuracy value for a specific note for printing
     * @param accuracy sets the current score message to be printed
     */
    public void setAccuracy(String accuracy) {
        currAccuracy = accuracy;
        frameCount = 0;
    }

    /** This method evaluates the score of a note if it is pressed
     * @param height the current y value of a note
     * @param targetHeight the target y value of a note for a perfect score
     * @param triggered an indicator of if the note has been pressed
     * @return int This returns the score value associated with the note press
     */
    public int evaluateScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= PERFECT_RADIUS) {
                setAccuracy(PERFECT);
                return PERFECT_SCORE;
            } else if (distance <= GOOD_RADIUS) {
                setAccuracy(GOOD);
                return GOOD_SCORE;
            } else if (distance <= BAD_RADIUS) {
                setAccuracy(BAD);
                return BAD_SCORE;
            } else if (distance <= MISS_RADIUS) {
                setAccuracy(MISS);
                return MISS_SCORE;
            }

        } else if (height >= (Window.getHeight())) {
            setAccuracy(MISS);
            return MISS_SCORE;
        }

        return NOT_SCORED;

    }

    /** determines the score for a special note
     * @param height the current y value of a Special Note
     * @param targetHeight the desired y value of a Special Note for a perfect score
     * @param triggered indicator of if the special note has been activated
     * @return int returns the score associated with the special note press
     */
    public int evaluateSpecialScore(int height, int targetHeight, boolean triggered){
        int distance = Math.abs(height - targetHeight);
        if (triggered){
            if (distance <= SPECIAL_DIST){
                return SPECIAL_ACTIVE;
            }
            else{
                return NOT_SCORED;
            }
        }
        else if (height >= (Window.getHeight())) {
            return SPECIAL_DONE;
        }
        return NOT_SCORED;
    }

    /** updates the accuracy score and prints the relevant message (if applicable)
     */
    public void update() {
        frameCount++;
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {
            ACCURACY_FONT.drawString(currAccuracy,
                    Window.getWidth()/2 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                    Window.getHeight()/2);
        }
    }
}
