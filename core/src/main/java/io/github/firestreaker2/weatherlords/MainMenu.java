package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenu implements Screen {
    final Weatherlords game;
    private Stage stage;
    private Skin skin;

    public MainMenu(final Weatherlords game) {
        this.game = game;

        stage = new Stage(new FitViewport(800, 480), game.batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label title = new Label("WEATHERLORDS", skin);
        title.setColor(Color.WHITE);
        title.setFontScale(2);
        title.setPosition(280, 400);

        TextButton startButton = new TextButton("START", skin);
        TextButton settingsButton = new TextButton("SETTINGS", skin);
        TextButton creditsButton = new TextButton("CREDITS", skin);
        TextButton quitButton = new TextButton("QUIT", skin);

        startButton.setSize(200, 60);
        startButton.setPosition(300, 300);

        settingsButton.setSize(200, 60);
        settingsButton.setPosition(300, 220);

        creditsButton.setSize(200, 60);
        creditsButton.setPosition(300, 140);

        quitButton.setSize(200, 60);
        quitButton.setPosition(300, 60);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        // TODO: add settings + credits screen

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
        ScreenUtils.clear(Color.BLACK);

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
        skin.dispose();
    }
}
