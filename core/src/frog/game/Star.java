package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 15.4.2018.
 */

public class Star extends UiObject {
    public Star(float width, boolean golden) {
        if (golden) {
            texture = new Texture(Gdx.files.internal(ConstantsManager.starGoldenPath));
        } else {
            texture = new Texture(Gdx.files.internal(ConstantsManager.starGreyPath));
        }
        rectangle = new Rectangle();
        rectangle.setWidth(width);
        rectangle.setHeight((width*texture.getHeight())/texture.getWidth());
    }
}
