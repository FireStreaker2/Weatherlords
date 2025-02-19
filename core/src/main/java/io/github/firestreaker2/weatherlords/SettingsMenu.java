package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SettingsMenu implements Screen {
    final Weatherlords game;
    private final Stage stage;

    public SettingsMenu(final Weatherlords game) {
        this.game = game;
        stage = new Stage(new FitViewport(800, 480));

        Label title = new Label("SETTINGS", game.labelStyle);
        title.setColor(Color.BLACK);
        title.pack();
        title.setPosition(stage.getWidth() / 2 - title.getWidth() / 2, 400);

        TextButton graphicsButton = new TextButton("GRAPHICS", game.skin);
        TextButton soundsButton = new TextButton("SOUNDS", game.skin);
        TextButton uiButton = new TextButton("UI", game.skin);
        TextButton controlsButton = new TextButton("CONTROLS", game.skin);
        TextButton doneButton = new TextButton("DONE", game.skin);
        TextButton[] buttons = {graphicsButton, soundsButton, uiButton, controlsButton, doneButton};

        Util.addLowTaperFade(stage, game, graphicsButton, "GraphicsMenu");
        Util.addLowTaperFade(stage, game, soundsButton, "SoundsMenu");
        Util.addLowTaperFade(stage, game, uiButton, "UserInterfaceMenu");
        Util.addLowTaperFade(stage, game, controlsButton, "ControlsMenu");
        Util.addLowTaperFade(stage, game, doneButton, "MainMenu");

        graphicsButton.setPosition(100, 175);
        soundsButton.setPosition(500, 175);
        uiButton.setPosition(100, 275);
        controlsButton.setPosition(500, 275);
        doneButton.setPosition(stage.getWidth() / 2 - 100, 60);

        stage.addActor(title);
        for (TextButton button : buttons) {
            button.setSize(200, 60);
            stage.addActor(button);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        stage.act(delta);
        stage.draw();

        // escape keybind
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenu(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
