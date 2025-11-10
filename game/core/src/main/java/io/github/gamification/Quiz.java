package io.github.gamification;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.Gdx;

public class Quiz {
	private Question[] questions;

	public Quiz (String jsonFilePath) {
		// Load JSON from given path (relative path inside assets folder)
		try {
			JsonValue questionsJson = getJsonQuestions(jsonFilePath);

			FileHandle answersFile = Gdx.files.internal("data/answers.dat");
			byte[] answerBytes = answersFile.readBytes();
			int i = 0; // counter to read bytes

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

				byte answer = answerBytes[i++]; // read the answer from the binary file
				this.questions[questionIndex++] = new Question(questionText, optionsArray, answer);
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

	private JsonValue getJsonQuestions(String jsonFilePath) {
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
}