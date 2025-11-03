package io.github.gamification;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** First screen of the application. Displays a centered idle animation (character does not move). */
public class FirstScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Character knight; // The main character
    private Character wizard; // The villain character
    
    // Title of the game, at the top of the screen
    private BitmapFont font;
    private GlyphLayout titleLayout;
    private String title = "Programming Quest";

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Set origin to bottom-left

        // Create a small knight (128x128) on the bottom-left quarter of the screen
        knight = new Character(camera.viewportWidth / 8f, camera.viewportHeight / 4f, 128f, 128f, "knight", 10);
        wizard = new Character(camera.viewportWidth / 1.2f, camera.viewportHeight / 4f, 128f, 128f, "wizard", 6);

        // Initialize font and layout for the title label
        font = new BitmapFont(); // default font
        font.getData().setScale(1.5f); // Make the font larger for the title
        titleLayout = new GlyphLayout(font, title);
    }

    @Override
    public void render(float delta) {
        knight.update(delta);
        wizard.update(delta);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        knight.render(batch);
        wizard.render(batch);

        titleLayout.setText(font, title);
        float titleX = camera.viewportWidth / 2f - titleLayout.width / 2f;
        float titleY = camera.viewportHeight - 15f;
        font.draw(batch, titleLayout, titleX, titleY);

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
}