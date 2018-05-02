package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 24.3.2018.
 */

public class RoundFish extends Enemy {
    private float movementSpeed;
    private boolean isMovingRight;

    public RoundFish(int TILE_DIMENSION) {
        textureSheet = new Texture("gfx/roundFishSheet.png");
        SHEET_COLUMNS = 4;
        SHEET_ROWS = 2;
        textureSheet2D = TextureRegion.split(textureSheet,
                textureSheet.getWidth() / SHEET_COLUMNS,
                textureSheet.getHeight() / SHEET_ROWS);
        textureSheet1D = convert2Dto1D(textureSheet2D);

        stateTime = 1f;
        animation = new Animation<TextureRegion>(5/60f, textureSheet1D);
        currentFrame = animation.getKeyFrame(stateTime, true);

        rectangle = new Rectangle(0, 0,TILE_DIMENSION,0);
        rectangle.setHeight((currentFrame.getRegionHeight()*rectangle.getWidth())/currentFrame.getRegionWidth());
        this.movementSpeed = 128f;
        this.isMovingRight = true;
    }

    @Override
    public void movement() {

        if(this.rectangle.x+this.rectangle.width < this.getMOVEMENT_ENDPOINT_X() && isMovingRight) {
            this.setX(this.getX()+Gdx.graphics.getDeltaTime()*movementSpeed);

        } else if (isMovingRight) {
            isMovingRight = false;
            flip(animation, true, false);
        }
        if(this.rectangle.x > this.getMOVEMENT_STARTPOINT_X() && !isMovingRight) {
            this.setX(this.getX()-Gdx.graphics.getDeltaTime()*movementSpeed);
        } else if (!isMovingRight) {
            isMovingRight = true;
            flip(animation, true, false);
        }

    }
}
