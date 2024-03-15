package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;

public class B2World {
    public B2World(World world, TiledMap map, MyGdxGame game){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        Body groundBody = world.createBody(bdef);
        PolygonShape groundshape = new PolygonShape();
        groundshape.setAsBox(30, 1.0f);
        FixtureDef groundFixture = new FixtureDef();
        groundFixture.density=0.0f;
        groundFixture.shape = groundshape;
        groundFixture.restitution = .5f;
        groundFixture.friction=0f;
        groundBody.createFixture(groundFixture);
        groundshape.dispose();


        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() +  rect.getWidth() / 2) / game.PPM, (rect.getY() + rect.getHeight() / 2) / game.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / game.PPM, rect.getHeight() / 2 / game.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }

}
