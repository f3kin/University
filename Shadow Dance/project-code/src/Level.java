import bagel.Input;

/**
 * Class to delineate the three levels of Shadow Dance
 * @author Finlay Ekins
 */
public class Level {
    private final String fileName;
    protected final int levelNum;
    private final int winsScore;
    protected boolean thirdLevel = false;
    private int numLanes;
    protected Lane[] lanes = new Lane[4];
    private final Accuracy accuracy = new Accuracy();
    protected int score = 0;
    protected int multiplier = 1;
    protected int scoreFrame = 0;
    private final Track track;

    /** This is the constructor for the Level class
     * @param fileName This is the file name of the level
     * @param levelNum This is the number corresponding to the level
     * @param winsScore This is the score required to beat the level
     * @param trackName This is the file name of the level's track
     */
    public Level(String fileName, int levelNum, int winsScore, String trackName) {
        this.fileName = fileName;
        this.levelNum = levelNum;
        this.winsScore = winsScore;
        this.track = new Track(trackName);
    }

    /** These get and set various values related to the level class
     */
    public int getWinsScore() {
        return winsScore;
    }

    public String getFileName() {
        return fileName;
    }

    public void setNumLanes(int numLanes) {
        this.numLanes = numLanes;
    }

    public int getNumLanes() {
        return numLanes;
    }

    public void setLanes(Lane[] lanes) {
        this.lanes = lanes;
    }

    public Lane[] getLanes() {
        return lanes;
    }

    public Track getTrack() {
        return track;
    }

    public Accuracy getAccuracy() {
        return accuracy;
    }

    public void addScore(int currScore) {
        score += currScore * multiplier;
    }

    public int getScore() {
        return score;
    }

    public void updateEffects(Lane currLane){
        scoreFrame++;
        checkScore();
        if (currLane.isSpeedEffect()){
            fastEffect();
            currLane.setSpeedEffect(false);
        }
        else if (currLane.isSlowEffect()){
            slowEffect();
            currLane.setSlowEffect(false);
        }
        else if (currLane.isScoreEffect()){
            scoreEffect();
            currLane.setScoreEffect(false);
        }
    }

    /** This applies the speed up effect to each lane in the level
     */
    public void fastEffect(){
        for(int i = 0; i < numLanes;i ++){
            lanes[i].extraSpeed += 1;
        }
        score += 15;
    }
    /** This applies the slow down effect to each lane in the level
     */
    public void slowEffect(){
        for(int i = 0; i < numLanes;i ++){
            lanes[i].extraSpeed -= 1;
        }
        score += 15;
    }
    /** This applies the double score effect to each lane in the level
     */
    public void scoreEffect(){
        multiplier *= 2;
        scoreFrame = 0;
    }

    /** This ensures the double score effect lasts for the desired amount of frames
     */
    public void checkScore(){
        if (multiplier != 1){
            if (scoreFrame > DoubleScore.FRAME_DURATION){
                multiplier = 1;
            }
        }
    }

    /** Method to update the level subjects that is overriden by the level three class
     */
    public void updateSubObjects(int currFrame, Input input){}
}
