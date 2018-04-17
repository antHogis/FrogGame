package frog.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Anton on 14.4.2018.
 */

public class SoundController {

    private static final float SOUNDS_VOLUME = 0.4f;
    private static final float MUSIC_VOLUME = 0.75f;

    public static Sound hitEnemy;
    public static Sound click;
    public static Sound collectCoin;
    public static Music backgroundMusic;

    public static void initialize() {
        hitEnemy = Gdx.audio.newSound(Gdx.files.internal("sounds/hitEnemy.mp3"));
        hitEnemy.setVolume(0, SOUNDS_VOLUME);

        click = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
        click.setVolume(0, SOUNDS_VOLUME);

        collectCoin = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));
        collectCoin.setVolume(0, SOUNDS_VOLUME);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/demo1-leikattu.wav"));
        backgroundMusic.setVolume(MUSIC_VOLUME);
        backgroundMusic.setLooping(true);
    }

    public static void playClickSound() {
        if(ConstantsManager.settings.getBoolean("sounds-on",
                ConstantsManager.DEFAULT_AUDIO_ON)) {
            SoundController.click.play();
        }
    }

    public static void dispose() {
        hitEnemy.dispose();
        backgroundMusic.dispose();
    }
}
