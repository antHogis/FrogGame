package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;


/**
 * SplashScreen is the opening screen that is launched before the game.
 *
 * <p>SplashScreen is used in the GameMain class, to be shown before the main menu.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0506
 */

public class SplashScreen extends ScreenAdapter {
    private GameMain host;
    private Texture splashTexture;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Long startTime;
    private int splashTime;

    /**
     * Constructor of SplashScreen
     *
     * Creates the SplashScreen, sets the texture, camera and spritebatch. Displays for 2 seconds
     * and the switches screens to main menu.
     *
     * @param host The main class, which controls the displayed screen.
     */
    public SplashScreen(GameMain host) {
        this.host = host;
        this.splashTexture = new Texture("ui/splashScreen.png");
        this.batch = host.getBatch();
        this.camera = host.getCamera();
        this.startTime = TimeUtils.millis();
        this.splashTime = 2000;

        setInputProcessor();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(splashTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();
        timerCountdown();

    }

    @Override
    public void dispose() {
        splashTexture.dispose();
    }

    /**
     * Timer for the Splash screen.
     *
     * Changes the screen to main menu when splashTime amount of milliseconds have passed since showing this class
     */
    private void timerCountdown() {
        if(TimeUtils.millis()>(startTime + splashTime)) {
            dispose();
            host.setScreen(new MainMenu(host));
        }

    }


    /**
     * Creates a means for the class to handle touch input
     *
     * Sets the application's InputProcessor, implements method touchUp so that the Screen is changed when the screen of the device is tapped.
     */
    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                dispose();
                host.setScreen(new MainMenu(host));
                return true;
            }
        });
    }
}
