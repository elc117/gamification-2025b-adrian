package io.github.gamification;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HealthBar {
    private int maxHealth;
    private int health;

    private float width;
    private float height;
    private float offsetY;

    private Texture pixel;

    public HealthBar(int maxHealth, float width, float height, float offsetY) {
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.width = width;
        this.height = height;
        this.offsetY = offsetY;

        // Create a 1x1 white pixel texture
		// The bar will be drawn using this texture scaled appropriately
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.WHITE);
        pix.fill();
        pixel = new Texture(pix);
        pix.dispose();
    }

    public int getHealth() {
        return health;
    }

	public void setHealth(int health) {
		this.health = health;
	}

    public void render(SpriteBatch batch, float charX, float charY, float charWidth) {
        // bottom-left position for the bar: centered horizontally below the character
        float barX = charX + (charWidth - width) / 2f;
        float barY = charY - offsetY - height;

        // Bar background (dark gray)
        batch.setColor(0.2f, 0.2f, 0.2f, 1f);
        batch.draw(pixel, barX, barY, width, height);

        // Bar color (green when >50%, yellow when 25-50, red when <25)
        float percent = (float) health / (float) maxHealth;
        Color color;
        if (percent > 0.5f) color = Color.GREEN;
        else if (percent > 0.25f) color = Color.YELLOW;
        else color = Color.RED;

        batch.setColor(color);
        batch.draw(pixel, barX, barY, width * percent, height);

        // restore white color for other draws
        batch.setColor(Color.WHITE);
    }

    public void dispose() {
        if (pixel != null) pixel.dispose();
    }
}
