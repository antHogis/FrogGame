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
    private Array<Enemy> enemies;
    private Array<Checkpoint> checkpoints;
    private Array<TimeCoin> timeCoins;
    private Array<Rock> rocks;
    private Array<Seaweed> seaweeds;

    private final float startTime;
    private int timeSubtracted = 0;

    private Music bgMusic;
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;

    private final int AMOUNT_ROUNDFISH;
    private final int AMOUNT_LONGFISH;
    private final int AMOUNT_OCTOPUS1;
    private final int AMOUNT_OCTOPUS2;

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
                 int AMOUNT_LONGFISH,
                 int AMOUNT_OCTOPUS1,
                 int AMOUNT_OCTOPUS2) {

        this.host = host;
        batch = host.getBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                WINDOW_WIDTH_PIXELS,
                WINDOW_HEIGHT_PIXELS);

        tiledMap = new TmxMapLoader().load(levelPath);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        frog = new Player(tiledMap, TILE_DIMENSION);

        this.AMOUNT_ROUNDFISH = AMOUNT_ROUNDFISH;
        this.AMOUNT_LONGFISH = AMOUNT_LONGFISH;
        this.AMOUNT_OCTOPUS1 = AMOUNT_OCTOPUS1;
        this.AMOUNT_OCTOPUS2 = AMOUNT_OCTOPUS2;

        enemies = new Array<Enemy>();
        checkpoints = new Array<Checkpoint>();
        timeCoins = new Array<TimeCoin>();
        seaweeds = new Array<Seaweed>();
        rocks = new Array<Rock>();
        addLevelObjects();

        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mariowater.mp3"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.25f);
        //bgMusic.play();
        spawnFrog();

        startTime = System.currentTimeMillis();
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

        checkObjectCollision();
        moveEnemies();

        frog.movementAndroid(Gdx.graphics.getDeltaTime());
        frog.moveTemporary(Gdx.graphics.getDeltaTime());
        moveCamera();

        tiledMapRenderer.render();

        batch.begin();
        frog.draw(batch);
        drawObjects();
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
            int endTime = (int) ((System.currentTimeMillis()-startTime)/1000);
            Gdx.app.log("TAG", "Time before subtracted: " + Integer.toString(endTime));
            endTime += timeSubtracted;
            Gdx.app.log("TAG", "Time after subtracted:" + Integer.toString(endTime));
            host.setScreen(new LevelFinish(host, endTime));
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

    private void drawObjects() {
        for(Enemy enemy : enemies) {
            enemy.draw(batch);
        }
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.draw(batch);
        }
        for (TimeCoin timeCoin : timeCoins) {
            if (!timeCoin.getIsCleared()) {
                timeCoin.draw(batch);
            }
        }
        for (Seaweed seaweed : seaweeds) {
            seaweed.draw(batch);
        }
        for (Rock rock : rocks) {
            rock.draw(batch);
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

    private void checkObjectCollision() {
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.checkCollision(frog);
        }
        for (TimeCoin timeCoin : timeCoins) {
            timeCoin.checkCollision(frog);
            if (timeCoin.getIsCleared() && !timeCoin.isSubtracted()) {
                timeSubtracted -= 5;
                timeCoin.setSubtracted(true);
            }
        }
        for (Seaweed seaweed : seaweeds) {
            seaweed.checkCollision(frog);
        }
        for (Rock rock : rocks) {
            rock.checkCollision(frog);
        }
    }

    private void addLevelObjects() {
        addRoundFish();
        addLongFish();
        addOctopi();
        addCheckpoints();
        addTimeCoins();
        addSeaweed();
        addRocks();
    }

    private void addRoundFish() {
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

    private void addLongFish() {
        for (int i=1; i<=AMOUNT_LONGFISH; i++) {
            enemies.add(new LongFish(TILE_DIMENSION));

            Array<RectangleMapObject> startPoints =
                    tiledMap.getLayers().get("longfish-"+i+"-start").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject startPoint : startPoints) {
                enemies.peek().setMOVEMENT_STARTPOINT_X(startPoint.getRectangle().getX());
                enemies.peek().setX(startPoint.getRectangle().getX());
                enemies.peek().setY(startPoint.getRectangle().getY());
            }

            Array<RectangleMapObject> endPoints =
                    tiledMap.getLayers().get("longfish-"+i+"-end").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject endPoint : endPoints) {
                enemies.peek().setMOVEMENT_ENDPOINT_X(endPoint.getRectangle().getX()+endPoint.getRectangle().getWidth());
            }
        }
    }

    private void addOctopi() {
        //Adding enemies of the type Octopus1
        for (int i=1; i<=AMOUNT_OCTOPUS1; i++) {
            enemies.add(new Octopus1(TILE_DIMENSION));

            Array<RectangleMapObject> startPoints =
                    tiledMap.getLayers().get("octopus1-"+i+"-start").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject startPoint : startPoints) {
                enemies.peek().setMOVEMENT_STARTPOINT_Y(startPoint.getRectangle().getY()+startPoint.getRectangle().getHeight()-enemies.peek().getHeight());
                enemies.peek().setX(startPoint.getRectangle().getX());
                enemies.peek().setY(enemies.peek().getMOVEMENT_STARTPOINT_Y());
            }

            Array<RectangleMapObject> endPoints =
                    tiledMap.getLayers().get("octopus1-"+i+"-end").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject endPoint : endPoints) {
                enemies.peek().setMOVEMENT_ENDPOINT_Y(endPoint.getRectangle().getY());
            }
        }

        //Adding enemies of the type Octopus2
        for (int i=1; i<=AMOUNT_OCTOPUS2; i++) {
            enemies.add(new Octopus1(TILE_DIMENSION));

            Array<RectangleMapObject> startPoints =
                    tiledMap.getLayers().get("octopus2-"+i+"-start").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject startPoint : startPoints) {
                enemies.peek().setMOVEMENT_STARTPOINT_Y(startPoint.getRectangle().getY()+startPoint.getRectangle().getHeight()-enemies.peek().getHeight());
                enemies.peek().setX(startPoint.getRectangle().getX());
                enemies.peek().setY(enemies.peek().getMOVEMENT_STARTPOINT_Y());
            }

            Array<RectangleMapObject> endPoints =
                    tiledMap.getLayers().get("octopus2-"+i+"-end").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject endPoint : endPoints) {
                enemies.peek().setMOVEMENT_ENDPOINT_Y(endPoint.getRectangle().getY());
            }
        }
    }

    private void addCheckpoints() {
        Array<RectangleMapObject> checkpointRectangles =
                tiledMap.getLayers().get("checkpoint-rectangle").getObjects().getByType(RectangleMapObject.class);

        for (RectangleMapObject checkpointRectangle : checkpointRectangles) {
            checkpoints.add(new Checkpoint(checkpointRectangle.getRectangle().getX(),
                    checkpointRectangle.getRectangle().getY(),
                    checkpointRectangle.getRectangle().getWidth(),
                    checkpointRectangle.getRectangle().getHeight(),
                    TILE_DIMENSION));
        }
    }

    private void addTimeCoins() {
        Array<RectangleMapObject> timeCoinRectangles =
                tiledMap.getLayers().get("timecoin-rectangle").getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject timeCoinRectangle : timeCoinRectangles) {
            timeCoins.add(new TimeCoin(timeCoinRectangle.getRectangle().getX(),
                    timeCoinRectangle.getRectangle().getY(),
                    TILE_DIMENSION));
        }
    }

    private void addSeaweed() {
        //Adding seaweed that point up
        Array<RectangleMapObject> seaweedRectangles =
                tiledMap.getLayers().get("grass-up").getObjects().getByType(RectangleMapObject.class);

        for (RectangleMapObject seaweedRectangle : seaweedRectangles) {
            seaweeds.add(new Seaweed(seaweedRectangle.getRectangle().getX(),
                    seaweedRectangle.getRectangle().getY(),
                    TILE_DIMENSION,
                    true));
        }

        //Adding seaweed that point down
        seaweedRectangles =
                tiledMap.getLayers().get("grass-down").getObjects().getByType(RectangleMapObject.class);

        for (RectangleMapObject seaweedRectangle : seaweedRectangles) {
            seaweeds.add(new Seaweed(seaweedRectangle.getRectangle().getX(),
                    0,
                    TILE_DIMENSION,
                    false));
            //Set post-construction to allow grass rectangles to be made as any size in Tiled
            seaweeds.peek().setY(seaweedRectangle.getRectangle().getY()
                    + seaweedRectangle.getRectangle().getHeight() - seaweeds.peek().getHeight());
        }
    }

    private void addRocks() {
        //Adding rocks that point up
        Array<RectangleMapObject> rockRectangles =
                tiledMap.getLayers().get("rock-up").getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rockRectangle : rockRectangles) {
            rocks.add(new Rock(rockRectangle.getRectangle().getX(),
                    rockRectangle.getRectangle().getY(),
                    TILE_DIMENSION,
                    true));
        }

        //Adding rocks that point down
        rockRectangles =
                tiledMap.getLayers().get("rock-down").getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rockRectangle : rockRectangles) {
            rocks.add(new Rock(rockRectangle.getRectangle().getX(),
                    0,
                    TILE_DIMENSION,
                    false));
            rocks.peek().setY(rockRectangle.getRectangle().getY()
                    + rockRectangle.getRectangle().getHeight() - rocks.peek().getHeight());
        }
    }

}
