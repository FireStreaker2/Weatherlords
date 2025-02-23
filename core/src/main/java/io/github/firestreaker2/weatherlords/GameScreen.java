
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.lang.Math;

public class GameScreen extends InputAdapter implements Screen {
    final Weatherlords game;
    final List<String> logs = new ArrayList<>();
    final List<String> weather = new ArrayList<>();
    final HashMap<String, Double> weatherReturn = new HashMap<>();
    final HashMap<Vector2, List<Double>> backcountryFarmers = new HashMap<>();

    float elapsed = 0;
    int day = 1;
    int totalDaysPassed = 0;

    Texture pfp;
    Texture sideUITexture;
    Vector2 touchPos;
    SpriteBatch spriteBatch;
    Sprite guraSprite;
    Sprite sideUI;
    Sprite bottomUI;
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;
    int guraXInTiles;
    int guraYInTiles;
    boolean isColliding = false;

    int currency = 50;
    int farms = 0;

    public GameScreen(final Weatherlords game) {
        this.game = game;

        Gdx.input.setInputProcessor(this);

        String[] events = {"Sunny", "Sunny", "Sunny", "Rainy", "Rainy", "Rainy", "Tornado", "Drought", "Snowy", "Icy"};
        // pre-generate all weather events
        for (int i = 0; i < 30; i++) weather.add(events[(int) (Math.random() * events.length)]);

        // initialize weather return values
        weatherReturn.put("Sunny", 1.5);
        weatherReturn.put("Rainy", 2.0);
        weatherReturn.put("Tornado", 0.7);
        weatherReturn.put("Drought", 0.2);
        weatherReturn.put("Snowy", 0.3);
        weatherReturn.put("Icy", 0.4);

        touchPos = new Vector2();
        pfp = new Texture("Selector.png");
        sideUITexture = new Texture("SideUI.png");

        spriteBatch = new SpriteBatch();
        guraSprite = new Sprite(pfp);
        guraSprite.setSize(1, 1);
        sideUI = new Sprite(sideUITexture);
        bottomUI = new Sprite(sideUITexture);
        sideUI.setSize(2, 10);
        bottomUI.setSize(18, 2);

        camera = new OrthographicCamera();
        game.viewport = new FitViewport(18, 12);
        camera.setToOrtho(false, 18, 12);
        sideUI.setPosition(camera.viewportWidth - 2, 2);

        float unitScale = 1 / 16f;
        tiledMap = new TmxMapLoader().load("WorldMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
        MapLayer layers = tiledMap.getLayers().get("SpawnLayer");
        MapObject objectSpawn = layers.getObjects().get("SpawnPoint");
        float x = objectSpawn.getProperties().get("x", Float.class);
        float y = objectSpawn.getProperties().get("y", Float.class);

        guraXInTiles = (int) (x / 16);
        guraYInTiles = (int) (y / 16 - 2);

        camera.translate(x / 16f, y);

    }

    private void clampCameraPosition() {
        float cameraHalfWidth = camera.viewportWidth / 2;
        float cameraHalfHeight = camera.viewportHeight / 2;

        // Get map width and height in tiles
        int mapWidth = ((TiledMapTileLayer) tiledMap.getLayers().get("Background")).getWidth();
        int mapHeight = ((TiledMapTileLayer) tiledMap.getLayers().get("Background")).getHeight();

        // Clamp camera position to prevent going outside the map bounds
        camera.position.x = MathUtils.clamp(camera.position.x, cameraHalfWidth, (float) mapWidth - cameraHalfWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, cameraHalfHeight, (float) mapHeight - cameraHalfHeight);

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
        elapsed += delta;

        // TODO: set to `300` for actual game
        if (elapsed >= 1f) {
            day = (day + 1) % 30;
            if (day == 0) day = 1;
            elapsed = 0;

            totalDaysPassed++;

            logs.add("New day! Weather: " + weather.get(day));
        }

        draw();
        input();

        // escape keybind
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenu(game)); // TODO: save progress, replace with popup modals
        }
    }

    private void input() {
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("Background");
        float tileWidth = layer.getTileWidth() / 16f;
        float tileHeight = layer.getTileHeight() / 16f;
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Background");
        float minX = 0;
        float minY = 0;
        float maxX = game.viewport.getWorldWidth() - guraSprite.getWidth();
        float maxY = game.viewport.getWorldHeight() - guraSprite.getHeight();

        // movement
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

        /**
         * FARM STUFF
         *
         * return values are calculated via:
         * initial cost / 2 * weather factor
         *
         * 1 - farms (-20)
         * 2 - workshop (-50)
         * 3 - mine (-100)
         * 4 - big farm (-500)
         * 5 - big workshop (-1000)
         * 6 - big mine (-5000)
         * 7 - large farm (-10000)
         * 8 - large workshop (-50000)
         * 9 - large mine (-100000)
         * 0 - diamond mine (-500000)
         */
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) (tiledMap.getLayers().get("ground"))).getCell(guraXInTiles, guraYInTiles);
        TiledMapTileLayer.Cell belowCell = ((TiledMapTileLayer) (tiledMap.getLayers().get("ground"))).getCell(guraXInTiles, guraYInTiles - 1);
        TiledMapTileLayer farm = ((TiledMapTileLayer) (tiledMap.getLayers().get("farmLayer")));

        TiledMapTileLayer.Cell farmCell = farm.getCell(0, 1);
        TiledMapTileLayer.Cell farmCellBelow = farm.getCell(0, 0);
        TiledMapTileLayer.Cell workshopCell = farm.getCell(1, 0);
        TiledMapTileLayer.Cell mineCell = farm.getCell(2, 0);
        TiledMapTileLayer.Cell bigFarmCell = farm.getCell(8, 1);
        TiledMapTileLayer.Cell bigFarmCellBelow = farm.getCell(8, 0);
        TiledMapTileLayer.Cell bigWorkshopCell = farm.getCell(6, 0);
        TiledMapTileLayer.Cell bigMineCell = farm.getCell(3, 0);
        TiledMapTileLayer.Cell largeFarmCell = farm.getCell(9, 1);
        TiledMapTileLayer.Cell largeFarmCellBelow = farm.getCell(9, 0);
        TiledMapTileLayer.Cell largeWorkshopCell = farm.getCell(7, 0);
        TiledMapTileLayer.Cell largeMineCell = farm.getCell(4, 0);
        TiledMapTileLayer.Cell diamondMineCell = farm.getCell(5, 0);

        TiledMapTileLayer.Cell[] cells = {farmCell, workshopCell, mineCell, bigFarmCell, bigWorkshopCell, bigMineCell, largeFarmCell, largeWorkshopCell, largeMineCell, diamondMineCell};
        Integer[] cellIds = {
            farmCell.getTile().getId(),
            workshopCell.getTile().getId(),
            mineCell.getTile().getId(),
            bigFarmCell.getTile().getId(),
            bigWorkshopCell.getTile().getId(),
            bigMineCell.getTile().getId(),
            largeFarmCell.getTile().getId(),
            largeWorkshopCell.getTile().getId(),
            largeMineCell.getTile().getId(),
            diamondMineCell.getTile().getId()
        };
        int[] efficiency = {10, 25, 50, 250, 500, 2500, 5000, 25000, 50000, 250000};


        if (Gdx.input.isKeyJustPressed(Util.getKey(game.getConfig(Weatherlords.Config.TOUCH)))) {
            // messy logic but its ok
            if (cell != null) {
                int id = cell.getTile().getId();

                if (id >= 11 && id <= 27 && currency >= 10) {
                    currency -= 10;
                    cell.setTile(tiledMap.getTileSets().getTile(3));
                    logs.add("Removed obstacle: -10");
                } else if (Arrays.asList(cellIds).contains(id)) {
                    if (!backcountryFarmers.containsKey(new Vector2(guraXInTiles, guraYInTiles))) {
                        logs.add("Started farming!");

                        // pre-calculate currency return using weather
                        double total = 1;
                        currency -= efficiency[Arrays.asList(cellIds).indexOf(id)];
                        for (int i = 0; i < 8; i++) {
                            total *= weatherReturn.get(weather.get((day + i) % 30));
                        }
                        // [0] = return value
                        // [1] = day of return
                        double finalTotal = (float) Math.round(efficiency[Arrays.asList(cellIds).indexOf(id)] * total);
                        List<Double> values = new ArrayList<Double>() {{
                            add(finalTotal);
                            add((double) (totalDaysPassed + 10));
                        }};

                        backcountryFarmers.put(new Vector2(guraXInTiles, guraYInTiles), values);
                    } else {
                        Vector2 position = new Vector2(guraXInTiles, guraYInTiles);

                        if (totalDaysPassed >= backcountryFarmers.get(position).get(1)) {
                            double amount = backcountryFarmers.get(position).get(0);

                            currency += (int) Math.round(amount);
                            logs.add("Collected! +" + amount);

                            backcountryFarmers.remove(position);
                        }
                    }
                } else logs.add("Unable to interact");
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if (cell != null && belowCell != null) {
                int id = cell.getTile().getId();
                int belowId = belowCell.getTile().getId();

                if (id >= 1 && id <= 5 && belowId >= 1 && belowId <= 5 && currency >= 20) {
                    currency -= 20;
                    int tileId = farmCell.getTile().getId();
                    int belowTileId = farmCellBelow.getTile().getId();
                    cell.setTile(tiledMap.getTileSets().getTile(tileId));
                    belowCell.setTile(tiledMap.getTileSets().getTile(belowTileId));
                    logs.add("Placed farm! -20");
                    farms += 1;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (cell != null) {
                int id = cell.getTile().getId();

                if (id >= 1 && id <= 5 && currency >= 50) {
                    currency -= 50;
                    int tileId = workshopCell.getTile().getId();
                    cell.setTile(tiledMap.getTileSets().getTile(tileId));
                    logs.add("Placed workshop! -50");
                    farms += 1;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {

            if (cell != null) {
                int id = cell.getTile().getId();

                if (id >= 1 && id <= 5 && currency >= 100) {
                    currency -= 100;
                    int tileId = mineCell.getTile().getId();
                    cell.setTile(tiledMap.getTileSets().getTile(tileId));
                    logs.add("Placed mine! -100");
                    farms += 1;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            if (cell != null && belowCell != null) {
                int id = cell.getTile().getId();
                int belowId = belowCell.getTile().getId();

                if (id >= 1 && id <= 5 && belowId >= 1 && belowId <= 5 && currency >= 500) {
                    currency -= 20;
                    int tileId = bigFarmCell.getTile().getId();
                    int belowTileId = bigFarmCellBelow.getTile().getId();
                    cell.setTile(tiledMap.getTileSets().getTile(tileId));
                    belowCell.setTile(tiledMap.getTileSets().getTile(belowTileId));
                    logs.add("Placed big farm! -500");
                    farms += 1;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            if (cell != null) {
                int id = cell.getTile().getId();

                if (id >= 1 && id <= 5 && currency >= 1000) {
                    currency -= 1000;
                    int tileId = bigWorkshopCell.getTile().getId();
                    cell.setTile(tiledMap.getTileSets().getTile(tileId));
                    logs.add("Placed big workshop! -1000");
                    farms += 1;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            if (cell != null) {
                int id = cell.getTile().getId();

                if (id >= 1 && id <= 5 && currency >= 5000) {
                    currency -= 5000;
                    int tileId = bigMineCell.getTile().getId();
                    cell.setTile(tiledMap.getTileSets().getTile(tileId));
                    logs.add("Placed big mine! -5000");
                    farms += 1;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            if (cell != null && belowCell != null) {
                int id = cell.getTile().getId();
                int belowId = belowCell.getTile().getId();

                if (id >= 1 && id <= 5 && belowId >= 1 && belowId <= 5 && currency >= 10000) {
                    currency -= 10000;
                    int tileId = largeFarmCell.getTile().getId();
                    int belowTileId = largeFarmCellBelow.getTile().getId();
                    cell.setTile(tiledMap.getTileSets().getTile(tileId));
                    belowCell.setTile(tiledMap.getTileSets().getTile(belowTileId));
                    logs.add("Placed large farm! -10000");
                    farms += 1;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            if (cell != null) {
                int id = cell.getTile().getId();

                if (id >= 1 && id <= 5 && currency >= 50000) {
                    currency -= 50000;
                    int tileId = largeWorkshopCell.getTile().getId();
                    cell.setTile(tiledMap.getTileSets().getTile(tileId));
                    logs.add("Placed large workshop! -50000");
                    farms += 1;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            if (cell != null) {
                int id = cell.getTile().getId();

                if (id >= 1 && id <= 5 && currency >= 100000) {
                    currency -= 100000;
                    int tileId = largeMineCell.getTile().getId();
                    cell.setTile(tiledMap.getTileSets().getTile(tileId));
                    logs.add("Placed large mine! -100000");
                    farms += 1;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            if (cell != null) {
                int id = cell.getTile().getId();

                if (id >= 1 && id <= 5 && currency >= 500000) {
                    currency -= 500000;
                    int tileId = diamondMineCell.getTile().getId();
                    cell.setTile(tiledMap.getTileSets().getTile(tileId));
                    logs.add("Placed diamond mine! -500000");
                    farms += 1;
                }
            }
        }

        // Clamp the sprite to screen bounds
        guraSprite.setX(MathUtils.clamp(guraSprite.getX(), minX, maxX));
        guraSprite.setY(MathUtils.clamp(guraSprite.getY(), minY, maxY));

        // Calculate the maximum position of the camera in the world
        float maxCameraX = (float) ((TiledMapTileLayer) tiledMap.getLayers().get("Background")).getWidth() - (camera.viewportWidth / 2);
        float maxCameraY = (float) ((TiledMapTileLayer) tiledMap.getLayers().get("Background")).getHeight() - (camera.viewportHeight / 2);

        // Check if the sprite is within 1 tile of the boundary and move the camera accordingly
        if (camera.position.y < maxCameraY) {
            if (guraSprite.getY() >= maxY - tileHeight / 2) {
                camera.position.y += tileHeight;
                guraSprite.translateY(-tileHeight);
            }
        }
        if (camera.position.y > (camera.viewportHeight / 2)) {
            if (guraSprite.getY() <= minY + tileHeight * 2) {
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
            if (guraSprite.getX() >= maxX - tileWidth * 2) {
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
        sideUI.draw(spriteBatch);
        bottomUI.draw(spriteBatch);

        // logs
        if (logs.size() > 3) logs.remove(0);

        float y = 0.75f;
        for (int i = logs.size() - 1; i >= 0; i--) {
            game.gameFont.draw(spriteBatch, logs.get(i), 1f, y);
            y += 0.6f;
        }

        // 100% optimization
        String currencyString = Integer.toString(currency);
        if (currencyString.length() > 4) {
            String firstCurrencyString = currencyString.substring(0, 4);
            String secondCurrencyString = currencyString.substring(4);
            game.sideUIFont.draw(spriteBatch, "$" + firstCurrencyString, sideUI.getX() + 0.2f, sideUI.getHeight() + bottomUI.getHeight() - 0.5f);
            game.sideUIFont.draw(spriteBatch, secondCurrencyString, sideUI.getX() + 0.2f, sideUI.getHeight() + bottomUI.getHeight() - 1f);
        } else
            game.sideUIFont.draw(spriteBatch, "$" + currencyString, sideUI.getX() + 0.2f, sideUI.getHeight() + bottomUI.getHeight() - 0.5f);

        game.sideUIFont.draw(spriteBatch, "Farms:", sideUI.getX() + 0.2f, sideUI.getHeight() + bottomUI.getHeight() - 2f);
        game.sideUIFont.draw(spriteBatch, Integer.toString(farms), sideUI.getX() + 0.25f, sideUI.getHeight() + bottomUI.getHeight() - 2.5f);
        game.sideUIFont.draw(spriteBatch, "Day:", sideUI.getX() + 0.25f, sideUI.getHeight() + bottomUI.getHeight() - 3f);
        if (totalDaysPassed < 100000) {
            game.sideUIFont.draw(spriteBatch, totalDaysPassed + "", sideUI.getX() + 0.25f, sideUI.getHeight() + bottomUI.getHeight() - 3.5f);
        } else {
            String totalPassedDays = totalDaysPassed + "";
            String totalPassedDays1 = totalPassedDays.substring(0, 5);
            String totalPassedDays2 = totalPassedDays.substring(5);
            game.sideUIFont.draw(spriteBatch, totalPassedDays1, sideUI.getX() + 0.25f, sideUI.getHeight() + bottomUI.getHeight() - 3.5f);
            game.sideUIFont.draw(spriteBatch, totalPassedDays2, sideUI.getX() + 0.25f, sideUI.getHeight() + bottomUI.getHeight() - 4f);
        }
        game.weatherFont.draw(spriteBatch, "Weather:", sideUI.getX() + 0.2f, sideUI.getHeight() + bottomUI.getHeight() - 5f);
        float sideY = 5.5f;
        for (int i = day + 2; i < day + 9; i++) {
            int index = (i % 30);
            if (index == 0) index = 30;

            game.weatherFont.draw(spriteBatch, weather.get(index - 1), sideUI.getX() + 0.2f, sideUI.getHeight() + bottomUI.getHeight() - sideY);
            sideY += 0.5f;
        }

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
