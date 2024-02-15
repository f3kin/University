import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Solution for SWEN20003 Project 2, Semester 2, 2023
 * Based off Stella Li's implementation of Project 1
 *
 * @author Finlay Ekins
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    protected final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final String LEVEL_1_CSV = "res/level1.csv";
    private final String LEVEL_2_CSV = "res/level2.csv";
    private final String LEVEL_3_CSV = "res/level3.csv";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int INS_Y_OFFSET = 190;
    private final static int SCORE_LOCATION = 35;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private static final String INSTRUCTIONS = "Press Space to Start\nUse Arrow Keys to Play";
    private static final int LEVEL_1_CLEAR_SCORE = 150;
    private static final int LEVEL_2_CLEAR_SCORE = 400;
    private static final int LEVEL_3_CLEAR_SCORE = 350;
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private static int currFrame = 0;
    private boolean started = false;
    private boolean finished = false;
    private boolean paused = false;
    private String RETURN_MESSAGE = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private Level level;
    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    private void readCsv(String fileName) {
        int numLanes = 0;
        Lane[] lanes = new Lane[4];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    Lane lane = new Lane(laneType, pos);
                    lanes[numLanes++] = lane;
                } else {
                    String dir = splitText[0];
                    Lane lane = null;
                    for (int i = 0; i < numLanes; i++) {
                        if (lanes[i].getType().equals(dir)) {
                            lane = lanes[i];
                        }
                    }
                    if (lane != null) {
                        switch (splitText[1].substring(0,4)) {
                            case "Norm":
                                Note note = new Note(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(note);
                                break;
                            case "Hold":
                                HoldNote holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addHoldNote(holdNote);
                                break;
                            case "Maki":
                                String type = splitText[1].split("\\.")[1];
                                SpecialNote specialNote = null;
                                switch (type) {
                                    case "Bomb":
                                        specialNote = new Bomb(Integer.parseInt(splitText[2]), type);
                                        break;
                                    case "SpeedUp":
                                        specialNote = new SpeedUp(Integer.parseInt(splitText[2]), type);
                                        break;
                                    case "SlowDown":
                                        specialNote = new SlowDown(Integer.parseInt(splitText[2]), type);
                                        break;
                                }
                                lane.addSpecialNote(specialNote);
                                break;
                            case "Doub":
                                SpecialNote specialNote1 = new DoubleScore(Integer.parseInt(splitText[2]), splitText[1]);
                                lane.addSpecialNote(specialNote1);
                                break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        level.setLanes(lanes);
        level.setNumLanes(numLanes);
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            // starting screen
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            level = null;
            INSTRUCTION_FONT.drawString("SELECT LEVELS WITH", TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
            INSTRUCTION_FONT.drawString("NUMBER KEYS", TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET + 25);
            INSTRUCTION_FONT.drawString("    1       2       3", TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET + 70);

            if (input.wasPressed(Keys.NUM_1)) {
                //track.start();
                level = new Level(LEVEL_1_CSV, 1, LEVEL_1_CLEAR_SCORE, "track1.wav");
                readCsv(level.getFileName());
                started = true;
            }
            else if (input.wasPressed(Keys.NUM_2)){
                level = new Level(LEVEL_2_CSV, 2, LEVEL_2_CLEAR_SCORE, "track2.wav");
                readCsv(level.getFileName());
                started = true;
            }
            else if (input.wasPressed(Keys.NUM_3)){
                level = new LevelThree(LEVEL_3_CSV, 3, LEVEL_3_CLEAR_SCORE, "track3.wav");
                readCsv(level.getFileName());
                started = true;
            }

        } else if (finished) {
            // end screen
            if (level.getScore() >= level.getWinsScore()) {
                TITLE_FONT.drawString(CLEAR_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(CLEAR_MESSAGE)/2,
                        WINDOW_HEIGHT/2);
            } else {
                TITLE_FONT.drawString(TRY_AGAIN_MESSAGE,
                        WINDOW_WIDTH/2 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2,
                        WINDOW_HEIGHT/2);
            }
            INSTRUCTION_FONT.drawString(RETURN_MESSAGE,WINDOW_WIDTH/2 - INSTRUCTION_FONT.getWidth(RETURN_MESSAGE)/2  ,500);
            if (input.wasPressed(Keys.SPACE)){
                finished = false;
                started = false;
            }
            currFrame = 0;
        }   else {
            // gameplay

            SCORE_FONT.drawString("Score " + level.getScore(), SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                    level.getTrack().run();
                }

                for (int i = 0; i < level.getNumLanes(); i++) {
                    level.getLanes()[i].draw();
                }

            } else {
                currFrame++;
                for (int i = 0; i < level.getNumLanes(); i++) {
                    Lane currLane = level.getLanes()[i];
                    level.addScore(currLane.update(input, level.getAccuracy()));
                    if (currLane.getType().equals("Special")){
                        level.updateEffects(currLane);
                    }
                    if (level.levelNum == 3){
                        level.updateSubObjects(currFrame, input);
                    }
                    //updates each lane
                }

                level.getAccuracy().update();
                //updates accuracy
                finished = checkFinished();
                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                    level.getTrack().pause();
                }
            }
        }

    }

    /**
     *
     * @return int current frame
     */
    public static int getCurrFrame() {
        return currFrame;
    }

    /**
     * @return boolean indicator of whether the level is finished or not
     */
    public boolean checkFinished() {
        for (int i = 0; i < level.getNumLanes(); i++) {
            if (!level.getLanes()[i].isFinished()) {
                return false;
            }
        }
        return true;
    }
}

