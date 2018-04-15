package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by anton on 04/04/2018.
 */

public class Rock extends GameObject {

    public Rock(float x, float y, int TILE_DIMENSION, boolean up) {
        //If the rock is pointing up
        if(up) {
            this.texture = new Texture("gfx/kiviPH.png");
            this.rectangle = new Rectangle(x,y-(TILE_DIMENSION/8),
                    TILE_DIMENSION*2, TILE_DIMENSION*2);
        }
        //If the rock is pointing down
        else {
            this.texture = new Texture("gfx/kiviPHdown.png");
            this.rectangle = new Rectangle(x,y+(TILE_DIMENSION/2),
                    TILE_DIMENSION*2, TILE_DIMENSION*2);
        }
    }

}
