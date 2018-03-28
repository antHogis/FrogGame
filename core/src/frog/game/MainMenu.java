package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Anton on 13.3.2018.
 */

public class MainMenu implements Screen {
    private FrogMain host;
    private Texture main;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    public MainMenu(FrogMain host) {
        this.host = host;
        main = new Texture(Gdx.files.internal("gfx/Menu.png"));
        batch = host.getBatch();
        camera = host.getCamera();
        camera.setToOrtho(false, host.getVIEWPORT_WIDTH(), host.getVIEWPORT_HEIGHT());
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(main, 0, 0);
        batch.end();

        if (Gdx.input.isTouched()) {
            Gdx.app.log("TAG", "Setting Screen");
            host.setScreen(new Level01(this.host));
        }

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

    }
}
