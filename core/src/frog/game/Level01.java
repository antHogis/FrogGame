package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by Anton on 13.3.2018.
 */

public class Level01 implements Screen {
    private FrogMain host;
    private SpriteBatch batch;
    private Player frog;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private final int TILE_WIDTH = 32;
    private final int TILE_HEIGHT = 32;
    private final int TILE_AMOUNT_WIDTH = 50;
    private final int TILE_AMOUNT_HEIGHT = 30;

    private OrthographicCamera camera;
    private final int WINDOW_WIDTH = 640;
    private final int WINDOW_HEIGHT = 400;
    private final int WORLD_WIDTH_PIXELS = TILE_AMOUNT_WIDTH * TILE_WIDTH;
    private final int WORLD_HEIGHT_PIXELS = TILE_AMOUNT_HEIGHT * TILE_HEIGHT;

    public Level01(FrogMain host) {
        this.host = host;
        batch = host.getBatch();
        camera = host.getCamera();
        camera.setToOrtho(false,
                WINDOW_WIDTH,
                WINDOW_HEIGHT);

        tiledMap = new TmxMapLoader().load("lvl/testMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        frog = new Player((TiledMapTileLayer) tiledMap.getLayers().get("walls-texture"));
        frog.setWidth(96);
        frog.setHeight(96);
        frog.setX(32);
        frog.setY(32);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);


        frog.movementAndroid(Gdx.graphics.getDeltaTime());

        moveCamera();

        tiledMapRenderer.render();

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

    private void moveCamera () {
        camera.position.set(frog.getX(),
                frog.getY(),
                0);

        if(camera.position.x < WINDOW_WIDTH / 2){
            camera.position.x = WINDOW_WIDTH / 2;
        }

        if(camera.position.y > WORLD_HEIGHT_PIXELS - WINDOW_HEIGHT / 2) {
            camera.position.y = WORLD_HEIGHT_PIXELS - WINDOW_HEIGHT / 2;
        }

        if(camera.position.y < WINDOW_HEIGHT / 2) {
            camera.position.y = WINDOW_HEIGHT / 2;
        }

        if(camera.position.x > WORLD_WIDTH_PIXELS - WINDOW_WIDTH / 2f) {
            camera.position.x = WORLD_WIDTH_PIXELS - WINDOW_WIDTH / 2f;
        }
    }
}
