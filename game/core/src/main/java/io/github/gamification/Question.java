package io.github.gamification;

import com.badlogic.gdx.Gdx;

public class Question {
	private String question;
	private String[] options;
	private int answer;

	public Question(String question, String[] options, int answer) {
		this.question = question;
		this.options = options;
		if (answer < options.length)
			this.answer = answer;
		else {
			Gdx.app.log("error", "Answer of question was set as " + (answer + 1) + ", but only " + options.length + " options were given.");
		}
	}

	public String getQuestion() {
		return question;
	}
	public String[] getOptions() {
		return options;
	}
	public int getAnswer() {
		return answer;
	}
}