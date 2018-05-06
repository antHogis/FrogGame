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
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * The level selection menu.
 *
 * <p>The level selection menu has a button for each level in the game. The amount of golden stars
 * the player has achieved from a level is displayed under each corresponding level button.
 * Six levels are displayed at a time, and they are arranged into two rows and three columns.</p>
 *
 * <p>There are currently 18 levels, so the player can find levels from different views of the level
 * select screen, of which there are 3. The player can navigate between views using arrow buttons.
 * The difficulty of the levels in a view are displayed on the bottom of the screen.</p>
 *
 * @author Tadpole Attack Squad
 * @version 2018.0506
 * @since 2018.0418
 * Created by Anton on 18.4.2018.
 */

public class LevelSelectMenu extends ScreenAdapter {
    private GameMain host;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private final float WINDOW_WIDTH, WINDOW_HEIGHT;

    private Texture background;
    private String chooseLevel;
    private GenericButton homeButton;
    private GenericButton arrowRight;
    private GenericButton arrowLeft;
    private ArrayList<GenericButton> levelButtons;
    private ArrayList<Star> stars;
    private BitmapFont font;

    private final int levelsInView = 6;
    private final int firstLevelView = 1;
    private final int lastLevelView = 3;
    private int currentLevelView = firstLevelView;
    private ArrayList<String> difficultyTexts;

    /**
     * The constructor of LevelSelectMenu.
     *
     * Retrieves the camera and SpriteBatch of the main class, and also calls for methods that initialize the
     * buttons and font of the menu, and set's the application's InputProcessor.
     *
     * @param host the main class, which controls the displayed screen.
     */
    public LevelSelectMenu(GameMain host) {
        this.host = host;
        camera = host.getCamera();
        batch = host.getBatch();
        WINDOW_WIDTH = camera.viewportWidth;
        WINDOW_HEIGHT = camera.viewportHeight;

        background = new Texture(Gdx.files.internal(ConstantsManager.bgGenericPath));
        createUI();
        setInputProcessor();

        font = new BitmapFont(Gdx.files.internal("ui/fonts/patHand90.txt"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            drawUI();
        batch.end();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        super.dispose();
        for (GenericButton levelButton : levelButtons) {
            levelButton.dispose();
        }
        for (Star star : stars) {
            star.dispose();
        }
        arrowLeft.dispose();
        arrowRight.dispose();
        homeButton.dispose();
        background.dispose();
        font.dispose();
    }

    /**
     * Draws the buttons of the menu.
     *
     * Only draws the level buttons the correspond to the level view, and arrows according to whether
     * or not there are more views to the left or to the right.
     * If currentLevelView = 1: levels 1-6 are displayed.
     * If currentLevelView = 2: levels 7-12 are displayed.
     * If currentLevelView = 3: levels 13-18 are displayed.
     */
    private void drawUI() {
        batch.draw(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        int starsInView = levelsInView * 3;

        for(int i = firstLevelView; i <= lastLevelView; i++) {
            if (currentLevelView == i) {
                for (int j = levelsInView * (i - 1); j < levelsInView * i; j++) {
                    levelButtons.get(j).draw(batch);
                }
                for (int j = starsInView * (i - 1); j < starsInView * i; j++) {
                    stars.get(j).draw(batch);
                }
            }
        }

        homeButton.draw(batch);
        if (currentLevelView > firstLevelView) {
            arrowLeft.draw(batch);
        }
        if (currentLevelView < lastLevelView) {
            arrowRight.draw(batch);
        }
        GlyphLayout glyph = new GlyphLayout(font, chooseLevel);
        font.draw(batch, chooseLevel, WINDOW_WIDTH/2 - glyph.width/2, WINDOW_HEIGHT * (39f/40f));
        if (difficultyTexts.get(currentLevelView-1) != null) {
            glyph = new GlyphLayout(font, difficultyTexts.get(currentLevelView-1));
            font.draw(batch, difficultyTexts.get(currentLevelView-1),
                    WINDOW_WIDTH/2-glyph.width/2, WINDOW_HEIGHT * (1f/40f) + glyph.height);
        }
    }

    /**
     * Creates the buttons and texts, and their respective positions in the user interface.
     */
    private void createUI() {
        levelButtons = new ArrayList<GenericButton>();
        stars = new ArrayList<Star>();
        final float BUTTON_WIDTH = WINDOW_WIDTH*(8f/40f);
        int buttonNumber = 1;

        for (int i = 0; i < lastLevelView; i++) {
            createLevelButtons(buttonNumber, BUTTON_WIDTH);
            createStars(buttonNumber, BUTTON_WIDTH/3);
            buttonNumber += levelsInView;
        }

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

        chooseLevel = host.getMyBundle().get("text_chooseLevel");
        difficultyTexts = new ArrayList<String>(lastLevelView);
        difficultyTexts.add(host.getMyBundle().get("text_easy"));
        difficultyTexts.add(host.getMyBundle().get("text_medium"));
        difficultyTexts.add(host.getMyBundle().get("text_hard"));
    }

    /**
     * Creates the buttons for opening levels.
     *
     * Creates the buttons for opening levels in a 2 rows by 3 columns array.
     *
     * @param buttonNumber the first button number that should be retrieved
     * @param BUTTON_WIDTH the desired width of a button
     */
    private void createLevelButtons(int buttonNumber, float BUTTON_WIDTH) {
        final float ROW_FIRST_X = WINDOW_WIDTH*(6f/40f);
        final float ROW_FIRST_Y = WINDOW_HEIGHT*(23f/40f);
        final float MARGIN_X = WINDOW_WIDTH*(2f/40f);
        final float MARGIN_Y = WINDOW_HEIGHT*(5f/40f);
        float currentX = ROW_FIRST_X;
        float currentY = ROW_FIRST_Y;

        final int columns = 3; final int rows = 2;

        String path = "ui/buttons/levelSelect/";
        String idle = "-idle.png";
        String pressed = "-pressed.png";

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                levelButtons.add(new GenericButton(BUTTON_WIDTH,
                        path+ formatLevelNumber(buttonNumber) + idle,
                        path + formatLevelNumber(buttonNumber) + pressed));
                levelButtons.get(levelButtons.size()-1).setX(currentX);
                levelButtons.get(levelButtons.size()-1).setY(currentY);

                currentX += BUTTON_WIDTH + MARGIN_X;
                buttonNumber++;
            }
            currentX = ROW_FIRST_X;
            currentY -= MARGIN_Y + levelButtons.get(0).getHeight();
        }
    }

    /**
     * Creates stars to be displayed below the level buttons.
     *
     * Creates the amount of stars achieved in a level below the corresponding level-opening button.
     *
     * @param buttonNumber the first button number corresponding to the level
     * @param STAR_WIDTH the width of a star.
     */
    private void createStars(int buttonNumber, float STAR_WIDTH) {
        final float ROW_FIRST_X = WINDOW_WIDTH*(6f/40f);
        final float ROW_FIRST_Y = WINDOW_HEIGHT*(22f/40f);
        final float MARGIN_X = WINDOW_WIDTH*(2f/40f);
        final float MARGIN_Y = WINDOW_HEIGHT*(5f/40f);
        float currentX = ROW_FIRST_X;
        float currentY = ROW_FIRST_Y - WINDOW_HEIGHT*(2f/40f);

        final int columns = 3; final int rows = 2;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)  {

                int goldenStars = ConstantsManager.settings.getInteger(formatLevelNumber(buttonNumber)+"_stars", 0);
                for (int k = 0; k < goldenStars; k++) {
                    stars.add(new Star(STAR_WIDTH, true));
                    stars.get(stars.size()-1).setX(currentX);
                    stars.get(stars.size()-1).setY(currentY);

                    currentX += STAR_WIDTH;
                }

                int greyStars = 3 - goldenStars;
                for (int k = 0; k < greyStars; k++) {
                    stars.add(new Star(STAR_WIDTH, false));
                    stars.get(stars.size()-1).setX(currentX);
                    stars.get(stars.size()-1).setY(currentY);

                    currentX += STAR_WIDTH;
                }
                currentX += MARGIN_X;
                buttonNumber++;
            }
            currentX = ROW_FIRST_X;
            currentY -= MARGIN_Y + levelButtons.get(0).getHeight();
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

                if (arrowLeft.getRectangle().contains(touch.x, touch.y) && currentLevelView > firstLevelView)
                    arrowLeft.setPressed(true);
                else arrowLeft.setPressed(false);

                if (arrowRight.getRectangle().contains(touch.x, touch.y) && currentLevelView < lastLevelView)
                    arrowRight.setPressed(true);
                else arrowRight.setPressed(false);

                if (homeButton.getRectangle().contains(touch.x, touch.y)) homeButton.setPressed(true);
                else homeButton.setPressed(false);

                for(int i = firstLevelView; i <= lastLevelView; i++) {
                    if(currentLevelView == i) {
                        for (int j = levelsInView * (i - 1); j < levelsInView * i; j++) {
                            if (levelButtons.get(j).getRectangle().contains(touch.x, touch.y))
                                levelButtons.get(j).setPressed(true);
                            else levelButtons.get(j).setPressed(false);
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                if (arrowLeft.getRectangle().contains(touch.x, touch.y) && currentLevelView > firstLevelView)
                    arrowLeft.setPressed(true);
                else arrowLeft.setPressed(false);

                if (arrowRight.getRectangle().contains(touch.x, touch.y) && currentLevelView < lastLevelView)
                    arrowRight.setPressed(true);
                else arrowRight.setPressed(false);

                if (homeButton.getRectangle().contains(touch.x, touch.y)) homeButton.setPressed(true);
                else homeButton.setPressed(false);

                for(int i = firstLevelView; i <= lastLevelView; i++) {
                    if(currentLevelView == i) {
                        for (int j = levelsInView * (i - 1); j < levelsInView * i; j++) {
                            if (levelButtons.get(j).getRectangle().contains(touch.x, touch.y))
                                levelButtons.get(j).setPressed(true);
                            else levelButtons.get(j).setPressed(false);
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                for(int i = firstLevelView; i <= lastLevelView; i++) {
                    if(currentLevelView == i) {
                        for (int j = levelsInView * (i - 1); j < levelsInView * i; j++) {
                            if (levelButtons.get(j).getRectangle().contains(touch.x, touch.y)) {
                                SoundController.playClickSound();
                                LevelSelectMenu.this.dispose();
                                //Level numbers range from 1 up, hence j+1 (array index + 1)
                                host.setScreen(createLevel(formatLevelNumber(j+1)));
                            }
                        }
                    }
                }

                if (arrowLeft.getRectangle().contains(touch.x, touch.y) && currentLevelView > firstLevelView) {
                    SoundController.playClickSound();
                    arrowLeft.setPressed(false);
                    currentLevelView--;
                }

                if (arrowRight.getRectangle().contains(touch.x, touch.y) && currentLevelView < lastLevelView) {
                    SoundController.playClickSound();
                    arrowRight.setPressed(false);
                    currentLevelView++;
                }

                if (homeButton.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    LevelSelectMenu.this.dispose();
                    host.setScreen(new MainMenu(host));
                }
                return true;
            }
        });
    }

    /**
     * Creates a new level.
     *
     * Creates a new level by reading the levels.properties file, and finding keys which define the
     * level's tiledMap file location, the amounts of enemies in the level, and the score tiers of
     * the level, that is in what maximum time should the level be completed in order to achieve
     * three stars, and the maximum time the level should be completed in order to achieve two stars
     *
     * @param identifier the number of the level to be created
     * @return the level that is created
     */
    private Level createLevel(String identifier) {
        String mapPath
                = ConstantsManager.levels.get(identifier + "_mapPath");
        int AMOUNT_ROUNDFISH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_ROUNDFISH"));
        int AMOUNT_LONGFISH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_LONGFISH"));
        int AMOUNT_OCTOPUS
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_OCTOPUS1"));
        int TILE_WIDTH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_TILE_AMOUNT_WIDTH"));
        int TILE_HEIGHT
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_TILE_AMOUNT_HEIGHT"));

        return new Level(host,
                identifier,
                mapPath,
                AMOUNT_ROUNDFISH,
                AMOUNT_LONGFISH,
                AMOUNT_OCTOPUS,
                TILE_WIDTH,
                TILE_HEIGHT);
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
