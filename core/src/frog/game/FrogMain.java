package frog.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FrogMain extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Player frog;
    private OrthographicCamera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		frog = new Player();
		img = new Texture("vesigraffat-v81.jpg");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 8f, 4f);
	}

	//Piia on hyvä Scrummaster :)
	@Override
	public void render () {
        batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        batch.draw(img, 0, 0,
                img.getWidth() / 200,
                img.getHeight() / 200);
		batch.draw(frog.getTexture(), frog.getX(), frog.getY(),
                frog.getWidth(),
                frog.getHeight());
		frog.moveTemporary();
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
