package com.mygdx.game.entities;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;

public class ShotEntity extends Actor{

    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    private Vector2 defaultImpulse;
    private Boolean alreadyImpulsed;
    private float sizeReductionFactor;

    public ShotEntity(World world, Texture texture, Vector2 initialPosition, Vector2 impulse){
        this.world = world;
        this.texture = texture;

        BodyDef bodydef = new BodyDef();
        bodydef.position.set(initialPosition);
        bodydef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodydef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.1f, 1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 3;
        fixtureDef.filter.categoryBits = 16;
        fixtureDef.filter.groupIndex = -1;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData("shot");

        shape.dispose();


        alreadyImpulsed = false;
        defaultImpulse = impulse;


        sizeReductionFactor = 0.1f;

        setSize(Constants.PIXELS_IN_METER*sizeReductionFactor, Constants.PIXELS_IN_METER*sizeReductionFactor*10);
    }

    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - sizeReductionFactor/2) * Constants.PIXELS_IN_METER,
                (body.getPosition().y - sizeReductionFactor/2) * Constants.PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void act(float delta){
        if(!alreadyImpulsed){
            alreadyImpulsed = true;
            body.applyLinearImpulse(defaultImpulse, new Vector2(0,0), true);
        }
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
