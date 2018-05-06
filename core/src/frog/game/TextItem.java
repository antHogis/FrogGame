package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * A non-interactive text item
 *
 * <p>Typically used in menus, when text is represented with Textures rather than BitmapFonts.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0414
 * Created by Anton on 14.4.2018.
 */

public class TextItem extends UiObject {
    /**
     * The constructor of TextItem
     *
     * Initializes the text item's texture and dimensions.
     *
     * @param path the path of the Texture
     * @param height the height of the TextItem
     */
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
