package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 14.4.2018.
 */

public class MenuButton extends UiObject {

    public MenuButton(float x, float y, float width, float height) {
        this.texture = new Texture("ui/MenuButton.png");
        this.rectangle = new Rectangle(x, y, width, height);
    }
}
