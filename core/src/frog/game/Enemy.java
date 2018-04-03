package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 24.3.2018.
 */

public abstract class Enemy extends GameObject {

    private float MOVEMENT_STARTPOINT_X;
    private float MOVEMENT_STARTPOINT_Y;
    private float MOVEMENT_ENDPOINT_X;
    private float MOVEMENT_ENDPOINT_Y;

    public void checkCollision(Player frog) {
        if (this.rectangle.overlaps(frog.rectangle)) {
            Gdx.app.log("TAG", "Player killed!");
            frog.returnToLastCheckpoint();
        }
    }

    //Dummy method to be overridden
    public void movement() {

    }

    public float getMOVEMENT_ENDPOINT_X() {
        return MOVEMENT_ENDPOINT_X;
    }

    public void setMOVEMENT_ENDPOINT_X(float MOVEMENT_ENDPOINT_X) {
        this.MOVEMENT_ENDPOINT_X = MOVEMENT_ENDPOINT_X;
    }

    public void setMOVEMENT_ENDPOINT_Y(float MOVEMENT_ENDPOINT_Y) {
        this.MOVEMENT_ENDPOINT_Y = MOVEMENT_ENDPOINT_Y;
    }

    public float getMOVEMENT_ENDPOINT_Y() {
        return MOVEMENT_ENDPOINT_Y;

    }

    public float getMOVEMENT_STARTPOINT_X() {
        return MOVEMENT_STARTPOINT_X;
    }

    public void setMOVEMENT_STARTPOINT_X(float MOVEMENT_STARTPOINT_X) {
        this.MOVEMENT_STARTPOINT_X = MOVEMENT_STARTPOINT_X;
    }

    public float getMOVEMENT_STARTPOINT_Y() {
        return MOVEMENT_STARTPOINT_Y;
    }

    public void setMOVEMENT_STARTPOINT_Y(float MOVEMENT_STARTPOINT_Y) {
        this.MOVEMENT_STARTPOINT_Y = MOVEMENT_STARTPOINT_Y;
    }

}
