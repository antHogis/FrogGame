package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class SplashScreen implements Screen {
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
     * @param host The main class, for the retrieval of SpriteBatch and Camera
     */
    public SplashScreen(GameMain host) {
        this.host = host;
        this.splashTexture = new Texture("gfx/splashtexture.png");
        this.batch = host.getBatch();
        this.camera = host.getCamera();
        this.startTime = TimeUtils.millis();
        this.splashTime = 2000;
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
        TimerCountdown();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        splashTexture.dispose();
        batch.dispose();
        host.dispose();

    }

    /**
     * Timer for the Splash screen.
     *
     * Counts up to two 2 seconds before changing the screen to main menu.
     */
    public void TimerCountdown() {
        if(TimeUtils.millis()>(startTime + splashTime)) {
            host.setScreen(new MainMenu(host));
        }

    }
}
