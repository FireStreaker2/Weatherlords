package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * actual game class
 * contains all logic for game
 */
public class GameScreen implements Screen {
    final Weatherlords game;

    Texture pfp;
    Vector2 touchPos;
    Sprite guraSprite;

    public GameScreen(final Weatherlords game) {
        this.game = game;

        touchPos = new Vector2();
        pfp = new Texture("Selector.png");

        guraSprite = new Sprite(pfp);
        guraSprite.setSize(1, 1);
    }

    private void clampCameraPosition() {
        float cameraHalfWidth = camera.viewportWidth / 2;
        float cameraHalfHeight = camera.viewportHeight / 2;

        // Get map width and height in tiles
        int mapWidth = ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth();
        int mapHeight = ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight();

        // Get map dimensions in world units
        float mapWidthInUnits = mapWidth; // Tile count along x-axis
        float mapHeightInUnits = mapHeight; // Tile count along y-axis

        // Clamp camera position to prevent going outside the map bounds
        camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, mapWidthInUnits - cameraHalfWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, mapHeightInUnits - cameraHalfHeight);

        camera.update();
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
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        float tileWidth = layer.getTileWidth()  * 1 / 16f;
        float tileHeight = layer.getTileHeight() * 1 / 16f;
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Background");
        float minX = 0;
        float minY = 0;
        float maxX = viewport.getWorldWidth() - guraSprite.getWidth();
        float maxY = viewport.getWorldHeight() - guraSprite.getHeight();

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

        // Update camera position if needed
        clampCameraPosition();
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
