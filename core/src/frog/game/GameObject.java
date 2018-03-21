package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 10.3.2018.
 */

abstract class GameObject {
    Rectangle rectangle;
    Texture texture;

    public Texture getTexture() {
        return this.texture;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(this.texture, this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
    }

    public float getX() {
        return this.rectangle.x;
    }

    public float getY() {
        return this.rectangle.y;
    }

    public void setX(float value) { this.rectangle.x = value; }

    public void setY(float value) {
        this.rectangle.y = value;
    }

    public float getWidth() {
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
}
