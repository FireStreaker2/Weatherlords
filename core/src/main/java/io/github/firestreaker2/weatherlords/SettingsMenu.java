package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.github.firestreaker2.weatherlords.settings.ControlsMenu;
import io.github.firestreaker2.weatherlords.settings.GraphicsMenu;
import io.github.firestreaker2.weatherlords.settings.SoundsMenu;
import io.github.firestreaker2.weatherlords.settings.UserInterfaceMenu;

public class SettingsMenu implements Screen {
    final Weatherlords game;
    private final Stage stage;

    public SettingsMenu(final Weatherlords game) {
        this.game = game;

        stage = new Stage(new FitViewport(800, 480));
        Gdx.input.setInputProcessor(stage);

        Label title = new Label("SETTINGS", game.labelStyle);
        title.setColor(Color.BLACK);
        title.pack();
        title.setPosition(stage.getWidth() / 2 - title.getWidth() / 2, 400);

        TextButton graphicsButton = new TextButton("GRAPHICS", game.skin);
        TextButton soundsButton = new TextButton("SOUNDS", game.skin);
        TextButton uiButton = new TextButton("UI", game.skin);
        TextButton controlsButton = new TextButton("CONTROLS", game.skin);
        TextButton doneButton = new TextButton("DONE", game.skin);
        TextButton[] buttons = { graphicsButton, soundsButton, uiButton, controlsButton, doneButton};

        // i tried to make this dynamic but it failed
        graphicsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GraphicsMenu(game));
                dispose();
            }
        });
        soundsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SoundsMenu(game));
                dispose();
            }
        });
        uiButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new UserInterfaceMenu(game));
                dispose();
            }
        });
        controlsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ControlsMenu(game));
                dispose();
            }
        });
        doneButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });

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
