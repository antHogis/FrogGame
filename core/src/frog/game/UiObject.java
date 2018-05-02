package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by anton on 12/04/2018.
 */

abstract class UiObject {

    Texture texture;
    Rectangle rectangle;

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

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
