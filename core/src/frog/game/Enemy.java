package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 24.3.2018.
 */

public abstract class Enemy extends GameObject {


    public void checkCollision(Player frog) {
        if (this.rectangle.overlaps(frog.rectangle)) {
            Gdx.app.log("TAG", "Player killed!");
            frog.returnToLastCheckpoint();
        }
    }
}
