package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Anton on 2.4.2018.
 */

public class LongFish extends Enemy {
    private float movementSpeed;
    private boolean isMovingRight;

    public LongFish(int TILE_DIMENSION) {
        this.texture = new Texture("gfx/ankerias.png");
        this.rectangle = new Rectangle(0, 0,
                texture.getWidth(),
                texture.getHeight());
        this.rectangle.setWidth(TILE_DIMENSION*2);
        this.rectangle.setHeight((texture.getHeight()*rectangle.getWidth())/texture.getWidth());
        this.movementSpeed = 150f;
        this.isMovingRight = true;
    }

    @Override
    public void movement() {

        if(this.rectangle.x+this.rectangle.width < this.getMOVEMENT_ENDPOINT_X() && isMovingRight) {
            this.setX(this.getX()+ Gdx.graphics.getDeltaTime()*movementSpeed);

        } else if (isMovingRight) {
            isMovingRight = false;
        }

        if(this.rectangle.x > this.getMOVEMENT_STARTPOINT_X() && !isMovingRight) {
            this.setX(this.getX()-Gdx.graphics.getDeltaTime()*movementSpeed);

        } else if (!isMovingRight) {
            isMovingRight = true;
        }

    }
}
