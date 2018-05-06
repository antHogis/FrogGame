package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 6.5.2018.
 */
public class Arrow extends GameObject {
    boolean moveUp;
    float lowestY, highestY;

    public Arrow(float width, String direction) {
        try {
            texture = new Texture(Gdx.files.internal("gfx/arrow-" + direction +".png"));
            rectangle = new Rectangle(0,0,0,0);
            rectangle.setWidth(width);
            rectangle.setHeight((texture.getHeight()*width) / texture.getWidth());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid direction: " + direction);
        }
        moveUp = true;
        lowestY = rectangle.y;
        highestY = lowestY + ConstantsManager.TILE_DIMENSION/2;
    }

    public void movement() {
        if (moveUp && rectangle.y < highestY) {
            rectangle.setY(rectangle.getY()+40* Gdx.graphics.getDeltaTime());
        } else if (moveUp) {
            moveUp = false;
        }
        if (!moveUp && rectangle.y > lowestY) {
            rectangle.setY(rectangle.getY()-40* Gdx.graphics.getDeltaTime());
        } else if (!moveUp) {
            moveUp = true;
        }
    }

    @Override
    public void setY(float y) {
        rectangle.y = y;
        lowestY = y;
        highestY = lowestY + ConstantsManager.TILE_DIMENSION/2;
    }
}
