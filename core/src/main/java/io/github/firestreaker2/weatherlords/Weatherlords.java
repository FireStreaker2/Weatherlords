package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * main handler for game
 * invokes main menu
 */
public class Weatherlords extends Game {
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

    // TODO: make more shared config + global mutator method

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
        skin = new Skin();
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

        this.setScreen(new MainMenu(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
