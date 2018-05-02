package frog.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class FrogMain extends Game {
	private SpriteBatch batch;
    private OrthographicCamera camera;
    private final int WINDOW_WIDTH = 1280;
    private final int WINDOW_HEIGHT = 800;

	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        SoundController.initialize();

        setScreen(new MainMenu(this));
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

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

}
