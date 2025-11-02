package io.github.gamification;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** First screen of the application. Displays a centered idle animation (character does not move). */
public class FirstScreen implements Screen {
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Character knight; // The main character
    private Character wizard; // The villain character

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Set origin to bottom-left

        // Create a small knight (128x128) on the bottom-left quarter of the screen
        knight = new Character(camera.viewportWidth / 8f, camera.viewportHeight / 4f, 128f, 128f, "knight", 10);
        wizard = new Character(camera.viewportWidth / 1.2f, camera.viewportHeight / 4f, 128f, 128f, "wizard", 6);
    }

    @Override
    public void render(float delta) {
        // Update animation state
        knight.update(delta);
        wizard.update(delta);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Draw the knight
        batch.begin();
        knight.render(batch);
        wizard.render(batch);
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
    }
}