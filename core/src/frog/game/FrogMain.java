package frog.game;

import com.badlogic.gdx.Game;
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

	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        SoundController.equalize();

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
        levels.add(createLevel("01"));
        //Level 2
        levels.add(createLevel("02"));
        //Level 3 (Test level)
        levels.add(createLevel("03"));
    }

    private Level createLevel(String identifier) {
        String mapPath
                = ConstantsManager.levels.get(identifier + "_mapPath");
        int AMOUNT_ROUNDFISH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_ROUNDFISH"));
        int AMOUNT_LONGFISH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_LONGFISH"));
        int AMOUNT_OCTOPUS1
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_OCTOPUS1"));
        int AMOUNT_OCTOPUS2
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_OCTOPUS2"));
        int TILE_WIDTH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_TILE_AMOUNT_WIDTH"));
        int TILE_HEIGHT
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_TILE_AMOUNT_HEIGHT"));
        String TIME_TWO_STARS
                = ConstantsManager.levels.get(identifier + "_TIME_TWO_STARS");
        String TIME_THREE_STARS
                = ConstantsManager.levels.get(identifier + "_TIME_THREE_STARS");

        return new Level(this,
                identifier,
                mapPath,
                AMOUNT_ROUNDFISH,
                AMOUNT_LONGFISH,
                AMOUNT_OCTOPUS1,
                AMOUNT_OCTOPUS2,
                TILE_WIDTH,
                TILE_HEIGHT,
                TIME_TWO_STARS,
                TIME_THREE_STARS);
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




}
