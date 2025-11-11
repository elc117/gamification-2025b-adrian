package io.github.gamification;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Simple Character object that holds its own idle animation and draws at a specified size.
 * Doesn't move by itself; position is provided at construction.
 */
public class Character {
    private static int frameCount;
    private static float frameDuration;

    private Texture[] textures;                 // Textures for each frame
    private Animation<TextureRegion> animation; // Idle animation
    private float stateTime;                    // Time elapsed since animation start

    protected float x;                            // X position of the character
    protected float y;                            // Y position of the character
    protected float width;                        // Width of the character
    protected float height;                       // Height of the character

    /**
     * Creates a character centered at (centerX, centerY) with given width/height in pixels.
     */
    public Character(float centerX, float centerY, float width, float height, String assetPath, int frameCount) {
        this.width = width;
        this.height = height;
        this.setCenter(centerX, centerY);
        this.frameCount = frameCount;
        this.frameDuration = 1f / frameCount;

        loadAnimation(assetPath);
        this.stateTime = 0f;
    }

    /**
     * Loads the idle animation frames from the assets.
     */
    private void loadAnimation(String assetPath) {
        textures = new Texture[frameCount];
        TextureRegion[] regions = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String path = assetPath + "/idle" + (i + 1) + ".png"; // assets root relative path
            textures[i] = new Texture(Gdx.files.internal(path));
            regions[i] = new TextureRegion(textures[i]);
        }
        animation = new Animation<>(frameDuration, regions);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    /**
     * Updates the animation state.
     */
    public void update(float delta) {
        stateTime += delta;
    }

    /**
     * Renders the character at its position with its size.
     */
    public void render(SpriteBatch batch) {
        TextureRegion current = animation.getKeyFrame(stateTime, true);
        batch.draw(current, x, y, width, height);
    }

    /**
     * Reposition the character so its center is at (centerX, centerY).
     * Useful when the viewport changes size and characters must be kept in corners.
     */
    public void setCenter(float centerX, float centerY) {
        this.x = centerX - width / 2f;
        this.y = centerY - height / 2f;
    }

    public void dispose() {
        if (textures != null) {
            for (Texture t : textures) {
                if (t != null) t.dispose();
            }
        }
    }
}
