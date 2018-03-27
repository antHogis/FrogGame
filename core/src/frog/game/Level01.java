package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Anton on 13.3.2018.
 */

public class Level01 implements Screen {
    private FrogMain host;
    private SpriteBatch batch;
    private Player frog;
    private Checkpoint check01;
    private EnemyFish fish;
    private EnemyFish fish2;
    private EnemyFish fish3;
    private TimeCoin coin1;

    private Music bgMusic;

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

        frog = new Player((TiledMapTileLayer) tiledMap.getLayers().get("walls-texture"), tiledMap);
        frog.setWidth(96);
        frog.setHeight(35.625f);
        frog.setX(32);
        frog.setY(32);
        frog.setLastCheckpointX(32);
        frog.setLastCheckpointY(32);
        fish = new EnemyFish(2f, 128f, true );
        fish.setWidth(48);
        fish.setHeight(32);
        fish.setX(280);
        fish.setY(128);
        fish2 = new EnemyFish(3f, 200f, true );
        fish2.setWidth(48);
        fish2.setHeight(32);
        fish2.setX(1080);
        fish2.setY(256);
        fish3 = new EnemyFish(5f, 600f, true );
        fish3.setWidth(48);
        fish3.setHeight(32);
        fish3.setX(280);
        fish3.setY(550);
        coin1 = new TimeCoin();
        coin1.setWidth(84);
        coin1.setHeight(76);
        coin1.setX(280);
        coin1.setY(256);
        check01 = new Checkpoint(314f, 314f);
        check01.setWidth(84);
        check01.setHeight(76);

        /*
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("../music/mariowater.mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.25f);
        bgMusic.play();*/
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
        fish.moveLeftRight();
        fish.checkCollision(frog);
        fish2.moveLeftRight();
        fish2.checkCollision(frog);
        fish3.moveLeftRight();
        fish3.checkCollision(frog);
        frog.moveTemporary();

        moveCamera();
        check01.checkCollision(frog);

        tiledMapRenderer.render();

        batch.begin();
        fish.draw(batch);
        fish2.draw(batch);
        fish3.draw(batch);
        //coin1.draw(batch);
        frog.draw(batch);
        check01.draw(batch);
        batch.end();

        endLevel();
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

    private boolean overlapsMapObject (String path) {
        Array<RectangleMapObject> endZones = tiledMap.getLayers().get(path).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject endZone : endZones) {
            Rectangle endZoneRectangle = endZone.getRectangle();
            if (frog.rectangle.overlaps(endZoneRectangle)) {
                return true;
            }
        }
        return false;
    }

    private void endLevel() {
        if (overlapsMapObject("endzone-rectangle")) {
            host.setScreen(new MainMenu(host));
        }
    }

}
