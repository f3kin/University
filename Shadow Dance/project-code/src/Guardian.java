import bagel.Image;
import bagel.Keys;
import bagel.util.Point;

/**
 * Class to represent the guardian sub object of level three
 * @author Finlay Ekins
 */
public class Guardian{
    private final Image image = new Image("res/guardian.png");
    protected static final Keys relevantKey = Keys.LEFT_SHIFT;
    protected static Point PRINT_LOCATION = new Point(800, 600);

    /** Method to draw the guardian image
     */
    public void draw(){
        image.draw(PRINT_LOCATION.x, PRINT_LOCATION.y);
    }

    /** Determines the distance between an enemy and the guardian
     * @param enemy, enemy which is being checked for distance
     * @return double, the distance between the guardian and the enemy
     */
    public double distance(Enemy enemy){
        return Math.sqrt(Math.pow((PRINT_LOCATION.x - enemy.getX()), 2) - Math.pow((PRINT_LOCATION.y - enemy.getY()), 2));
    }
}
