package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 14.4.2018.
 */

public class GenericButton extends UiObject {

    private Texture idleTexture;
    private Texture pressedTexture;
    boolean pressed = false;

    public GenericButton(float width, String path) {
        this.texture = new Texture(Gdx.files.internal(path));
        this.rectangle = new Rectangle(0,0,0,0);
        rectangle.width = width;
        rectangle.height = (rectangle.width*texture.getHeight())/texture.getWidth();

    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
