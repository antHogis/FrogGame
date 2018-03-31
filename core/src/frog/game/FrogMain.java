package frog.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FrogMain extends Game {
	private SpriteBatch batch;
    private OrthographicCamera camera;
    private final int VIEWPORT_WIDTH = 1280;
    private final int VIEWPORT_HEIGHT = 800;
    private final int TILE_DIMENSION = 128;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
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
    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public int getVIEWPORT_WIDTH() {
        return VIEWPORT_WIDTH;
    }

    public int getVIEWPORT_HEIGHT() {
        return VIEWPORT_HEIGHT;
    }

    public int getTILE_DIMENSION() {
        return TILE_DIMENSION;
    }
}
