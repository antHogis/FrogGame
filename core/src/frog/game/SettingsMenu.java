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

    private GenericButton homeButton;
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

        sensitivity_Text = new TextItem(host.getMyBundle().get("text_sensitivity"), TEXT_HEIGHT);
        sensitivity_Text.setX(textPositionX);
        sensitivity_Text.setY(textPositionY);

        textPositionY -= textMargin;

        threshold_Text = new TextItem(host.getMyBundle().get("text_threshold"), TEXT_HEIGHT);
        threshold_Text.setX(textPositionX);
        threshold_Text.setY(textPositionY);

        textPositionY -= textMargin;

        invertY_Text = new TextItem(host.getMyBundle().get("text_y-axis"), TEXT_HEIGHT);
        invertY_Text.setX(textPositionX);
        invertY_Text.setY(textPositionY);

        /*
         * Buttons and Sliders
         */
        final float BUTTON_HEIGHT = TEXT_HEIGHT;
        final float buttonPosition_X_plusWidth = WINDOW_WIDTH - (WINDOW_WIDTH/16f);

        //Homebutton (returns to main menu)
        homeButton = new GenericButton(WINDOW_WIDTH * (4f/40f),
                ConstantsManager.homeButtonIdlePath,
                ConstantsManager.homeButtonPressedPath);
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

        String idle = "idle.png";
        String pressed = "pressed.png";

        //Invert Y-axis button
        invertY_Switch = new SwitchButton(host.getMyBundle().get("button_on") + idle,
                host.getMyBundle().get("button_on") + pressed,
                host.getMyBundle().get("button_off") + idle,
                host.getMyBundle().get("button_off") + pressed,
                BUTTON_HEIGHT,
                ConstantsManager.settings.getBoolean("y-invert", ConstantsManager.DEFAULT_INVERT_Y));

        //Placing the button in the middle of right end of the text and the right edge of the screen
        invertY_Switch.setX(invertY_Text.getX() +invertY_Text.getWidth() +
                (WINDOW_WIDTH - (invertY_Text.getX() +invertY_Text.getWidth())) / 2
                - invertY_Switch.getWidth()/2);

        invertY_Switch.setY(invertY_Text.getY());


        //Music button (turns music off/on)
        musicButton = new SwitchButton(ConstantsManager.musicOnIdlePath,
                ConstantsManager.musicOnPressedPath,
                ConstantsManager.musicOffIdlePath,
                ConstantsManager.musicOffPressedPath,
                BUTTON_HEIGHT*1.5f,
                ConstantsManager.settings.getBoolean("music-on",
                        ConstantsManager.DEFAULT_AUDIO_ON));
        musicButton.setX((WINDOW_WIDTH/2)-(musicButton.getWidth()/2));
        musicButton.setY(WINDOW_WIDTH * (1f/40f));
    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            Vector3 touch;
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (homeButton.getRectangle().contains(touch.x, touch.y)) homeButton.setPressed(true);
                else homeButton.setPressed(false);

                if (invertY_Switch.getRectangle().contains(touch.x, touch.y)) invertY_Switch.setPressed(true);
                else invertY_Switch.setPressed(false);

                if (musicButton.getRectangle().contains(touch.x,touch.y)) musicButton.setPressed(true);
                else musicButton.setPressed(false);

                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (homeButton.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    homeButton.setPressed(false);
                    ConstantsManager.settings.flush();
                    SettingsMenu.this.dispose();
                    host.setScreen(new MainMenu(host));
                }
                if (invertY_Switch.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    invertY_Switch.setPressed(false);
                    invertY_Switch.setOn(!invertY_Switch.isOn());
                    ConstantsManager.settings.putBoolean("y-invert", invertY_Switch.isOn());
                }
                if (musicButton.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    musicButton.setPressed(false);
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

                if (homeButton.getRectangle().contains(touch.x,touch.y)) homeButton.setPressed(true);
                else homeButton.setPressed(false);

                if (invertY_Switch.getRectangle().contains(touch.x, touch.y)) invertY_Switch.setPressed(true);
                else invertY_Switch.setPressed(false);

                if (musicButton.getRectangle().contains(touch.x,touch.y)) musicButton.setPressed(true);
                else musicButton.setPressed(false);

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
