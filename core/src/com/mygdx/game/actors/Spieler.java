package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;

import java.util.Random;

public class Spieler extends SpielObjekt{
    private Rectangle boundary;
    private float acceleration = 0.2f;
    private float speed = 4;
    private float direction = 0;
    private Animation<TextureRegion> animation;
    private float stateTime;
    private MyGdxGame game;
    public World world;
    public Body b2body;
    public enum State {FALLING, JUMPING, STANDING, RUNNING, DEAD};
    public State currentState;
    public State previousState;
    public float stateTimer;
    private boolean marioIsDead;
    public Spieler(int x, int y, TextureAtlas atlas, World world, MyGdxGame game) {
        super(x, y, atlas.findRegion("Armature_Idle").getTexture());
        generateAnimation(atlas);

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        this.world = world;
        defineSpieler();

        boundary = new Rectangle();
        this.setBoundary();

        setHeight(50);
        setWidth(50);
    }

    public void defineSpieler(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / game.PPM, 32 / game.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        // Define the vertices of the polygon (in meters)
        float[] vertices = new float[] {
                -6 / game.PPM, -8 / game.PPM,
                -6 / game.PPM, 8 / game.PPM,
                6 / game.PPM, 8 / game.PPM,
                6 / game.PPM, -8 / game.PPM
        };
        shape.set(vertices);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

    private void generateAnimation(TextureAtlas atlas) {
        animation = new Animation<>(0.1f, atlas.findRegions("Armature_Idle"), Animation.PlayMode.LOOP);
        stateTime = 0f;
    }

    public Rectangle getBoundary() {
        return boundary;
    }

    public void setBoundary(){
        this.boundary.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime);
        batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 0.4f, 0.4f, getRotation());
    }

    public void update(float delta){
        stateTime += delta;
        //setOrigin(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setPosition(b2body.getPosition().x - getWidth() / 2 + 10, b2body.getPosition().y - getHeight() / 2 + 17);
        currentState = getState();
    }

    public void act(float delta){
        super.act(delta);
        this.update(delta);
    }

    public float getStateTime() {
        return stateTime;
    }

    public State getState(){
        if(marioIsDead)
            return State.DEAD;
        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
            //if negative in Y-Axis mario is falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            //if mario is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
            //if none of these return then he must be standing
        else
            return State.STANDING;
    }

    public void jump(){
        if (currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 90f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public boolean collideRectangle(Rectangle shape) {
        if(Intersector.overlaps(this.boundary, shape)){
            return true;
        } else {
            return false;
        }
    }
}