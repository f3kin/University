import bagel.Input;
import bagel.Keys;
/**
 * Generic abstract class for the special notes in the game
 * @author Finlay Ekins
 */

public abstract class SpecialNote {
    private final String type;
    protected boolean effect;
    protected boolean active = false;
    protected final int appearanceFrame;
    protected final int speed = 2;
    protected int y = 100;
    protected boolean completed = false;

    /** This is the constructor for the Special Note Class
     * @param appearanceFrame This is the appearance frame of the special note
     * @param type This is the type of special note
     */
    public SpecialNote(int appearanceFrame, String type){
        this.appearanceFrame = appearanceFrame;
        this.type = type;
    }

    /** This sets the special note's effect to active
     */
    public void applyEffect(){
        effect = true;
    };

    /** This draws the special note
     * @param x int This is the horizontal location (lane) to draw the special note
     */
    public abstract void draw(int x);

    /** This updates (moves) the special note
     * @param extra This is the extra speed to add based on what effects are applied
     */
    public void update(int extra) {
        if (active) {
            y += speed + extra;
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
    }

    /** This checks if a special note is completed
     * @return boolean This is the indicator to whether the note is complete
     */
    public boolean isCompleted() {
        return completed;
    }

    /** This checks if a special note is completed and scores it accordingly
     * @param input This is the keyboard input
     * @param accuracy This is the scoring object of the level
     * @param targetHeight This is the desired height of the special note
     * @param message This is the special note's message
     * @param relevantKey This is the relevant key to be pressed
     * @param type This is the type of special note
     */
    public void checkSpecial(Input input, Accuracy accuracy, int targetHeight, String message, Keys relevantKey, String type) {
        if (isActive()) {
            int score = accuracy.evaluateSpecialScore(y, targetHeight, input.wasPressed(relevantKey));
            if (score == Accuracy.SPECIAL_ACTIVE){
                applyEffect();
                accuracy.setAccuracy(message);
            }
            else if (score == Accuracy.SPECIAL_DONE) {
                deactivate();
            }
        }
    }

    /** This deactivates the special note
     */
    public void deactivate() {
        active = false;
        completed = true;
    }

    /** This checks if a special note is active
     * @return boolean This is the indicator of a special note
     */
    public boolean isActive() {
        return active;
    }

    /** This returns the message of the special note
     * @return String This is the message of the special note
     */
    public abstract String getMessage();

    /** This returns the type of special note
     * @return String This is the type of special note
     */
    public String getType() {
        return type;
    }

    /** This checks what effect is active
     * @return boolean This is the active effect
     */
    public boolean isEffect() {
        return effect;
    }

    /** This sets the indicator that an effect has occured
     * @param effect This is the indicator that an effect is active
     */
    public void setEffect(boolean effect) {
        this.effect = effect;
    }
}
