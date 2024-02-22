package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actors.Spieler;
import com.mygdx.game.helper.ImageHelper;
import com.mygdx.game.scenes.Hud;

public class PlayScreen implements Screen {

    public SpriteBatch batch;
    private SettingsScreen settings;
    private Stage stage;
    private MyGdxGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    Spieler spieler;
    Texture img;

    private TextureAtlas atlas;
    private Animation<TextureRegion> animation;
    float elapsedTime = 0.0f;

    public PlayScreen(MyGdxGame game){
        this.game = game;
        batch = new SpriteBatch();
        //erstellt Kamera zum Folgen von Mario
        camera = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.WORLD_WIDTH,MyGdxGame.WORLD_HEIGHT,camera);
        hud = new Hud(game, game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Images/snow.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        //paul = new Texture("Images/paul.png");
        ImageHelper ih = new ImageHelper();
        spieler = new Spieler(0,0,new Texture("Images/player.png"));
        //spieler = new Spieler(0,0,paul );

        atlas = new TextureAtlas(Gdx.files.internal("Animations/player_Idle.atlas"));
        Array<TextureAtlas.AtlasRegion> frames = atlas.findRegions("Armature_Idle");
        animation = new Animation<>(0.1f,frames, Animation.PlayMode.LOOP);

        System.out.println(frames.size);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void handleInput(float dt){

        if(Gdx.input.isKeyPressed(Input.Keys.D))
            camera.position.x += 100 * dt;

        if(Gdx.input.isKeyPressed(Input.Keys.A) && camera.position.x > MyGdxGame.WORLD_WIDTH / 2 +1)
            camera.position.x += 100 * -dt;

        float delta = Gdx.graphics.getDeltaTime();
        //update
        if(spieler.getX() > 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) spieler.move(1);
        }
        if(spieler.getX() < 824) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) spieler.move(0);
        }

    }

    public void update(float dt) {
        if (game.isGameState()) {
            handleInput(dt);
            camera.update();
            renderer.setView(camera);
        }
    }


    @Override
    public void render(float delta) {
        delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);

        renderer.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //spieler.draw(batch, 1);
        batch.draw(currentFrame, 10, 0);
        batch.end();

        // Rendere das HUD
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }



    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
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
        batch.dispose();
        atlas.dispose();
    }
}
