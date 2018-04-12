package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 12.4.2018.
 */

public class ReturnPrompt {
    private Texture texture;
    private Rectangle rectangle;

    public ReturnPrompt(float WINDOW_WIDTH, float WINDOW_HEIGHT) {
        texture = new Texture("ui/valikkoPaluuPrompt.png");
        rectangle = new Rectangle(0,0, WINDOW_WIDTH/1.5f, 0);
        rectangle.setHeight((rectangle.width*texture.getHeight())/texture.getWidth());
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture,
                rectangle.x,
                rectangle.y,
                rectangle.width,
                rectangle.height);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setX(float cameraX) {
        this.rectangle.x = cameraX - (rectangle.width/2);
    }

    public void setY(float cameraY) {
        this.rectangle.y = cameraY - (rectangle.height/2);
    }
}
