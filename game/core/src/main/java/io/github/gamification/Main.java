package io.github.gamification;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/*
* the main class of the game
* it initializes the game, soundtrack, and background texture
 */
public class Main extends Game {
    private static Texture backgroundTexture;
    private static Music soundtrack;
    private static Sound hurt;
    private static Sound heal;
    
    @Override
    public void create() {  
        hurt = Gdx.audio.newSound(Gdx.files.internal("audio/hurt.ogg"));
        heal = Gdx.audio.newSound(Gdx.files.internal("audio/heal.ogg"));
        playSoundtrack();
        backgroundTexture = new Texture(Gdx.files.internal("texture/background.jpg"));

        setScreen(new GameScreen());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        soundtrack.dispose();
    }

    public static Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    private static void playSoundtrack() {
        soundtrack = Gdx.audio.newMusic(Gdx.files.internal("audio/soundtrack.ogg"));
        soundtrack.setVolume(1f);
        soundtrack.setLooping(true);
        soundtrack.play();
    }

    public static void playHurtSound() {
        hurt.play();
    }

    public static void playHealSound() {
        heal.play();
    }
}