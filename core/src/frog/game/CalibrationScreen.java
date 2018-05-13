package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * CalibrationScreen calibrates the player's movement.
 *
 * <p>CalibrationScreen is used before the MainMenu is opened, it gives the player directions to keep the device
 * upright. The purpose is to calibrate the movement in the gameplay, and ensure a decent movement range for the accelerometer.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0513
 * @since 2018.0513
 */
public class CalibrationScreen extends ScreenAdapter {
    private GameMain host;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture background;
    private Texture directionsText;

    /**
     * The constructor for CalibrationScreen.
     *
     * Initializes the textures for the background and the directions text, also retrieves basic components
     * like the SpriteBatch and the Camera. Creates the means to handle input.
     *
     * @param host The main class, which controls the displayed screen.
     */
    public CalibrationScreen(GameMain host) {
        this.host = host;
        camera = host.getCamera();
        batch = host.getBatch();
        background = new Texture(Gdx.files.internal(ConstantsManager.bgGenericPath));
        directionsText = new Texture(Gdx.files.internal(host.getMyBundle().get("text_directions")));

        setInputProcessor();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.draw(directionsText, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();
    }

    @Override
    public void dispose() {
        directionsText.dispose();
        background.dispose();
    }

    /**
     * Creates a means for the class to handle touch input
     *
     * Sets the application's InputProcessor, implements method touchUp so that the current accelerometer
     * values are saved as neutral points when then screen of the device is tapped, after this it
     * changes the Screen to the main menu
     */
    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                ConstantsManager.settings.putFloat(ConstantsManager.neutralX_Key,
                        Gdx.input.getAccelerometerY()).flush();
                ConstantsManager.settings.putFloat(ConstantsManager.neutralY_Key,
                        Gdx.input.getAccelerometerZ()).flush();
                dispose();
                host.setScreen(new MainMenu(host));
                return true;
            }
        });
    }
}
