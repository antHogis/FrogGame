package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 14.4.2018.
 */

public class TextItem extends UiObject {

    public TextItem(String path, float height) {
        texture = new Texture(Gdx.files.internal(path));
        rectangle = new Rectangle(0,0,0,0);
        rectangle.setHeight(height);
        rectangle.setWidth((texture.getWidth() * height) / texture.getHeight());
    }

    public void setTexture(String path) {
        texture = new Texture(Gdx.files.internal(path));
    }
}
