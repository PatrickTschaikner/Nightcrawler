package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;


import java.awt.*;
import java.util.logging.Level;

public class Hud {
    public Stage stage;
    private Viewport viewPort;

    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    private MyGdxGame game;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label nameLabel;

    public  Hud(MyGdxGame game, SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewPort = new FitViewport(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        BitmapFont font = new BitmapFont(Gdx.files.internal("Fonts/Font.fnt"));

        // Erstellen eines neuen Label-Stils mit der benutzerdefinierten Schriftart
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE; // Farbe anpassen, falls erforderlich

        countdownLabel = new Label(String.format("%03d", worldTimer), labelStyle);
        countdownLabel.setFontScale(0.2f);
        scoreLabel = new Label(String.format("%06d", score), labelStyle);
        scoreLabel.setFontScale(0.2f);
        /*timeLabel = new Label("TIME", labelStyle);
        timeLabel.setFontScale(0.2f);
        levelLabel = new Label("Level 1", labelStyle);
        levelLabel.setFontScale(0.2f);
        worldLabel = new Label("WORLD", labelStyle);
        worldLabel.setFontScale(0.2f);
        nameLabel = new Label("Score", labelStyle);
        nameLabel.setFontScale(0.2f);*/

        //table.add(nameLabel).expandX().padTop(1);
        //table.add(levelLabel).expandX().padTop(1);
        table.add(countdownLabel).expandX().padTop(1);
        table.row();
        table.add(scoreLabel).expandX().padTop(1);


        stage.addActor(table);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                // Timer-Logik hier einfügen
                if(game.isGameState()){
                    worldTimer--;
                }
                countdownLabel.setText(String.format("%03d", worldTimer));

                if (worldTimer <= 0) {
                    // Timer stoppen oder weitere Aktionen ausführen, wenn gewünscht
                    // Timer.instance().clear();
                    // z.B.: System.out.println("Time's up!");
                }
            }
        }, 1, 1);
    }

    public Integer getWorldTimer() {
        return worldTimer;
    }

    public void setWorldTimer(Integer worldTimer) {
        this.worldTimer = worldTimer;
    }
}

