package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.Locale;


/**
 * Created by Anton on 13.3.2018.
 */

public class MainMenu extends ScreenAdapter {
    private GameMain host;
    private Texture background;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private GenericButton playButton;
    private GenericButton settingsButton;
    private GenericButton highScoreButton;
    private GenericButton exitButton;
    private GenericButton finnishButton;
    private GenericButton englishButton;

    public MainMenu(GameMain host) {
        this.host = host;
        background = new Texture(Gdx.files.internal(ConstantsManager.bgMainMenuPath));
        batch = host.getBatch();
        camera = host.getCamera();

        addMenuButtons();
        addLanguageSwitches();
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
        finnishButton.dispose();
        englishButton.dispose();
        disposeLocalizedButtons();
    }

    private void disposeLocalizedButtons() {
        playButton.dispose();
        settingsButton.dispose();
        highScoreButton.dispose();
        exitButton.dispose();
    }

    private void drawButtons() {
        playButton.draw(batch);
        settingsButton.draw(batch);
        highScoreButton.draw(batch);
        exitButton.draw(batch);
        finnishButton.draw(batch);
        englishButton.draw(batch);
    }

    private void addMenuButtons() {
        float WINDOW_WIDTH = camera.viewportWidth;
        float WINDOW_HEIGHT = camera.viewportHeight;
        float BUTTON_WIDTH = WINDOW_WIDTH * (8f/40f);

        float buttonX = WINDOW_WIDTH * (16f/40f);
        float buttonY = WINDOW_HEIGHT * (2f/40f);
        float marginY = WINDOW_HEIGHT * (2f/40f);

        String filePath;
        String idle = "idle.png";
        String pressed = "pressed.png";

        //Exit button
        filePath = host.getMyBundle().get("button_exit");
        exitButton = new GenericButton(BUTTON_WIDTH,
                filePath + idle,
                filePath + pressed);
        exitButton.setX(buttonX);
        exitButton.setY(buttonY);

        marginY += exitButton.getHeight();
        buttonY += marginY;

        //High Score button
        filePath = host.getMyBundle().get("button_highScore");
        highScoreButton = new GenericButton(BUTTON_WIDTH,
                filePath + idle,
                filePath + pressed);
        highScoreButton.setX(buttonX);
        highScoreButton.setY(buttonY);

        buttonY += marginY;

        //Settings button
        filePath = host.getMyBundle().get("button_settings");
        settingsButton = new GenericButton(BUTTON_WIDTH,
                filePath + idle,
                filePath + pressed);
        settingsButton.setX(buttonX);
        settingsButton.setY(buttonY);

        buttonY += marginY;

        //Play button
        filePath = host.getMyBundle().get("button_play");
        playButton = new GenericButton(BUTTON_WIDTH,
                filePath + idle,
                filePath + pressed);
        playButton.setX(buttonX);
        playButton.setY(buttonY);



    }

    private void addLanguageSwitches() {
        float WINDOW_WIDTH = camera.viewportWidth;
        float WINDOW_HEIGHT = camera.viewportHeight;
        float BUTTON_WIDTH = WINDOW_WIDTH * (3f/40f);
        float buttonX = WINDOW_WIDTH * (1f/40f);
        float buttonY = WINDOW_HEIGHT * (1f/40f);

        //Finnish flag button
        finnishButton = new GenericButton(BUTTON_WIDTH,
                ConstantsManager.finnishButtonIdlePath,
                ConstantsManager.finnishButtonPressedPath);
        finnishButton.setX(buttonX);
        finnishButton.setY(buttonY);

        buttonX += BUTTON_WIDTH + WINDOW_WIDTH * (1f/40f);

        englishButton = new GenericButton(BUTTON_WIDTH,
                ConstantsManager.englishButtonIdlePath,
                ConstantsManager.englishButtonPressedPath);
        englishButton.setX(buttonX);
        englishButton.setY(buttonY);
    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            Vector3 touch;

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                //Play button
                if (playButton.getRectangle().contains(touch.x,touch.y)) playButton.setPressed(true);
                else playButton.setPressed(false);

                //Settings button
                if (settingsButton.getRectangle().contains(touch.x,touch.y)) settingsButton.setPressed(true);
                else settingsButton.setPressed(false);

                //High Score button
                if (highScoreButton.getRectangle().contains(touch.x,touch.y)) highScoreButton.setPressed(true);
                else highScoreButton.setPressed(false);

                //Exit button
                if (exitButton.getRectangle().contains(touch.x,touch.y)) exitButton.setPressed(true);
                else exitButton.setPressed(false);

                //Finnish flag button
                if (finnishButton.getRectangle().contains(touch.x,touch.y)) finnishButton.setPressed(true);
                else finnishButton.setPressed(false);

                //English flag button
                if (englishButton.getRectangle().contains(touch.x,touch.y)) englishButton.setPressed(true);
                else englishButton.setPressed(false);

                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                //Play button
                if (playButton.getRectangle().contains(touch.x,touch.y)) playButton.setPressed(true);
                else playButton.setPressed(false);

                //Settings button
                if (settingsButton.getRectangle().contains(touch.x,touch.y)) settingsButton.setPressed(true);
                else settingsButton.setPressed(false);

                //High Score button
                if (highScoreButton.getRectangle().contains(touch.x,touch.y)) highScoreButton.setPressed(true);
                else highScoreButton.setPressed(false);

                //Exit button
                if (exitButton.getRectangle().contains(touch.x,touch.y)) exitButton.setPressed(true);
                else exitButton.setPressed(false);

                //Finnish flag button
                if(finnishButton.getRectangle().contains(touch.x,touch.y)) finnishButton.setPressed(true);
                else finnishButton.setPressed(false);

                //English flat button
                if (englishButton.getRectangle().contains(touch.x, touch.y)) englishButton.setPressed(true);
                else englishButton.setPressed(false);


                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                //Play button
                if (playButton.getRectangle().contains(touch.x,touch.y)) {
                    SoundController.playClickSound();
                    MainMenu.this.dispose();
                    host.setScreen(new LevelSelectMenu(host));
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
                    host.setScreen(new HighScoreMenu(host));
                }
                //Exit button
                if (exitButton.getRectangle().contains(touch.x,touch.y)) {
                    SoundController.playClickSound();
                    MainMenu.this.dispose();
                    Gdx.app.exit();
                }
                //Finnish flag button
                if (finnishButton.getRectangle().contains(touch.x,touch.y)) {
                    SoundController.playClickSound();
                    finnishButton.setPressed(false);
                    if(MainMenu.this.host.getMyBundle().getLocale() != new Locale("fi", "FI")) {
                        MainMenu.this.host.setLocale(new Locale("fi", "FI"));
                        disposeLocalizedButtons();
                        addMenuButtons();
                    }
                }
                //English flag button
                if (englishButton.getRectangle().contains(touch.x,touch.y)) {
                    SoundController.playClickSound();
                    englishButton.setPressed(false);
                    if(MainMenu.this.host.getMyBundle().getLocale() != new Locale("en", "UK")) {
                        MainMenu.this.host.setLocale(new Locale("en", "UK"));
                        disposeLocalizedButtons();
                        addMenuButtons();
                    }
                }
                return true;
            }


        });
    }

}
