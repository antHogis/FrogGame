package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 14.4.2018.
 */

public class GenericButton extends UiObject {

    private Texture idleTexture;
    private Texture pressedTexture;
    boolean pressed = false;

    public GenericButton(float width, String pathIdle, String pathPressed) {
        idleTexture = new Texture(Gdx.files.internal(pathIdle));
        pressedTexture = new Texture(Gdx.files.internal(pathPressed));
        this.rectangle = new Rectangle(0,0,0,0);
        rectangle.width = width;
        rectangle.height = (rectangle.width*idleTexture.getHeight())/idleTexture.getWidth();

    }

    @Override
    public void draw(SpriteBatch batch) {
        if (pressed) {
            batch.draw(pressedTexture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } else {
            batch.draw(idleTexture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (idleTexture != null) {
            idleTexture.dispose();
        }
        if (idleTexture != null) {
            pressedTexture.dispose();
        }
    }
}
