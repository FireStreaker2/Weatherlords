package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.math.Vector2;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. Listens to user input. */
public class Main extends InputAdapter implements ApplicationListener {
    Texture pfp;

    Vector2 touchPos;
    SpriteBatch spriteBatch;
    FitViewport viewport;

    Sprite guraSprite;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);

        touchPos = new Vector2();
        pfp = new Texture("pfp.png");

        spriteBatch = new SpriteBatch();
        guraSprite = new Sprite(pfp);
        guraSprite.setSize(1, 1);
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
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            guraSprite.translateY(speed * delta);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            guraSprite.translateY(-speed * delta);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            guraSprite.translateX(-speed * delta);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            guraSprite.translateX(speed * delta);
        }
        if(Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            guraSprite.setCenter(touchPos.x, touchPos.y);
        }
    }

    private void logic() {

    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix((viewport.getCamera().combined));
        spriteBatch.begin();

        guraSprite.draw(spriteBatch);

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
