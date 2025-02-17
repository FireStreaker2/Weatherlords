package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class SettingsMenu implements Screen {
    final Weatherlords game;

    private Stage stage;
    private Skin skin;

    public SettingsMenu(final Weatherlords game) {
        this.game = game;

        stage = new Stage(new FitViewport(800, 480));
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Label title = new Label("SETTINGS", skin);
        title.setColor(Color.WHITE);
        title.setFontScale(2);
        title.pack();
        title.setPosition(stage.getWidth() / 2 - title.getWidth() / 2, 400);

        TextButton graphicsButton = new TextButton("GRAPHICS", skin);
        TextButton soundsButton = new TextButton("SOUNDS", skin);
        TextButton uiButton = new TextButton("UI", skin);
        TextButton controlsButton = new TextButton("CONTROLS", skin);
        TextButton doneButton = new TextButton("DONE", skin);
        TextButton[] buttons = { graphicsButton, soundsButton, uiButton, controlsButton, doneButton };

        for (TextButton button : buttons) {
            button.setSize(200, 60);
//            button.addListener(new ClickListener() {
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    game.setScreen();
//                    dispose();
//                }
//            });
        }

        graphicsButton.setPosition(100, 175);
        soundsButton.setPosition(500, 175);
        uiButton.setPosition(100, 275);
        controlsButton.setPosition(500, 275);
        doneButton.setPosition(stage.getWidth() / 2 - doneButton.getWidth() / 2, 50);

        stage.addActor(title);
        for (TextButton button : buttons) stage.addActor(button);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

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
        skin.dispose();
    }
}
