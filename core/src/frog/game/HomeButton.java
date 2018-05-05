package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 12.4.2018.
 */

public class HomeButton extends UiObject {

    public HomeButton(float width) {
        texture = new Texture(Gdx.files.internal(ConstantsManager.homeButtonIdlePath));
        rectangle = new Rectangle(0,0,0,0);
        rectangle.setWidth(width);
        rectangle.setHeight((texture.getHeight() * rectangle.getWidth()) / texture.getWidth());

    }
}
