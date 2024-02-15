import bagel.Input;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PrimitiveIterator;

/**
 * Class to represent level three of the game
 * @author Finlay Ekins
 */

public class LevelThree extends Level {
    protected ArrayList<Enemy> enemies;
    protected int numEnemies = 0;
    protected int numProjectiles = 0;
    private int prevCurrFrame = 0;
    private Guardian guardian;
    private ArrayList<Projectile> projectiles;
    private final int MAX_DIST = 1000;

    /** Constructor for the level three class
     * @param filename This is the filename of the class
     * @param levelNum This is the level number
     * @param winScore This is the score required to win the level
     * @param trackname This is the track for the level
     */
    public LevelThree(String filename, int levelNum, int winScore, String trackname) {
        super(filename, levelNum, winScore, trackname);
        thirdLevel = true;
        this.guardian = new Guardian();
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
    }
    @Override
    /** This updates the subobjects of level three
     * @param currFrame This is the current frame of the level
     * @param input This is the current level input
     */
    public void updateSubObjects(int currFrame, Input input) {
        guardian.draw();
        for(Enemy other: enemies) {
            if (other.completed){
                continue;
            }
            other.update();
            for(Projectile projectile: projectiles){
                projectile.hasCollided(other.getX(), other.getY());
            }
        }
        checkEnemyCollision();
        if (currFrame % Enemy.FREQUENCY == 0 && prevCurrFrame % Enemy.FREQUENCY != 0){
            addEnemy();
        }
        if(prevCurrFrame != currFrame) {
            if (input.wasPressed(Guardian.relevantKey) && (numEnemies != 0)) {
                shootProjectile();
            }
        }
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile item = iterator.next();
            if (item.completed){
                continue;
            }
            Enemy closest = findClosest();
            item.update(closest);
        }
        prevCurrFrame = currFrame;
    }

    /** This adds an enemy to the level three screen
     */
     public void addEnemy(){
        Enemy curr = new Enemy();
        enemies.add(curr);
        numEnemies++;
     }

    /** This determines the closest enemy to the guardian
     * @return Enemy this is the closest enemy to the guardian
     */
     public Enemy findClosest(){
         int closestIndex = 0;
         for (int i = 0; i < numEnemies; i++){
             if (enemies.get(i).completed){
                 continue;
             }
             closestIndex = i;
             break;
         }
         double distance = MAX_DIST;
         for (int i = 0; i < numEnemies; i++){
             Enemy currEnemy = enemies.get(i);
             if (currEnemy.completed){
                 continue;
             }
             if (guardian.distance(currEnemy) < distance){
                 distance = guardian.distance(enemies.get(i));
                 closestIndex = i;
             }
         }
         System.out.println(closestIndex);
         return enemies.get(closestIndex);
     }

    /** This checks if an enemy has collided with any normal notes
     */
    public void checkEnemyCollision(){
         for (Enemy enemy: enemies) {
             if (!enemy.isActive()){
                 continue;
             }
             for(Lane lane: lanes){
                 if (lane == null){
                     break;
                 }
                 for (Note note: lane.notes){
                     if ((note == null)){
                         break;
                     }
                     if (note.isActive() && enemy.collide(lane.getLocation(),note.getY())){
                         note.deactivate();
                     }
                 }
             }
         }
     }

    /** This fires a projectile from the guardian
     */
    public void shootProjectile(){
        Enemy closest = findClosest();
        Projectile currProjectile = new Projectile(closest);
        numProjectiles++;
        currProjectile.render();
        projectiles.add(currProjectile);
    }
}