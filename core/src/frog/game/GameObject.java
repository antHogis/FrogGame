package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Lauri on 10.3.2018.
 */

abstract class GameObject {
    Rectangle rectangle;
    Texture texture;

    Texture textureSheet;
    TextureRegion [][] textureSheet2D;
    TextureRegion [] textureSheet1D;
    int SHEET_COLUMNS;
    int SHEET_ROWS;

    Animation<TextureRegion> animation;
    float stateTime;
    TextureRegion currentFrame;



    public void draw(SpriteBatch batch) {
        batch.draw(this.texture, this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
    }

    public void drawAnimation(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public float getX() {
        return this.rectangle.x;
    }

    public float getY() {
        return this.rectangle.y;
    }

    public void setX(float value) { this.rectangle.x = value; }

    public void setY(float value) {
        this.rectangle.y = value;
    }

    public float getWidth() {
        return this.rectangle.width;
    }

    public float getHeight() {
        return this.rectangle.height;
    }

    public void setWidth(float width) {
        this.rectangle.width = width;
    }

    public void setHeight(float height) {
        this.rectangle.height = height;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public TextureRegion[] convert2Dto1D (TextureRegion[][] twoDim) {
        TextureRegion [] oneDim = new TextureRegion[twoDim.length * twoDim[0].length];
        int index = 0;

        for (int i = 0; i < twoDim.length; i++) {
            for (int j = 0; j < twoDim[i].length; j++) {
                oneDim[index++] = twoDim[i][j];
            }
        }

        return oneDim;
    }

    public void flip(Animation<TextureRegion> animation, boolean xFlip, boolean yFlip) {
        TextureRegion[] regions = animation.getKeyFrames();
        for(TextureRegion r : regions) {
            r.flip(xFlip, yFlip);
        }
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
        if (textureSheet != null) {
            textureSheet.dispose();
        }
    }

}
