package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by anton on 04/04/2018.
 */

public class Octopus1 extends Enemy {
    private float movementSpeed;
    private boolean isMovingDown;

    public Octopus1(int TILE_DIMENSION) {
        this.texture = new Texture("gfx/mustekala1.png");
        this.rectangle = new Rectangle(0, 0,
                texture.getWidth(),
                texture.getHeight());
        this.rectangle.setWidth(TILE_DIMENSION*3);
        this.rectangle.setHeight((texture.getHeight()*rectangle.getWidth())/texture.getWidth());
        this.movementSpeed = 128f;
        this.isMovingDown = true;
    }

    @Override
    public void movement() {

        if(this.rectangle.y > this.getMOVEMENT_ENDPOINT_Y() && isMovingDown) {
            this.setY(this.getY()- Gdx.graphics.getDeltaTime()*movementSpeed);

        } else if (isMovingDown) {
            isMovingDown = false;
        }
        if(this.rectangle.y < this.getMOVEMENT_STARTPOINT_Y() && !isMovingDown) {
            this.setY(this.getY()+Gdx.graphics.getDeltaTime()*movementSpeed);

        } else if (!isMovingDown) {
            isMovingDown = true;
        }

    }
}
