package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.MyGdxGame;

import javax.swing.event.ChangeEvent;

public class SettingsScreen implements Screen {


    //Hallo
    private Stage stage;
    private MyGdxGame game;
    private String music = "";
    private boolean musicState;
    private Slider volumeSlider;

    public SettingsScreen(MyGdxGame aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());


        Label title = new Label("Settings", MyGdxGame.gameSkin,"big-black");
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight()-100);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        musicState = aGame.music.isPlaying();

        TextButton musicButton = new TextButton("Musik " + (musicState ? "ON" : "OFF"), MyGdxGame.gameSkin);
        musicButton.setWidth(Gdx.graphics.getWidth()/2);
        musicButton.setPosition(Gdx.graphics.getWidth()/2-musicButton.getWidth()/2,Gdx.graphics.getHeight()-360);
        musicButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                musicState = !musicState;
                musicButton.setText("Musik " + (musicState ? "ON" : "OFF"));

                if(aGame.music.isPlaying()) {
                    aGame.pauseMusic();
                } else{
                    aGame.startMusic();
                }
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(musicButton);


        TextButton backButton = new TextButton("Back",MyGdxGame.gameSkin);
        backButton.setWidth(Gdx.graphics.getWidth()/2);
        backButton.setPosition(Gdx.graphics.getWidth()/2-backButton.getWidth()/2,Gdx.graphics.getHeight()-470);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(backButton);

        Label volume = new Label("Volume", MyGdxGame.gameSkin,"big-black");
        volume.setAlignment(Align.center);
        volume.setY(Gdx.graphics.getHeight()-230);
        volume.setWidth(Gdx.graphics.getWidth());
        stage.addActor(volume);

        //Slider
        volumeSlider = new Slider(0f, 1f, 0.1f, false, MyGdxGame.gameSkin, "default-horizontal");
        volumeSlider.setValue(aGame.music.getVolume()); // Setze den aktuellen Lautstärkewert
        volumeSlider.setWidth(Gdx.graphics.getWidth() / 2);
        volumeSlider.setPosition(Gdx.graphics.getWidth() / 2 - volumeSlider.getWidth() / 2, Gdx.graphics.getHeight() - 250);
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = volumeSlider.getValue();
                // Setze die Lautstärke der Musik entsprechend dem Slider-Wert
                aGame.music.setVolume(volume);
            }
        });
        stage.addActor(volumeSlider);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
