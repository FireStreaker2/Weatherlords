package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.EnumMap;

/**
 * main handler for game
 * invokes main menu
 */
public class Weatherlords extends Game {
    // actual game config
    // TODO: also store config in separate file to persist through sessions(?)
    private final EnumMap<Config, String> config = new EnumMap<>(Config.class); // TODO: actually use config in gameplay
    /**
     * universal config
     * access via `game.<config>`
     */
    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;
    public Skin skin;
    public Label.LabelStyle labelStyle;
    public TextButton.TextButtonStyle textButtonStyle;

    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        // messy font generator thing
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Cinzel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.size = 24;
        font = generator.generateFont(parameter);
        generator.dispose();
        font.setUseIntegerPositions(false);

        // shared skin
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        skin.add("default-font", font);
        // use `linear` for clean, `nearest` for pixelated
        skin.getFont("default-font").getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // label style
        labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");

        // text button style
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.fontColor = Color.WHITE;

        Drawable buttonBackground = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("button.png"))));
        textButtonStyle.up = buttonBackground;
        textButtonStyle.down = ((TextureRegionDrawable) buttonBackground).tint(Color.DARK_GRAY);
        textButtonStyle.over = ((TextureRegionDrawable) buttonBackground).tint(Color.LIGHT_GRAY);

        skin.add("default", textButtonStyle);

        // init default game config
        config.put(Config.UP, "W");
        config.put(Config.LEFT, "A");
        config.put(Config.DOWN, "S");
        config.put(Config.RIGHT, "D");
        config.put(Config.VOLUME, "1"); // 0f-1f
        config.put(Config.FOV, "1"); // 0f-2f

        this.setScreen(new MainMenu(this));
    }

    /**
     * getConfig
     *
     * @param key
     * @return
     */
    public String getConfig(Config key) {
        return config.getOrDefault(key, "");
    }

    /**
     * editConfig
     *
     * @param key
     * @param value
     */
    public void editConfig(Config key, String value) {
        if (config.containsKey(key)) config.put(key, value);
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    // global config enum
    public enum Config {
        UP, DOWN, LEFT, RIGHT, VOLUME, FOV
    }
}
