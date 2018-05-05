package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 15.4.2018.
 */

public class SwitchButton extends UiObject {
    private Texture texture_on_idle;
    private Texture texture_on_pressed;
    private Texture texture_off_idle;
    private Texture texture_off_pressed;
    private boolean on;
    private boolean pressed;

    public SwitchButton(String path_on_idle,
                        String path_on_pressed,
                        String path_off_idle,
                        String path_off_pressed,
                        float height,
                        boolean on) {
        texture_on_idle = new Texture(path_on_idle);
        texture_on_pressed = new Texture(path_on_pressed);
        texture_off_idle = new Texture(path_off_idle);
        texture_off_pressed = new Texture(path_off_pressed);
        this.on = on;

        rectangle = new Rectangle();
        rectangle.setHeight(height);
        rectangle.setWidth((rectangle.getHeight()* texture_on_idle.getWidth()) / texture_on_idle.getHeight());
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(on) {
            if (pressed) {
                batch.draw(texture_on_pressed, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            } else {
                batch.draw(texture_on_idle, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }
        } else {
            if (pressed) {
                batch.draw(texture_off_pressed, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            } else {
                batch.draw(texture_off_idle, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            }
        }
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public void dispose() {
        texture_on_idle.dispose();
        texture_on_pressed.dispose();
        texture_off_idle.dispose();
        texture_off_pressed.dispose();
    }
}
