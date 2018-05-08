package frog.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;
/**
 * GameMain is the core of the application.
 *
 * <p>GameMain is delegated to other classes which implement com.badlogic.gdx.Screen.</p>
 * <p>It contains a SpriteBatch and an OrthographicCamera which are used in Screen-implementing classes,
 * and also an I18bundle, which contains Strings for localization</p>
 *
 * @see com.badlogic.gdx.Game
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0306
 */
public class GameMain extends Game {
	private SpriteBatch batch;
    private OrthographicCamera camera;
    private int WINDOW_WIDTH;
    private int WINDOW_HEIGHT;

    private Locale locale;
    private I18NBundle myBundle;

	@Override
	public void create () {
        WINDOW_WIDTH = 1280;
        WINDOW_HEIGHT = (WINDOW_WIDTH * Gdx.graphics.getHeight()) / Gdx.graphics.getWidth();

		batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        SoundController.initialize();
        locale = Locale.getDefault();
        myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
        ConstantsManager.settings.putInteger(ConstantsManager.previousLevelPlayedKey, 1).flush();
        setScreen(new SplashScreen(this));


	}

	@Override
	public void render () {
        super.render();
	}

    @Override
    public void dispose () {
        batch.dispose();
    }

    @Override
    public void resume() {
        SoundController.initialize();
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        myBundle = new I18NBundle();
        myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), this.locale);

    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public I18NBundle getMyBundle() {
        return myBundle;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

}
