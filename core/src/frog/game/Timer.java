package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by Anton on 9.4.2018.
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

    public Timer(float WINDOW_WIDTH, float WINDOW_HEIGHT) {
        this.WINDOW_WIDTH = WINDOW_WIDTH;
        this.WINDOW_HEIGHT = WINDOW_HEIGHT;
        
        numberSheet = new Texture("ui/timerSheet3.png");
        colon = new TextureRegion(new Texture("ui/colon3.png"));

        //font = new BitmapFont(Gdx.files.internal("ui/fonts/patHand120.txt"));

        numberSplitSheet2D = TextureRegion.split(numberSheet,
                numberSheet.getWidth()/10,
                numberSheet.getHeight()/1);
        numberSplitSheet1D = convert2Dto1D(numberSplitSheet2D);
        initializeTimerTextures();
        
        singleSecondCounter = 0;
    }

    public void draw(SpriteBatch batch) {

        for (int i = 0; i < timerTextures.length; i++) {
            batch.draw(timerTextures[i],
                    timerRectangles.get(i).x,
                    timerRectangles.get(i).y,
                    timerRectangles.get(i).width,
                    timerRectangles.get(i).height);
        }

       /* batch.draw(timerTextures[0], timerX, timerY);

        for (int i=1; i < timerTextures.length; i++) {
            timerX += timerTextures[i].getRegionWidth();
            batch.draw(timerTextures[i], timerX, timerY);
        }*/
        /*font.draw(batch, timeString, timerX, timerY);*/

    }

    public void update (float delta) {
        singleSecondCounter += delta;
        if (singleSecondCounter >= 1) {
            timerSeconds += 1;
            singleSecondCounter = 0;
        }
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
                String numberAtIndex = "" + timeString.charAt(i);
                int sheetIndex = Integer.parseInt(Character.toString(timeString.charAt(i)));
                timerTextures[i] = numberSplitSheet1D[sheetIndex];
            }
        }
    }

    private void initializeTimerTextures() {
        timerTextures = new TextureRegion[5];
        timerRectangles = new ArrayList<Rectangle>(5);
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

    public void dispose() {
        numberSheet.dispose();
    }
}
