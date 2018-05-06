package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Arrow is an object with the texture of an arrow
 *
 * <p>Arrow is meant to be used in the class Level, in order to provide directions for the user</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0506
 */
public class Arrow extends GameObject {
    boolean moveUp;
    float lowestY, highestY;

    /**
     *The Constructor of Arrow
     *
     * Creates an Arrow, looks for a file in the assets folder for the Texture modified by the parameter direction
     *
     * @param width the width of the arrow
     * @param direction the direction of the arrow (possible directions: up,down,left and right)
     */
    public Arrow(float width, String direction) {
        try {
            texture = new Texture(Gdx.files.internal("gfx/arrow-" + direction +".png"));
            rectangle = new Rectangle(0,0,0,0);
            rectangle.setWidth(width);
            rectangle.setHeight((texture.getHeight()*width) / texture.getWidth());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        moveUp = true;
        lowestY = rectangle.y;
        highestY = lowestY + ConstantsManager.TILE_DIMENSION/2;
    }

    /**
     * Moves the arrow
     *
     * Creates a floating effect for the arrow by moving it up and down
     */
    public void movement() {
        if (moveUp && rectangle.y < highestY) {
            rectangle.setY(rectangle.getY()+40* Gdx.graphics.getDeltaTime());
        } else if (moveUp) {
            moveUp = false;
        }
        if (!moveUp && rectangle.y > lowestY) {
            rectangle.setY(rectangle.getY()-40* Gdx.graphics.getDeltaTime());
        } else if (!moveUp) {
            moveUp = true;
        }
    }

    /**
     * Positions the arrow
     *
     * Places the arrows y coordinate, and also alters its movement range based on this coordinate
     *
     * @param y the y coordinate of the arrow
     */
    @Override
    public void setY(float y) {
        rectangle.y = y;
        lowestY = y;
        highestY = lowestY + ConstantsManager.TILE_DIMENSION/2;
    }
}
