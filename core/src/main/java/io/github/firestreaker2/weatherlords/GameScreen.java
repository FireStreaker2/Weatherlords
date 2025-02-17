package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final Weatherlords game;

    Texture pfp;
    Vector2 touchPos;
    Sprite guraSprite;

    public GameScreen(final Weatherlords game) {
        this.game = game;

        touchPos = new Vector2();
        pfp = new Texture("pfp.png");

        guraSprite = new Sprite(pfp);
        guraSprite.setSize(1, 1);
    }

    @Override
    public void resize(final int width, final int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            guraSprite.translateY(speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            guraSprite.translateY(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            guraSprite.translateX(-speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            guraSprite.translateX(speed * delta);
        }
        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);
            guraSprite.setCenter(touchPos.x, touchPos.y);
        }
    }

    private void logic() {
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix((game.viewport.getCamera().combined));
        game.batch.begin();

        guraSprite.draw(game.batch);

        game.batch.end();
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
    }
}
