package frog.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by Anton on 13.3.2018.
 */

public class Level01 implements Screen {
    FrogMain host;
    SpriteBatch batch;
    OrthographicCamera camera;
    Player frog;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    public Level01(FrogMain host) {
        this.host = host;
        batch = host.getBatch();
        camera = host.getCamera();

        frog = new Player();
        tiledMap = new TmxMapLoader().load("lvl/testMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);

        frog.moveTemporary();

        batch.begin();
        frog.draw(batch);
        batch.end();
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
        host.dispose();

    }
}
