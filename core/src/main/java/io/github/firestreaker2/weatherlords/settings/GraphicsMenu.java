package io.github.firestreaker2.weatherlords.settings;

import io.github.firestreaker2.weatherlords.Weatherlords;

public class GraphicsMenu extends Setting {
    public GraphicsMenu(Weatherlords game) {
        super(game);
    }

    @Override
    protected void content() {
        createLabel("GRAPHICS");
        createLabel("COMING SOON", stage.getWidth() / 2 - 90, 250);
        createButton("DONE", stage.getWidth() / 2 - 100, 60, "SettingsMenu");
    }
}
