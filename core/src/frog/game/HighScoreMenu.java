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
 * The high score menu
 *
 * <p>Shows the top 5 best scores achieved for each level, one level at a time. The levels are cycled
 * through by arrow buttons for left and right.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0414
 */

public class HighScoreMenu extends ScreenAdapter {

    private GameMain host;
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

    /**
     * The constructor of HighScoreMenu.
     *
     * Retrieves the camera and SpriteBatch of the main class, and also calls for methods that initialize the
     * buttons,scores, and font of the menu, and set's the application's InputProcessor.
     *
     * @param host the main class, which controls the displayed screen.
     */
    public HighScoreMenu(GameMain host) {
        this.host = host;
        batch = host.getBatch();
        camera = host.getCamera();
        WINDOW_WIDTH = camera.viewportWidth;
        WINDOW_HEIGHT = camera.viewportHeight;

        firstLevelView = 1;
        currentLevelView = firstLevelView;
        lastLevelView = ConstantsManager.LEVELS_AMOUNT;

        font = new BitmapFont(Gdx.files.internal("ui/fonts/patHand72.txt"));
        createScoreView(currentLevelView);
        createUI();
        setInputProcessor();
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
        background.dispose();
        font.dispose();
        arrowLeft.dispose();
        arrowRight.dispose();
        homeButton.dispose();
    }

    /**
     * Draws the top 5 scores of hte current level in view.
     */
    private void drawScores() {
        for (int i = 0; i < texts.size; i++) {
            font.draw(batch, texts.get(i), textPositions.get(i).x, textPositions.get(i).y);
        }
    }

    /**
     * Draws the user interface.
     */
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

    /**
     * Creates the buttons in the user interface.
     */
    private void createUI() {
        background = new Texture(Gdx.files.internal(ConstantsManager.bgHighScorePath));

        homeButton = new GenericButton(WINDOW_WIDTH*(4f/40f),
                ConstantsManager.homeButtonIdlePath,
                ConstantsManager.homeButtonPressedPath);
        homeButton.setY(WINDOW_HEIGHT-homeButton.getHeight());

        arrowLeft = new GenericButton(WINDOW_WIDTH*(4f/40f),
                ConstantsManager.arrowLeftIdlePath,
                ConstantsManager.arrowLeftPressedPath);
        arrowLeft.setY(WINDOW_HEIGHT/2 - arrowLeft.getHeight()/2);
        arrowLeft.setX(WINDOW_WIDTH * (0.5f/40f));

        arrowRight = new GenericButton(arrowLeft.getWidth(),
                ConstantsManager.arrowRightIdlePath,
                ConstantsManager.arrowRightPressedPath);
        arrowRight.setY(arrowLeft.getY());
        arrowRight.setX(WINDOW_WIDTH - arrowRight.getWidth() - (WINDOW_WIDTH * (0.5f/40f)));
    }

    /**
     * Creates the top 5 scores.
     *
     * Retrieves the top 5 scores of a specified level, and sets the positions where they should
     * be drawn.
     *
     * @param level the specified level
     */
    private void createScoreView(int level) {
        texts = new Array<String>();
        textPositions = new Array<Vector2>();

        String title = host.getMyBundle().get("level") + level;
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

    /**
     * Sets the application's InputProcessor.
     *
     * Sets the application's InputProcessor, and implements the methods touchDown, touchUp, and touchDragged.
     * touchDown makes a button appear as pressed.
     * touchDragged makes a button appear as pressed.
     * touchUp performs the function of a button.
     */
    private void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            Vector3 touch;

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (arrowLeft.getRectangle().contains(touch.x, touch.y) && currentLevelView > firstLevelView) {
                    arrowLeft.setPressed(true);
                } else if(arrowLeft.isPressed()) {
                    arrowLeft.setPressed(false);
                }

                if (arrowRight.getRectangle().contains(touch.x, touch.y) && currentLevelView < lastLevelView) {
                    arrowRight.setPressed(true);
                } else if (arrowRight.isPressed()) {
                    arrowRight.setPressed(false);
                }

                if (homeButton.getRectangle().contains(touch.x, touch.y)) {
                    homeButton.setPressed(true);
                } else if (homeButton.isPressed()) {
                    homeButton.setPressed(false);
                }

                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (!arrowLeft.getRectangle().contains(touch.x, touch.y)) {
                    arrowLeft.setPressed(false);
                }
                if (!arrowRight.getRectangle().contains(touch.x, touch.y)) {
                    arrowRight.setPressed(false);

                }
                if (!homeButton.getRectangle().contains(touch.x, touch.y)) {
                    homeButton.setPressed(false);
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (arrowLeft.getRectangle().contains(touch.x, touch.y) && currentLevelView > firstLevelView) {
                    SoundController.playClickSound();
                    arrowLeft.setPressed(false);
                    currentLevelView--;
                    createScoreView(currentLevelView);
                }

                if (arrowRight.getRectangle().contains(touch.x, touch.y) && currentLevelView < lastLevelView) {
                    SoundController.playClickSound();
                    arrowRight.setPressed(false);
                    currentLevelView++;
                    createScoreView(currentLevelView);
                }

                if (homeButton.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    homeButton.setPressed(false);
                    HighScoreMenu.this.dispose();
                    host.setScreen(new MainMenu(host));
                }
                return true;
            }
        });
    }

    /**
     * Formats Integers into Strings for the levels.properties file.
     *
     * Formats Integers into Strings for the levels.properties file, so that values below 10 have
     * a leading zero.
     *
     * @param number the integer to convert
     * @return the String format of the integer
     */
    private String formatLevelNumber(int number) {
        if (number < 10) {
            return  "0" + number;
        } else {
            return Integer.toString(number);
        }
    }
}
