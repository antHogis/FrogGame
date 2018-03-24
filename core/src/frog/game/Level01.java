package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
    private FrogMain host;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Player frog;
    private Checkpoint check01;
    private EnemyFish fish;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    public Level01(FrogMain host) {
        this.host = host;
        batch = host.getBatch();
        camera = host.getCamera();

        frog = new Player();
        fish = new EnemyFish(0.03f, 5f, true);
        check01 = new Checkpoint(4.5f, 4.5f);
        tiledMap = new TmxMapLoader().load("lvl/testMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        frog.movementAndroid(Gdx.graphics.getDeltaTime());
        frog.moveTemporary();
        fish.moveLeftRight();
        fish.checkCollision(frog);

        batch.begin();
        frog.draw(batch);
        fish.draw(batch);
        if (!check01.getIsCleared()) {
            check01.checkCollision(frog);
            check01.draw(batch);
        }
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
