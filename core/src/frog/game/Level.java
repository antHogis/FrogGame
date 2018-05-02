package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Anton on 13.3.2018.
 */

public class Level extends ScreenAdapter {
    /*
     * Core elements that enable rendering and gameplay.
     */
    private FrogMain host;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private boolean gameRunning;

    /*
     * Characters and objects.
     */
    private Player frog;
    private Array<Enemy> enemies;
    private Array<Checkpoint> checkpoints;
    private Array<TimeCoin> timeCoins;
    private Array<Rock> rocks;
    private Array<Seaweed> seaweeds;

    /*
     * Amounts of enemies in the Level. Initialized in the constructor.
     *
     * The enemies' movement paths are defined in the TiledMap, so defining their amounts separately
     * is imperative in order to identify them and place them in the correct positions and avoid
     * conflicting paths.
     */
    private final int AMOUNT_ROUNDFISH, AMOUNT_LONGFISH, AMOUNT_OCTOPUS1, AMOUNT_OCTOPUS2;

    /*
     * HUD elements and relevant modifiers
     */
    private Timer timer;
    private HomeButton homeButton;

    private final String identifier;

    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;
    private Texture backgroundTexture;
    private Array<Rectangle> backgroundRectangles;

    private final int TILE_DIMENSION;
    private final int TILE_AMOUNT_WIDTH, TILE_AMOUNT_HEIGHT;

    private final int WINDOW_WIDTH_PIXELS = 1600;
    private final int WINDOW_HEIGHT_PIXELS = 1000;
    private final int WORLD_WIDTH_PIXELS, WORLD_HEIGHT_PIXELS;

    public Level(FrogMain host,
                 String identifier,
                 String levelPath,
                 int AMOUNT_ROUNDFISH,
                 int AMOUNT_LONGFISH,
                 int AMOUNT_OCTOPUS1,
                 int AMOUNT_OCTOPUS2,
                 int TILE_AMOUNT_WIDTH,
                 int TILE_AMOUNT_HEIGHT) {

        this.host = host;
        batch = host.getBatch();
        TILE_DIMENSION = ConstantsManager.TILE_DIMENSION;

        this.TILE_AMOUNT_WIDTH = TILE_AMOUNT_WIDTH;
        this.TILE_AMOUNT_HEIGHT = TILE_AMOUNT_HEIGHT;
        WORLD_WIDTH_PIXELS = this.TILE_AMOUNT_WIDTH * TILE_DIMENSION;
        WORLD_HEIGHT_PIXELS = this.TILE_AMOUNT_HEIGHT * TILE_DIMENSION;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                WINDOW_WIDTH_PIXELS,
                WINDOW_HEIGHT_PIXELS);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS);

        tiledMap = new TmxMapLoader().load(levelPath);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        this.AMOUNT_ROUNDFISH = AMOUNT_ROUNDFISH;
        this.AMOUNT_LONGFISH = AMOUNT_LONGFISH;
        this.AMOUNT_OCTOPUS1 = AMOUNT_OCTOPUS1;
        this.AMOUNT_OCTOPUS2 = AMOUNT_OCTOPUS2;

        frog = new Player(tiledMap, TILE_DIMENSION);

        enemies = new Array<Enemy>();
        checkpoints = new Array<Checkpoint>();
        timeCoins = new Array<TimeCoin>();
        seaweeds = new Array<Seaweed>();
        rocks = new Array<Rock>();
        addLevelObjects();
        createBackground();
        createUI();

        this.identifier = identifier;
        gameRunning = true;
    }

    @Override
    public void show() {
        if (ConstantsManager.settings.getBoolean("music-on", ConstantsManager.DEFAULT_AUDIO_ON)) {
            SoundController.backgroundMusic.play();
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.658f, 0.980f, 0.980f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        float x = camera.position.x - camera.viewportWidth * camera.zoom;
        float y = camera.position.y - camera.viewportHeight * camera.zoom;

        float width = camera.viewportWidth * camera.zoom * 2;
        float height = camera.viewportHeight * camera.zoom * 2;

        tiledMapRenderer.setView(camera.combined, x, y, width, height);
        batch.setProjectionMatrix(camera.combined);

        checkObjectCollision();
        moveEnemies();
        frog.movementAndroid(Gdx.graphics.getDeltaTime());
        frog.moveTemporary(Gdx.graphics.getDeltaTime());
        respawnFromWall();
        endLevel();
        moveCamera();
        timer.update(delta);



        batch.begin();
            drawBackground();
            frog.drawAnimation(batch);
            drawObjects();
        batch.end();

        tiledMapRenderer.render();

        batch.begin();
            drawCheckpoints();
        batch.end();

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        drawUI();
        batch.end();
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        frog.dispose();
        for(Enemy enemy : enemies) {
            enemy.dispose();
        }
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.dispose();
        }
        for (TimeCoin timeCoin: timeCoins) {
            timeCoin.dispose();
        }
        for (Seaweed seaweed : seaweeds) {
            seaweed.dispose();
        }
        for (Rock rock : rocks) {
            rock.dispose();
        }
        backgroundTexture.dispose();
        homeButton.dispose();
        timer.dispose();
    }

    @Override
    public void pause() {
        SoundController.backgroundMusic.pause();
    }

    @Override
    public void resume() {
        SoundController.initialize();
        if (ConstantsManager.settings.getBoolean("music-on", ConstantsManager.DEFAULT_AUDIO_ON)) {
            SoundController.backgroundMusic.play();
        }
    }

    private void moveCamera () {
        camera.position.set(frog.getX()+frog.getWidth()/2,
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
            SoundController.backgroundMusic.stop();
            this.dispose();
            host.setScreen(new LevelFinish(host, identifier, timer.getTimeString()));
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
            if (enemy.collidesWith(frog)) {
                frog.returnToLastCheckpoint();
                SoundController.hitEnemy.play();
            }
        }
    }

    private void checkObjectCollision() {
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.checkCollision(frog);
        }
        for (TimeCoin timeCoin : timeCoins) {
            timeCoin.checkCollision(frog);
            if (timeCoin.isCleared() && !timeCoin.isSubtracted()) {
                SoundController.collectCoin.play();
                timeCoin.setSubtracted(true);
                timer.subtractTime(-5);
            }
        }
    }

    private void drawObjects() {
        for(Enemy enemy : enemies) {
            enemy.drawAnimation(batch);
        }
        for (TimeCoin timeCoin : timeCoins) {
            if (!timeCoin.isCleared()) {
                timeCoin.drawAnimation(batch);
                timeCoin.movement();
            }
        }
        for (Seaweed seaweed : seaweeds) {
            seaweed.draw(batch);
        }
        for (Rock rock : rocks) {
            rock.draw(batch);
        }
    }

    private void drawCheckpoints() {
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.drawAnimation(batch);
        }
    }

    private void drawUI() {
        timer.draw(batch);
        homeButton.draw(batch);
    }

    private void drawBackground() {
        for (int i = 0; i < backgroundRectangles.size; i++) {
            batch.draw(backgroundTexture,
                    backgroundRectangles.get(i).x,
                    backgroundRectangles.get(i).y,
                    backgroundRectangles.get(i).width,
                    backgroundRectangles.get(i).height);
        }
        /*batch.draw(backgroundTexture,
                bgRectangle.x,
                bgRectangle.y,
                bgRectangle.width,
                bgRectangle.height);*/

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
        //Adding enemies of the type Octopus
        for (int i=1; i<=AMOUNT_OCTOPUS1; i++) {
            enemies.add(new Octopus(TILE_DIMENSION));

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
            enemies.add(new Octopus(TILE_DIMENSION));

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
        if (tiledMap.getLayers().get("checkpoint-rectangle") != null) {
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
    }

    private void addTimeCoins() {
        if (tiledMap.getLayers().get("timecoin-rectangle") != null) {
            Array<RectangleMapObject> timeCoinRectangles =
                    tiledMap.getLayers().get("timecoin-rectangle").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject timeCoinRectangle : timeCoinRectangles) {
                timeCoins.add(new TimeCoin(timeCoinRectangle.getRectangle().getX(),
                        timeCoinRectangle.getRectangle().getY(),
                        TILE_DIMENSION));
            }
        }
    }

    private void addSeaweed() {
        //Adding seaweed that point up
        if (tiledMap.getLayers().get("grass-up") != null) {
            Array<RectangleMapObject> seaweedRectangles =
                    tiledMap.getLayers().get("grass-up").getObjects().getByType(RectangleMapObject.class);

            for (RectangleMapObject seaweedRectangle : seaweedRectangles) {
                seaweeds.add(new Seaweed(seaweedRectangle.getRectangle().getX(),
                        seaweedRectangle.getRectangle().getY(),
                        TILE_DIMENSION,
                        true));
            }
        }

        //Adding seaweed that point down
        if (tiledMap.getLayers().get("grass-down") != null) {
            Array<RectangleMapObject> seaweedRectangles =
                    tiledMap.getLayers().get("grass-down").getObjects().getByType(RectangleMapObject.class);

            for (RectangleMapObject seaweedRectangle : seaweedRectangles) {
                seaweeds.add(new Seaweed(seaweedRectangle.getRectangle().getX(),
                        0,
                        TILE_DIMENSION,
                        false));
                //Set post-construction to allow grass rectangles to be made as any size in Tiled
                seaweeds.peek().setY(seaweedRectangle.getRectangle().getY()
                        + seaweedRectangle.getRectangle().getHeight() - seaweeds.peek().getHeight()
                        + TILE_DIMENSION/4);
            }
        }

    }

    private void addRocks() {
        //Adding rocks that point up
        if (tiledMap.getLayers().get("rock-up") != null) {
            Array<RectangleMapObject> rockRectangles =
                    tiledMap.getLayers().get("rock-up").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject rockRectangle : rockRectangles) {
                rocks.add(new Rock(rockRectangle.getRectangle().getX(),
                        rockRectangle.getRectangle().getY(),
                        TILE_DIMENSION,
                        true));
            }
        }

        //Adding rocks that point down
        if (tiledMap.getLayers().get("rock-down") != null) {
            Array<RectangleMapObject> rockRectangles =
                    tiledMap.getLayers().get("rock-down").getObjects().getByType(RectangleMapObject.class);
            for (RectangleMapObject rockRectangle : rockRectangles) {
                rocks.add(new Rock(rockRectangle.getRectangle().getX(),
                        0,
                        TILE_DIMENSION,
                        false));
                rocks.peek().setY(rockRectangle.getRectangle().getY()
                        + rockRectangle.getRectangle().getHeight() - rocks.peek().getHeight() + TILE_DIMENSION/4);
            }
        }
    }

    private void createUI() {
        timer = new Timer(WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS);

        float menuButtonWidth = WINDOW_WIDTH_PIXELS * (1.5f/16);
        homeButton = new HomeButton(menuButtonWidth);
        homeButton.setX(0);
        homeButton.setY(WINDOW_HEIGHT_PIXELS-homeButton.getHeight());

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                uiCamera.unproject(touch);
                if (homeButton.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    Level.this.dispose();
                    SoundController.backgroundMusic.stop();
                    host.setScreen(new MainMenu(host));
                }
                return true;
            }
        });
    }

    private void createBackground() {
        backgroundTexture = new Texture(ConstantsManager.bgGamePath);
        backgroundRectangles = new Array<Rectangle>();
        float backgroundWidth = WINDOW_WIDTH_PIXELS*1.5f;
        float backgroundHeight = (backgroundWidth * backgroundTexture.getHeight()) / backgroundTexture.getWidth();

        for (int y = 0; y < WORLD_HEIGHT_PIXELS; y+= backgroundHeight) {
            for (int x = 0; x  < WORLD_WIDTH_PIXELS; x += backgroundWidth) {
                backgroundRectangles.add(new Rectangle(x,y, backgroundWidth, backgroundHeight));
            }
        }
        /*backgroundTexture = new Texture(ConstantsManager.bgGamePath);
        if(WORLD_WIDTH_PIXELS > WORLD_HEIGHT_PIXELS) {
            float height = (backgroundTexture.getWidth()*WORLD_HEIGHT_PIXELS) / WORLD_WIDTH_PIXELS;
            bgRectangle = new Rectangle(0,0, WORLD_WIDTH_PIXELS, height);
        } else {
            float width = (backgroundTexture.getHeight()*WORLD_WIDTH_PIXELS) / WORLD_HEIGHT_PIXELS;
            bgRectangle = new Rectangle(0,0, width, WORLD_HEIGHT_PIXELS);
        }*/
    }
}
