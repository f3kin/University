import bagel.*;

/**
 * Class to represent and deal with the projectiles in level three
 * @author Finlay Ekins
 */
public class Projectile {
    private final Image image = new Image("res/arrow.png");
    private final int STEP_SIZE = 6;
    private final int COLLISION_DIST = 62;
    private int multiplier = 1;
    private final DrawOptions current = new DrawOptions();
    private Enemy closest;
    private double x = Guardian.PRINT_LOCATION.x;
    private double y = Guardian.PRINT_LOCATION.y;
    private boolean active = false;
    private double rotation;
    protected boolean completed = false;

    /** This is the constructor for the projectile object
     * @param closest This is the enemy closest to the projectile (the target)
     */
    public Projectile(Enemy closest){
        this.closest = closest;
    }

    /** This finds and sets the image rotation of the projectile
     * @param enemyX This is the is location of the closest enemy - x
     * @param enemyY This is the is location of the closest enemy - y
     */
    public void findRotation (double enemyX, double enemyY){
        double xLen = enemyX - Guardian.PRINT_LOCATION.x;
        double yLen = enemyY - Guardian.PRINT_LOCATION.y;
        double adjustFactor = 0;
        if(xLen < 0){
            adjustFactor = Math.PI;
            multiplier = -1;
        }
        rotation = Math.atan(yLen/xLen);
        current.setRotation((adjustFactor + rotation) % (2.0*Math.PI));
    }

    /** The projectile has collided with the enemy, so they should be deactivated
     */
    public void hasCollided(int enemyX, int enemyY){
        if(distanceEnemy(enemyX, enemyY) <= COLLISION_DIST){
            active = false;
            completed = true;
            closest.deactivate();
        }
    }

    /** This draws the projectile image
     */
    public void draw() {
        if(active){
            image.draw(x, y, current);
        }

    }

    /** This initialises the projectile object for the first time
     */
    public void render() {
        active = true;
        findRotation(closest.getX(), closest.getY());
        draw();
    }

    /** This updates the projectile object image
     * @param closest This is the enemy closest to the projectile (the target)
     */
    public void update(Enemy closest){
        this.closest = closest;
        checkBounds();
        if (active){
            x += STEP_SIZE*Math.cos(rotation)*multiplier;
            y += STEP_SIZE*Math.sin(rotation)*multiplier;
            draw();
        }
    }

    /** This checks whether a projectile is active
     * @return boolean This indicates if the projectile is active
     */
    public boolean isActive() {
        return active;
    }

    /** This finds the distance of the enemy from the projectile
     * @return double This is the distance to the enemy from the projectile
     */
    public double distanceEnemy(int enemyX, int enemyY){
        double distance = Math.sqrt(Math.pow((closest.getX() - x), 2) - Math.pow((closest.getY() - y), 2));
        return distance;
    }

    /** This checks whether the projectile is still in the bounds of the screen
     */
    public void checkBounds(){
        if (x >= Window.getWidth() || x <= 0){
            active = false;
        }
        if (y >= Window.getHeight() || y <= 0){
            active = false;
        }
    }
}

