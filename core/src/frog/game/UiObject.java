package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


/**
 * An abstract class for user interface elements
 *
 * Created to provide all user interface elements with mandatory methods, like a com.badlogic.gdx.graphics.g2d.SpriteBatch.draw() method and relevant getters and setters.
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0412
 */

abstract class UiObject {

    Texture texture;
    Rectangle rectangle;

    /**
     * Draws the object's texture at the rectangle's position
     *
     * @param batch the SpriteBatch that should draw the object's texture at the set rectangle position
     */
    public void draw(SpriteBatch batch) {
        batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void setX(float x) {
        this.rectangle.x = x;
    }

    public void setY(float y) {
        this.rectangle.y = y;
    }

    public float getX() {
        return this.rectangle.x;
    }

    public float getY() {
        return this.rectangle.y;
    }

    public float getWidth()  {
        return this.rectangle.width;
    }

    public float getHeight() {
        return this.rectangle.height;
    }

    public void setWidth(float width) {
        this.rectangle.width = width;
    }

    public void setHeight(float height) {
        this.rectangle.height = height;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Disposes the Texture texture if it is set
     */
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
