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
/**
 * Created by patri on 08/03/2018.
 */

public class Asteroide extends Actor {

    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;

    public Asteroide(World world, Texture texture, Vector2 initialPosition, Vector2 impulse){
        this.world = world;
        this.texture = texture;

        BodyDef bodydef = new BodyDef();
        bodydef.position.set(initialPosition);
        bodydef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bodydef);

        CircleShape asteroideCircle = new CircleShape();
        asteroideCircle.getRadius();
        fixture = body.createFixture(asteroideCircle,3);
        fixture.setUserData("asteroide");

        body.applyLinearImpulse(impulse.x, impulse.y, getX(),getY(),  true);
        asteroideCircle.dispose();

        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
    }

    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * Constants.PIXELS_IN_METER,
                (body.getPosition().y - 0.5f) * Constants.PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);

    }

    //Por si se quiere hacer con un constructor con más parámetros
    //public Asteroide (World world, Float x, Float y, Vector2 impulse){
    //}

}
