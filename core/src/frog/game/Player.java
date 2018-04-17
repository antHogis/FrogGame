package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Lauri on 10.3.2018.
 */

class Player extends GameObject {

    private Rectangle hitBox;

    private TiledMap tiledMap;
    private float moveSpeed;
    private float lastCheckpointX;
    private float lastCheckpointY;

    private boolean flippedRight = true;

    private float NEUTRAL_POINT_X;
    private float NEUTRAL_POINT_Y;

    private float THRESHOLD_VALUE;
    private float THRESHOLD_MIN_X_RIGHT;
    private float THRESHOLD_MIN_X_LEFT;
    private float THRESHOLD_MIN_Y_FORWARD;
    private float THRESHOLD_MIN_Y_BACK;

    private float SPEED_X;
    private float SPEED_FORWARD;
    private float SPEED_BACKWARDS;

    private float invert_Value;

    public Player(TiledMap tiledMap, int TILE_DIMENSION) {
        this.tiledMap = tiledMap;

        textureSheet = new Texture("gfx/salesheet.png");
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

        //For desktop
        moveSpeed = 1000f;
        //Android
        initializeMovement();
    }

    private void initializeMovement() {
        NEUTRAL_POINT_X = Gdx.input.getAccelerometerY();
        NEUTRAL_POINT_Y = Gdx.input.getAccelerometerZ();
        Gdx.app.log("TAG", "neutral x:" + NEUTRAL_POINT_X);
        Gdx.app.log("TAG", "neutral y:" + NEUTRAL_POINT_Y);

        THRESHOLD_VALUE = ConstantsManager.settings.getFloat("threshold",
                ConstantsManager.DEFAULT_THRESHOLD);

        THRESHOLD_MIN_X_RIGHT = THRESHOLD_VALUE;
        THRESHOLD_MIN_X_LEFT = -1*THRESHOLD_VALUE;
        THRESHOLD_MIN_Y_FORWARD = THRESHOLD_VALUE + 0.3f;
        THRESHOLD_MIN_Y_BACK = (-1*THRESHOLD_VALUE) + 0.15f;
        Gdx.app.log("Thresh:", Float.toString(THRESHOLD_VALUE));

        SPEED_X = ConstantsManager.settings.getFloat("speed",
                ConstantsManager.DEFAULT_SPEED);

        SPEED_FORWARD = SPEED_X + 10;
        SPEED_BACKWARDS = SPEED_X + 30;
        Gdx.app.log("Speed:", Float.toString(SPEED_X));

        boolean INVERT_Y_AXIS = ConstantsManager.settings.getBoolean("y-invert",
                ConstantsManager.DEFAULT_INVERT_Y);

        if (INVERT_Y_AXIS) {
            invert_Value = -1;
        } else {
            invert_Value = 1;
        }

    }

    //Väliaikainen liikkuminen desktop-testiä varten
    public void moveTemporary(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                && !overlapsMapObject("walls-rectangle",
                new Rectangle(this.rectangle.x+(moveSpeed*delta),
                        this.rectangle.y,
                        this.rectangle.width,
                        this.rectangle.height))) {
            this.setX(this.getX() + this.moveSpeed * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
                && !overlapsMapObject("walls-rectangle",
                new Rectangle(this.rectangle.x-(moveSpeed*delta),
                        this.rectangle.y,
                        this.rectangle.width,
                        this.rectangle.height))) {
            this.setX(this.getX() - this.moveSpeed * delta);

        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)
                && !overlapsMapObject("walls-rectangle",
                new Rectangle(this.rectangle.x,
                        this.rectangle.y+(moveSpeed*delta),
                        this.rectangle.width,
                        this.rectangle.height))) {
            this.setY(this.getY() + this.moveSpeed * delta);

        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
                && !overlapsMapObject("walls-rectangle",
                new Rectangle(this.rectangle.x,
                        this.rectangle.y-(moveSpeed*delta),
                        this.rectangle.width,
                        this.rectangle.height))) {
            this.setY(this.getY() - this.moveSpeed * delta);

        }
    }

    public void movementAndroid (float delta) {
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


    private float getAdjustedX() {
        return Gdx.input.getAccelerometerY() - NEUTRAL_POINT_X;
    }

    private float getAdjustedY() {
        return Gdx.input.getAccelerometerZ() - NEUTRAL_POINT_Y;
    }

    public void returnToLastCheckpoint() {
        this.setX(getLastCheckpointX());
        this.setY(getLastCheckpointY());
        Gdx.app.log("TAG", "Returned to Checkpoint!");
    }

    private boolean overlapsMapObject (String path, Rectangle rectangle) {
        Array<RectangleMapObject> mapObjects = tiledMap.getLayers().get(path).getObjects().getByType(RectangleMapObject.class);

        for (RectangleMapObject mapObject : mapObjects) {
            Rectangle mapObjectRectangle = mapObject.getRectangle();
            if (rectangle.overlaps(mapObjectRectangle)) {
                return true;
            }
        }
        return false;
    }

    /*
     * Sets the player's initial coordinates.
     *
     * Gets the coordinates from a rectangle in the tiled map which defines the starting position.
     * If world walls are found on the left side of said rectangle, he is spawned in the coordinates
     * of the rectangle.
      *
      * @param spawnRectangle is the rectangle which defines the spawn coordinates
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
            this.setX(spawnRectangle.getX()-this.getWidth());
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

    public float getLastCheckpointX() {
        return this.lastCheckpointX;
    }

    public float getLastCheckpointY() {
        return this.lastCheckpointY;
    }

    @Override
    public void dispose() {
        textureSheet.dispose();
        tiledMap.dispose();
    }

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
