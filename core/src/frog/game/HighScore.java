package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Lauri on 14.4.2018.
 */

public class HighScore extends ScreenAdapter {

    private FrogMain host;
    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private final float WINDOW_WIDTH, WINDOW_HEIGHT;

    private final int firstLevelView;
    private final int lastLevelView;
    private int currentLevelView;

    private Texture background;
    private GenericButton homeButton;
    private GenericButton arrowRight;
    private GenericButton arrowLeft;
    private Array<String> texts;
    private Array<Vector2> textPositions;

    public HighScore(FrogMain host) {
        this.host = host;
        batch = host.getBatch();
        camera = host.getCamera();
        WINDOW_WIDTH = camera.viewportWidth;
        WINDOW_HEIGHT = camera.viewportHeight;

        currentLevelView = 1;
        firstLevelView = currentLevelView;
        lastLevelView = ConstantsManager.LEVELS_AMOUNT;

        font = new BitmapFont(Gdx.files.internal("ui/fonts/lato90.txt"));
        createScoreView(currentLevelView);
        createUI();
        setInputProcessor();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            drawUI();
            drawScores();
        batch.end();
    }

    @Override
    public void dispose() {

    }

    private void drawScores() {
        for (int i = 0; i < texts.size; i++) {
            font.draw(batch, texts.get(i), textPositions.get(i).x, textPositions.get(i).y);
        }
    }

    private void drawUI() {
        batch.draw(background, 0,0, WINDOW_WIDTH, WINDOW_HEIGHT);
        homeButton.draw(batch);

        if (currentLevelView > firstLevelView) {
            arrowLeft.draw(batch);
        }
        if (currentLevelView < lastLevelView) {
            arrowRight.draw(batch);
        }
    }

    private void createUI() {
        background = new Texture(Gdx.files.internal(ConstantsManager.bgGenericPath));

        homeButton = new GenericButton(WINDOW_WIDTH*(4f/40f), ConstantsManager.homeButtonPath);
        homeButton.setY(WINDOW_HEIGHT-homeButton.getHeight());

        arrowLeft = new GenericButton(WINDOW_WIDTH*(4f/40f), ConstantsManager.menuArrowLeftPath);
        arrowLeft.setY(WINDOW_HEIGHT/2 - arrowLeft.getHeight()/2);

        arrowRight = new GenericButton(arrowLeft.getWidth(), ConstantsManager.menuArrowRightPath);
        arrowRight.setY(arrowLeft.getY());
        arrowRight.setX(WINDOW_WIDTH - arrowRight.getWidth());
    }

    private void createScoreView(int level) {
        texts = new Array<String>();
        textPositions = new Array<Vector2>();

        String title = ConstantsManager.myBundle.get("level") + level;
        GlyphLayout glyph = new GlyphLayout(font, title);
        float titlePosX = WINDOW_WIDTH/2 - glyph.width/2;
        float titlePosY = WINDOW_HEIGHT * (39f/40f);

        texts.add(title);
        textPositions.add(new Vector2(titlePosX, titlePosY));

        float scorePosX;
        float scorePosY = WINDOW_HEIGHT * (32f/40f);
        float rankPosX = WINDOW_WIDTH * (12f/40f);

        for (int i = 1; i <= ConstantsManager.TOP_SCORES_AMOUNT; i++) {
            texts.add(i + ".");
            textPositions.add(new Vector2(rankPosX, scorePosY));

            String score = ConstantsManager.settings.getString(
                    formatLevelNumber(level) + "_top_" + i, "-");
            glyph = new GlyphLayout(font, score);
            scorePosX = (WINDOW_WIDTH * (28/40f) - glyph.width);
            texts.add(score);
            textPositions.add(new Vector2(scorePosX, scorePosY));

            scorePosY -= glyph.height + WINDOW_HEIGHT * (2f/40f);
        }
    }

    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            Vector3 touch;

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (arrowLeft.getRectangle().contains(touch.x, touch.y) && currentLevelView > firstLevelView) {
                    SoundController.playClickSound();
                    currentLevelView--;
                    createScoreView(currentLevelView);
                }

                if (arrowRight.getRectangle().contains(touch.x, touch.y) && currentLevelView < lastLevelView) {
                    SoundController.playClickSound();
                    currentLevelView++;
                    createScoreView(currentLevelView);
                }

                if (homeButton.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    HighScore.this.dispose();
                    host.setScreen(new MainMenu(host));
                }
                return true;
            }
        });
    }

    private String formatLevelNumber(int number) {
        if (number < 10) {
            return  "0" + number;
        } else {
            return Integer.toString(number);
        }
    }
}
