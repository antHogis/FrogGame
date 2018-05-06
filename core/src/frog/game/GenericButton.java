package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * GenericButton is a button for general purposes.
 *
 * <p>A button which is only meant to have one function, and display different textures when it's not pressed and when it's pressed</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0414
 */
public class GenericButton extends UiObject {

    private Texture idleTexture;
    private Texture pressedTexture;
    boolean pressed = false;

    /**
     *
     * @param width the desired width of the button
     * @param pathIdle the path of the button's texture when it is not pressed
     * @param pathPressed the path of the button's texture when it is pressed
     */
    public GenericButton(float width, String pathIdle, String pathPressed) {
        idleTexture = new Texture(Gdx.files.internal(pathIdle));
        pressedTexture = new Texture(Gdx.files.internal(pathPressed));
        this.rectangle = new Rectangle(0,0,0,0);
        rectangle.width = width;
        rectangle.height = (rectangle.width*idleTexture.getHeight())/idleTexture.getWidth();

    }

    /**
     * Draws the button.
     *
     * Draws the button's pressed texture if it is pressed, and idle texture if it is not.
     *
     * @param batch the SpriteBatch that should draw the object's texture at the set rectangle position
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (pressed) {
            batch.draw(pressedTexture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } else {
            batch.draw(idleTexture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    /**
     * @return true if the button is pressed, false if it is not
     */
    public boolean isPressed() {
        return pressed;
    }

    /**
     * Sets the button's pressed state
     *
     * @param pressed true if the button is pressed, false it is not
     */
    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (idleTexture != null) {
            idleTexture.dispose();
        }
        if (pressedTexture != null) {
            pressedTexture.dispose();
        }
    }
}
