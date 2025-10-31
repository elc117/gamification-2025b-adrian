package io.github.gamification;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This is the main character of the game.
 * Simple Knight actor that holds its own idle animation and draws at a specified size.
 * The Knight does not move by itself; position is provided at construction.
 */
public class Knight {
    private static final int FRAME_COUNT = 10;
    private static final float FRAME_DURATION = 0.1f;

    private Texture[] textures;                 // Textures for each frame
    private Animation<TextureRegion> animation; // Idle animation
    private float stateTime;                    // Time elapsed since animation start

    private float x;                            // X position of the knight
    private float y;                            // Y position of the knight
    private float width;                        // Width of the knight
    private float height;                       // Height of the knight

    /**
     * Creates a Knight centered at (centerX, centerY) with given width/height in pixels.
     */
    public Knight(float centerX, float centerY, float width, float height) {
        this.width = width;
        this.height = height;
        this.x = centerX - width / 2f;
        this.y = centerY - height / 2f;

        loadAnimation();
        this.stateTime = 0f;
    }

    /**
     * Loads the idle animation frames from the assets.
     */
    private void loadAnimation() {
        textures = new Texture[FRAME_COUNT];
        TextureRegion[] regions = new TextureRegion[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++) {
            String path = "knight/idle" + (i + 1) + ".png"; // assets root relative path
            textures[i] = new Texture(Gdx.files.internal(path));
            regions[i] = new TextureRegion(textures[i]);
        }
        animation = new Animation<>(FRAME_DURATION, regions);
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    /**
     * Updates the animation state.
     */
    public void update(float delta) {
        stateTime += delta;
    }

    /**
     * Renders the knight at its position with its size.
     */
    public void render(SpriteBatch batch) {
        TextureRegion current = animation.getKeyFrame(stateTime, true);
        batch.draw(current, x, y, width, height);
    }

    public void dispose() {
        if (textures != null) {
            for (Texture t : textures) {
                if (t != null) t.dispose();
            }
        }
    }
}
