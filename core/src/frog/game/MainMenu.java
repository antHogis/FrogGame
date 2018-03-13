package frog.game;

import com.badlogic.gdx.Screen;

/**
 * Created by Anton on 13.3.2018.
 */

public class MainMenu implements Screen {
    private FrogMain host;

    public MainMenu(FrogMain host) {
        this.host = host;
        host.setScreen(new Level01(this.host));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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

    }
}
