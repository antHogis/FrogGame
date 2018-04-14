package frog.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class FrogMain extends Game {
	private SpriteBatch batch;
    private OrthographicCamera camera;
    private final int WINDOW_WIDTH = 1280;
    private final int WINDOW_HEIGHT = 800;
    private final int TILE_DIMENSION = 128;
    private Array<Level> levels;
    private Preferences preferences;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
        createNewLevels();
        SoundController.initialize();
        preferences = Gdx.app.getPreferences("Preferences");

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

    public void createNewLevels() {
        levels = new Array<Level>();

        //Level 1
        levels.add(new Level(this,
                "lvl/0-1.tmx",
                0,
                0,
                1,
                0,
                25,
                15));
        //Level 2
        levels.add(new Level(this,
                "lvl/0-2.tmx",
                1,
                0,
                0,
                0,
                25,
                15));
        //Level 3 (Test level)
        levels.add(new Level(this,
                "lvl/piialevel.tmx",
                1,
                1,
                1,
                0,
                50,
                30));



    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public int getTILE_DIMENSION() {
        return TILE_DIMENSION;
    }

    public Array<Level> getLevels() {
        return levels;
    }

    public void resetLevelTimers() {
	    for (Level level : levels) {
	        level.resetTimer();
        }
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
