package io.github.firestreaker2.weatherlords.settings;

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

import io.github.firestreaker2.weatherlords.SettingsMenu;
import io.github.firestreaker2.weatherlords.Weatherlords;

/**
 * example implementation of a specific settings page
 * ex. graphics, sounds, ui, controls
 */
public abstract class Setting implements Screen {
    protected final Weatherlords game;
    protected final Stage stage;

    /**
     * Setting constructor
     * @param game
     */
    public Setting(final Weatherlords game) {
        this.game = game;

        stage = new Stage(new FitViewport(800, 480));
        Gdx.input.setInputProcessor(stage);
    }

    // props
    protected abstract void content();

    /**
     * createButton
     * @param label
     * @param x
     * @param y
     * @param action
     * @return
     *
     * easily make a new textbutton
     */
    protected TextButton createButton(String label, float x, float y, Runnable action) {
        TextButton button = new TextButton(label, game.skin);
        button.setSize(200, 60);
        button.setPosition(x, y);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });

        return button;
    }

    /**
     * createLabel
     * @param title
     * @return
     *
     * easily make a label
     * auto positions in title area
     */
    protected Label createLabel(String title) {
        Label label = new Label(title, game.labelStyle);
        label.setColor(Color.BLACK);
        label.pack();
        // settings pages really only need 1 label and in this position
        label.setPosition(stage.getWidth() / 2 - label.getWidth() / 2, 400);

        return label;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);

        stage.act(delta);
        stage.draw();

        // escape keybind
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new SettingsMenu(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        content();
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
