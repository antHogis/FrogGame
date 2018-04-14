package frog.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 12.4.2018.
 */

public class HomeButton extends UiObject {

    private int edgeDistance = 10;
    private final float WINDOW_WIDTH, WINDOW_HEIGHT;

    public HomeButton(float WINDOW_WIDTH, float WINDOW_HEIGHT, int TILE_DIMENSION) {
        texture = new Texture("ui/button-home.png");
        rectangle = new Rectangle(0,0,TILE_DIMENSION, 0);

        rectangle.setHeight((texture.getHeight() * rectangle.getWidth()) / texture.getWidth());
        this.WINDOW_WIDTH = WINDOW_WIDTH;
        this.WINDOW_HEIGHT = WINDOW_HEIGHT;
    }

    public void draw(SpriteBatch batch, float cameraX, float cameraY) {
        rectangle.setX(cameraX - WINDOW_WIDTH/2 + edgeDistance);
        rectangle.setY(cameraY + WINDOW_HEIGHT/2 - edgeDistance - rectangle.height);

        batch.draw(texture, rectangle.x, rectangle.y, rectangle.width, rectangle.height);

    }
}
