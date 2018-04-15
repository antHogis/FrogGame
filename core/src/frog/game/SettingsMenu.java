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

    public SettingsMenu(FrogMain host) {
        this.host = host;
        camera = host.getCamera();
        batch = host.getBatch();
        WINDOW_WIDTH = camera.viewportWidth;
        WINDOW_HEIGHT = camera.viewportHeight;

        background = new Texture(Gdx.files.internal("ui/bg.png"));
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

    private void createUI() {
        /*
         * TextItem objects
         */
        final float TEXT_HEIGHT = WINDOW_HEIGHT * (1.5f/16);
        final float textPadding = WINDOW_HEIGHT*(4f/16f) - (TEXT_HEIGHT/2f);
        final float textPositionX = WINDOW_WIDTH/16f;
        float textPositionY = WINDOW_HEIGHT*(12f/16f) - (TEXT_HEIGHT/2f);

        sensitivity_Text = new TextItem("ui/text_fi/herkkyys.png", TEXT_HEIGHT);
        sensitivity_Text.setX(textPositionX);
        sensitivity_Text.setY(textPositionY);

        textPositionY -= textPadding;

        threshold_Text = new TextItem("ui/text_fi/raja-arvo.png", TEXT_HEIGHT);
        threshold_Text.setX(textPositionX);
        threshold_Text.setY(textPositionY);

        textPositionY -= textPadding;

        invertY_Text = new TextItem("ui/text_fi/y-akseli.png", TEXT_HEIGHT);
        invertY_Text.setX(textPositionX);
        invertY_Text.setY(textPositionY);

        /*
         * Buttons and Sliders
         *
         * Tää on vielä vammanen, pitää määritellä konstruktorissa positiot koska sliderissä on
         * 2 rectanglea ja 2 tekstuuria. Myöhemmin voisi tehdä järkevän setterin.
         */
        final float BUTTON_HEIGHT = TEXT_HEIGHT;
        final float buttonPosition_X_plusWidth = WINDOW_WIDTH - (WINDOW_WIDTH/16f);

        sensitivity_Slider = new Slider(ConstantsManager.MIN_SPEED,
                ConstantsManager.MAX_SPEED,
                ConstantsManager.settings.getFloat("speed",
                        ConstantsManager.DEFAULT_SPEED),
                BUTTON_HEIGHT,
                buttonPosition_X_plusWidth,
                sensitivity_Text.getY());


        threshold_Slider = new Slider(ConstantsManager.MIN_THRESHOLD,
                ConstantsManager.MAX_THRESHOLD,
                ConstantsManager.settings.getFloat("threshold",
                        ConstantsManager.DEFAULT_THRESHOLD),
                BUTTON_HEIGHT,
                buttonPosition_X_plusWidth,
                threshold_Text.getY());

        invertY_Switch = new SwitchButton("ui/buttons/on_fi.png",
                "ui/buttons/off_fi.png",
                BUTTON_HEIGHT,
                ConstantsManager.settings.getBoolean("y-invert", ConstantsManager.DEFAULT_INVERT_Y));
        invertY_Switch.setX(buttonPosition_X_plusWidth - invertY_Switch.getWidth());
        invertY_Switch.setY(invertY_Text.getY());

        homeButton = new HomeButton(0,0, BUTTON_HEIGHT*1.5f);
        homeButton.setX(0);
        homeButton.setY(WINDOW_HEIGHT-homeButton.getHeight());

    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (invertY_Switch.getRectangle().contains(touch.x, touch.y)) {
                    invertY_Switch.setOn(!invertY_Switch.isOn());
                    ConstantsManager.settings.putBoolean("y-invert", invertY_Switch.isOn());
                }
                if (homeButton.getRectangle().contains(touch.x, touch.y)) {
                    ConstantsManager.settings.putFloat("speed", sensitivity_Slider.getOutput());
                    ConstantsManager.settings.putFloat("threshold", threshold_Slider.getOutput());
                    ConstantsManager.settings.flush();
                    host.setScreen(new MainMenu(host));
                }
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if(sensitivity_Slider.getRectangle().contains(touch.x, touch.y)) {
                    sensitivity_Slider.movePoint(touch.x);
                }

                if (threshold_Slider.getRectangle().contains(touch.x, touch.y)) {
                    threshold_Slider.movePoint(touch.x);
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
    }
}
