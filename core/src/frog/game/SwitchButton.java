package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 15.4.2018.
 */

public class SwitchButton extends UiObject {
    private Texture texture_on;
    private Texture texture_off;
    private boolean on;

    public SwitchButton(String path_on, String path_off, float height, boolean on) {
        texture_on = new Texture(path_on);
        texture_off = new Texture(path_off);
        this.on = on;

        rectangle = new Rectangle();
        rectangle.setHeight(height);
        rectangle.setWidth((rectangle.getHeight()*texture_on.getWidth()) / texture_on.getHeight());
    }

    @Override
    public void draw(SpriteBatch batch) {
        if(on) {
            batch.draw(texture_on, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } else {
            batch.draw(texture_off, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    public void dispose() {
        texture_on.dispose();
        texture_off.dispose();
    }
}
