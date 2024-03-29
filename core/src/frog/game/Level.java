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
 * A level of the game
 *
 * <p>A general class for a level in the game, contains variables that modify the objects to make
 * different levels. The tiledMap file contains the spawn points for game objects, they are created
 * and placed accordingly to specified RectangleMapObjects.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0513
 * @since 2018.0313
 */
public class Level extends ScreenAdapter {
    /*
     * Core elements that enable rendering and gameplay.
     */
    private GameMain host;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;

    /*
     * Characters and objects.
     */
    private Player frog;
    private Array<Enemy> enemies;
    private Array<Checkpoint> checkpoints;
    private Array<TimeCoin> timeCoins;
    private Array<Rock> rocks;
    private Array<Seaweed> seaweeds;
    private Array<Arrow> arrows;

    /*
     * Amounts of enemies in the Level. Initialized in the constructor.
     *
     * The enemies' movement paths are defined in the TiledMap, so defining their amounts separately
     * is imperative in order to identify them and place them in the correct positions and avoid
     * conflicting paths.
     */
    private final int AMOUNT_ROUNDFISH, AMOUNT_LONGFISH, AMOUNT_OCTOPUS;

    /*
     * HUD elements
     */
    private Timer timer;
    private GenericButton homeButton;

    private final String identifier;

    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer tiledMapRenderer;
    private Texture backgroundTexture;
    private Array<Rectangle> backgroundRectangles;

    private final int TILE_DIMENSION;
    private final int TILE_AMOUNT_WIDTH, TILE_AMOUNT_HEIGHT;

    private final float WINDOW_WIDTH_PIXELS;
    private final float WINDOW_HEIGHT_PIXELS;
    private final int WORLD_WIDTH_PIXELS, WORLD_HEIGHT_PIXELS;

    /**
     * The constructor for Level
     *
     * Creates a new Level with the specified tiledMap, creates game objects and the user interface.
     *
     * @param host The main class, which controls the displayed screen.
     * @param identifier the level id/number
     * @param levelPath the path of the level's tiledMap
     * @param AMOUNT_ROUNDFISH the amount of RoundFish in the level
     * @param AMOUNT_LONGFISH the amount of LongFish in the level
     * @param AMOUNT_OCTOPUS the amount of Octopuses in the level
     * @param TILE_AMOUNT_WIDTH the amount of tiles in width in the level
     * @param TILE_AMOUNT_HEIGHT the amount of tiles in height in the level
     */
    public Level(GameMain host,
                 String identifier,
                 String levelPath,
                 int AMOUNT_ROUNDFISH,
                 int AMOUNT_LONGFISH,
                 int AMOUNT_OCTOPUS,
                 int TILE_AMOUNT_WIDTH,
                 int TILE_AMOUNT_HEIGHT) {

        this.host = host;
        batch = host.getBatch();
        TILE_DIMENSION = ConstantsManager.TILE_DIMENSION;

        this.TILE_AMOUNT_WIDTH = TILE_AMOUNT_WIDTH;
        this.TILE_AMOUNT_HEIGHT = TILE_AMOUNT_HEIGHT;
        WORLD_WIDTH_PIXELS = this.TILE_AMOUNT_WIDTH * TILE_DIMENSION;
        WORLD_HEIGHT_PIXELS = this.TILE_AMOUNT_HEIGHT * TILE_DIMENSION;

        WINDOW_WIDTH_PIXELS = host.getCamera().viewportWidth * 1.25f;
        WINDOW_HEIGHT_PIXELS = host.getCamera().viewportHeight * 1.25f;
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
        this.AMOUNT_OCTOPUS = AMOUNT_OCTOPUS;

        frog = new Player(tiledMap, TILE_DIMENSION);

        addLevelObjects();
        createBackground();
        createUI();

        this.identifier = identifier;
    }

    @Override
    public void show() {
        SoundController.playMusic();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.658f, 0.980f, 0.980f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        checkObjectCollision();
        moveEnemies();
        frog.movement(Gdx.graphics.getDeltaTime());
        respawnFromWall();
        endLevel();
        moveCamera();
        timer.update(delta);

        float x = camera.position.x - camera.viewportWidth * camera.zoom;
        float y = camera.position.y - camera.viewportHeight * camera.zoom;

        float width = camera.viewportWidth * camera.zoom * 2;
        float height = camera.viewportHeight * camera.zoom * 2;

        tiledMapRenderer.setView(camera.combined, x, y, width, height);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
            drawBackground();
            drawArrows();
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
        for (Arrow arrow : arrows) {
            arrow.dispose();
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
        SoundController.playMusic();
    }

    /**
     * Moves the camera in relation to the player's position.
     */
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

    /**
     * Collision detection between player and the tiledMap's object layers.
     *
     * Checks if the player's rectangle overlaps a RectangleMapObject in the specified object layer.
     *
     * @param layerName the name of the tiledMap object layer
     * @return true if it overlaps, false if not
     */
    private boolean overlapsMapObject (String layerName) {
        Array<RectangleMapObject> mapObjects = tiledMap.getLayers().get(layerName).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject mapObject : mapObjects) {
            Rectangle mapObjectRectangle = mapObject.getRectangle();
            if (frog.rectangle.overlaps(mapObjectRectangle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ends the level if it is completed.
     *
     * The level is completed if the player has gotten to the end of the level. Sets the screen to
     * the level finish screen if Xe has.
     */
    private void endLevel() {
        if (overlapsMapObject("endzone-rectangle")) {
            SoundController.backgroundMusic.stop();
            this.dispose();
            SoundController.playLevelFinishSound();
            host.setScreen(new LevelFinishMenu(host, identifier, timer.getTimeString()));
        }
    }

    /**
     * Respawns the player from within a wall.
     *
     * Returns the player to the last checkpoint in case Xe is within a wall of the tiledMap.
     * This should technically not happen.
     */
    private void respawnFromWall () {
        if (overlapsMapObject("walls-rectangle")) {
            frog.returnToLastCheckpoint();
        }
    }

    /**
     * Moves all enemies
     */
    private void moveEnemies() {
        for(Enemy enemy : enemies) {
            enemy.movement();
            if (enemy.collidesWith(frog)) {
                frog.returnToLastCheckpoint();
                SoundController.playHitSound();
            }
        }
    }

    /**
     * Collision detection between player and game objects.
     */
    private void checkObjectCollision() {
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.checkCollision(frog);
        }
        for (TimeCoin timeCoin : timeCoins) {
            timeCoin.checkCollision(frog);
            if (timeCoin.isCleared() && !timeCoin.isSubtracted()) {
                SoundController.playCoinSound();
                timeCoin.setSubtracted(true);
                timer.subtractTime(-5);
            }
        }
    }

    /**
     * Draws most game objects.
     */
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

    /**
     * Draws checkpoints.
     */
    private void drawCheckpoints() {
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.drawAnimation(batch);
        }
    }

    /**
     * Draws arrows.
     */
    private void drawArrows() {
        for (Arrow arrow : arrows) {
            arrow.draw(batch);
            arrow.movement();
        }
    }

    /**
     * Draws the user interface (timer and home button)
     */
    private void drawUI() {
        timer.draw(batch);
        homeButton.draw(batch);
    }

    /**
     * Draws the background texture.
     */
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

    /**
     * Creates all game objects.
     */
    private void addLevelObjects() {
        enemies = new Array<Enemy>();
        checkpoints = new Array<Checkpoint>();
        timeCoins = new Array<TimeCoin>();
        seaweeds = new Array<Seaweed>();
        rocks = new Array<Rock>();
        arrows = new Array<Arrow>();

        addRoundFish();
        addLongFish();
        addOctopuses();
        addCheckpoints();
        addTimeCoins();
        addSeaweed();
        addRocks();
        addArrows();
    }

    /**
     * Creates RoundFish
     */
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

    /**
     * Creates LongFish
     */
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

    /**
     * Creates Octopuses
     */
    private void addOctopuses() {
        //Adding enemies of the type Octopus
        for (int i=1; i<=AMOUNT_OCTOPUS; i++) {
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

    }

    /**
     * Creates checkpoints.
     */
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

    /**
     * Creates TimeCoins (reward)
     */
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

    /**
     * Creates Seaweed (scenery)
     */
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

    /**
     * Creates Rocks (obstacle)
     */
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

    /**
     * Creates directional Arrows
     */
    private void addArrows() {
        String[] directions = {"up", "down", "left", "right"};
        String arrow = "arrow-";

        for (String direction : directions) {
            if (tiledMap.getLayers().get(arrow + direction) != null) {
                Array<RectangleMapObject> arrowRectangles =
                        tiledMap.getLayers().get(arrow + direction).getObjects().getByType(RectangleMapObject.class);
                for (RectangleMapObject arrowRectangle : arrowRectangles) {
                    arrows.add(new Arrow(TILE_DIMENSION*1.5f, direction));
                    arrows.peek().setX(arrowRectangle.getRectangle().getX());
                    arrows.peek().setY(arrowRectangle.getRectangle().getY());
                }
            }
        }
    }

    /**
     * Creates the user interface
     */
    private void createUI() {
        timer = new Timer(WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS);

        float menuButtonWidth = WINDOW_WIDTH_PIXELS * (4f/40f);
        homeButton = new GenericButton(menuButtonWidth,
                ConstantsManager.homeButtonIdlePath,
                ConstantsManager.homeButtonPressedPath);
        homeButton.setX(0);
        homeButton.setY(WINDOW_HEIGHT_PIXELS-homeButton.getHeight());

        Gdx.input.setInputProcessor(new InputAdapter() {
            Vector3 touch;

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                uiCamera.unproject(touch);
                if (homeButton.getRectangle().contains(touch.x, touch.y)) homeButton.setPressed(true);

                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                uiCamera.unproject(touch);
                if (homeButton.getRectangle().contains(touch.x, touch.y)) homeButton.setPressed(true);
                else homeButton.setPressed(false);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
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

    /**
     * Creates the background texture and specifies how it should be positioned.
     */
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
