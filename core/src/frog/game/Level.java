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
    /*
     * Core elements that enable rendering and gameplay.
     */
    private FrogMain host;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private OrthographicCamera camera;
    private OrthographicCamera overlayCamera;
    private boolean gameRunning;

    /*
     * Characters and objects in the level.
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
    private HomeButton menuButton;
    private PromptResponse returnToMenuButton;
    private PromptResponse resumeGameButton;
    private ReturnPrompt prompt;
    private boolean drawPrompt;


    private final Music bgMusic;
    private final TiledMap tiledMap;
    private final TiledMapRenderer tiledMapRenderer;

    private final int TILE_DIMENSION;
    private final int TILE_AMOUNT_WIDTH, TILE_AMOUNT_HEIGHT;

    private final int WINDOW_WIDTH_PIXELS = 1664;
    private final int WINDOW_HEIGHT_PIXELS = 1040;
    private final int WORLD_WIDTH_PIXELS, WORLD_HEIGHT_PIXELS;

    public Level(FrogMain host,
                 String levelPath,
                 int AMOUNT_ROUNDFISH,
                 int AMOUNT_LONGFISH,
                 int AMOUNT_OCTOPUS1,
                 int AMOUNT_OCTOPUS2,
                 int TILE_AMOUNT_WIDTH,
                 int TILE_AMOUNT_HEIGHT) {

        this.host = host;
        batch = host.getBatch();
        TILE_DIMENSION = host.getTILE_DIMENSION();

        this.TILE_AMOUNT_WIDTH = TILE_AMOUNT_WIDTH;
        this.TILE_AMOUNT_HEIGHT = TILE_AMOUNT_HEIGHT;
        WORLD_WIDTH_PIXELS = this.TILE_AMOUNT_WIDTH * TILE_DIMENSION;
        WORLD_HEIGHT_PIXELS = this.TILE_AMOUNT_HEIGHT * TILE_DIMENSION;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                WINDOW_WIDTH_PIXELS,
                WINDOW_HEIGHT_PIXELS);
        overlayCamera = camera;

        tiledMap = new TmxMapLoader().load(levelPath);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        this.AMOUNT_ROUNDFISH = AMOUNT_ROUNDFISH;
        this.AMOUNT_LONGFISH = AMOUNT_LONGFISH;
        this.AMOUNT_OCTOPUS1 = AMOUNT_OCTOPUS1;
        this.AMOUNT_OCTOPUS2 = AMOUNT_OCTOPUS2;

        frog = new Player(tiledMap, TILE_DIMENSION);
        spawnFrog();

        enemies = new Array<Enemy>();
        checkpoints = new Array<Checkpoint>();
        timeCoins = new Array<TimeCoin>();
        seaweeds = new Array<Seaweed>();
        rocks = new Array<Rock>();
        addLevelObjects();

        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("music/demo1-leikattu.wav"));
        bgMusic.setLooping(true);
        bgMusic.setVolume(0.4f);

        createHUD_elements();
        gameRunning = true;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.658f, 0.980f, 0.980f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isTouched()) {
            promptReturn();
            Gdx.app.log("TAG", "Touched");
        }

        if (gameRunning) {
            checkObjectCollision();
            moveEnemies();
            frog.movementAndroid(Gdx.graphics.getDeltaTime());
            frog.moveTemporary(Gdx.graphics.getDeltaTime());
            respawnFromWall();
            endLevel();
            moveCamera();
            timer.update();
        }

        batch.begin();
            frog.drawAnimation(batch);
            drawObjects();
        batch.end();

        tiledMapRenderer.render();

        batch.begin();
            drawCheckpoints();
            drawHUD();
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
        batch.dispose();
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
            int nextIndex = host.getLevels().indexOf(this, true) + 1;
            bgMusic.stop();
            host.setScreen(new LevelFinish(host, timer.getTimeString(), nextIndex));
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
                SoundController.perkele.play();
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
            checkpoint.draw(batch);
        }
    }

    private void drawHUD() {
        timer.draw(batch, camera.position.x, camera.position.y);
        menuButton.draw(batch, camera.position.x, camera.position.y);

        if (drawPrompt) {
            prompt.draw(batch);
            returnToMenuButton.draw(batch);
            resumeGameButton.draw(batch);
        }
    }

    private void spawnFrog() {
        Array<RectangleMapObject> startPoints = 
                tiledMap.getLayers().get("frog-spawn-rectangle").getObjects().getByType(RectangleMapObject.class);

        for (RectangleMapObject startPoint : startPoints) {
            frog.setFrogSpawn(startPoint.getRectangle(), TILE_DIMENSION);
        }
    }

    private void checkObjectCollision() {
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.checkCollision(frog);
        }
        for (TimeCoin timeCoin : timeCoins) {
            timeCoin.checkCollision(frog);
            if (timeCoin.isCleared() && !timeCoin.isSubtracted()) {
                timeCoin.setSubtracted(true);
                timer.subtractTime(-5);
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
                        + seaweedRectangle.getRectangle().getHeight() - seaweeds.peek().getHeight());
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
                        + rockRectangle.getRectangle().getHeight() - rocks.peek().getHeight());
            }
        }
    }

    public void resetTimer() {
        timer.reset();
    }

    private void createHUD_elements() {
        timer = new Timer(WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS);
        menuButton = new HomeButton(WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS, TILE_DIMENSION);

        prompt = new ReturnPrompt(WINDOW_WIDTH_PIXELS, WINDOW_HEIGHT_PIXELS);
        returnToMenuButton = new PromptResponse("ui/joo.png", prompt.getRectangle().getWidth());
        resumeGameButton = new PromptResponse("ui/ei.png", prompt.getRectangle().getWidth());

    }

    private void promptReturn() {
        if (gameRunning && menuButton.isTouched(camera)) {
            gameRunning = false;
        }

        if (!gameRunning && !drawPrompt) {
            drawPrompt = true;
            prompt.setX(camera.position.x);
            prompt.setY(camera.position.y);
            Gdx.app.log("Camera", "x " + camera.position.x +" y "+ camera.position.y);
            Gdx.app.log("Prompt", "x " + prompt.getRectangle().getX()
                    + "y " + prompt.getRectangle().getY());

            /*
             * Creating variables of prompt window size, in order to set it's buttons to scale
             */
            float promptX = prompt.getRectangle().getX();
            float promptY = prompt.getRectangle().getY();
            float promptWidth = prompt.getRectangle().getWidth();
            float promptEight = promptWidth/8;

            returnToMenuButton.setX(promptX + promptEight);
            returnToMenuButton.setY(promptY + promptEight);
            Gdx.app.log("Return ", "x " + returnToMenuButton.getX() + "y " + returnToMenuButton.getY());

            resumeGameButton.setX(promptX + promptWidth - promptEight - resumeGameButton.getWidth());
            resumeGameButton.setY(promptY + promptEight);
            Gdx.app.log("Resume ", "x " + resumeGameButton.getX() + " y " + resumeGameButton.getY());
        }

        if (!gameRunning) {
            if(returnToMenuButton.isTouched(camera)) {
                host.setScreen(new MainMenu(host));
            } else if (resumeGameButton.isTouched(camera)) {
                gameRunning = true;
                drawPrompt = false;
            }
        }
    }

    public void startMusic() {
        bgMusic.play();
    }
}
