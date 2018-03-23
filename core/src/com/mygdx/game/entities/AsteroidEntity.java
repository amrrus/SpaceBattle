package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

        FixtureDef fds = new FixtureDef();
        fds.shape = circleShape;
        fds.density = Constants.ASTEROID_DENSITY;
        fds.friction = 0f;
        fds.filter.categoryBits=1;
        fds.filter.maskBits=4;
        fds.filter.groupIndex = 0;

        fixture = body.createFixture(fds);
        fixture.setUserData("asteroid");

        body.setLinearVelocity(impulse);
        circleShape.dispose();

        setSize(2 * this.radius * Constants.PIXELS_IN_METER,
                2 * this.radius * Constants.PIXELS_IN_METER);
        //TODO: método ramdon en los asteroides en una clase Utils
        //TODO: Que no se pisen los asteroides cuando salga uno no puede salir otro en la misma posicion o en el rango donde esté el vector de impulso
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

}
