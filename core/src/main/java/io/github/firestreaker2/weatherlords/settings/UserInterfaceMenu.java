package io.github.firestreaker2.weatherlords.settings;

import io.github.firestreaker2.weatherlords.SettingsMenu;
import io.github.firestreaker2.weatherlords.Weatherlords;

public class UserInterfaceMenu extends Setting {
    public UserInterfaceMenu(Weatherlords game) {
        super(game);
    }

    @Override
    protected void content() {
        createLabel("USER INTERFACE");

        createLabel("FOV", stage.getWidth() / 2 - 20, 190);
        createSlider(stage.getWidth() / 2 - 100, 200, 0f, 2f, 0.1f, Weatherlords.Config.FOV);
        createButton("DONE", stage.getWidth() / 2 - 100, 60, () -> {
            game.setScreen(new SettingsMenu(game));
        });
    }
}
