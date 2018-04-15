package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 9.4.2018.
 */

public class Timer {
    private Texture numberSheet;
    private TextureRegion colon;
    private TextureRegion[] numberSplitSheet1D;
    private TextureRegion[][] numberSplitSheet2D;
    private TextureRegion[] timerTextures;

    private final float WINDOW_WIDTH;
    private final float WINDOW_HEIGHT;
    private float timerWidth;
    private float timerHeight;
    private float timerX;
    private float timerY;

    private int timerMinutes = 0;
    private int timerSeconds = 0;
    private float singleSecondCounter;
    private String timeString = "";

    public Timer(float WINDOW_WIDTH, float WINDOW_HEIGHT) {
        numberSheet = new Texture("ui/numbers.png");
        colon = new TextureRegion(new Texture("ui/colon2.png"));

        numberSplitSheet2D = TextureRegion.split(numberSheet,
                numberSheet.getWidth()/10,
                numberSheet.getHeight()/1);
        numberSplitSheet1D = convert2Dto1D(numberSplitSheet2D);
        initializeTimerTextures();

        this.WINDOW_WIDTH = WINDOW_WIDTH;
        this.WINDOW_HEIGHT = WINDOW_HEIGHT;

        reset();
    }

    public void draw(SpriteBatch batch) {
        //Timerin yhteisleveys 292, x tulee 350 pikselin päähän oikeasta reunasta
        float placementX = WINDOW_WIDTH - 350;
        //Timerin korkeus 64, y tulee 100px päähän yläreunasta
        float placementY = WINDOW_HEIGHT- 100;

        batch.draw(timerTextures[0], placementX, placementY);

        for (int i=1; i < timerTextures.length; i++) {
            placementX += timerTextures[i].getRegionWidth();
            batch.draw(timerTextures[i], placementX, placementY);
        }

    }

    public void update () {
        if (System.currentTimeMillis()-singleSecondCounter >= 1000) {
            timerSeconds += 1;
            singleSecondCounter = System.currentTimeMillis();
        }
        if (timerSeconds >= 60) {
            timerMinutes +=1;
            timerSeconds = 0;
        }

        String secondsString, minutesString;

        if (timerSeconds < 10) {
            secondsString = "0" + timerSeconds;
        } else {
            secondsString = "" + timerSeconds;
        }

        if (timerMinutes < 10) {
            minutesString = "0" + timerMinutes;
        } else {
            minutesString = "" + timerMinutes;
        }

        timeString = minutesString + ":" + secondsString;

        if (timeString.equals("59:59")) {
            timeString = "00:00";
        }

        for (int i=0; i < timeString.length(); i++) {
            if (i != 2) {
                String numberAtIndex = "" + timeString.charAt(i);
                int sheetIndex = Integer.parseInt(numberAtIndex);
                timerTextures[i] = numberSplitSheet1D[sheetIndex];
            }
        }
    }

    public void reset(){
        singleSecondCounter = System.currentTimeMillis();
    }

    private void initializeTimerTextures() {
        timerTextures = new TextureRegion[5];
        timerHeight = 0;
        timerWidth = 0;

        for (int i = 0; i < timerTextures.length; i++) {
            if (i==2) {
                timerTextures[i] = colon;
            } else {
                timerTextures[i] = numberSplitSheet1D[0];
            }
            timerWidth += timerTextures[i].getRegionWidth();
        }
        timerHeight = timerTextures[0].getRegionHeight();

        timerX = WINDOW_WIDTH - timerWidth;
        timerY = WINDOW_HEIGHT - timerHeight;
    }

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

    public void subtractTime(int amount) {
        //In any case if seconds greater or equal to five
        if (timerSeconds >= 5) {
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

    public int getTimerMinutes() {
        return timerMinutes;
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }
}
