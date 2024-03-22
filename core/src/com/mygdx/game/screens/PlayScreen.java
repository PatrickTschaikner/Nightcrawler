package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Tools.B2World;
import com.mygdx.game.actors.Spieler;
import com.mygdx.game.scenes.Hud;

public class PlayScreen implements Screen {

    private SpriteBatch batch;
    private MyGdxGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Hud hud;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Stage stage;
    private Spieler spieler;
    private boolean flying = false;
    private World world;
    private Box2DDebugRenderer b2dr;
    private int jumper;

    public PlayScreen(MyGdxGame game){
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT, camera);
        hud = new Hud(game, game.batch);

        // Load Tiled map
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("Tilemap/stone island.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / game.PPM);

        camera.position.set(viewport.getWorldWidth() / game.PPM, viewport.getWorldHeight() / game.PPM, 0);
        stage = new Stage(viewport);


        world = new World(new Vector2(0, -160), true);
        b2dr = new Box2DDebugRenderer();

        new B2World(world, map, game);

        // Create Spieler actor and add to stage
        spieler = new Spieler(0, 0, new TextureAtlas("Animations/unnamed.atlas"), world, game);
        stage.addActor(spieler);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void handleInput(float dt){

        if(jumper == 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                spieler.jump();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && spieler.b2body.getLinearVelocity().x >= -2) {
            spieler.b2body.applyLinearImpulse(new Vector2(-1000.0f, 0), spieler.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && spieler.b2body.getLinearVelocity().x <= 2) {
            spieler.b2body.applyLinearImpulse(new Vector2(1000f, 0), spieler.b2body.getWorldCenter(), true);
        }


        if(!flying && !Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                spieler.b2body.setLinearVelocity(new Vector2(0, 0));
        }
        float delta = Gdx.graphics.getDeltaTime();
    }

    public void update(float dt) {
        if (game.isGameState()) {
            handleInput(dt);

            world.step(1/60f, 6, 2);

            spieler.update(dt);

            hud.update(dt);

            if(spieler.currentState != Spieler.State.DEAD) {
                camera.position.x = spieler.b2body.getPosition().x;
            }

            camera.update();
            renderer.setView(camera);

            System.out.println("flying:" + flying);

            if(isPlayerOnGround()) {
                flying = false;
                jumper = 0;
            } else{
                jumper = 1;
                flying = true;
            }

            //Tod des Spielers
            tod();
        }
    }

    public boolean isPlayerOnGround() {
        // Definiere den Bereich des Spielers
        Rectangle playerBounds = spieler.getBoundary();

        // Iteriere über alle MapObjects in der Boden-Layer der Karte
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            // Überprüfe, ob der Spieler mit dem Boden kollidiert
            if(Intersector.overlaps(playerBounds, rect)){
                return true;
            }
        }
        return false;
    }


    @Override
    public void render(float delta) {
        update(delta);

        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the Tiled map
        renderer.render();

        // Render Box2D
        b2dr.render(world, camera.combined);

        // Render the stage (actors)
        stage.act();
        stage.draw();

        // Render HUD
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void tod() {
        if (spieler.getY() < -1) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    game.setScreen(new TitleScreen(game));
                }
            }, 1); // 1 Sekunden Verzögerung
        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}