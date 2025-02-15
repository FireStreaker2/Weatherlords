package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. Listens to user input. */
public class Main extends InputAdapter implements ApplicationListener {
    Texture pfp;
    SpriteBatch spriteBatch;
    FitViewport viewport;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);

        pfp = new Texture("pfp.png");

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
    }

    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input() {

    }

    private void logic() {

    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix((viewport.getCamera().combined));
        spriteBatch.begin();

        spriteBatch.draw(pfp, 1, 1, 1, 1);

        spriteBatch.end();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    // Note: you can override methods from InputAdapter API to handle user's input.
}
