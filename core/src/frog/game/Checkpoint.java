package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 21.3.2018.
 */

public class Checkpoint extends GameObject {
    private boolean isCleared;
    private Rectangle zone;

    public Checkpoint(float x, float y, float width, float height, int TILE_DIMENSION) {
        texture = new Texture("gfx/checkpoint.png");
        zone = new Rectangle(x, y, width, height);
        rectangle = new Rectangle(zone.x, zone.y-(TILE_DIMENSION/4), TILE_DIMENSION, TILE_DIMENSION);
        isCleared = false;
    }

    public void checkCollision(Player frog) {
        if (this.zone.overlaps(frog.rectangle) && !isCleared) {
            frog.setLastCheckpointX(this.zone.x);
            frog.setLastCheckpointY(this.zone.y);
            Gdx.app.log("TAG", "Checkpoint Saved!");
            isCleared = true;
        }
    }

    public boolean getIsCleared() {
        return this.isCleared;
    }

    public void setIsCleared(boolean x) {
        this.isCleared = x;
    }


}
