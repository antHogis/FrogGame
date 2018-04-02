package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 24.3.2018.
 */

public abstract class Enemy extends GameObject {

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

    public void setMOVEMENT_ENDPOINT_X(float MOVEMENT_ENDPOINT_X) {
        this.MOVEMENT_ENDPOINT_X = MOVEMENT_ENDPOINT_X;
    }

    public void setMOVEMENT_ENDPOINT_Y(float MOVEMENT_ENDPOINT_Y) {
        this.MOVEMENT_ENDPOINT_Y = MOVEMENT_ENDPOINT_Y;
    }
}
