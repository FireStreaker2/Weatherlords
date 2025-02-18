package io.github.firestreaker2.weatherlords.settings;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import io.github.firestreaker2.weatherlords.SettingsMenu;
import io.github.firestreaker2.weatherlords.Weatherlords;

public class GraphicsMenu extends Setting {
    public GraphicsMenu(Weatherlords game) {
        super(game);
    }

    @Override
    protected void content() {
        Label title = createLabel("GRAPHICS");
        TextButton backButton = createButton("BACK", 300, 200, () -> {
            game.setScreen(new SettingsMenu(game));
        });

        // todo: add actual settings, edit global config

        stage.addActor(title);
        stage.addActor(backButton);
    }
}
