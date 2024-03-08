package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.screens.TitleScreen;

public class MyGdxGame extends Game {
	public static final int WORLD_WIDTH = 200;
	public static final int WORLD_HEIGHT = 104;
	public static final float PPM = 1;
	public static Music music;
	public static Music music2;

	static public Skin gameSkin;

	public SpriteBatch batch;
	private boolean toggleMusic;
	private boolean gameState = true;
	private Hud hud;

	@Override
	public void create() {
			MyGdxGame.music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Nightcrawler.mp3"));
			MyGdxGame.music.setLooping(true);
			MyGdxGame.music.play();
			MyGdxGame.music.pause();

			MyGdxGame.music2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/Butterfly.mp3"));
			MyGdxGame.music2.setLooping(true);
			MyGdxGame.music2.play();

		batch = new SpriteBatch();

		gameSkin = new Skin(Gdx.files.internal("skin/glassy/glassy-ui.json"));
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

	public void startMusic2() {
		music2.play();
	}

	public void pauseMusic2() {
		music2.pause();
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
