package com.mygdx.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Feind extends SpielObjekt{
    private float acceleration = 0.1f;
    private float speed = 2;
    private Rectangle boundary;
    public Feind(int x, int y, Texture image) {
        super(x, y, image);
        boundary = new Rectangle();

        //Setze gleich den Delay
        this.setRandomPosition();
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
        speed += acceleration;
        this.setY((int)(this.getY()-4));
        //this.setY((int)(this.getY()-speed));

        if (this.getY() < 0){
            setRandomPosition();
        }
        this.setBoundary();
    }

    public void setRandomPosition() {
        Random r = new Random();

        int maxW = (int) (Gdx.graphics.getWidth() - this.getWidth());
        int minY = Gdx.graphics.getHeight() + (int) this.getHeight();
        int rx = (r.nextInt(maxW + 1 - 0) + 0);
        int ry = (r.nextInt(1500 + 1 - minY) + minY);
        Rectangle rect = new Rectangle(rx,ry,this.getWidth(),this.getHeight());


        this.setX(rx);
        this.setY(ry);
        this.setBoundary();
    }

    public boolean collideEnemies(ArrayList<Feind> feinde) {
        for (Feind feind : feinde) {
            if (feind != this && Intersector.overlaps(this.boundary, feind.getBoundary())) {
                return true;
            }
        }
        return false;
    }


    public void act(float delta){
        super.act(delta);
        this.update(delta);
    }
}