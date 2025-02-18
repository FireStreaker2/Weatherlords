package io.github.firestreaker2.weatherlords.settings;

import io.github.firestreaker2.weatherlords.SettingsMenu;
import io.github.firestreaker2.weatherlords.Weatherlords;

public class SoundsMenu extends Setting {
    public SoundsMenu(Weatherlords game) {
        super(game);
    }

    @Override
    protected void content() {
        createLabel("SOUNDS");
        createSlider(stage.getWidth() / 2 - 100, 200, 0f, 1f, 0.01f, Weatherlords.Config.VOLUME);
        createButton("DONE", stage.getWidth() / 2 - 100, 60, () -> {
            game.setScreen(new SettingsMenu(game));
        });
    }
}
