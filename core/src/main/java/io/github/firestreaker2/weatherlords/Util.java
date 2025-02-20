package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.firestreaker2.weatherlords.settings.ControlsMenu;
import io.github.firestreaker2.weatherlords.settings.GraphicsMenu;
import io.github.firestreaker2.weatherlords.settings.SoundsMenu;
import io.github.firestreaker2.weatherlords.settings.UserInterfaceMenu;

/**
 * util class
 */
public class Util {


    /**
     * addLowTaperFade
     * massive transition
     *
     * @param stage
     * @param game
     * @param button
     * @param screen
     */
    public static void addLowTaperFade(final Stage stage, final Weatherlords game, TextButton button, String screen) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(
                    Actions.fadeOut(0.1f), // low duration
                    Actions.run(() -> {
                        game.setScreen(convert(screen, game));
                        stage.dispose();
                    }),
                    Actions.fadeIn(0.1f)
                ));
            }
        });
    }

    /**
     * convert
     * <p>
     * converts string to class on the fly during
     * runtime so you dont have to reserve memory ahead of time
     * </p>
     *
     * @param screenName
     * @param game
     * @return
     */
    private static Screen convert(String screenName, Weatherlords game) {
        switch (screenName) {
            case "MainMenu":
                return new MainMenu(game);
            case "SettingsMenu":
                return new SettingsMenu(game);
            case "CreditsMenu":
                return new CreditsMenu(game);
            case "ControlsMenu":
                return new ControlsMenu(game);
            case "GraphicsMenu":
                return new GraphicsMenu(game);
            case "SoundsMenu":
                return new SoundsMenu(game);
            case "UserInterfaceMenu":
                return new UserInterfaceMenu(game);
            case "GameScreen":
                return new GameScreen(game);
            default:
                throw new IllegalArgumentException("Unknown screen: " + screenName);
        }
    }

    /**
     * getKey
     *
     * @param key
     * @return
     */
    public static int getKey(String key) {
        try {
            return (int) Input.Keys.class.getField(key.toUpperCase()).get(null);
        } catch (Exception e) {
            return -1;
        }
    }

}
