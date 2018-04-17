package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Anton on 13.3.2018.
 */

public class MainMenu extends ScreenAdapter {
    private FrogMain host;
    private Texture background;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private GenericButton playButton;
    private GenericButton settingsButton;
    private GenericButton highScoreButton;
    private GenericButton exitButton;

    public MainMenu(FrogMain host) {
        this.host = host;
        background = new Texture(Gdx.files.internal("ui/bg2.png"));
        batch = host.getBatch();
        camera = host.getCamera();

        addMenuButtons();
        setInputProcessor();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        //checkButtonsIsTouched();

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        drawButtons();
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();
        settingsButton.dispose();
        highScoreButton.dispose();
        exitButton.dispose();
    }

    private void addMenuButtons() {
        float WINDOW_WIDTH = camera.viewportWidth;
        float WINDOW_HEIGHT = camera.viewportHeight;
        float BUTTON_WIDTH = WINDOW_WIDTH * (4f/16f);

        float LEFT_X = WINDOW_WIDTH*(1f/16f);
        float RIGHT_X = WINDOW_WIDTH - WINDOW_WIDTH*(1f/16f) - BUTTON_WIDTH;
        float TOP_Y = WINDOW_HEIGHT/2f;
        float BOTTOM_Y = WINDOW_HEIGHT*(2f/16f);

        playButton = new GenericButton(BUTTON_WIDTH,
                ConstantsManager.myBundle.get("button_play"));
        playButton.setX(LEFT_X);
        playButton.setY(TOP_Y);

        settingsButton = new GenericButton(BUTTON_WIDTH,
                ConstantsManager.myBundle.get("button_settings"));
        settingsButton.setX(RIGHT_X);
        settingsButton.setY(TOP_Y);

        highScoreButton = new GenericButton(BUTTON_WIDTH,
                ConstantsManager.myBundle.get("button_highScore"));
        highScoreButton.setX(LEFT_X);
        highScoreButton.setY(BOTTOM_Y);

        exitButton = new GenericButton(BUTTON_WIDTH,
                ConstantsManager.myBundle.get("button_exit"));
        exitButton.setX(RIGHT_X);
        exitButton.setY(BOTTOM_Y);
    }

    private void drawButtons() {
        playButton.draw(batch);
        settingsButton.draw(batch);
        highScoreButton.draw(batch);
        exitButton.draw(batch);
    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);
                //Level-select button
                if (playButton.getRectangle().contains(touch.x,touch.y)) {
                    SoundController.playClickSound();
                    host.createNewLevels();
                    MainMenu.this.dispose();
                    host.setScreen(host.getLevels().get(0));
                }
                //Settings button
                if (settingsButton.getRectangle().contains(touch.x,touch.y)) {
                    MainMenu.this.dispose();
                    SoundController.playClickSound();
                    host.setScreen(new SettingsMenu(host));
                }
                //High Score button
                if (highScoreButton.getRectangle().contains(touch.x,touch.y)) {
                    SoundController.playClickSound();
                    MainMenu.this.dispose();
                    host.setScreen(new HighScoreScreen(host));
                }
                //Exit button
                if (exitButton.getRectangle().contains(touch.x,touch.y)) {
                    SoundController.playClickSound();
                    MainMenu.this.dispose();
                    Gdx.app.exit();
                }
                return true;
            }


        });
    }

}
