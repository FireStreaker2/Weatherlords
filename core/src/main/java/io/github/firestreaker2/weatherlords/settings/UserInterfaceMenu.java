package io.github.firestreaker2.weatherlords.settings;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import io.github.firestreaker2.weatherlords.SettingsMenu;
import io.github.firestreaker2.weatherlords.Weatherlords;

public class UserInterfaceMenu extends Setting {
    public UserInterfaceMenu(Weatherlords game) {
        super(game);
    }

    @Override
    protected void content() {
        Label title = createLabel("USER INTERFACE");
        TextButton doneButton = createButton("DONE", stage.getWidth() / 2 - 100, 60, () -> {
            game.setScreen(new SettingsMenu(game));
        });

        // todo: add actual settings, edit global config

        stage.addActor(title);
        stage.addActor(doneButton);
    }
}
