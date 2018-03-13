package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by Lauri on 10.3.2018.
 */

class Player extends GameObject {
    float moveSpeed;

    public Player() {
        texture = new Texture("badlogic.jpg");
        rectangle = new Rectangle(0f, 0f,
                texture.getWidth() / 150f,
                texture.getHeight() / 150f);
        setMoveSpeed(4f);
    }

    public void setMoveSpeed(float speed) {
        this.moveSpeed = speed;
    }

    //Väliaikainen liikkuminen testiä varten
    public void moveTemporary() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.setX(this.getX() + this.moveSpeed * Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.setX(this.getX() - this.moveSpeed * Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.setY(this.getY() + this.moveSpeed * Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            this.setY(this.getY() - this.moveSpeed * Gdx.graphics.getDeltaTime());
        }
    }
}
