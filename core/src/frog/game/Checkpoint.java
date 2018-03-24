package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 21.3.2018.
 */

public class Checkpoint extends GameObject {
    private boolean isCleared;

    public Checkpoint(float x, float y) {
        texture = new Texture("gfx/paahahmoluonnos.png");
        rectangle = new Rectangle(x, y, texture.getWidth() / 250f, texture.getHeight() / 250f);
        isCleared = false;
    }

    public void checkCollision(Player frog) {
        if (this.rectangle.overlaps(frog.rectangle)) {
            frog.setLastCheckpointX(this.rectangle.x + (texture.getWidth() / 2));
            frog.setLastCheckpointY(this.rectangle.y + (texture.getHeight() / 2));
            Gdx.app.log("TAG", "Checkpoint Saved!");
            setIsCleared(true);
        }
    }

    public boolean getIsCleared() {
        return this.isCleared;
    }

    public void setIsCleared(boolean x) {
        this.isCleared = x;
    }


}
