package io.github.firestreaker2.weatherlords.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.firestreaker2.weatherlords.Weatherlords;

public class ControlsMenu extends Setting {
    private final Weatherlords.Config[] keybinds = {Weatherlords.Config.UP, Weatherlords.Config.DOWN, Weatherlords.Config.LEFT, Weatherlords.Config.RIGHT};
    private final Vector2[] positions = {
        new Vector2(100f, 175f),
        new Vector2(500f, 175f),
        new Vector2(100f, 275f),
        new Vector2(500f, 275f)
    };

    public ControlsMenu(Weatherlords game) {
        super(game);
    }

    @Override
    protected void content() {
        createLabel("CONTROLS");
        createButton("DONE", stage.getWidth() / 2 - 100, 60, "SettingsMenu");

        // keybinds
        int i = 0;
        for (Weatherlords.Config keybind : keybinds) {
            TextButton rebind = createButtonManual(String.format("%s: %s", keybind.name(), game.getConfig(keybind)), positions[i].x, positions[i].y);
            rebind.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    rebind.setText("PRESS BUTTON");
                    Gdx.input.setInputProcessor(new InputAdapter() {
                        @Override
                        public boolean keyDown(int keycode) {
                            game.editConfig(keybind, Input.Keys.toString(keycode));
                            Gdx.input.setInputProcessor(stage);
                            rebind.setText(String.format("%s: %s", keybind.name(), game.getConfig(keybind)));

                            return true;
                        }
                    });

                }
            });

            stage.addActor(rebind);
            i++;
        }
    }
}
