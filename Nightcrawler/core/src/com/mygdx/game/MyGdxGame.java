package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.scenes.TitleScreen;
import com.mygdx.game.screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MyGdxGame extends Game {
	public static final int WORLD_WIDTH = 200;
	public static final int WORLD_HEIGHT = 104;
	public static Music music;

	static public Skin gameSkin;

	public SpriteBatch batch;
	private boolean toggleMusic;
	private boolean gameState = true;
	private Hud hud;

	@Override
	public void create() {

			MyGdxGame.music = Gdx.audio.newMusic(Gdx.files.internal("sounds/background.mp3"));
			MyGdxGame.music.setLooping(true);
			MyGdxGame.music.play();

		batch = new SpriteBatch();

		gameSkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
		setScreen(new TitleScreen(this));
	}

	@Override
	public void render() {
		pauseGame();
		super.render();
	}

	public void pauseGame() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			gameState = !gameState;
		}
	}

	public void startMusic() {
		music.play();
	}

	public void pauseMusic() {
		music.pause();
	}

	public boolean isGameState() {
		return gameState;
	}

	public void setGameState(boolean gameState) {
		this.gameState = gameState;
	}

	public static Music getMusic() {
		return music;
	}

	public static void setMusic(Music music) {
		MyGdxGame.music = music;
	}

	@Override
	public void dispose() {
		batch.dispose();
		music.dispose();
	}

}
