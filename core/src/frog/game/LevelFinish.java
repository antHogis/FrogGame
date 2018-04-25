package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Anton on 4.4.2018.
 */

public class LevelFinish extends ScreenAdapter {
    private FrogMain host;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private final float WINDOW_WIDTH, WINDOW_HEIGHT;

    private Vector2 textPos;
    String completed;
    private BitmapFont font;

    private final String identifier, timeString, TIME_TWO_STARS, TIME_THREE_STARS;
    private Array<Star> stars;

    private int timerMinutes, timerSeconds;


    public LevelFinish(FrogMain host,
                       String identifier,
                       String timeString) {
        this.host = host;
        batch = host.getBatch();
        camera = host.getCamera();
        WINDOW_WIDTH = camera.viewportWidth;
        WINDOW_HEIGHT = camera.viewportHeight;

        this.identifier = identifier;
        this.timeString = timeString;
        TIME_THREE_STARS = ConstantsManager.levels.get(identifier + "_TIME_THREE_STARS");
        TIME_TWO_STARS = ConstantsManager.levels.get(identifier + "_TIME_TWO_STARS");

        createStars();
        updateScoreboard();

        for (int i = 1; i <= 5; i++) {
            Gdx.app.log("Level " + identifier + " scoreboard",
                    ConstantsManager.settings.getString(identifier+"_top_"+i, "derp"));
        }

        font = new BitmapFont(Gdx.files.internal("ui/fonts/lato90.txt"));
        setTextPosition();

        setInputProcessor();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.658f, 0.980f, 0.980f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch,completed,textPos.x,textPos.y);

        drawStars();
        batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
        for (Star star : stars) {
            star.dispose();
        }
    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                LevelFinish.this.dispose();
                host.setScreen(new LevelSelect(host));
                return true;
            }
        });
    }

    private void setTextPosition() {
        completed = ConstantsManager.myBundle.format("completed") + timeString;
        //completed = "Kenttä läpäisty ajassa " + timeString;
        GlyphLayout glyph = new GlyphLayout(font, completed);
        textPos = new Vector2();
        textPos.set(WINDOW_WIDTH/2-glyph.width/2, WINDOW_HEIGHT*(3f/4f));
    }

    private void drawStars() {
        for (Star star : stars) {
            star.draw(batch);
        }
    }

    private void createStars() {
        int goldenStars = calculateStars();
        int greyStars = 3 - goldenStars;

        Gdx.app.log("Golden:", Integer.toString(goldenStars));
        Gdx.app.log("Grey:", Integer.toString(greyStars));
        stars = new Array<Star>(3);

        float starWidth = WINDOW_WIDTH * (4f/16f);
        float xPos = WINDOW_WIDTH * (1f/16f);  
        float yPos = WINDOW_HEIGHT * (1f/16f);
        float nextX = xPos + starWidth;
        
        for (int i = 0; i < goldenStars; i++) {
            stars.add(new Star(starWidth, true));
            stars.get(stars.size-1).setX(xPos);
            stars.get(stars.size-1).setY(yPos);
            xPos += nextX;
        }

        for(int i = 0; i < greyStars; i++) {
            stars.add(new Star(starWidth, false));
            stars.get(stars.size-1).setX(xPos);
            stars.get(stars.size-1).setY(yPos);
            xPos += nextX;
        }

        String key = identifier + "_stars";
        if (goldenStars > ConstantsManager.settings.getInteger(key, 0)) {
            ConstantsManager.settings.putInteger(key, goldenStars).flush();
        }
    }

    private void updateScoreboard() {
        boolean scoreSaved = false;

        for (int i = 1; i <= ConstantsManager.TOP_SCORES_AMOUNT && !scoreSaved; i++) {
            String key = identifier + "_top_" + i;
            String topScore = ConstantsManager.settings.getString(key, null);

            if (topScore == null) {
                ConstantsManager.settings.putString(key, timeString).flush();
                scoreSaved = true;
            } else {
                int topScoreMinutes = parseTimeString(topScore, true);
                int topScoreSeconds = parseTimeString(topScore, false);

                if (timerMinutes <= topScoreMinutes && timerSeconds < topScoreSeconds) {
                    ConstantsManager.settings.putString(key, timeString).flush();
                    scoreSaved = true;
                }
            }
        }
    }

    private int calculateStars() {
        int threeStarMinutes = parseTimeString(TIME_THREE_STARS, true);
        int threeStarSeconds = parseTimeString(TIME_THREE_STARS, false);
        int twoStarMinutes = parseTimeString(TIME_TWO_STARS, true);
        int twoStarSeconds = parseTimeString(TIME_TWO_STARS, false);

        timerMinutes = parseTimeString(timeString, true);
        timerSeconds = parseTimeString(timeString, false);

        if (timerMinutes < threeStarMinutes) {
            return 3;
        } else if (timerMinutes <= threeStarMinutes && timerSeconds <= threeStarSeconds) {
            return 3;
        } else if(timerMinutes < twoStarMinutes) {
            return 2;
        } else if(timerMinutes <= twoStarMinutes && timerSeconds <= twoStarSeconds) {
            return 2;
        } else {
            return 1;
        }
    }

    private int parseTimeString(String timeString, boolean minutes) {
        if (minutes) {
            String tempString = "";
            for (int i=0; i < timeString.length(); i++) {
                if(timeString.charAt(i) != ':') tempString += timeString.charAt(i);
                else return Integer.parseInt(tempString);

            }
        } else {
            String tempString = "";
            boolean found = false;
            for (int i=0; i < timeString.length(); i++) {
                if(found) tempString += timeString.charAt(i);
                if(timeString.charAt(i) == ':') found = true;
            }
            return Integer.parseInt(tempString);
        }
        return 0;
    }

}
