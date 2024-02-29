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

import java.util.Random;

public class Spieler extends SpielObjekt{
    private Rectangle boundary;
    private float acceleration = 0.2f;
    private float speed = 4;
    private float direction = 0;
    private Animation<TextureRegion> animation;
    private float stateTime;

    public Spieler(int x, int y, TextureAtlas atlas) {
        super(x, y, atlas.findRegion("Armature_Idle").getTexture());
        generateAnimation(atlas);

        boundary = new Rectangle();
        this.setBoundary();

        setHeight(50);
        setWidth(50);
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
    }

    public void move(int direction) {

        if(direction != this.direction){
            speed = 2;
        }
        speed += acceleration;
        if(direction == 1) {
            this.setX(this.getX()-speed);
        } else{
            this.setX(this.getX()+speed);
        }
        //muss Grafikposition neu berechnen !!
        this.setBoundary();
        this.direction = direction;
    }

    public void act(float delta){
        super.act(delta);
        this.update(delta);
    }

    public float getStateTime() {
        return stateTime;
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