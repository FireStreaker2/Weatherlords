package io.github.firestreaker2.weatherlords.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
     *
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
     *
     * @param label
     * @param x
     * @param y
     * @param action easily make a new textbutton
     */
    protected void createButton(String label, float x, float y, Runnable action) {
        TextButton button = new TextButton(label, game.skin);
        button.setSize(200, 60);
        button.setPosition(x, y);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                action.run();
            }
        });

        stage.addActor(button);
    }

    /**
     * createRebindButton
     * <p>
     * manual version for rebind buttons
     * so i can reference obj to change text
     * </p>
     *
     * @param label
     * @param x
     * @param y
     * @return
     */
    protected TextButton createButtonManual(String label, float x, float y) {
        TextButton button = new TextButton(label, game.skin);
        button.setSize(200, 60);
        button.setPosition(x, y);

        return button;
    }

    /**
     * createLabel
     *
     * @param title
     */
    protected void createLabel(String title) {
        Label label = new Label(title, game.labelStyle);
        label.setColor(Color.BLACK);
        label.pack();
        // settings pages really only need 1 label and in this position aside from some specific ones
        label.setPosition(stage.getWidth() / 2 - label.getWidth() / 2, 400); // TODO: change

        stage.addActor(label);
    }

    /**
     * createLabel
     *
     * overload for all the other labels
     *
     * @param title
     * @param x
     * @param y
     */
    protected void createLabel(String title, float x, float y) {
        Label label = new Label(title, game.labelStyle);
        label.setColor(Color.BLACK);
        label.pack();
        label.setPosition(x, y);

        stage.addActor(label);
    }

    /**
     * createSlider
     *
     * @param min
     * @param max
     * @param step
     * @param config
     */
    protected void createSlider(float x, float y, float min, float max, float step, Weatherlords.Config config) {
        Slider slider = new Slider(min, max, step, false, game.skin); // TODO: custom sprite
        slider.setValue(Float.parseFloat(game.getConfig(config)));
        slider.setPosition(x, y);
        slider.setSize(200, 100);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.editConfig(config, slider.getValue() + "");
            }
        });

        stage.addActor(slider);
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
