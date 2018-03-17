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
        texture = new Texture("gfx/paahahmoluonnos.png");
        rectangle = new Rectangle(4f, 4f,
                texture.getWidth() / 200f,
                texture.getHeight() / 200f);
        setMoveSpeed(10f);
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
