package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Anton on 13.4.2018.
 */

public class SettingsMenu extends ScreenAdapter {
    private FrogMain host;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private final float WINDOW_WIDTH, WINDOW_HEIGHT;
    private Texture background;

    private TextItem sensitivity_Text;
    private TextItem threshold_Text;
    private TextItem invertY_Text;

    private Slider sensitivity_Slider;
    private Slider threshold_Slider;
    private SwitchButton invertY_Switch;

    private HomeButton homeButton;
    private SwitchButton musicButton;

    public SettingsMenu(FrogMain host) {
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
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawUI();
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        sensitivity_Text.dispose();
        sensitivity_Slider.dispose();
        threshold_Text.dispose();
        threshold_Slider.dispose();

        invertY_Text.dispose();
        invertY_Switch.dispose();
        homeButton.dispose();
        musicButton.dispose();
    }

    private void createUI() {
        /*
         * TextItem objects
         */
        final float TEXT_HEIGHT = WINDOW_HEIGHT * (1.5f/16);
        final float textMargin = WINDOW_HEIGHT*(4f/16f) - (TEXT_HEIGHT/2f);
        final float textPositionX = WINDOW_WIDTH/16f;
        float textPositionY = WINDOW_HEIGHT*(12f/16f) - (TEXT_HEIGHT/2f);

        sensitivity_Text = new TextItem("ui/text_fi/herkkyys.png", TEXT_HEIGHT);
        sensitivity_Text.setX(textPositionX);
        sensitivity_Text.setY(textPositionY);

        textPositionY -= textMargin;

        threshold_Text = new TextItem("ui/text_fi/raja-arvo.png", TEXT_HEIGHT);
        threshold_Text.setX(textPositionX);
        threshold_Text.setY(textPositionY);

        textPositionY -= textMargin;

        invertY_Text = new TextItem("ui/text_fi/y-akseli.png", TEXT_HEIGHT);
        invertY_Text.setX(textPositionX);
        invertY_Text.setY(textPositionY);

        /*
         * Buttons and Sliders
         */
        final float BUTTON_HEIGHT = TEXT_HEIGHT;
        final float buttonPosition_X_plusWidth = WINDOW_WIDTH - (WINDOW_WIDTH/16f);

        //Homebutton (returns to main menu)
        homeButton = new HomeButton(BUTTON_HEIGHT*1.5f);
        homeButton.setX(0);
        homeButton.setY(WINDOW_HEIGHT-homeButton.getHeight());

        //Sensitivity Slider
        sensitivity_Slider = new Slider(ConstantsManager.MIN_SPEED,
                ConstantsManager.MAX_SPEED,
                ConstantsManager.settings.getFloat("speed",
                        ConstantsManager.DEFAULT_SPEED),
                BUTTON_HEIGHT,
                buttonPosition_X_plusWidth,
                sensitivity_Text.getY());

        //Threshold slider
        threshold_Slider = new Slider(ConstantsManager.MIN_THRESHOLD,
                ConstantsManager.MAX_THRESHOLD,
                ConstantsManager.settings.getFloat("threshold",
                        ConstantsManager.DEFAULT_THRESHOLD),
                BUTTON_HEIGHT,
                buttonPosition_X_plusWidth,
                threshold_Text.getY());

        //Invert Y-axis button
        invertY_Switch = new SwitchButton("ui/buttons/on_fi.png",
                "ui/buttons/off_fi.png",
                BUTTON_HEIGHT,
                ConstantsManager.settings.getBoolean
                        ("y-invert", ConstantsManager.DEFAULT_INVERT_Y));
        invertY_Switch.setX(buttonPosition_X_plusWidth - invertY_Switch.getWidth());
        invertY_Switch.setY(invertY_Text.getY());


        //Music button (turns music off/on)
        musicButton = new SwitchButton(ConstantsManager.musicOnPath,
                ConstantsManager.musicOffPath,
                BUTTON_HEIGHT*1.5f,
                ConstantsManager.settings.getBoolean("music-on",
                        ConstantsManager.DEFAULT_AUDIO_ON));
        musicButton.setX((WINDOW_WIDTH/2)-(musicButton.getWidth()/2));
        musicButton.setY(0f);
    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            Vector3 touch;
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (invertY_Switch.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    invertY_Switch.setOn(!invertY_Switch.isOn());
                    ConstantsManager.settings.putBoolean("y-invert", invertY_Switch.isOn());
                }
                if (homeButton.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    ConstantsManager.settings.flush();
                    SettingsMenu.this.dispose();
                    host.setScreen(new MainMenu(host));
                }
                if (musicButton.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    musicButton.setOn(!musicButton.isOn());
                    ConstantsManager.settings.putBoolean("music-on", musicButton.isOn()).flush();
                }
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (sensitivity_Slider.getRectangle().contains(touch.x, touch.y)) {
                    sensitivity_Slider.movePoint(touch.x);
                    ConstantsManager.settings.putFloat("speed", sensitivity_Slider.getOutput());
                }

                if (threshold_Slider.getRectangle().contains(touch.x, touch.y)) {
                    threshold_Slider.movePoint(touch.x);
                    ConstantsManager.settings.putFloat("threshold", threshold_Slider.getOutput());
                }
                return true;
            }
        });
    }

    private void drawUI() {
        batch.draw(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        sensitivity_Text.draw(batch);
        threshold_Text.draw(batch);
        invertY_Text.draw(batch);

        sensitivity_Slider.draw(batch);
        threshold_Slider.draw(batch);
        invertY_Switch.draw(batch);
        homeButton.draw(batch);
        musicButton.draw(batch);
    }
}
