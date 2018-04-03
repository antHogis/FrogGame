package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 24.3.2018.
 */

public class RoundFish extends Enemy {
    private float amountMoved;
    private float movementSpeed;
    private boolean isMovingRight;

    public RoundFish(int TILE_DIMENSION) {
        this.texture = new Texture("gfx/kala.png");
        this.rectangle = new Rectangle(5f, 5f,
                texture.getWidth() / 300f,
                texture.getHeight() / 300f);
        this.rectangle.setWidth(TILE_DIMENSION);
        this.rectangle.setHeight((texture.getHeight()*rectangle.getWidth())/texture.getWidth());
        this.amountMoved = 0f;
        this.movementSpeed = 128f;
        this.isMovingRight = true;
    }

    @Override
    public void movement() {

        if(this.rectangle.x < this.getMOVEMENT_ENDPOINT_X() && isMovingRight) {
            this.setX(this.getX()+Gdx.graphics.getDeltaTime()*movementSpeed);

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
