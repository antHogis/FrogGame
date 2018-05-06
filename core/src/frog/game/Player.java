package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


/**
 * Player is the player's character.
 *
 * <p>Player is the player's character, which is represented as frog with an old-school strongman aesthetic.
 * The character is controlled using accelerometer input.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0310
 */
class Player extends GameObject {

    private TiledMap tiledMap;
    private float lastCheckpointX, lastCheckpointY;
    private Rectangle hitBox;
    private boolean flippedRight = true;

    /*
     * Movement modifying values
     */
    private float NEUTRAL_POINT_X, NEUTRAL_POINT_Y,THRESHOLD_VALUE,THRESHOLD_MIN_X_RIGHT,
            THRESHOLD_MIN_X_LEFT,THRESHOLD_MIN_Y_FORWARD,THRESHOLD_MIN_Y_BACK, SPEED_X,SPEED_FORWARD,
            SPEED_BACKWARDS,invert_Value;

    /**
     * The constructor of Player.
     *
     * Initializes the animation textures of the player, the movement control modifiers of the player,
     * the initial position of the player and the hit-box of the player.
     *
     * @param tiledMap the tiledMap with which the player's character interacts.
     * @param TILE_DIMENSION the dimension of a tile in the tiledMap.
     */
    public Player(TiledMap tiledMap, int TILE_DIMENSION) {
        this.tiledMap = tiledMap;

        textureSheet = new Texture("gfx/playerSheet.png");
        SHEET_COLUMNS = 4;
        SHEET_ROWS = 3;
        textureSheet2D = TextureRegion.split(textureSheet,
                textureSheet.getWidth() / SHEET_COLUMNS,
                textureSheet.getHeight() / SHEET_ROWS);
        textureSheet1D = convert2Dto1D(textureSheet2D);

        stateTime = 1f;
        animation = new Animation<TextureRegion>(3/60f, textureSheet1D);
        currentFrame = animation.getKeyFrame(stateTime, true);

        rectangle = new Rectangle();
        this.rectangle.setWidth(TILE_DIMENSION*3);
        this.rectangle.setHeight((currentFrame.getRegionHeight()*rectangle.getWidth())/currentFrame.getRegionWidth());

        hitBox = new Rectangle();
        hitBox.setWidth((8f/16f)*rectangle.getWidth());
        hitBox.setHeight((11f/16f)*rectangle.getHeight());

        setFrogSpawn(TILE_DIMENSION);
        initializeMovement();
    }

    /**
     * Initializes the movement controls.
     *
     * Initializes the movement controls with the modifiers stored in persistent memory.
     * Also adjusts the neutral point X and Y values, which signify how the tablet is tilted when a
     * level is started. The movement is later adjusted on the basis of the neutral point X and Y.
     * Note: X = accelerometer Y-axis, Y = accelerometer Z-axis.
     */
    private void initializeMovement() {
        resetAccelerometerPosition();

        THRESHOLD_VALUE = ConstantsManager.settings.getFloat("threshold",
                ConstantsManager.DEFAULT_THRESHOLD);

        THRESHOLD_MIN_X_RIGHT = THRESHOLD_VALUE;
        THRESHOLD_MIN_X_LEFT = -1*THRESHOLD_VALUE;
        THRESHOLD_MIN_Y_FORWARD = THRESHOLD_VALUE + 0.3f;
        THRESHOLD_MIN_Y_BACK = (-1*THRESHOLD_VALUE) + 0.15f;

        SPEED_X = ConstantsManager.settings.getFloat("speed",
                ConstantsManager.DEFAULT_SPEED);

        SPEED_FORWARD = SPEED_X + 10;
        SPEED_BACKWARDS = SPEED_X + 30;

        boolean INVERT_Y_AXIS = ConstantsManager.settings.getBoolean("y-invert",
                ConstantsManager.DEFAULT_INVERT_Y);

        if (INVERT_Y_AXIS) {
            invert_Value = -1;
        } else {
            invert_Value = 1;
        }

    }

    /**
     * Moves the player's character.
     *
     * Handles the movement based on accelerometer input, and moves the player accordingly.
     * Restricts movement through the walls in the tiledMap.
     * Also moves the players hit-box according to the player's position.
     *
     * @param delta the rendering delta time
     */
    public void movement (float delta) {
        float movementRight = delta * SPEED_X * getAdjustedX();
        float movementLeft = movementRight;
        float movementForward = delta * SPEED_FORWARD * getAdjustedY() * invert_Value;
        float movementBack = delta * SPEED_BACKWARDS * getAdjustedY() * invert_Value;


        //RIGHT
        if (getAdjustedX()> THRESHOLD_MIN_X_RIGHT
                && !overlapsMapObject("walls-rectangle",
                    new Rectangle(this.rectangle.x+movementRight, this.rectangle.y,
                        this.rectangle.width,
                        this.rectangle.height))) {
            rectangle.setX(rectangle.getX() + movementRight);
            if (!flippedRight) {
                flippedRight = true;
                flip(animation, true, false);
            }
        }

        //LEFT
        if (getAdjustedX() < THRESHOLD_MIN_X_LEFT
                && !overlapsMapObject("walls-rectangle",
                    new Rectangle(this.rectangle.x+movementLeft, this.rectangle.y,
                        this.rectangle.width,
                        this.rectangle.height))) {
            rectangle.setX(rectangle.getX() + movementLeft);
            if(flippedRight) {
                flippedRight = false;
                flip(animation, true, false);
            }
        }

        //FORWARD
        if (getAdjustedY() > THRESHOLD_MIN_Y_FORWARD
                && !overlapsMapObject("walls-rectangle",
                    new Rectangle(this.rectangle.x, this.rectangle.y + movementForward,
                        this.rectangle.width,
                        this.rectangle.height))) {
            rectangle.setY(rectangle.getY() + movementForward);
        }

        //BACKWARDS
        if (getAdjustedY() < THRESHOLD_MIN_Y_BACK
                && !overlapsMapObject("walls-rectangle",
                    new Rectangle(this.rectangle.x, this.rectangle.y + movementBack,
                        this.rectangle.width,
                        this.rectangle.height))) {
            rectangle.setY(rectangle.getY() + movementBack);
        }

        moveHitBox();
    }

    /**
     * @return the acceloremeter Y input adjusted to the initial position.
     */
    private float getAdjustedX() {
        return Gdx.input.getAccelerometerY() - NEUTRAL_POINT_X;
    }

    /**
     * @return the acceloremetr Z input adjusted to the initial position.
     */
    private float getAdjustedY() {
        return Gdx.input.getAccelerometerZ() - NEUTRAL_POINT_Y;
    }

    /**
     * Returns the player to the last checkpoint Xe passed through
     */
    public void returnToLastCheckpoint() {
        this.setX(lastCheckpointX);
        this.setY(lastCheckpointY);
        Gdx.app.log("TAG", "Returned to Checkpoint!");
    }

    /**
     * Checks if a rectangle overlaps a rectangleMapObject in the tiledMap
     *
     * @param layerName the name of the tiledMap object layer
     * @param rectangle the rectangle which is used to check collision detection
     * @return true, if the rectangle overlaps the rectangleMapObject, false if not
     */
    private boolean overlapsMapObject (String layerName, Rectangle rectangle) {
        Array<RectangleMapObject> mapObjects = tiledMap.getLayers().get(layerName).getObjects().getByType(RectangleMapObject.class);

        for (RectangleMapObject mapObject : mapObjects) {
            Rectangle mapObjectRectangle = mapObject.getRectangle();
            if (rectangle.overlaps(mapObjectRectangle)) return true;
        }
        return false;
    }

    /**
     * Sets the player's initial coordinates.
     *
     * Gets the coordinates from a rectangle in the tiled map which defines the starting position.
     * If world walls are found on the left side of said rectangle, he is spawned in the coordinates
     * of the rectangle.
     * If they are not, the player is spawned so that the right edge of the rectangle of the player's texture
     * is in the same x-axis position as the right edge of the rectangle in the tiledMap which signifies
     * the player's spawning point.
     * Also moves the player's hit-box according to the rectangle of the player's texture.
     *
     * @param TILE_DIMENSION the length of an edge in a tile
     */
    public void setFrogSpawn(int TILE_DIMENSION) {
        Rectangle spawnRectangle = new Rectangle();
        Array<RectangleMapObject> startPoints =
                tiledMap.getLayers().get("frog-spawn-rectangle").getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject startPoint : startPoints) {
            spawnRectangle = startPoint.getRectangle();
        }

        Rectangle testRectangle = new Rectangle(spawnRectangle.getX()-TILE_DIMENSION,
                spawnRectangle.getY(), spawnRectangle.getWidth(),spawnRectangle.getHeight());

        if (overlapsMapObject("walls-rectangle", testRectangle)) {
            this.setX(spawnRectangle.getX());
            this.setY(spawnRectangle.getY());
            moveHitBox();
        } else {
            this.setX(spawnRectangle.getX() + spawnRectangle.getWidth() - rectangle.width);
            this.setY(spawnRectangle.getY());
            flip(animation, true, false);
            flippedRight = false;
            moveHitBox();
        }
        this.setLastCheckpointX(this.getX());
        this.setLastCheckpointY(this.getY());
    }

    public void setLastCheckpointX(float x) {
        this.lastCheckpointX = x;
    }

    public void setLastCheckpointY(float y) {
        this.lastCheckpointY = y;
    }

    /**
     * Resets the neutral points of movement to the current accelerometer values.
     */
    public void resetAccelerometerPosition() {
        NEUTRAL_POINT_X = Gdx.input.getAccelerometerY();
        NEUTRAL_POINT_Y = Gdx.input.getAccelerometerZ();
    }

    @Override
    public void dispose() {
        textureSheet.dispose();
        tiledMap.dispose();
    }

    /**
     * Moves the player's hit-box.
     *
     * Moves the player's hit-box adjusted to the position of the texture's rectangle. The hit-box
     * is smaller than the texture's rectangle, it roughly cover's the center mass of the player's texture.
     * Therefore it has to be moved in different ways depending on whether the player's character is
     * facing left or right.
     */
    private void moveHitBox() {
        if(flippedRight) {
            hitBox.setX(rectangle.getX() + (7f/16f)*rectangle.getWidth());
        } else {
            hitBox.setX(rectangle.getX() + (1f/16f)*rectangle.getWidth());
        }
        hitBox.setY(rectangle.getY() + (4f/16f)*rectangle.getHeight());

    }

    public Rectangle getHitBox() {
        return hitBox;
    }
}
