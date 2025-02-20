package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends InputAdapter implements Screen {
    final Weatherlords game;

    Texture pfp;

    Vector2 touchPos;
    SpriteBatch spriteBatch;

    Sprite guraSprite;

    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;

    int guraXInTiles;

    int guraYInTiles;

    boolean isColliding = false;


    public GameScreen(final Weatherlords game) {
        this.game = game;

        Gdx.input.setInputProcessor(this);

        touchPos = new Vector2();
        pfp = new Texture("Selector.png");

        spriteBatch = new SpriteBatch();
        guraSprite = new Sprite(pfp);
        guraSprite.setSize(1, 1);
        camera = new OrthographicCamera();
        game.viewport = new FitViewport(14, 10);
        camera.setToOrtho(false, 14, 10);


        float unitScale = 1 / 16f;
        tiledMap = new TmxMapLoader().load("WorldMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
        MapLayer layers = tiledMap.getLayers().get("SpawnLayer");
        MapObject objectSpawn = layers.getObjects().get("SpawnPoint");
        float x = objectSpawn.getProperties().get("x", Float.class);
        float y = objectSpawn.getProperties().get("y", Float.class);

        guraXInTiles = (int) (x / 16);
        guraYInTiles = (int) (y / 16);

        camera.translate(x / 16f, y);
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
    public void show() {
    }

    @Override
    public void resize(final int width, final int height) {
        game.viewport.update(width, height, true);
        camera.update();
    }

    @Override
    public void render(float delta) {
        input();
        draw();

        // escape keybind
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenu(game)); // TODO: save progress, replace with popup modals
            dispose();
        }
    }

    private void input() {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        float tileWidth = layer.getTileWidth() / 16f;
        float tileHeight = layer.getTileHeight() / 16f;
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Background");
        float minX = 0;
        float minY = 0;
        float maxX = game.viewport.getWorldWidth() - guraSprite.getWidth();
        float maxY = game.viewport.getWorldHeight() - guraSprite.getHeight();

        if (Gdx.input.isKeyJustPressed(Util.getKey(game.getConfig(Weatherlords.Config.UP)))) {
            isColliding = false;

            // Calculates the position of the next tile
            int guraNextYInTiles = guraYInTiles + 1;
            if (guraNextYInTiles > 99) {
                guraNextYInTiles = 99;
            }
            TiledMapTile nextTile = collisionLayer.getCell(guraXInTiles, guraNextYInTiles).getTile();
            if (nextTile.getProperties().containsKey("isSolid")) {
                isColliding = nextTile.getProperties().get("isSolid", Boolean.class);
            }

            // Checks for collision before moving
            if (!isColliding) {
                guraSprite.translateY(tileHeight);
                guraYInTiles += 1;
                if (guraYInTiles > 99) {
                    guraYInTiles = 99;
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Util.getKey(game.getConfig(Weatherlords.Config.DOWN)))) {
            isColliding = false;

            // Calculates the position of the next tile
            int guraNextYInTiles = guraYInTiles - 1;
            if (guraNextYInTiles < 0) {
                guraNextYInTiles = 0;
            }
            TiledMapTile nextTile = collisionLayer.getCell(guraXInTiles, guraNextYInTiles).getTile();
            if (nextTile.getProperties().containsKey("isSolid")) {
                isColliding = nextTile.getProperties().get("isSolid", Boolean.class);
            }

            // Checks for collision before moving
            if (!isColliding) {
                guraSprite.translateY(-tileHeight);
                guraYInTiles -= 1;
                if (guraYInTiles < 0) {
                    guraYInTiles = 0;
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Util.getKey(game.getConfig(Weatherlords.Config.LEFT)))) {
            isColliding = false;
            int guraNextXInTiles = guraXInTiles - 1;
            if (guraNextXInTiles < 0) {
                guraNextXInTiles = 0;
            }
            TiledMapTile nextTile = collisionLayer.getCell(guraNextXInTiles, guraYInTiles).getTile();
            if (nextTile.getProperties().containsKey("isSolid")) {
                isColliding = nextTile.getProperties().get("isSolid", Boolean.class);
            }

            // Checks for collision before moving
            if (!isColliding) {
                guraSprite.translateX(-tileWidth);
                guraXInTiles -= 1;
                if (guraXInTiles < 0) {
                    guraXInTiles = 0;
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Util.getKey(game.getConfig(Weatherlords.Config.RIGHT)))) {
            isColliding = false;
            int guraNextXInTiles = guraXInTiles + 1;
            if (guraNextXInTiles > 99) {
                guraNextXInTiles = 99;
            }
            TiledMapTile nextTile = collisionLayer.getCell(guraNextXInTiles, guraYInTiles).getTile();
            if (nextTile.getProperties().containsKey("isSolid")) {
                isColliding = nextTile.getProperties().get("isSolid", Boolean.class);
            }

            // Checks for collision before moving
            if (!isColliding) {
                guraSprite.translateX(tileWidth);
                guraXInTiles += 1;
                if (guraXInTiles > 99) {
                    guraXInTiles = 99;
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            System.out.println("(" + guraXInTiles + ", " + guraYInTiles + ")");
        }

        // Clamp the sprite to screen bounds
        guraSprite.setX(MathUtils.clamp(guraSprite.getX(), minX, maxX));
        guraSprite.setY(MathUtils.clamp(guraSprite.getY(), minY, maxY));

        // Calculate the maximum position of the camera in the world
        float maxCameraX = (float) ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth() - (camera.viewportWidth / 2);
        float maxCameraY = (float) ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight() - (camera.viewportHeight / 2);

        // Check if the sprite is within 1 tile of the boundary and move the camera accordingly
        if (camera.position.y < maxCameraY) {
            if (guraSprite.getY() >= maxY - tileHeight / 2) {
                camera.position.y += tileHeight;
                guraSprite.translateY(-tileHeight);
            }
        }
        if (camera.position.y > (camera.viewportHeight / 2)) {
            if (guraSprite.getY() <= minY + tileHeight / 2) {
                camera.position.y -= tileHeight;
                guraSprite.translateY(tileHeight);
            }
        }
        if (camera.position.x > (camera.viewportWidth / 2)) {
            if (guraSprite.getX() <= minX + tileWidth / 2) {
                camera.position.x -= tileWidth;
                guraSprite.translateX(tileWidth);
            }
        }
        if (camera.position.x < maxCameraX) {
            if (guraSprite.getX() >= maxX - tileWidth / 2) {
                camera.position.x += tileWidth;
                guraSprite.translateX(-tileWidth);
            }
        }

        // Update camera position if needed
        clampCameraPosition();
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);

        // Update camera and set view for map renderer
        camera.update();
        tiledMapRenderer.setView(camera);

        // Render the map
        tiledMapRenderer.render();
        game.viewport.apply();
        spriteBatch.setProjectionMatrix((game.viewport.getCamera().combined));
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
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
