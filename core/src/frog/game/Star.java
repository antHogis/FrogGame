package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Star is an inanimate, non-responsive golden or grey star texture.
 *
 * <p>Stars are the way the player is scored for completing a level, besides the time taken to complete a level. The score of a
 * completed level will be 1-3 golden stars, and if there are less than three golden stars, the rest will be grey.
 * This class creates the visual representation of the star.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0415
 */
public class Star extends UiObject {
    /**
     * The constructor of Star
     *
     * Creates a star that is either golden or grey.
     *
     * @param width the desired width of the star
     * @param golden if true the star is golden, if false the star is grey
     */
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
