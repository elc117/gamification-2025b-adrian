package io.github.gamification;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Knight extends Character {
	private static final int MAX_HEALTH = 10;
	private int health;
	private int score;
	private HealthBar healthBar;

	public Knight(float centerX, float centerY) {
		super(centerX, centerY, 128, 128, "texture/knight", 10); // creates a new 128x128 character with the "knight" animation sprite with 10 frames
		this.health = MAX_HEALTH; // initial health
		this.score = 0; // amount of questions answered right
		this.healthBar = new HealthBar(this.health, 96f, 10f, 8f);
	}

	// Score API so other classes (screens, UI) can update or read the player's score
	public int getScore() {
		return score;
	}

	public void incrementScore() {
		this.score++;
	}

	public int getHealth() {
		return health;
	}

	public void heal() {
		if (health < MAX_HEALTH) this.health++;
	}

	public void damage() {
		if (this.health >= 2)
			this.health -= 2;
		else
			this.health = 0;
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