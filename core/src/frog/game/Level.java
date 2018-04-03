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

public class Level implements Screen {
    private FrogMain host;
    private SpriteBatch batch;
    private Player frog;
    private Checkpoint check01;
    private Array<Enemy> enemies;
    private TimeCoin coin1;

    private Music bgMusic;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private final int AMOUNT_ROUNDFISH;
    private final int AMOUNT_FISHLONG;
    private final int AMOUNT_OCTOPUS1;
    private final int AMOUNT_OCTOPUS2;
    private final int AMOUNT_CHECKPOINT;

    private final int TILE_DIMENSION = 128;

    private final int TILE_AMOUNT_WIDTH = 50;
    private final int TILE_AMOUNT_HEIGHT = 30;

    private OrthographicCamera camera;
    private final int WINDOW_WIDTH_PIXELS = 2048;
    private final int WINDOW_HEIGHT_PIXELS = 1600;
    private final int WORLD_WIDTH_PIXELS = TILE_AMOUNT_WIDTH * TILE_DIMENSION;
    private final int WORLD_HEIGHT_PIXELS = TILE_AMOUNT_HEIGHT * TILE_DIMENSION;

    public Level(FrogMain host,
                 String levelPath,
                 int AMOUNT_ROUNDFISH,
                 int AMOUNT_FISHLONG,
                 int AMOUNT_OCTOPUS1,
                 int AMOUNT_OCTOPUS2,
                 int AMOUNT_CHECKPOINT) {
        this.host = host;
        batch = host.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                WINDOW_WIDTH_PIXELS,
                WINDOW_HEIGHT_PIXELS);

        tiledMap = new TmxMapLoader().load(levelPath);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        frog = new Player(tiledMap, host.getTILE_DIMENSION());

        this.AMOUNT_ROUNDFISH = AMOUNT_ROUNDFISH;
        this.AMOUNT_FISHLONG = AMOUNT_FISHLONG;
        this.AMOUNT_OCTOPUS1 = AMOUNT_OCTOPUS1;
        this.AMOUNT_OCTOPUS2 = AMOUNT_OCTOPUS2;
        this.AMOUNT_CHECKPOINT = AMOUNT_CHECKPOINT;

        enemies = new Array<Enemy>();
        addEnemies();

        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mariowater.mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.25f);
        //bgMusic.play();
        spawnFrog();
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

        moveEnemies();

        frog.movementAndroid(Gdx.graphics.getDeltaTime());
        frog.moveTemporary(Gdx.graphics.getDeltaTime());

        moveCamera();
        //check01.checkCollision(frog);

        tiledMapRenderer.render();

        batch.begin();
        frog.draw(batch);
        drawEnemies();
        //check01.draw(batch);
        batch.end();

        respawnFromWall();
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
        batch.dispose();
    }

    private void moveCamera () {
        camera.position.set(frog.getX(),
                frog.getY(),
                0);

        if(camera.position.x < WINDOW_WIDTH_PIXELS / 2){
            camera.position.x = WINDOW_WIDTH_PIXELS / 2;
        }

        if(camera.position.y > WORLD_HEIGHT_PIXELS - WINDOW_HEIGHT_PIXELS / 2) {
            camera.position.y = WORLD_HEIGHT_PIXELS - WINDOW_HEIGHT_PIXELS / 2;
        }

        if(camera.position.y < WINDOW_HEIGHT_PIXELS / 2) {
            camera.position.y = WINDOW_HEIGHT_PIXELS / 2;
        }

        if(camera.position.x > WORLD_WIDTH_PIXELS - WINDOW_WIDTH_PIXELS / 2f) {
            camera.position.x = WORLD_WIDTH_PIXELS - WINDOW_WIDTH_PIXELS / 2f;
        }
    }

    private boolean overlapsMapObject (String path) {
        Array<RectangleMapObject> mapObjects = tiledMap.getLayers().get(path).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject mapObject : mapObjects) {
            Rectangle mapObjectRectangle = mapObject.getRectangle();
            if (frog.rectangle.overlaps(mapObjectRectangle)) {
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

    private void respawnFromWall () {
        if (overlapsMapObject("walls-rectangle")) {
            frog.returnToLastCheckpoint();
        }
    }

    private void moveEnemies() {
        for(Enemy enemy : enemies) {
            enemy.movement();
            enemy.checkCollision(frog);
        }
    }

    private void drawEnemies() {
        for(Enemy enemy : enemies) {
            enemy.draw(batch);
        }
    }

    private void spawnFrog() {
        Array<RectangleMapObject> startPoints = 
                tiledMap.getLayers().get("frog-spawn-rectangle").getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject startPoint : startPoints) {
            frog.setX(startPoint.getRectangle().getX());
            frog.setY(startPoint.getRectangle().getY());
            frog.setLastCheckpointX(frog.getX());
            frog.setLastCheckpointY(frog.getY());
        }
    }

    private void addEnemies() {
        //Adding enemies of the type RoundFish
        for (int i=1; i<=AMOUNT_ROUNDFISH; i++) {
            enemies.add(new RoundFish(TILE_DIMENSION));

            Array<RectangleMapObject> startPoints = 
                    tiledMap.getLayers().get("roundfish-"+i+"-start").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject startPoint : startPoints) {
                enemies.peek().setMOVEMENT_STARTPOINT_X(startPoint.getRectangle().getX());
                enemies.peek().setX(startPoint.getRectangle().getX());
                enemies.peek().setY(startPoint.getRectangle().getY());
            }

            Array<RectangleMapObject> endPoints =
                    tiledMap.getLayers().get("roundfish-"+i+"-end").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject endPoint : endPoints) {
                enemies.peek().setMOVEMENT_ENDPOINT_X(endPoint.getRectangle().getX()+endPoint.getRectangle().getWidth());
            }
        }

    }

}
