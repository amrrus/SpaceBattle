package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.mygdx.game.Constants;


public class AsteroidEntity extends Actor {

    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    private Float radius;

    public AsteroidEntity(World world, Texture texture, Vector2 initialPosition, Vector2 impulse, Float radius){
        this.world = world;
        this.texture = texture;
        this.radius = radius;

        BodyDef bodydef = new BodyDef();
        bodydef.position.set(initialPosition);
        bodydef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodydef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        fixture = body.createFixture(circleShape,1);
        fixture.setFriction(0);
        fixture.setUserData("asteroid");

        body.applyLinearImpulse(impulse.x, impulse.y, getX(),getY(),  true);
        circleShape.dispose();

        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
    }

    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - radius) * Constants.PIXELS_IN_METER,
                (body.getPosition().y - radius) * Constants.PIXELS_IN_METER);
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
    //Por si se quiere hacer con un constructor con más parámetros
    //public Asteroide (World world, Float x, Float y, Vector2 impulse){
    //}

}
