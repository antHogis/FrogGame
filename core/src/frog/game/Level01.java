package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Anton on 13.3.2018.
 */

public class Level01 implements Screen {
    FrogMain host;
    Player frog;
    Texture background;
    public Level01(FrogMain host) {
        this.host = host;
        frog = new Player();
        background = new Texture("vesigraffat-v81.jpg");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        host.getBatch().setProjectionMatrix(host.getCamera().combined);

        frog.moveTemporary();

        host.getBatch().begin();
        host.getBatch().draw(background, 0, 0,
                background.getWidth() / 50f, background.getHeight() / 50f);
        host.getBatch().draw(frog.getTexture(), frog.getX() , frog.getY(),
                frog.getWidth() / 1f, frog.getHeight() /1f);

        host.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        host.dispose();

    }
}
