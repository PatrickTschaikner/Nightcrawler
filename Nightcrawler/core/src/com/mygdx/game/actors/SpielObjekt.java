package com.mygdx.game.actors;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class SpielObjekt extends Actor {

    protected Sprite image;

    public abstract void draw(Batch b, float parentAlpha);
    public void act(float dt){
        super.act(dt);
        this.image.setX(this.getX());
        this.image.setY(this.getY());
    }

    public SpielObjekt(int x, int y, Texture image) {
        super();
        this.setX(x);
        this.setY(y);
        this.image = new Sprite(image);
        this.setWidth(image.getWidth());
        this.setHeight(image.getHeight());
        this.image.setX(x);
        this.image.setY(y);
    }

    public Sprite getImage( ){return this.image;}

}