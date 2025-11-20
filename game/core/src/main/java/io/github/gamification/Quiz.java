package io.github.gamification;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.Random;

public class Quiz {
	private Question[] questions;
	private static final int MAX_QUESTIONS = 16; // if there's more than this number of questions, some will be removed
	private static final int MAX_OPTIONS = 4; // maximum number of options per question

	public Quiz (String jsonFilePath) {
		// Load JSON from given path (relative path inside assets folder)
		try {
			JsonValue questionsJson = getJsonQuestions(jsonFilePath);
			// get questions array
			this.questions = new Question[questionsJson.size];
			int questionIndex = 0;
			for (JsonValue question = questionsJson.child; question != null; question = question.next) {
				String questionText = question.getString("question", ""); // get question string

				// get options array
				JsonValue optionsJson = question.get("options");
				String[] optionsArray;
				if (optionsJson != null) {
					optionsArray = new String[optionsJson.size];
					int optionIndex = 0;
					for (JsonValue option = optionsJson.child; option != null; option = option.next) {
						optionsArray[optionIndex++] = option.asString();
					}
				} else {
					Gdx.app.log("error", "Question '" + questionText + "' has no options defined.");
					optionsArray = new String[0];
				}

				String answer_string = optionsArray[0]; // for now, first option is the correct answer, but will be shuffled later

				byte answer_index = 0;
				do {
					shuffleOptions(optionsArray);
					answer_index = findOptionIndex(optionsArray, answer_string);
					// if the correct answer index is out of bounds due to options truncation, reshuffle
				} while (answer_index >= MAX_OPTIONS);

				// selects only up to MAX_OPTIONS. Since it shuffles before, it's random which options are kept
				if (optionsArray.length > MAX_OPTIONS) {
					// truncate options array to MAX_OPTIONS
					String[] truncatedOptions = new String[MAX_OPTIONS];
					System.arraycopy(optionsArray, 0, truncatedOptions, 0, MAX_OPTIONS);
					optionsArray = truncatedOptions;
				}

				this.questions[questionIndex++] = new Question(questionText, optionsArray, answer_index);
			}

			// shuffles questions
			shuffleQuestions(this.questions);

			// selects only up to MAX_QUESTIONS. Since it shuffles before, it's random which questions are kept
			if (this.questions.length > MAX_QUESTIONS) {
				// truncate questions array to MAX_QUESTIONS
				Question[] truncatedQuestions = new Question[MAX_QUESTIONS];
				System.arraycopy(this.questions, 0, truncatedQuestions, 0, MAX_QUESTIONS);
				this.questions = truncatedQuestions;
			}

		} catch (Exception e) {
			Gdx.app.log("error", "Failed to load quiz from '" + jsonFilePath + "': " + e.getMessage());
		}
	}

	public Question[] getQuestions() {
		return questions;
	}

	public void debug() {
		Question[] questions = getQuestions();
        for (Question q : questions) {
            Gdx.app.log("debug", "Question: " + q.getQuestion());
            String[] options = q.getOptions();
            for (int i = 0; i < options.length; i++) {
				Gdx.app.log("debug", "\tOption " + (i + 1) + ": " + options[i]);
            }
			Gdx.app.log("debug", "\tAnswer: Option " + (q.getAnswer() + 1));
        }
	}

	private static JsonValue getJsonQuestions(String jsonFilePath) {
		FileHandle jsonFile = Gdx.files.internal(jsonFilePath);
		if (!jsonFile.exists()) {
			Gdx.app.log("error", "Quiz JSON file not found: " + jsonFilePath);
		}

		JsonReader jsonReader = new JsonReader();
		JsonValue parsedJson = jsonReader.parse(jsonFile); // parses the JSON content
		JsonValue questionsJson = parsedJson.get("questions"); // get the questions JSON array
		if (questionsJson == null) {
			Gdx.app.log("error", "Quiz JSON does not contain 'questions' array: " + jsonFilePath);
		}

		return questionsJson;
	}

	private static void shuffleOptions(String[] options) {
		Random rand = new Random();
		for (int i = options.length - 1; i > 0; i--) {
			int r = rand.nextInt(i + 1);
			String aux = options[r];
			options[r] = options[i];
			options[i] = aux;
		}
	}

	private static void shuffleQuestions(Question[] questions) {
		Random rand = new Random();
		for (int i = questions.length - 1; i > 0; i--) {
			int r = rand.nextInt(i + 1);
			Question aux = questions[r];
			questions[r] = questions[i];
			questions[i] = aux;
		}
	}

	private static byte findOptionIndex(String[] options, String option) {
		for (byte i = 0; i < options.length; i++) {
			if (options[i] == option)
				return i;
		}
		return -1;
	}
}