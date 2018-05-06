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

    static Sound hitEnemy;
    static Sound click;
    static Sound collectCoin;
    static Sound checkpoint;
    static Music backgroundMusic;

    static void initialize() {
        hitEnemy = Gdx.audio.newSound(Gdx.files.internal("sounds/hitEnemy.ogg"));
        hitEnemy.setVolume(0, SOUNDS_VOLUME);

        click = Gdx.audio.newSound(Gdx.files.internal("sounds/click.ogg"));
        click.setVolume(0, SOUNDS_VOLUME);

        collectCoin = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.ogg"));
        collectCoin.setVolume(0, SOUNDS_VOLUME);

        checkpoint = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.ogg"));
        collectCoin.setVolume(0, SOUNDS_VOLUME);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/musicWithWater.ogg"));
        backgroundMusic.setVolume(MUSIC_VOLUME);
        backgroundMusic.setLooping(true);

    }

    static void playClickSound() {
        if(ConstantsManager.settings.getBoolean("sounds-on",
                ConstantsManager.DEFAULT_SOUNDS_ON)) SoundController.click.play();
    }
    
    static void playHitSound() {
        if (ConstantsManager.settings.getBoolean("sounds-on", 
                ConstantsManager.DEFAULT_SOUNDS_ON)) SoundController.hitEnemy.play();
    }

    static void playCoinSound() {
        if (ConstantsManager.settings.getBoolean("sounds-on",
                ConstantsManager.DEFAULT_SOUNDS_ON)) SoundController.collectCoin.play();
    }

    static void playCheckpointSound() {
        if (ConstantsManager.settings.getBoolean("sounds-on",
                ConstantsManager.DEFAULT_SOUNDS_ON)) SoundController.checkpoint.play();
    }

    static void playMusic() {
        float randomPosition = (float) Math.random() * 100;
        Gdx.app.log("songPos", Float.toString(randomPosition));
        if (ConstantsManager.settings.getBoolean("music-on", ConstantsManager.DEFAULT_MUSIC_ON)) {
            backgroundMusic.setPosition(randomPosition);
            backgroundMusic.play();
        }

    }

    static void dispose() {
        hitEnemy.dispose();
        collectCoin.dispose();
        click.dispose();
        backgroundMusic.dispose();
    }
}
