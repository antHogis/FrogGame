package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Lauri on 14.4.2018.
 */

public class HighScoreScreen extends ScreenAdapter {

    private FrogMain host;
    private Texture highScoreTexture;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    public HighScoreScreen(FrogMain host) {
        this.host = host;
        batch = host.getBatch();
        camera = host.getCamera();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //batch.draw(highScoreTexture, 0, 0);
        batch.end();

        if (Gdx.input.isTouched()) {
            Gdx.app.log("TAG", "Setting Screen");
            host.setScreen(new MainMenu(host));
        }

    }

    @Override
    public void dispose() {

    }
}
