import bagel.Image;
import java.util.Random;

/**
 * Class to represent the enemy sub object of level three
 * @author Finlay Ekins
 */

public class Enemy{
    private final int STEP_SIZE = 1;
    private int x;
    private int y;
    private int direction;
    private final int COLLISION = 104;
    public static final int FREQUENCY = 600;
    private final Image image = new Image("res/enemy.png");
    private final int MIN_COORD = 100;
    private final int MAX_X = 900;
    private final int MAX_Y = 500;
    private Random random = new Random();
    private int[] directions = {-1, 1};
    private boolean active = false;
    private boolean newEnemy = true;
    protected boolean completed = false;

    /** update the enemy class so that it can move
     */
    public void update(){
        if(newEnemy){
            active = true;
            newEnemy = false;
            render();
        }
        if (active){
            x += STEP_SIZE*direction;
            if ((x == MAX_X) || (x == MIN_COORD)){
                direction *= -1;
            }
            draw();
        }
    }

    /** Randomly choose and set a point within a range of values for the enemy to spawn
     */
    public void choosePoint(){
        x = random.nextInt(MAX_X - MIN_COORD + 1) + MIN_COORD;
        y = random.nextInt(MAX_Y - MIN_COORD + 1) + MIN_COORD;
    }

    /** Randomly choose a direction for the enemy to initially move
     */
    public void chooseDirection(){
        int randomDir = random.nextInt(directions.length);
        direction = directions[randomDir];
    }

    /** Draw the image of the enemy
     */
    public void draw(){
        if(active){
            image.draw(x,y);
        }
    }

    /** Initially render the enemy by activating it and determining initial values
     */
    public void render(){
        choosePoint();
        chooseDirection();
        active = true;
        draw();
    }

    /** finds the enemy's x location
     * @return int the enemy's horizontal location
     */
    public int getX() {
        return x;
    }
    /** finds the enemy's y location
     * @return int the enemy's vertical location
     */
    public int getY() {
        return y;
    }

    /** take the enemy off the screen and deactivate it
     */
    public void deactivate(){
        active = false;
        completed = true;
    }

    /** Determine whether a collision has occurred between an enemy and a note
     * @param noteX the note's x location
     * @param noteY the note's y location
     * @return boolean whether the note and the enemy have collided or not
     */
    public boolean collide(int noteX, int noteY){
        if (distanceNote(noteX, noteY) <= COLLISION){
            return true;
        }
        return false;
    }

    /** Calculates the distance between an enemy and a note
     * @param noteX the note's x location
     * @param noteY the note's y location
     * @return double, the distance between the note and the enemy
     */
    public double distanceNote(int noteX, int noteY){
        return Math.sqrt(Math.pow((noteX - x), 2) - Math.pow((noteY - y), 2));
    }

    /** checks if an enemy is active
     * @return boolean indicator of whether or not the enemy is active
     */
    public boolean isActive() {
        return active;
    }
}
