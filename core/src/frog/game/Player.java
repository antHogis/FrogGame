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

    private TextureRegion [][] imageSheet2D;
    private TextureRegion [] imageSheet1D;
    private final int IMAGE_COLS = 1;
    private final int IMAGE_ROWS = 1;
    private boolean facedRight;
    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;
    private float stateTime;

    private float NEUTRAL_POINT_X;
    private float NEUTRAL_POINT_Y;

    private final float THRESHOLD_VALUE = 0.5f;

    private float THRESHOLD_MIN_X_RIGHT = THRESHOLD_VALUE;
    private float THRESHOLD_MIN_X_LEFT = -1*THRESHOLD_VALUE;
    private float THRESHOLD_MIN_Y_FORWARD = THRESHOLD_VALUE;
    private float THRESHOLD_MIN_Y_BACK = (-1*THRESHOLD_VALUE) + 0.15f;

    private final float SPEED_X = 50f;
    private final float SPEED_UP = SPEED_X - 10;
    private final float SPEED_DOWN = SPEED_X + 10;

    public Player(TiledMap tiledMap) {
        texture = new Texture("gfx/sammakko.png");
        rectangle = new Rectangle(4f, 4f,
                texture.getWidth() / 1f,
                texture.getHeight() / 1f);
        moveSpeed = 256f;

        NEUTRAL_POINT_X = Gdx.input.getAccelerometerY();
        NEUTRAL_POINT_Y = Gdx.input.getAccelerometerZ();
        Gdx.app.log("TAG", "x:" + NEUTRAL_POINT_X);
        Gdx.app.log("TAG", "y:" + NEUTRAL_POINT_Y);

        //THRESHOLD_MIN_X_RIGHT = NEUTRAL_POINT_X + THRESHOLD_VALUE;
        //THRESHOLD_MIN_X_LEFT = NEUTRAL_POINT_X - THRESHOLD_VALUE;
        Gdx.app.log("TAG", "Thresh right:" + THRESHOLD_MIN_X_RIGHT );
        Gdx.app.log("TAG", "Thresh left:" + THRESHOLD_MIN_X_LEFT );

        //THRESHOLD_MIN_Y_FORWARD = NEUTRAL_POINT_Y + THRESHOLD_VALUE;
        //THRESHOLD_MIN_Y_BACK = NEUTRAL_POINT_Y - THRESHOLD_VALUE;
        Gdx.app.log("TAG", "Thresh forw:" + THRESHOLD_MIN_Y_FORWARD );
        Gdx.app.log("TAG", "Thresh back:" + THRESHOLD_MIN_Y_BACK );

        this.tiledMap = tiledMap;

        //imageSheet2D = TextureRegion.split();
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

    //Väliaikainen liikkuminen testiä varten
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
        float movementForward = delta * SPEED_UP * getAdjustedY();
        float movementBack = delta * SPEED_DOWN * getAdjustedY();

        //RIGHT
        if (getAdjustedX()> THRESHOLD_MIN_X_RIGHT
                && !overlapsMapObject("walls-rectangle",
                    new Rectangle(this.rectangle.x+movementRight, this.rectangle.y,
                        this.rectangle.width,
                        this.rectangle.height))) {
            rectangle.setX(rectangle.getX() + movementRight);
        }

        //LEFT
        if (getAdjustedX() < THRESHOLD_MIN_X_LEFT
                && !overlapsMapObject("walls-rectangle",
                    new Rectangle(this.rectangle.x+movementLeft, this.rectangle.y,
                        this.rectangle.width,
                        this.rectangle.height))) {
            rectangle.setX(rectangle.getX() + movementLeft);
        }

        //UP
        if (getAdjustedY() > THRESHOLD_MIN_Y_FORWARD
                && !overlapsMapObject("walls-rectangle",
                    new Rectangle(this.rectangle.x, this.rectangle.y + movementForward,
                        this.rectangle.width,
                        this.rectangle.height))) {
            rectangle.setY(rectangle.getY() + movementForward);
        }

        //DOWN
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
}
