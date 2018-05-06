package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * A button that functions as a switch.
 *
 * <p>A button which has two states, on and off. Example: used to toggle music on and off.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.1504
 */

public class SwitchButton extends UiObject {
    private Texture texture_on_idle;
    private Texture texture_on_pressed;
    private Texture texture_off_idle;
    private Texture texture_off_pressed;
    private boolean on;
    private boolean pressed;

    /**
     * The constructor for SwitchButton.
     *
     * <p>Initializes the switch button's textures and dimensions.</p>
     *
     * @param path_on_idle the texture of the button when it's state is on but is not pressed
     * @param path_on_pressed the texture of the button when it's state is on and it is pressed
     * @param path_off_idle the texture of the button when it's state is off and is not pressed
     * @param path_off_pressed the texture of the button when it's state is off but is pressed
     * @param height the height of the button
     * @param on if true, the switch button will be on, if false the switch button will be off
     */
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

    /**
     * Draws the switch button.
     *
     * Draws a different texture of the button depending on whether it's state is on or not, and whether it's pressed or not.
     *
     * @param batch the SpriteBatch that should draw the object's texture at the set rectangle position
     */
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

    /**
     * Returns the state of the button's function.
     *
     * @return the state of the button's function.
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Sets the state of the buttons function.
     *
     * @param on the state of the button's function, usually !isOn().
     */
    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isPressed() {
        return pressed;
    }

    /**
     * Sets the pressed state of the button.
     *
     * @param pressed whether the button is pressed or not.
     */
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
