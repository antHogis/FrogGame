package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


/**
 * Created by anton on 12/04/2018.
 */

abstract class Button {

    Texture texture;
    Rectangle rectangle;

    public void draw(SpriteBatch batch) {
        batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public boolean isTouched(OrthographicCamera camera) {
        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touch);
        if (rectangle.contains(touch.x, touch.y)) {
            return true;
        } else {
            return false;
        }
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
}
