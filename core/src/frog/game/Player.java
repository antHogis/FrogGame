package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by Lauri on 10.3.2018.
 */

class Player extends GameObject {

    private TiledMapTileLayer walls;
    boolean upleft, upright, downleft, downright;
    private int TILE_DIMENSION = 32;
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

    private final float THRESHOLD_VALUE_X = 1.5f;
    private float THRESHOLD_MIN_X_RIGHT;
    private float THRESHOLD_MIN_X_LEFT;

    private final float THRESHOLD_VALUE_Y_FORWARD = 3f;
    private final float THRESHOLD_VALUE_Y_BACK = 0.5f;
    private float THRESHOLD_MIN_Y_FORWARD;
    private float THRESHOLD_MIN_Y_BACK;

    private final float SPEED_X = 25f;
    private final float SPEED_FORWARD = SPEED_X * 0.25f;
    private final float SPEED_BACK = SPEED_X * 5f;

    public Player(TiledMapTileLayer walls) {
        texture = new Texture("gfx/sammakko.png");
        rectangle = new Rectangle(4f, 4f,
                texture.getWidth() / 1f,
                texture.getHeight() / 1f);
        moveSpeed = 256f;

        NEUTRAL_POINT_X = Gdx.input.getAccelerometerY();
        NEUTRAL_POINT_Y = Gdx.input.getAccelerometerZ();
        Gdx.app.log("TAG", "x:" + NEUTRAL_POINT_X);
        Gdx.app.log("TAG", "y:" + NEUTRAL_POINT_Y);

        THRESHOLD_MIN_X_RIGHT = NEUTRAL_POINT_X + THRESHOLD_VALUE_X;
        THRESHOLD_MIN_X_LEFT = NEUTRAL_POINT_X - THRESHOLD_VALUE_X;
        Gdx.app.log("TAG", "Thresh right:" + THRESHOLD_MIN_X_RIGHT );
        Gdx.app.log("TAG", "Thresh left:" + THRESHOLD_MIN_X_LEFT );

        THRESHOLD_MIN_Y_FORWARD = NEUTRAL_POINT_Y + THRESHOLD_VALUE_Y_FORWARD;
        THRESHOLD_MIN_Y_BACK = NEUTRAL_POINT_Y;
        Gdx.app.log("TAG", "Thresh forw:" + THRESHOLD_MIN_Y_FORWARD );
        Gdx.app.log("TAG", "Thresh back:" + THRESHOLD_MIN_Y_BACK );

        this.walls = walls;

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
    public void moveTemporary() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.setX(this.getX() + this.moveSpeed * Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.setX(this.getX() - this.moveSpeed * Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.setY(this.getY() + this.moveSpeed * Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.setY(this.getY() - this.moveSpeed * Gdx.graphics.getDeltaTime());
        }
    }


    public void movementAndroid (float delta) {
        float movementX = delta * SPEED_X * Gdx.input.getAccelerometerY();
        float movementForward = delta * SPEED_FORWARD * Gdx.input.getAccelerometerZ();
        float movementBack = delta * SPEED_BACK * Gdx.input.getAccelerometerZ();

        //RIGHT
        getMyCorners(rectangle.x + movementX, rectangle.y);
        if (Gdx.input.getAccelerometerY() > THRESHOLD_MIN_X_RIGHT && upright && downright) {
            rectangle.setX(rectangle.getX() + movementX);
        }

        //LEFT
        getMyCorners(rectangle.x + movementX, rectangle.y);
        if (Gdx.input.getAccelerometerY() < THRESHOLD_MIN_X_LEFT && upleft && downleft) {
            rectangle.setX(rectangle.getX() + movementX);
        }

        //UP
        getMyCorners(rectangle.x, rectangle.y + movementForward + rectangle.height
        );
        if (Gdx.input.getAccelerometerZ() > THRESHOLD_MIN_Y_FORWARD && upleft && upright) {
            rectangle.setY(rectangle.getY() + movementForward);
        }

        //DOWN
        getMyCorners(rectangle.x, rectangle.y + movementBack);
        if (Gdx.input.getAccelerometerZ() < THRESHOLD_MIN_Y_BACK && downleft && downright) {
            rectangle.setY(rectangle.getY() + movementBack);
        }

    }

    public void getMyCorners(float pX, float pY){

        float downYPos  = pY;
        float upYPos    = rectangle.height + downYPos;

        float leftXPos  = pX;
        float rightXPos = rectangle.width  + leftXPos;

        upleft    = isFree(leftXPos,  upYPos);
        downleft  = isFree(leftXPos,  downYPos);
        upright   = isFree(rightXPos, upYPos);
        downright = isFree(rightXPos, downYPos);
    }

    private boolean isFree(float x, float y) {

        int indexX = (int) x / TILE_DIMENSION;
        int indexY = (int) y / TILE_DIMENSION;

        if(walls.getCell(indexX, indexY) != null) {
            return false;
        } else {
            return true;
        }
    }

    public void returnToLastCheckpoint() {
        this.setX(getLastCheckpointX());
        this.setY(getLastCheckpointY());
        Gdx.app.log("TAG", "Returned to Checkpoint!");
    }
}
