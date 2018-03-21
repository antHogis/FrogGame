package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by Lauri on 10.3.2018.
 */

class Player extends GameObject {
    float moveSpeed;

    private float NEUTRAL_POINT_X;
    private float NEUTRAL_POINT_Y;

    private final float THRESHOLD_VALUE_X = 3f;
    private float THRESHOLD_MAX_X_RIGHT;
    private float THRESHOLD_MAX_X_LEFT;
    private float THRESHOLD_MIN_X_RIGHT;
    private float THRESHOLD_MIN_X_LEFT;

    private final float THRESHOLD_VALUE_Y = 2f;
    private float THRESHOLD_MAX_Y_FORWARD;
    private float THRESHOLD_MAX_Y_BACK;
    private float THRESHOLD_MIN_Y_FORWARD;
    private float THRESHOLD_MIN_Y_BACK;

    private final float ACCELEROMETER_MULTIPLIER = 50f;

    public Player() {
        texture = new Texture("gfx/paahahmoluonnos.png");
        rectangle = new Rectangle(4f, 4f,
                texture.getWidth() / 300f,
                texture.getHeight() / 300f);

        NEUTRAL_POINT_X = Gdx.input.getAccelerometerY();
        NEUTRAL_POINT_Y = Gdx.input.getAccelerometerZ();

        THRESHOLD_MIN_X_RIGHT = NEUTRAL_POINT_X + THRESHOLD_VALUE_X;
        THRESHOLD_MIN_X_LEFT = NEUTRAL_POINT_X - THRESHOLD_VALUE_X;
        THRESHOLD_MAX_X_RIGHT = NEUTRAL_POINT_X + 10f;
        THRESHOLD_MAX_X_LEFT =  NEUTRAL_POINT_X - 10f;

        THRESHOLD_MIN_Y_FORWARD = NEUTRAL_POINT_Y + THRESHOLD_VALUE_Y;
        THRESHOLD_MIN_Y_BACK = NEUTRAL_POINT_Y - THRESHOLD_VALUE_Y;
        THRESHOLD_MAX_Y_FORWARD = NEUTRAL_POINT_Y + 10f;
        THRESHOLD_MAX_Y_BACK = NEUTRAL_POINT_Y - 10f;

    }

    /*
    public void setMoveSpeed(float speed) {
        this.moveSpeed = speed;
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
    }*/

    //Huom Lauri! Taitaa vielä olla aika paska! Pitää miettiä vähän vielä
    public void movementAndroid (float delta) {
        if ((Gdx.input.getAccelerometerY() > THRESHOLD_MIN_X_RIGHT
                && Gdx.input.getAccelerometerY() < THRESHOLD_MAX_X_RIGHT) ||
                (Gdx.input.getAccelerometerY() < THRESHOLD_MIN_X_LEFT
                && Gdx.input.getAccelerometerY() > THRESHOLD_MAX_X_LEFT)) {
            rectangle.setX(rectangle.getX() + delta * ACCELEROMETER_MULTIPLIER * Gdx.input.getAccelerometerY());
        }
        if ((Gdx.input.getAccelerometerZ() > THRESHOLD_MIN_Y_FORWARD
                && Gdx.input.getAccelerometerZ() < THRESHOLD_MAX_Y_FORWARD) ||
                (Gdx.input.getAccelerometerZ() < THRESHOLD_MIN_Y_BACK
                && Gdx.input.getAccelerometerZ() > THRESHOLD_MAX_Y_BACK))
            rectangle.setY(rectangle.getY() + delta * ACCELEROMETER_MULTIPLIER * Gdx.input.getAccelerometerZ());

    }
}
