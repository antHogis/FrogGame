package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Seaweed is a decorative object.
 *
 * Seaweed is used in the Level class as a decorative object.
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0404
 */

public class Seaweed extends GameObject {

    /**
     * The Constructor of Seaweed.
     *
     * Creates a Seaweed, sets it's dimensions and coordinates. Texture is set based on the direction
     * of the Seaweed. Places the Seaweed slightly into the ground or ceiling based on direction.
     *
     * @param x X-coordinate of the Seaweed.
     * @param y Y-coordinate of the Seaweed.
     * @param TILE_DIMENSION Dimension of a tile in pixels.
     * @param up Whether or not the Seaweed is facing up.
     */
    public Seaweed(float x, float y, int TILE_DIMENSION, boolean up) {
        if (up) this.texture = new Texture("gfx/seaweedUp.png");
        else this.texture = new Texture("gfx/seaweedDown.png");

        this.rectangle = new Rectangle(x,y,TILE_DIMENSION*2,TILE_DIMENSION*2);

        if (up) rectangle.setY(y - TILE_DIMENSION/4);
        else rectangle.setY(y + TILE_DIMENSION/3);

    }

}
