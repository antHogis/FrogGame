package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private HomeButton homeButton;
    private GenericButton arrowRight;
    private GenericButton arrowLeft;
    private ArrayList<GenericButton> levelButtons;
    private ArrayList<Star> stars;

    private final int firstLevelView;
    private final int lastLevelView;
    private int currentLevelView;

    public LevelSelect(FrogMain host) {
        this.host = host;
        camera = host.getCamera();
        batch = host.getBatch();
        WINDOW_WIDTH = camera.viewportWidth;
        WINDOW_HEIGHT = camera.viewportHeight;

        background = new Texture(Gdx.files.internal(ConstantsManager.bgGenericPath));
        createUI();
        firstLevelView = 1;
        currentLevelView = firstLevelView;
        lastLevelView = levelButtons.size()/9;
        setInputProcessor();
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
    }

    private void createUI() {
        levelButtons = new ArrayList<GenericButton>();
        stars = new ArrayList<Star>();
        final float BUTTON_WIDTH = WINDOW_WIDTH*(6f/40f);
        int buttonNumber = 1;

        createLevelButtons(buttonNumber, BUTTON_WIDTH);
        createStars(buttonNumber, BUTTON_WIDTH/3);

        buttonNumber += levelButtons.size();

        createLevelButtons(buttonNumber, BUTTON_WIDTH);
        createStars(buttonNumber, BUTTON_WIDTH/3);

        homeButton = new HomeButton(WINDOW_WIDTH*(4f/40f));
        homeButton.setY(WINDOW_HEIGHT-homeButton.getHeight());

        arrowLeft = new GenericButton(WINDOW_WIDTH*(4f/40f), ConstantsManager.menuArrowLeftPath);
        arrowLeft.setY(WINDOW_HEIGHT/2 - arrowLeft.getHeight()/2);

        arrowRight = new GenericButton(arrowLeft.getWidth(), ConstantsManager.menuArrowRightPath);
        arrowRight.setY(arrowLeft.getY());
        arrowRight.setX(WINDOW_WIDTH - arrowRight.getWidth());

        chooseLevel = new TextItem(ConstantsManager.myBundle.format("text_chooseLevel"),
                WINDOW_HEIGHT*(6f/40f));
        chooseLevel.setX(WINDOW_WIDTH/2 - chooseLevel.getWidth()/2);
        chooseLevel.setY(WINDOW_HEIGHT - chooseLevel.getHeight());
    }

    private void createLevelButtons(int buttonNumber, float BUTTON_WIDTH) {
        final float ROW_FIRST_X = WINDOW_WIDTH*(6f/40f);
        final float ROW_FIRST_Y = WINDOW_HEIGHT*(26f/40f);
        final float MARGIN_X = WINDOW_WIDTH*(5f/40f);
        final float MARGIN_Y = WINDOW_HEIGHT*(3f/40f);
        float currentX = ROW_FIRST_X;
        float currentY = ROW_FIRST_Y;

        final int columns = 3; final int rows = 3;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                levelButtons.add(new GenericButton(BUTTON_WIDTH,
                        "ui/buttons/level/" + formatLevelNumber(buttonNumber) + ".png"));
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
        final float ROW_FIRST_Y = WINDOW_HEIGHT*(26f/40f);
        final float MARGIN_X = WINDOW_WIDTH*(5f/40f);
        final float MARGIN_Y = WINDOW_HEIGHT*(3f/40f);
        float currentX = ROW_FIRST_X;
        float currentY = ROW_FIRST_Y - WINDOW_HEIGHT*(2f/40f);

        final int columns = 3; final int rows = 3;

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
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touch);

                for (int i = 0; i < 9 && currentLevelView == 1; i++) {
                    if (levelButtons.get(i).getRectangle().contains(touch.x, touch.y)) {
                        SoundController.playClickSound();
                        LevelSelect.this.dispose();
                        //Level numbers range from 1 up, hence i+1
                        host.setScreen(createLevel(formatLevelNumber(i+1)));
                    }
                }

                for (int i = 10; i < 18 && currentLevelView == 2; i++) {
                    if (levelButtons.get(i).getRectangle().contains(touch.x, touch.y)) {
                        SoundController.playClickSound();
                        LevelSelect.this.dispose();
                        //Level numbers range from 1 up, hence i+1
                        host.setScreen(createLevel(formatLevelNumber(i+1)));
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

    private void drawUI() {
        batch.draw(background, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        chooseLevel.draw(batch);
        int levelsInView = 9;
        int starsInView = levelsInView*3;

        if (currentLevelView == 1) {
            for (int i = 0; i < levelsInView; i++) {
                levelButtons.get(i).draw(batch);
            }
            for (int i = 0; i < starsInView; i++) {
                stars.get(i).draw(batch);
            }
        }

        if (currentLevelView == 2) {
            for (int i = levelsInView; i < levelsInView*2; i++) {
                levelButtons.get(i).draw(batch);
            }
            for (int i = starsInView; i < starsInView*2; i++) {
                stars.get(i).draw(batch);
            }
        }

        homeButton.draw(batch);
        if (currentLevelView > firstLevelView) {
            arrowLeft.draw(batch);
        }
        if (currentLevelView < lastLevelView) {
            arrowRight.draw(batch);
        }
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
        String TIME_TWO_STARS
                = ConstantsManager.levels.get(identifier + "_TIME_TWO_STARS");
        String TIME_THREE_STARS
                = ConstantsManager.levels.get(identifier + "_TIME_THREE_STARS");

        return new Level(host,
                identifier,
                mapPath,
                AMOUNT_ROUNDFISH,
                AMOUNT_LONGFISH,
                AMOUNT_OCTOPUS1,
                AMOUNT_OCTOPUS2,
                TILE_WIDTH,
                TILE_HEIGHT,
                TIME_TWO_STARS,
                TIME_THREE_STARS);
    }

    private String formatLevelNumber(int number) {
        if (number < 10) {
            return  "0" + number;
        } else {
            return Integer.toString(number);
        }
    }

}
