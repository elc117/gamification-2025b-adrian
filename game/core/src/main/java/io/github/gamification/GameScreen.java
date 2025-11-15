package io.github.gamification;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color; // added import

/*
* This is the main screen of the game.
*/
public class GameScreen implements Screen {
    // Screen configuration
    private SpriteBatch batch;
    private OrthographicCamera camera;

    // Background texture
    private Texture backgroundTexture;

    // Characters of the game
    private Knight knight;
    private Character wizard;

    // Title of the game
    private BitmapFont font;
    private GlyphLayout titleLayout;
    private final String title = "Programming Quest";

    // Game soundtrack that will play in loop
    private Music soundtrack;

    // The quiz object that will contain all the questions and answers
    private final Quiz quiz = new Quiz("data/quiz.json");

    // UI helpers for quiz
    private ShapeRenderer shapeRenderer;
    private GlyphLayout questionLayout;

    // Layout constants
    private static final float BUTTON_WIDTH = 768f;
    private static final float BUTTON_HEIGHT = 40f;
    private static final float BUTTON_PADDING = 12f;

    private int currentQuestionIndex = 0;
    private boolean quizFinished = false;

    @Override
    public void show() {
        batch = new SpriteBatch();
        initCamera();

        backgroundTexture = new Texture(Gdx.files.internal("texture/background.jpg"));
        knight = new Knight(camera.viewportWidth * 0.2f, camera.viewportHeight * 0.2f);
        wizard = new Wizard(camera.viewportWidth * 0.8f, camera.viewportHeight * 0.2f);

        initTitle();
        playSoundtrack();
        shapeRenderer = new ShapeRenderer();
        questionLayout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        knight.update(delta);
        wizard.update(delta);

        clear();
        renderCamera();

        // Draw background and characters
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        knight.render(batch);
        wizard.render(batch);
        renderTitle();
        batch.end();

        // If player is dead
        if (knight.getHealth() == 0) {
            quizFinished = true;
        }

        // Get questions
        Question[] questions = quiz.getQuestions();
        if (questions == null) return;

        if (currentQuestionIndex >= questions.length) {
            quizFinished = true;
        }

        if (!quizFinished) {
            Question question = questions[currentQuestionIndex];
            String[] options = question.getOptions();

            // Build button rectangles for options
            Rectangle[] optionRects = buildOptionButtons(options.length);

            // Handle input (touch/click)
            handleAnswer(optionRects, question);

            // Draw option backgrounds with ShapeRenderer
            shapeRenderer.setProjectionMatrix(camera.combined);
            // Enable alpha blending so the renderer respects the alpha component
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeType.Filled);
            for (int i = 0; i < optionRects.length; i++) {
                Rectangle r = optionRects[i];
                // light background for buttons (uses alpha for translucency)
                shapeRenderer.setColor(0.16f, 0.16f, 0.16f, 0.5f); // slight dark gray, translucent
                shapeRenderer.rect(r.x, r.y, r.width, r.height);
            }
            shapeRenderer.end();
            // Optional: disable blending to restore state
            Gdx.gl.glDisable(GL20.GL_BLEND);

            // Draw question and option text on top
            batch.begin();

            // Save previous color to restore later
            Color oldColor = font.getColor().cpy();
            
            // Question text
            font.setColor(Color.BLACK);
            questionLayout.setText(font, question.getQuestion());
            float qx = camera.viewportWidth / 2f - questionLayout.width / 2f; // centered
            float qy = camera.viewportHeight - 80f;
            font.draw(batch, questionLayout, qx, qy);

            font.setColor(oldColor); // restore font color

            // Options text
            for (int i = 0; i < options.length; i++) {
                Rectangle r = optionRects[i];
                GlyphLayout optLayout = new GlyphLayout(font, options[i]);
                float textX = r.x + 12f;
                float textY = r.y + r.height - 12f; // slightly inset from top of button
                font.draw(batch, optLayout, textX, textY);
            }
            batch.end();

        } else {
            endGame();
        }
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if (width <= 0 || height <= 0) return;

        camera.setToOrtho(false, width, height); // Update camera with new size

        // Reposition characters so they remain anchored to the corners after a resize
        if (knight != null) {
            knight.setCenter(camera.viewportWidth * 0.2f, camera.viewportHeight * 0.2f);
        }
        if (wizard != null) {
            wizard.setCenter(camera.viewportWidth * 0.8f, camera.viewportHeight * 0.2f);
        }
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Dispose of assets when no longer needed
        if (batch != null) batch.dispose();
        if (knight != null) knight.dispose();
        if (wizard != null) wizard.dispose();
        if (font != null) font.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
    }

    // My custom functions

    private void clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void initCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Set origin to bottom-left
    }

    private void initTitle() {
        font = new BitmapFont(); // default font
        font.getData().setScale(1f); // Make the font larger for the title
        titleLayout = new GlyphLayout(font, title);
    }

    private void renderCamera() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    private void renderTitle() {
        titleLayout.setText(font, title);
        float titleX = camera.viewportWidth / 2f - titleLayout.width / 2f;
        float titleY = camera.viewportHeight - 15f;
        font.draw(batch, titleLayout, titleX, titleY);
    }

    private void playSoundtrack() {
        soundtrack = Gdx.audio.newMusic(Gdx.files.internal("audio/soundtrack.ogg"));
        soundtrack.setVolume(1f);
        soundtrack.setLooping(true);
        soundtrack.play();
    }

    private Rectangle[] buildOptionButtons(int numOptions) {
        Rectangle[] optionRects = new Rectangle[numOptions];
        float startX = camera.viewportWidth / 2f - BUTTON_WIDTH / 2f; // centered
        float startY = camera.viewportHeight - 128f; // below title

        for (int i = 0; i < numOptions; i++) {
            float y = startY - i * (BUTTON_HEIGHT + BUTTON_PADDING) - BUTTON_HEIGHT;
            optionRects[i] = new Rectangle(startX, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        }

        return optionRects;
    }

    private void handleAnswer(Rectangle[] optionRects, Question question) {
        if (Gdx.input.justTouched()) {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            for (int i = 0; i < optionRects.length; i++) {
                if (optionRects[i].contains(touch.x, touch.y)) {
                    // if answer is right, increment score and health
                    if (i == question.getAnswer()) {
                        knight.incrementScore();
                        knight.heal();
                    }
                    else {
                        knight.damage();
                    }
                    // advance to next question
                    currentQuestionIndex++;
                    break;
                }
            }
        }
    }

    private void endGame() {
        // Quiz finished - show final score out of total questions
        String scoreText = "Game Over! Your score: " + knight.getScore() + "/" + quiz.getQuestions().length;
        GlyphLayout scoreLayout = new GlyphLayout(font, scoreText);

        // Save previous font scale and color to restore later
        float oldScaleX = font.getData().scaleX;
        float oldScaleY = font.getData().scaleY;
        Color oldColor = font.getColor().cpy();

        // Increase font size and set color to blue
        font.getData().setScale(1.8f); // adjust multiplier as desired
        font.setColor(Color.BLUE);

        // Recalculate layout for the larger font
        scoreLayout.setText(font, scoreText);
        float sx = camera.viewportWidth / 2f - scoreLayout.width / 2f;
        float sy = camera.viewportHeight / 2f + scoreLayout.height / 2f;

        batch.begin();
        font.draw(batch, scoreLayout, sx, sy);
        batch.end();

        // Restore previous font settings
        font.getData().setScale(oldScaleX, oldScaleY);
        font.setColor(oldColor);
    }
}