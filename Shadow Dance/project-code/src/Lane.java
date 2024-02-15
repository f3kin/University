import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.sql.SQLOutput;

/**
 * Class for lanes from which the notes fall
 * @author Finlay Ekins
 */
public class Lane {
    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    private final String type;
    private final Image image;
    protected final Note[] notes = new Note[100];
    private int numNotes = 0;
    private final HoldNote[] holdNotes = new HoldNote[20];
    private int numHoldNotes = 0;
    protected double speedMult = 1;
    private final SpecialNote[] specialNotes = new SpecialNote[20];
    private int numSpecialNotes = 0;
    private Keys relevantKey;
    private final int location;
    private int currNote = 0;
    private int currHoldNote = 0;
    private int currSpecialNote = 0;
    private boolean scoreEffect = false;
    private boolean speedEffect = false;
    private boolean slowEffect = false;
    protected int extraSpeed = 0;

    /** Constructor for the lane class
     * @param dir This is the direction of the lane
     * @param location This is the x-coordinate where the lane should be drawn
     */
    public Lane(String dir, int location) {
        this.type = dir;
        this.location = location;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
            case "Special":
                relevantKey = Keys.SPACE;
        }
    }

    /** Gives the lane type
     * @return String This is the type of lane
     */
    public String getType() {
        return type;
    }

    /** Updates all notes in the lane class
     * @param input This is the input commands from the keyboard
     * @param accuracy This is the scoring object for the level
     * @return int This gives the score value for each lane
     */
    public int update(Input input, Accuracy accuracy) {
        draw();
        for (int i = currNote; i < numNotes; i++) {
            notes[i].update(extraSpeed);
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes[j].update(extraSpeed);
        }

        for (int k = currSpecialNote; k < numSpecialNotes; k++){
            specialNotes[k].update(extraSpeed);
        }
        if (currNote < numNotes) {
            int score = notes[currNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (notes[currNote].isCompleted()) {
                currNote++;
                return score;
            }
        }
        if (currHoldNote < numHoldNotes) {
            int score = holdNotes[currHoldNote].checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);
            if (holdNotes[currHoldNote].isCompleted()) {
                currHoldNote++;
                return score;
            }
        }

        if (currSpecialNote < numSpecialNotes) {
            SpecialNote now = specialNotes[currSpecialNote];
            String message = now.getMessage();
            now.checkSpecial(input, accuracy, TARGET_HEIGHT, message, relevantKey, now.getType());
            if (now.isCompleted()) {
                currSpecialNote++;
                return 0;
            }
            else if (now.isEffect()){
                applyEffect(now);
            }
        }
        return Accuracy.NOT_SCORED;
    }

    /** This adds a note to the lane
     * @param n This is the new note to be added
     */
    public void addNote(Note n) {
        notes[numNotes++] = n;
    }
    /** This adds a hold note to the lane
     * @param hn This is the new hold note to be added
     */
    public void addHoldNote(HoldNote hn) {
        holdNotes[numHoldNotes++] = hn;
    }
    /** This adds a special note to the lane
     * @param sn This is the new special note to be added
     */
    public void addSpecialNote(SpecialNote sn) {specialNotes[numSpecialNotes++] = sn;}

    /** This checks if a lane is finished for the level
     * @return boolean This indicates whether the lane has more notes to print
     */
    public boolean isFinished() {
        for (int i = 0; i < numNotes; i++) {
            if (!notes[i].isCompleted()) {
                return false;
            }
        }

        for (int j = 0; j < numHoldNotes; j++) {
            if (!holdNotes[j].isCompleted()) {
                return false;
            }
        }

        for (int k = 0; k < numSpecialNotes; k++) {
            if (!specialNotes[k].isCompleted()) {
                return false;
            }
        }

        return true;
    }

    /** This draws the image for the lane and its notes
     */
    public void draw() {
        image.draw(location, HEIGHT);

        for (int i = currNote; i < numNotes; i++) {
            notes[i].draw(location);
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes[j].draw(location);
        }

        for (int k = currSpecialNote; k < numSpecialNotes; k++) {
                specialNotes[k].draw(location);

        }
    }

    /** This activates the bomb effect when a bomb has been triggered
     */
    public void bombEffect(){
        for (int i = currNote; i < numNotes; i++) {
            notes[i].deactivate();
        }

        for (int j = currHoldNote; j < numHoldNotes; j++) {
            holdNotes[j].deactivate();
        }
    }
    /** This activates the double score effect when a double score note has been triggered
     */
    public boolean isScoreEffect() {
        return scoreEffect;
    }
    /** This activates the speed up effect when a speed up special note has been triggered
     */
    public boolean isSpeedEffect() {
        return speedEffect;
    }
    /** This activates the slow down effect when a slow down special note has been triggered
     */
    public boolean isSlowEffect() {
        return slowEffect;
    }

    /** This sets the indicator for the score effect
     * @param scoreEffect boolean This indicates whether a score effect is active
     */
    public void setScoreEffect(boolean scoreEffect) {
        this.scoreEffect = scoreEffect;
    }
    /** This sets the indicator for the speed up effect
     * @param speedEffect boolean This indicates whether a speed up effect is active
     */
    public void setSpeedEffect(boolean speedEffect) {
        this.speedEffect = speedEffect;
    }
    /** This sets the indicator for the slow down effect
     * @param slowEffect boolean This indicates whether a slow down effect is active
     */
    public void setSlowEffect(boolean slowEffect) {
        this.slowEffect = slowEffect;
    }

    /** Returns the location of the lane (x-value)
     * @return int This is the x location of the lane
     */
    public int getLocation(){
        return location;
    }

    /** This applies the effect of a special note
     * @param now This is the special note that has been triggered
     */
    public void applyEffect(SpecialNote now){
        switch(now.getType()){
            case "Bomb":
                bombEffect();
                now.setEffect(false);
                now.deactivate();
                break;
            case "SpeedUp":
                speedEffect = true;
                now.setEffect(false);
                now.deactivate();
                break;
            case "SlowDown":
                slowEffect = true;
                now.setEffect(false);
                now.deactivate();
                break;
            case "DoubleScore":
                scoreEffect = true;
                now.setEffect(false);
                now.deactivate();
                break;
        }
    }
}
