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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
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


    private World world;
    private Box2DDebugRenderer b2dr;

    public PlayScreen(MyGdxGame game){
        this.game = game;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.WORLD_WIDTH, MyGdxGame.WORLD_HEIGHT, camera);
        hud = new Hud(game, game.batch);

        // Load Tiled map
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("Tilemap/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / game.PPM);

        camera.position.set(viewport.getWorldWidth() / game.PPM, viewport.getWorldHeight() / game.PPM, 0);
        stage = new Stage(viewport);


        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() +  rect.getWidth() / 2) / game.PPM, (rect.getY() + rect.getHeight() / 2) / game.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / game.PPM, rect.getHeight() / 2 / game.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // Create Spieler actor and add to stage
        spieler = new Spieler(0, 0, new TextureAtlas("Animations/player_Idle.atlas"), world, game);
        stage.addActor(spieler);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void handleInput(float dt){

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            spieler.b2body.applyLinearImpulse(new Vector2(0, 10f), spieler.b2body.getWorldCenter(), true);

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

            world.step(1/60f, 6, 2);
            camera.update();
            renderer.setView(camera);
        }
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
    }
}