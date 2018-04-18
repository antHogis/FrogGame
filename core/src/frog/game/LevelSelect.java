package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by Anton on 18.4.2018.
 */

public class LevelSelect extends ScreenAdapter {
    private FrogMain host;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private final float WINDOW_WIDTH, WINDOW_HEIGHT;

    private Texture background;
    private HomeButton homeButton;
    private GenericButton arrowRight;
    private GenericButton arrowLeft;
    private ArrayList<Level> levels;
    private ArrayList<GenericButton> levelButtons;
    private ArrayList<Star> stars;
    private int levelView;

    public LevelSelect(FrogMain host) {
        this.host = host;
        camera = host.getCamera();
        batch = host.getBatch();
        WINDOW_WIDTH = camera.viewportWidth;
        WINDOW_HEIGHT = camera.viewportHeight;

        background = new Texture(Gdx.files.internal(ConstantsManager.bgGenericPath));
        createUI();
        setInputProcessor();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            drawUI();
        batch.end();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
        for (GenericButton levelButton : levelButtons) {
            levelButton.dispose();
        }
        for (Star star : stars) {
            star.dispose();
        }
    }

    private void createUI() {
        final float BUTTON_WIDTH = WINDOW_WIDTH*(6f/40f);
        final float ROW_FIRST_X = WINDOW_WIDTH*(6f/40f);
        final float MARGIN_X = BUTTON_WIDTH + WINDOW_WIDTH*(5f/40f);
        final float MARGIN_Y = WINDOW_HEIGHT*(3f/40f);
        float currentX = ROW_FIRST_X;
        float currentY = WINDOW_HEIGHT*(26f/40f);

        int columns = 3; int rows = 3;
        int buttonNumber = 1;

        levelButtons = new ArrayList<GenericButton>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                levelButtons.add(new GenericButton(BUTTON_WIDTH,
                        "ui/buttons/level/" + formatLevelNumber(buttonNumber) + ".png"));
                levelButtons.get(levelButtons.size()-1).setX(currentX);
                levelButtons.get(levelButtons.size()-1).setY(currentY);
                currentX += MARGIN_X;
                buttonNumber++;
            }
            currentX = ROW_FIRST_X;
            currentY -= MARGIN_Y + levelButtons.get(0).getHeight();
        }

        final float STAR_WIDTH = BUTTON_WIDTH/3;
        stars = new ArrayList<Star>();

        homeButton = new HomeButton(WINDOW_WIDTH*(4f/40f));
        homeButton.setY(WINDOW_HEIGHT-homeButton.getHeight());
    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            Vector3 touch;

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                for (int i=0; i<levelButtons.size(); i++) {
                    if (levelButtons.get(i).getRectangle().contains(touch.x, touch.y)) {
                        SoundController.playClickSound();
                        LevelSelect.this.dispose();
                        //Levels range from 1 up, array indexes from 0 up
                        host.setScreen(createLevel(formatLevelNumber(i+1)));
                    }
                }

                if (homeButton.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    LevelSelect.this.dispose();
                    host.setScreen(new MainMenu(host));
                }
                return true;
            }
        });
    }

    private void drawUI() {
        batch.draw(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        for (GenericButton levelButton : levelButtons) {
            levelButton.draw(batch);
        }
        for (Star star : stars) {
            star.draw(batch);
        }
        homeButton.draw(batch);
    }

    private Level createLevel(String identifier) {
        String mapPath
                = ConstantsManager.levels.get(identifier + "_mapPath");
        int AMOUNT_ROUNDFISH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_ROUNDFISH"));
        int AMOUNT_LONGFISH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_LONGFISH"));
        int AMOUNT_OCTOPUS1
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_OCTOPUS1"));
        int AMOUNT_OCTOPUS2
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_OCTOPUS2"));
        int TILE_WIDTH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_TILE_AMOUNT_WIDTH"));
        int TILE_HEIGHT
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_TILE_AMOUNT_HEIGHT"));
        String TIME_TWO_STARS
                = ConstantsManager.levels.get(identifier + "_TIME_TWO_STARS");
        String TIME_THREE_STARS
                = ConstantsManager.levels.get(identifier + "_TIME_THREE_STARS");

        return new Level(host,
                identifier,
                mapPath,
                AMOUNT_ROUNDFISH,
                AMOUNT_LONGFISH,
                AMOUNT_OCTOPUS1,
                AMOUNT_OCTOPUS2,
                TILE_WIDTH,
                TILE_HEIGHT,
                TIME_TWO_STARS,
                TIME_THREE_STARS);
    }

    private String formatLevelNumber(int number) {
        if (number < 10) {
            return  "0" + number;
        } else {
            return Integer.toString(number);
        }
    }

}
