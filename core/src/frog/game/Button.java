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

public abstract class Button {

    Texture texture;
    Rectangle rectangle;
    boolean touched;

    public void draw(SpriteBatch batch) {
        batch.draw(this.texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void processInput(OrthographicCamera camera) {
        Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touch);
        if (rectangle.contains(touch.x, touch.y)) {
            touched = true;
        }
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void setX(float x) {
        this.rectangle.x = x;
    }

    public void setY(float y) {
        this.rectangle.y = y;
    }
}
