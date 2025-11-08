package io.github.gamification;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Knight extends Character {
	private int health;
	private int score;
	private HealthBar healthBar;

	public Knight(float centerX, float centerY) {
		super(centerX, centerY, 128, 128, "texture/knight", 10); // creates a new 128x128 character with the "knight" animation sprite with 10 frames
		this.health = 10; // initial health
		this.score = 0; // amount of questions answered right
		this.healthBar = new HealthBar(this.health, 96f, 10f, 8f);
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);

		if (healthBar != null) { // update and render health bar
			healthBar.setHealth(this.health);
			healthBar.render(batch, this.x, this.y, this.width);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if (healthBar != null) {
			healthBar.dispose();
			healthBar = null;
		}
	}
}