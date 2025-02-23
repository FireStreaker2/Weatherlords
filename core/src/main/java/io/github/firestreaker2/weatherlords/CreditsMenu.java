package io.github.firestreaker2.weatherlords;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;

public class CreditsMenu implements Screen {
    final Weatherlords game;
    private final Stage stage;
    private final HashMap<String, String> credits = new HashMap<>();

    public CreditsMenu(final Weatherlords game) {
        this.game = game;
        stage = new Stage(new FitViewport(800, 480));

        // credits!
        credits.put("FireStreaker2", "Lead Developer");
        credits.put("OPWinner", "Lead Designer, Developer");
        credits.put("YeOldBeasts", "THE weatherlord");

        Label title = new Label("CREDITS", game.labelStyle);
        title.setColor(Color.BLACK);
        title.pack();
        title.setPosition(stage.getWidth() / 2 - title.getWidth() / 2, 400);

        StringBuilder creditString = new StringBuilder();
        for (String name : credits.keySet()) creditString.append(name).append(": ").append(credits.get(name)).append("\n");

        Label credits = new Label(creditString.toString(), game.labelStyle);
        credits.setColor(Color.BLACK);
        credits.pack();
        credits.setPosition(stage.getWidth() / 2 - credits.getWidth() / 2, 200);

        TextButton doneButton = new TextButton("DONE", game.skin);
        Util.addLowTaperFade(stage, game, doneButton, "MainMenu");
        doneButton.setPosition(stage.getWidth() / 2 - 100, 60);
        doneButton.setSize(200, 60);

        stage.addActor(game.background);
        stage.addActor(title);
        stage.addActor(credits);
        stage.addActor(doneButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Util.addLowTaperFade(stage, game, "MainMenu");
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        stage.dispose();
    }
}
