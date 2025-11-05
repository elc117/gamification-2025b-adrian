package io.github.gamification;

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
			System.out.printf("Error: answer of question was set as %d, but only %d options were given. Aborting...\n", answer + 1, options.length);
			System.exit(1);
		}
	}
}