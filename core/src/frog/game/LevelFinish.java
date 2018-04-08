package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Anton on 4.4.2018.
 */

public class LevelFinish implements Screen {
    private FrogMain host;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private int timeElapsed;
    private BitmapFont font;
    private int nextIndex;


    public LevelFinish(FrogMain host, int timeElapsed, int nextIndex) {
        this.host = host;
        batch = host.getBatch();
        camera = host.getCamera();
        this.timeElapsed = timeElapsed;

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(4f);

        this.nextIndex = nextIndex;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        changeScreen();

        batch.begin();
        font.draw(batch, "Kenttä läpäisty ajassa: " + Integer.toString(timeElapsed) + " sekuntia",0,host.getVIEWPORT_HEIGHT()/2);
        batch.end();
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

    private void changeScreen() {
        if (Gdx.input.isTouched()) {
            if(nextIndex >= host.getLevels().size) {
                host.setScreen(new MainMenu(host));
            } else {
                host.setScreen(host.getLevels().get(nextIndex));
            }

        }
    }
}
