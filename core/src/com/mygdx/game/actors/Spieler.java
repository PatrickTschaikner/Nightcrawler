package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Spieler extends SpielObjekt{
    private Rectangle boundary;
    private float acceleration = 0.2f;
    private float speed = 4;
    private float direction = 0;
    public Spieler(int x, int y, Texture image) {
        super(x, y, image);
        boundary = new Rectangle();
        this.setBoundary();
    }

    public Rectangle getBoundary() {
        return boundary;
    }

    public void setBoundary(){
        this.boundary.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void draw(Batch b, float parentAlpha) {
        this.getImage().draw(b);
    }

    public void update(float delta){

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

    public boolean collideRectangle(Rectangle shape) {
        if(Intersector.overlaps(this.boundary, shape)){
            return true;
        } else {
            return false;
        }
    }
}