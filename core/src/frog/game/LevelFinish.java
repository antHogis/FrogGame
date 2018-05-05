package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Anton on 4.4.2018.
 */

public class LevelFinish extends ScreenAdapter {
    private FrogMain host;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private final float WINDOW_WIDTH, WINDOW_HEIGHT;

    private Vector2 scoreTextPos;
    private Vector2 scoreTimerPos;
    private Vector2 timeTwoStarsPos;
    private Vector2 timeThreeStarsPos;

    private BitmapFont font;
    private Texture background;

    private final String identifier, timeString, TIME_TWO_STARS, TIME_THREE_STARS;
    private String score, starsEqual;
    private Array<Star> stars;
    private GenericButton homeButton;
    private GenericButton nextLevelButton;

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

        font = new BitmapFont(Gdx.files.internal("ui/fonts/patHand90.txt"));
        background = new Texture(ConstantsManager.bgLevelFinishPath);

        createUI();
        updateScoreboard();
/*
        for (int i = 1; i <= 5; i++) {
            Gdx.app.log("Level " + identifier + " scoreboard",
                    ConstantsManager.settings.getString(identifier+"_top_"+i, "derp"));
        }
*/

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
            drawUI();
        batch.end();
    }

    @Override
    public void dispose() {
        font.dispose();
        for (Star star : stars) {
            star.dispose();
        }
        homeButton.dispose();
        if (nextLevelButton != null) {
            nextLevelButton.dispose();
        }
    }

    private void drawUI() {
        batch.draw(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        font.draw(batch, score, scoreTextPos.x, scoreTextPos.y);
        font.draw(batch, timeString, scoreTimerPos.x, scoreTimerPos.y);
        font.draw(batch, starsEqual + TIME_THREE_STARS, timeThreeStarsPos.x, timeThreeStarsPos.y);
        font.draw(batch, starsEqual + TIME_TWO_STARS, timeTwoStarsPos.x, timeTwoStarsPos.y);
        drawStars();
        homeButton.draw(batch);
        if (nextLevelButton != null) {
            nextLevelButton.draw(batch);
        }
    }

    private void createUI() {
        float starWidth, xPos, yPos;
        stars = new Array<Star>(9);
        starsEqual = " = ";

        //First row (text Your score:)
        score = host.getMyBundle().get("score");
        GlyphLayout glyph = new GlyphLayout(font, score);
        xPos = WINDOW_WIDTH/2 - glyph.width/2;
        yPos =  WINDOW_HEIGHT*(39f/40f);
        scoreTextPos = new Vector2(xPos, yPos);

        //Second row (the time scored in a level)
        yPos -= glyph.height + WINDOW_HEIGHT * (1f/40f);
        glyph = new GlyphLayout(font, timeString);
        xPos = WINDOW_WIDTH/2 - glyph.width/2;
        scoreTimerPos = new Vector2(xPos, yPos);

        //Third row (the stars scored from the time)
        starWidth = WINDOW_WIDTH * (4f/40f);
        xPos = WINDOW_WIDTH / 2 - starWidth*1.5f;
        yPos -= glyph.height + (WINDOW_HEIGHT * (1f/40f)) + starWidth;
        createStars(calculateStars(), starWidth, xPos, yPos);

        //Fourth row (three golden stars, and the time needed to score three stars)
        starWidth = WINDOW_WIDTH * (2f/40f);
        yPos -= starWidth + WINDOW_HEIGHT * (2f/40f);
        glyph = new GlyphLayout(font, starsEqual + TIME_THREE_STARS);
        xPos = WINDOW_WIDTH/2 - (starWidth*3 + glyph.width)/2;
        createStars(3, starWidth, xPos, yPos);
        xPos += starWidth*3;
        yPos += starWidth/2 + glyph.height/2;
        timeThreeStarsPos = new Vector2(xPos, yPos);

        //Fifth row (two golden and one grey star, and the time needed to score two stars)
        yPos -= glyph.height + WINDOW_HEIGHT * (2f/40f) + starWidth;
        glyph = new GlyphLayout(font, starsEqual + TIME_TWO_STARS);
        xPos = WINDOW_WIDTH/2 - (starWidth*3 + glyph.width)/2;
        createStars(2, starWidth, xPos, yPos);
        xPos += starWidth*3;
        yPos += starWidth/2 + glyph.height/2;
        timeTwoStarsPos = new Vector2(xPos, yPos);

        //Bottom middle (Next level button)
        if (identifier != formatLevelNumber(ConstantsManager.LEVELS_AMOUNT)) {
            String path = host.getMyBundle().get("button_nextLevel");
            String idle = "idle.png";
            String pressed= "pressed.png";
            nextLevelButton = new GenericButton(WINDOW_WIDTH * (8f/40f), path + idle,path + pressed);
            nextLevelButton.setX(WINDOW_WIDTH/2 - nextLevelButton.getWidth()/2);
            nextLevelButton.setY(WINDOW_HEIGHT * (2/40f));
        }

        //Top left (Home button)
        homeButton = new GenericButton(WINDOW_WIDTH * (4f/40f),
                ConstantsManager.homeButtonIdlePath, ConstantsManager.homeButtonPressedPath);
        homeButton.setY(WINDOW_HEIGHT - homeButton.getHeight());
    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            Vector3 touch;
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (homeButton.getRectangle().contains(touch.x,touch.y)) homeButton.setPressed(true);
                else homeButton.setPressed(false);

                if (nextLevelButton != null) {
                    if (nextLevelButton.getRectangle().contains(touch.x,touch.y)) nextLevelButton.setPressed(true);
                    else nextLevelButton.setPressed(false);
                }
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (homeButton.getRectangle().contains(touch.x,touch.y)) homeButton.setPressed(true);
                else homeButton.setPressed(false);

                if (nextLevelButton != null) {
                    if (nextLevelButton.getRectangle().contains(touch.x,touch.y)) nextLevelButton.setPressed(true);
                    else nextLevelButton.setPressed(false);
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);
                if (homeButton.getRectangle().contains(touch.x,touch.y)) {
                    SoundController.playClickSound();
                    homeButton.setPressed(false);
                    LevelFinish.this.dispose();
                    host.setScreen(new MainMenu(host));
                }

                if (nextLevelButton != null) {
                    if (nextLevelButton.getRectangle().contains(touch.x,touch.y)) {
                        SoundController.playClickSound();
                        nextLevelButton.setPressed(false);
                        LevelFinish.this.dispose();
                        host.setScreen(createNextLevel());
                    }
                }
                return true;
            }
        });
    }

    private void drawStars() {
        for (Star star : stars) {
            star.draw(batch);
        }
    }

    private void createStars(int goldenStars, float starWidth, float xPos, float yPos) {
        int greyStars = 3 - goldenStars;

        Gdx.app.log("Golden:", Integer.toString(goldenStars));
        Gdx.app.log("Grey:", Integer.toString(greyStars));

        
        for (int i = 0; i < goldenStars; i++) {
            stars.add(new Star(starWidth, true));
            stars.get(stars.size-1).setX(xPos);
            stars.get(stars.size-1).setY(yPos);
            xPos += starWidth;
        }

        for(int i = 0; i < greyStars; i++) {
            stars.add(new Star(starWidth, false));
            stars.get(stars.size-1).setX(xPos);
            stars.get(stars.size-1).setY(yPos);
            xPos += starWidth;
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
                    //Pushing scores scores down in top 5
                    for (int j = ConstantsManager.TOP_SCORES_AMOUNT; j > i; j--) {
                        key = identifier + "_top_" + (j-1);
                        String temp = ConstantsManager.settings.getString(key, null);
                        key = identifier + "_top_" + j;
                        ConstantsManager.settings.putString(key, temp).flush();
                    }
                    //Saving score to top 5 at i
                    key = identifier + "_top_" + i;
                    ConstantsManager.settings.putString(key, timeString);
                    ConstantsManager.settings.flush();
                    scoreSaved = true;


                }
            }
        }

        //Saving the stars scored in the level to persistent memory
        String key = identifier + "_stars";
        int goldenStars = calculateStars();
        if (goldenStars > ConstantsManager.settings.getInteger(key, 0)) {
            ConstantsManager.settings.putInteger(key, goldenStars).flush();
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

    private Level createNextLevel() {
        String nextId = formatLevelNumber(Integer.parseInt(identifier) + 1);
        
        String mapPath
                = ConstantsManager.levels.get(nextId + "_mapPath");
        int AMOUNT_ROUNDFISH
                = Integer.parseInt(ConstantsManager.levels.get(nextId + "_AMOUNT_ROUNDFISH"));
        int AMOUNT_LONGFISH
                = Integer.parseInt(ConstantsManager.levels.get(nextId + "_AMOUNT_LONGFISH"));
        int AMOUNT_OCTOPUS1
                = Integer.parseInt(ConstantsManager.levels.get(nextId + "_AMOUNT_OCTOPUS1"));
        int AMOUNT_OCTOPUS2
                = Integer.parseInt(ConstantsManager.levels.get(nextId + "_AMOUNT_OCTOPUS2"));
        int TILE_WIDTH
                = Integer.parseInt(ConstantsManager.levels.get(nextId + "_TILE_AMOUNT_WIDTH"));
        int TILE_HEIGHT
                = Integer.parseInt(ConstantsManager.levels.get(nextId + "_TILE_AMOUNT_HEIGHT"));

        return new Level(host,
                nextId,
                mapPath,
                AMOUNT_ROUNDFISH,
                AMOUNT_LONGFISH,
                AMOUNT_OCTOPUS1,
                AMOUNT_OCTOPUS2,
                TILE_WIDTH,
                TILE_HEIGHT);
    }

    private String formatLevelNumber(int number) {
        if (number < 10) {
            return  "0" + number;
        } else {
            return Integer.toString(number);
        }
    }

}
