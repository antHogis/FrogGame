package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by Anton on 12.4.2018.
 */

public class PromptResponse extends Button {

    public PromptResponse(String path, float promptWidth) {
        texture = new Texture(path);
        rectangle = new Rectangle(0,0,
                (5/16)*promptWidth, 0);
        rectangle.setHeight((rectangle.getWidth() * texture.getHeight()) / texture.getWidth());
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
}
