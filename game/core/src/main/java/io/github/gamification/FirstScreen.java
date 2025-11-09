package io.github.gamification;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** First screen of the application. Displays a centered idle animation (character does not move). */
public class FirstScreen implements Screen {
    // Screen configuration
    private SpriteBatch batch;
    private OrthographicCamera camera;

    // Background texture
    private Texture backgroundTexture;

    // Characters of the game
    private Character knight;
    private Character wizard;

    // Title of the game
    private BitmapFont font;
    private GlyphLayout titleLayout;
    private final String title = "Programming Quest";

    // Game soundtrack that will play in loop
    private Music soundtrack;

    // The quiz object that will contain all the questions and answers
    private final Quiz quiz = new Quiz("data/quiz.json");

    @Override
    public void show() {
        batch = new SpriteBatch();
        initCamera();
        backgroundTexture = new Texture(Gdx.files.internal("texture/background.jpg"));
        knight = new Knight(camera.viewportWidth * 0.2f, camera.viewportHeight * 0.2f);
        wizard = new Wizard(camera.viewportWidth * 0.8f, camera.viewportHeight * 0.2f);
        initTitle();
        playSoundtrack();
    }

    @Override
    public void render(float delta) {
        knight.update(delta);
        wizard.update(delta);

        clear();
        renderCamera();

        batch.begin();

        batch.draw(backgroundTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        knight.render(batch);
        wizard.render(batch);
        renderTitle();

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if (width <= 0 || height <= 0) return;

        camera.setToOrtho(false, width, height); // Update camera with new size
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Dispose of assets when no longer needed
        if (batch != null) batch.dispose();
        if (knight != null) knight.dispose();
        if (wizard != null) wizard.dispose();
        if (font != null) font.dispose();
    }

    // My custom functions

    private void clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void initCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Set origin to bottom-left
    }

    private void initTitle() {
        font = new BitmapFont(); // default font
        font.getData().setScale(1f); // Make the font larger for the title
        titleLayout = new GlyphLayout(font, title);
    }

    private void renderCamera() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    private void renderTitle() {
        titleLayout.setText(font, title);
        float titleX = camera.viewportWidth / 2f - titleLayout.width / 2f;
        float titleY = camera.viewportHeight - 15f;
        font.draw(batch, titleLayout, titleX, titleY);
    }

    private void playSoundtrack() {
        soundtrack = Gdx.audio.newMusic(Gdx.files.internal("audio/soundtrack.ogg"));
        soundtrack.setVolume(0.4f);
        soundtrack.setLooping(true);
        soundtrack.play();
    }
}