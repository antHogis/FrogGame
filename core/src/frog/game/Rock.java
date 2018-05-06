package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Rock is a decorative object.
 *
 * Rock is used in the Level class as a decorative object.
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0404
 */

public class Rock extends GameObject {

    /**
     * The Constructor of Rock.
     *
     * Creates a Rock, sets it's dimensions and coordinates. Texture is set based on the direction
     * of the Rock. Places the Rock slightly into the ground or ceiling based on direction.
     *
     * @param x X-coordinate of the Rock.
     * @param y Y-coordinate of the Rock.
     * @param TILE_DIMENSION Dimension of a tile in pixels.
     * @param up Whether or not the Rock is facing up.
     */
    public Rock(float x, float y, int TILE_DIMENSION, boolean up) {
        //If the rock is pointing up
        if(up) {
            this.texture = new Texture("gfx/rockUp.png");
            this.rectangle = new Rectangle(x,y-(TILE_DIMENSION/4),
                    TILE_DIMENSION*2, TILE_DIMENSION*2);
        }
        //If the rock is pointing down
        else {
            this.texture = new Texture("gfx/rockDown.png");
            this.rectangle = new Rectangle(x,y+(TILE_DIMENSION/4),
                    TILE_DIMENSION*2, TILE_DIMENSION*2);
        }
    }

}
