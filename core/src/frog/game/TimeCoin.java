package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 24.3.2018.
 */

public class TimeCoin extends GameObject {
    private boolean isCleared;
    private boolean subtracted;

    public TimeCoin(float x, float y, int TILE_DIMENSION) {
        this.texture = new Texture("gfx/TimeCoin.png");
        this.rectangle = new Rectangle(x, y,
                TILE_DIMENSION,
                TILE_DIMENSION);
        isCleared = false;
    }

    public void checkCollision(Player frog) {
        if (frog.rectangle.overlaps(this.rectangle)) {
            isCleared = true;
        }
    }

    public boolean getIsCleared() {
        return isCleared;
    }

    public boolean isSubtracted() {
        return subtracted;
    }

    public void setSubtracted(boolean subtracted) {
        this.subtracted = subtracted;
    }
}
