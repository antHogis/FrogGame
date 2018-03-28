package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 24.3.2018.
 */

public class EnemyFish extends Enemy {
    private float amountMoved;
    private float movementSpeed;
    private float range;
    private boolean isMovingRight;

    public EnemyFish(float speed, float rangeAmount, boolean movingRight) {
        this.texture = new Texture("gfx/kala.png");
        this.rectangle = new Rectangle(5f, 5f,
                texture.getWidth() / 300f,
                texture.getHeight() / 300f);
        this.amountMoved = 0f;
        this.movementSpeed = speed;
        this.isMovingRight = movingRight;
        this.range = rangeAmount;
    }

    @Override
    public void movement() {
        if (isMovingRight && amountMoved < range) {
            this.setX(this.getX() + movementSpeed);
            this.amountMoved = this.amountMoved + this.movementSpeed;
            if (amountMoved >= range) {
                this.isMovingRight = false;
            }
        } else if (!isMovingRight && amountMoved >= 0) {
            this.setX(this.getX() - movementSpeed);
            this.amountMoved = this.amountMoved - this.movementSpeed;
            if (this.amountMoved <= 0) {
                this.isMovingRight = true;
            }
        }

    }
}
