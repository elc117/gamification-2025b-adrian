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
					System.out.printf("ERROR: question '%s' has no options defined.\n", questionText);
					optionsArray = new String[0];
					System.exit(1);
				}

				int answer = question.getInt("answer", 0);
				this.questions[questionIndex++] = new Question(questionText, optionsArray, answer);
			}

		} catch (Exception e) {
			System.out.printf("ERROR: Failed to load quiz from %s: %s\n", jsonFilePath, e.getMessage());
			System.exit(1);
		}
	}

	public Question[] getQuestions() {
		return questions;
	}

	public void debug() {
		Question[] questions = getQuestions();
        for (Question q : questions) {
            System.out.println("Question: " + q.getQuestion());
            String[] options = q.getOptions();
            for (int i = 0; i < options.length; i++) {
                System.out.printf("  Option %d: %s\n", i + 1, options[i]);
            }
            System.out.println("  Answer: Option " + (q.getAnswer() + 1));
        }
	}

	private JsonValue getJsonQuestions(String jsonFilePath) {
		FileHandle jsonFile = Gdx.files.internal(jsonFilePath);
		if (!jsonFile.exists()) {
			System.out.printf("Quiz JSON file not found: %s\n", jsonFilePath);
			System.exit(1);
		}

		JsonReader jsonReader = new JsonReader();
		JsonValue parsedJson = jsonReader.parse(jsonFile); // parses the JSON content
		JsonValue questionsJson = parsedJson.get("questions"); // get the questions JSON array
		if (questionsJson == null) {
			System.out.printf("Quiz JSON does not contain 'questions' array: %s\n", jsonFilePath);
			System.exit(1);
		}

		return questionsJson;
	}
}