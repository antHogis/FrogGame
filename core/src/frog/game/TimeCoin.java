package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 24.3.2018.
 */

public class TimeCoin extends GameObject {

    public TimeCoin() {
        this.texture = new Texture("gfx/TimeCoin.png");
        this.rectangle = new Rectangle(4f, 4f,
                texture.getWidth() / 300f,
                texture.getHeight() / 300f);
    }
}
