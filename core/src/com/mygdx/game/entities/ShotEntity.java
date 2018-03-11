package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;


public class ShotEntity extends Actor {

    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    private Integer idClient;

    public ShotEntity(World world, Texture texture, Vector2 initialPosition, Vector2 impulse, Integer idClient){
        this.world = world;
        this.texture = texture;
        this.idClient = idClient;

        BodyDef bodydef = new BodyDef();
        bodydef.position.set(initialPosition);
        bodydef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodydef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(Constants.SHOT_RADIUS);
        fixture = body.createFixture(circleShape,Constants.SHOT_DENSITY);
        fixture.setFriction(0);
        fixture.setUserData("shot");

        body.applyLinearImpulse(impulse.x, impulse.y, getX(),getY(),  true);
        circleShape.dispose();

        setSize(2*Constants.SHOT_RADIUS*Constants.PIXELS_IN_METER,
                2*Constants.SHOT_RADIUS*Constants.PIXELS_IN_METER);
    }

    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - Constants.SHOT_RADIUS) * Constants.PIXELS_IN_METER,
                (body.getPosition().y - Constants.SHOT_RADIUS) * Constants.PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);

    }

    public Body getBody(){
        return this.body;
    }

    public World getWorld(){
        return this.world;
    }

    public Integer getIdClient(){
        return this.idClient;
    }
    //Por si se quiere hacer con un constructor con más parámetros
    //public Asteroide (World world, Float x, Float y, Vector2 impulse){
    //}

}
