package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by Anton on 18.4.2018.
 */

public class LevelSelect extends ScreenAdapter {
    private FrogMain host;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private final float WINDOW_WIDTH, WINDOW_HEIGHT;

    private Texture background;
    private TextItem chooseLevel;
    private GenericButton homeButton;
    private GenericButton arrowRight;
    private GenericButton arrowLeft;
    private ArrayList<GenericButton> levelButtons;
    private ArrayList<Star> stars;
    private BitmapFont font;
    private ArrayList<Vector2> numberPlacements;

    private final int levelsInView = 6;
    private final int firstLevelView = 1;
    private final int lastLevelView = 3;
    private int currentLevelView = firstLevelView;


    public LevelSelect(FrogMain host) {
        this.host = host;
        camera = host.getCamera();
        batch = host.getBatch();
        WINDOW_WIDTH = camera.viewportWidth;
        WINDOW_HEIGHT = camera.viewportHeight;

        background = new Texture(Gdx.files.internal(ConstantsManager.bgGenericPath));
        createUI();
        setInputProcessor();

        font = new BitmapFont(Gdx.files.internal("ui/fonts/patHand120.txt"));
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
        chooseLevel.dispose();
        font.dispose();
    }

    private void drawUI() {
        batch.draw(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        chooseLevel.draw(batch);
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

        //font.draw(batch, "0", WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
    }

    private void createUI() {
        levelButtons = new ArrayList<GenericButton>();
        stars = new ArrayList<Star>();
        numberPlacements = new ArrayList<Vector2>();
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

        arrowRight = new GenericButton(arrowLeft.getWidth(),
                ConstantsManager.arrowRightIdlePath,
                ConstantsManager.arrowRightPressedPath);
        arrowRight.setY(arrowLeft.getY());
        arrowRight.setX(WINDOW_WIDTH - arrowRight.getWidth());

        chooseLevel = new TextItem(host.getMyBundle().format("text_chooseLevel"),
                WINDOW_HEIGHT*(3f/40f));
        chooseLevel.setX(WINDOW_WIDTH/2 - chooseLevel.getWidth()/2);
        chooseLevel.setY(WINDOW_HEIGHT * (39f/40f) - chooseLevel.getHeight());
    }

    private void createLevelButtons(int buttonNumber, float BUTTON_WIDTH) {
        final float ROW_FIRST_X = WINDOW_WIDTH*(6f/40f);
        final float ROW_FIRST_Y = WINDOW_HEIGHT*(22f/40f);
        final float MARGIN_X = WINDOW_WIDTH*(2f/40f);
        final float MARGIN_Y = WINDOW_HEIGHT*(5f/40f);
        float currentX = ROW_FIRST_X;
        float currentY = ROW_FIRST_Y;

        final int columns = 3; final int rows = 2;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                levelButtons.add(new GenericButton(BUTTON_WIDTH,
                        "ui/buttons/levelSelect/" + formatLevelNumber(buttonNumber) + ".png",
                        "ui/buttons/levelSelect/" + formatLevelNumber(buttonNumber) + ".png"));
                levelButtons.get(levelButtons.size()-1).setX(currentX);
                levelButtons.get(levelButtons.size()-1).setY(currentY);

                currentX += BUTTON_WIDTH + MARGIN_X;
                buttonNumber++;
            }
            currentX = ROW_FIRST_X;
            currentY -= MARGIN_Y + levelButtons.get(0).getHeight();
        }
    }

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
                                LevelSelect.this.dispose();
                                //Level numbers range from 1 up, hence j+1 (array index + 1)
                                host.setScreen(createLevel(formatLevelNumber(j+1)));
                            }
                        }
                    }
                }

                if (arrowLeft.getRectangle().contains(touch.x, touch.y) && currentLevelView > firstLevelView) {
                    SoundController.playClickSound();
                    currentLevelView--;
                }

                if (arrowRight.getRectangle().contains(touch.x, touch.y) && currentLevelView < lastLevelView) {
                    SoundController.playClickSound();
                    currentLevelView++;
                }

                if (homeButton.getRectangle().contains(touch.x, touch.y)) {
                    SoundController.playClickSound();
                    LevelSelect.this.dispose();
                    host.setScreen(new MainMenu(host));
                }
                return true;
            }
        });
    }

    private Level createLevel(String identifier) {
        String mapPath
                = ConstantsManager.levels.get(identifier + "_mapPath");
        int AMOUNT_ROUNDFISH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_ROUNDFISH"));
        int AMOUNT_LONGFISH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_LONGFISH"));
        int AMOUNT_OCTOPUS1
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_OCTOPUS1"));
        int AMOUNT_OCTOPUS2
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_AMOUNT_OCTOPUS2"));
        int TILE_WIDTH
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_TILE_AMOUNT_WIDTH"));
        int TILE_HEIGHT
                = Integer.parseInt(ConstantsManager.levels.get(identifier + "_TILE_AMOUNT_HEIGHT"));

        return new Level(host,
                identifier,
                mapPath,
                AMOUNT_ROUNDFISH,
                AMOUNT_LONGFISH,
                AMOUNT_OCTOPUS1,
                AMOUNT_OCTOPUS2,
                TILE_WIDTH,
                TILE_HEIGHT);
    }

    private String formatLevelNumber(int number) {
        if (number < 10) {
            return  "0" + number;
        } else {
            return Integer.toString(number);
        }
    }

}
