package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by anton on 04/04/2018.
 */

public class Seaweed extends GameObject {
    private final float MOVEMENT_SLOWING = 0.75f;

    public Seaweed(float x, float y, int TILE_DIMENSION, boolean up) {
        if (up) {
            this.texture = new Texture("gfx/kasviPH.png");
        } else {
            this.texture = new Texture("gfx/kasviPHdown.png");
        }
        this.rectangle = new Rectangle(x,y,TILE_DIMENSION*2,TILE_DIMENSION*2);

    }

    public void checkCollision(Player frog) {
        if (this.rectangle.overlaps(frog.rectangle)) {
            frog.setMovementModifier(MOVEMENT_SLOWING);
        } else {
            frog.setMovementModifier(1);
        }
    }
}
