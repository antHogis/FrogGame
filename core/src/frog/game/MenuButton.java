package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 14.4.2018.
 */

public class MenuButton extends UiObject {

    public MenuButton(float width, String path) {
        this.texture = new Texture(Gdx.files.internal(path));
        this.rectangle = new Rectangle();
        rectangle.width = width;
        rectangle.height = (rectangle.width*texture.getHeight())/texture.getWidth();
    }
}
