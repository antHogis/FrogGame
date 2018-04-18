package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 12.4.2018.
 */

public class HomeButton extends UiObject {

    public HomeButton(float width) {
        texture = new Texture(Gdx.files.internal(ConstantsManager.homeButtonPath));
        rectangle = new Rectangle();
        rectangle.setWidth(width);
        rectangle.setHeight((texture.getHeight() * rectangle.getWidth()) / texture.getWidth());
        rectangle.setX(0);
        rectangle.setY(0);

    }
}
