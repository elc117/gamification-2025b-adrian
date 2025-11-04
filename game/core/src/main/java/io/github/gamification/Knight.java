package io.github.gamification;

public class Knight extends Character {
	private int health;
	private int score;

	public Knight(float centerX, float centerY) {
		super(centerX, centerY, 128, 128, "knight", 10); // creates a new 128x128 character with the "knight" animation sprite with 10 frames
		this.health = 5; // I decided to make his initial health as 5; can be changed just fine
		this.score = 0; // will be added to 1 for every question answered right
	}
}