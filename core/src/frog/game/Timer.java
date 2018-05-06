package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Timer is a visually represented timer.
 *
 * <p>Timer is meant to be used in the class Level, the class of the application which contains the gameplay.
 * It's purpose is to show the user how long Xe has been playing the level in real time.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0409
 */

public class Timer {
    private Texture numberSheet;
    private TextureRegion colon;
    private TextureRegion[] numberSplitSheet1D;
    private TextureRegion[][] numberSplitSheet2D;
    private TextureRegion[] timerTextures;
    private ArrayList<Rectangle> timerRectangles;

    private final float WINDOW_WIDTH;
    private final float WINDOW_HEIGHT;

    private int timerMinutes = 0;
    private int timerSeconds = 0;
    private float singleSecondCounter;
    private String timeString = "";

    /**
     * The constructor of Timer
     *
     * Initializes the timer's textures and the position in XY-coordinates.
     *
     * @param WINDOW_WIDTH the width of the window where the timer is used.
     * @param WINDOW_HEIGHT the width of the window where the timer is used.
     */
    public Timer(float WINDOW_WIDTH, float WINDOW_HEIGHT) {
        this.WINDOW_WIDTH = WINDOW_WIDTH;
        this.WINDOW_HEIGHT = WINDOW_HEIGHT;
        
        numberSheet = new Texture("ui/timerSheet.png");
        colon = new TextureRegion(new Texture("ui/colon.png"));

        numberSplitSheet2D = TextureRegion.split(numberSheet,
                numberSheet.getWidth()/10,
                numberSheet.getHeight()/1);
        numberSplitSheet1D = convert2Dto1D(numberSplitSheet2D);
        initializeTimerTextures();
        
        singleSecondCounter = 0;
    }

    /**
     * Draws the timer.
     *
     * Iterates through the timer's different textures, e.g. 01:32, and draws them all at their set positions.
     *
     * @param batch the Spritebatch that draws the timer's textures
     */
    public void draw(SpriteBatch batch) {
        for (int i = 0; i < timerTextures.length; i++) {
            batch.draw(timerTextures[i],
                    timerRectangles.get(i).x,
                    timerRectangles.get(i).y,
                    timerRectangles.get(i).width,
                    timerRectangles.get(i).height);
        }
    }

    /**
     * Updates the time of the timer.
     *
     * Counts time based on the delta time of rendering in to singleSecondCounter, which calculates single seconds.
     * When a single second has passed through the singleSecondCounter, the timer's seconds updated.
     * If the timer's seconds reaches a minute, the timer's minutes are updated.
     * The textures of the timer are updated to match the passed time.
     *
     * @param delta the render delta time, used to calculate time.
     */
    public void update (float delta) {
        singleSecondCounter += delta;

        if (singleSecondCounter >= 1) {
            timerSeconds += 1;
            singleSecondCounter = 0;

            if (timerSeconds >= 60) {
                timerMinutes +=1;
                timerSeconds = 0;
            }

            String secondsString, minutesString;

            if (timerSeconds < 10) {
                secondsString = "0" + timerSeconds;
            } else {
                secondsString = Integer.toString(timerSeconds);
            }

            if (timerMinutes < 10) {
                minutesString = "0" + timerMinutes;
            } else {
                minutesString = Integer.toString(timerMinutes);
            }

            timeString = minutesString + ":" + secondsString;

            if (timeString.equals("99:59")) {
                timeString = "00:00";
            }

            for (int i=0; i < timeString.length(); i++) {
                if (timeString.charAt(i) != ':') {
                    int sheetIndex = Integer.parseInt(Character.toString(timeString.charAt(i)));
                    timerTextures[i] = numberSplitSheet1D[sheetIndex];
                }
            }
        }

    }

    /**
     * Initializes the timer's textures.
     *
     * Initializes the arrays containing the timer's textures and XY positions. Presents textures representing 00:00.
     */
    private void initializeTimerTextures() {
        timerTextures = new TextureRegion[5];
        timerRectangles = new ArrayList<Rectangle>(timerTextures.length);
        float rectangleHeight = WINDOW_HEIGHT* (4f/40f);
        float posY = WINDOW_HEIGHT - rectangleHeight - (WINDOW_HEIGHT * (1f/40f));
        float posX = WINDOW_WIDTH - (WINDOW_WIDTH * (1f/40f));

        for (int i = 0; i < timerTextures.length; i++) {
            if (i==2) {
                timerTextures[i] = colon;
                timerRectangles.add(new Rectangle());
                timerRectangles.get(i).setWidth(
                        (timerTextures[i].getRegionWidth() * rectangleHeight) / timerTextures[i].getRegionHeight());
                timerRectangles.get(i).setHeight(rectangleHeight);
                timerRectangles.get(i).setY(posY);
            } else {
                timerTextures[i] = numberSplitSheet1D[0];
                timerRectangles.add(new Rectangle());
                timerRectangles.get(i).setWidth(
                        (timerTextures[i].getRegionWidth() * rectangleHeight) / timerTextures[i].getRegionHeight());
                timerRectangles.get(i).setHeight(rectangleHeight);
                timerRectangles.get(i).setY(posY);
            }
        }

        for (Rectangle timerRectangle : timerRectangles) {
            posX -= timerRectangle.width;
        }

       for (Rectangle timerRectangle : timerRectangles) {
            timerRectangle.setX(posX);
            posX += timerRectangle.width;
       }
    }


    /**
     * Converts a two-dimensional TextureRegion array into a one-dimensional array
     *
     * @param twoDim the two-dimensional TextureRegion array
     * @return the one-dimensional TextureRegion array
     */
    private TextureRegion[] convert2Dto1D (TextureRegion[][] twoDim) {
        TextureRegion [] oneDim = new TextureRegion[twoDim.length * twoDim[0].length];
        int index = 0;

        for (int i = 0; i < twoDim.length; i++) {
            for (int j = 0; j < twoDim[i].length; j++) {
                oneDim[index++] = twoDim[i][j];
            }
        }

        return oneDim;
    }

    /**
     * Subtracts time from the timer.
     *
     * Is called in the game when the player clears a time coin, in which case time is subtracted from timer.
     * Warning: time subtracted should not be equal to or greater than a minute.
     *
     * @param amount the time that should be subtracted
     */
    public void subtractTime(int amount) {
        //In any case if seconds greater or equal to five
        if (timerSeconds >= Math.abs(amount)) {
            timerSeconds -= Math.abs(amount);
        }
        //If seconds below five but minutes equal one or more
        else if (timerMinutes >= 1) {
            timerMinutes -= 1;
            timerSeconds = 60 + timerSeconds - Math.abs(amount);
        }
        //If minutes are equal to 0 and seconds lesser than five
        else {
            timerSeconds = 0;
        }
    }

    public String getTimeString() {
        return timeString;
    }

    /**
     * Disposes the timer's textures from memory.
     */
    public void dispose() {
        numberSheet.dispose();
        colon.getTexture().dispose();
    }
}
