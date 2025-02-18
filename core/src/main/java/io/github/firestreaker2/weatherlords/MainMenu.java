package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * main menu
 */
public class MainMenu implements Screen {
    final Weatherlords game;
    private final Stage stage;

    public MainMenu(final Weatherlords game) {
        this.game = game;

        stage = new Stage(new FitViewport(800, 480), game.batch);
        Gdx.input.setInputProcessor(stage);

        // TODO: clean up code :3 (dynamic)
        Label title = new Label("WEATHERLORDS", game.labelStyle);
        title.setColor(Color.BLACK);
        title.pack();
        title.setPosition(stage.getWidth() / 2 - title.getWidth() / 2, 400);

        // buttons
        TextButton startButton = new TextButton("START", game.skin);
        TextButton settingsButton = new TextButton("SETTINGS", game.skin);
        TextButton creditsButton = new TextButton("CREDITS", game.skin);
        TextButton quitButton = new TextButton("QUIT", game.skin);

        startButton.setSize(200, 60);
        startButton.setPosition(300, 300);

        settingsButton.setSize(200, 60);
        settingsButton.setPosition(300, 220);

        creditsButton.setSize(200, 60);
        creditsButton.setPosition(300, 140);

        quitButton.setSize(200, 60);
        quitButton.setPosition(300, 60);

        // html vibes
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsMenu(game));
                dispose();
            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CreditsMenu(game));
                dispose();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(title);
        stage.addActor(startButton);
        stage.addActor(settingsButton);
        stage.addActor(creditsButton);
        stage.addActor(quitButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
