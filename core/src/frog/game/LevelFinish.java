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
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private final float WINDOW_WIDTH, WINDOW_HEIGHT;
    private BitmapFont font;
    private int nextIndex;
    private String timeString;


    public LevelFinish(FrogMain host, String timeString, int nextIndex) {
        this.host = host;
        batch = host.getBatch();
        camera = host.getCamera();
        WINDOW_WIDTH = camera.viewportWidth;
        WINDOW_HEIGHT = camera.viewportHeight;

        font = new BitmapFont(Gdx.files.internal("ui/fonts/lato90.txt"));

        this.timeString = timeString;
        this.nextIndex = nextIndex;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.658f, 0.980f, 0.980f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        changeScreen();

        batch.begin();
        font.draw(batch, "Kenttä läpäisty ajassa " + timeString,50,WINDOW_HEIGHT*0.75f);
        font.draw(batch, "Paina ruutua jatkaaksesi",150,WINDOW_HEIGHT*0.5f);
        font.draw(batch, "seuraavaan kenttään",220,WINDOW_HEIGHT*0.35f);
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
                //host.createNewLevels();
                host.setScreen(new MainMenu(host));
            } else {
                host.resetLevelTimers();
                host.getLevels().get(nextIndex).startMusic();
                host.setScreen(host.getLevels().get(nextIndex));
            }

        }
    }
}
