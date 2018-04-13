package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


/**
 * Created by Lauri on 10.3.2018.
 */

class Player extends GameObject {

    private TiledMap tiledMap;
    private float moveSpeed;
    private float lastCheckpointX;
    private float lastCheckpointY;

    private boolean flippedRight = true;

    private float NEUTRAL_POINT_X;
    private float NEUTRAL_POINT_Y;
    private final float THRESHOLD_VALUE = 0.35f;
    private final float THRESHOLD_MIN_X_RIGHT = THRESHOLD_VALUE;
    private final float THRESHOLD_MIN_X_LEFT = -1*THRESHOLD_VALUE;
    private final float THRESHOLD_MIN_Y_FORWARD = THRESHOLD_VALUE + 0.3f;
    private final float THRESHOLD_MIN_Y_BACK = (-1*THRESHOLD_VALUE) + 0.15f;

    private final float SPEED_X = 220f;
    private final float SPEED_FORWARD = SPEED_X - 10;
    private final float SPEED_BACKWARDS = SPEED_X + 10;
    //Olemassa hidastuksia varten
    private float movementModifier = 1f;

    public Player(TiledMap tiledMap, int TILE_DIMENSION) {
        textureSheet = new Texture("gfx/sammakko.png");
        SHEET_COLUMNS = 1;
        SHEET_ROWS = 1;
        textureSheet2D = TextureRegion.split(textureSheet,
                textureSheet.getWidth() / SHEET_COLUMNS,
                textureSheet.getHeight() / SHEET_ROWS);
        textureSheet1D = convert2Dto1D(textureSheet2D);

        stateTime = 1f;
        animation = new Animation<TextureRegion>(4/60f, textureSheet1D);
        currentFrame = animation.getKeyFrame(stateTime, true);

        rectangle = new Rectangle(0, 0, TILE_DIMENSION*2f, 0);
        rectangle.setHeight((this.rectangle.getWidth()*currentFrame.getRegionHeight())/currentFrame.getRegionWidth());

        rectangle = new Rectangle(0, 0,
                0, 0);
        this.rectangle.setWidth(TILE_DIMENSION*3);
        this.rectangle.setHeight((currentFrame.getRegionHeight()*rectangle.getWidth())/currentFrame.getRegionWidth());
        moveSpeed = 512f;

        NEUTRAL_POINT_X = Gdx.input.getAccelerometerY();
        NEUTRAL_POINT_Y = Gdx.input.getAccelerometerZ();
        Gdx.app.log("TAG", "x:" + NEUTRAL_POINT_X);
        Gdx.app.log("TAG", "y:" + NEUTRAL_POINT_Y);

        this.tiledMap = tiledMap;
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
        float movementRight = delta * SPEED_X * getAdjustedX() * movementModifier;
        float movementLeft = movementRight * movementModifier;
        float movementForward = delta * SPEED_FORWARD * getAdjustedY() * movementModifier;
        float movementBack = delta * SPEED_BACKWARDS * getAdjustedY() * movementModifier;

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

    public void setMovementModifier(float movementModifier) {
        this.movementModifier = movementModifier;
    }

    /*
     * Flips the direction of the frog if he's supposed to face left. Sets position in
     *
     * Checks if the closest world edges are on the left side of the rectangle defining the
     * player's starting point.
     *
     * @returns true if the closest world edge is on the left side of the player
     */
    public void setFrogSpawn(Rectangle spawnRectangle, int TILE_DIMENSION) {
        Rectangle testRectangle = spawnRectangle;
        testRectangle.setX(spawnRectangle.getX()-TILE_DIMENSION);

        if (overlapsMapObject("walls-rectangle", testRectangle)) {
            this.setX(spawnRectangle.getX());
            this.setY(spawnRectangle.getY());
        } else {
            this.setX(spawnRectangle.getX()-this.getWidth());
            this.setY(spawnRectangle.getY());
            flip(animation, true, false);
            flippedRight = false;
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
}
