package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;


public class LimiterEntity extends Actor {

    /** The world instance this player is in. */
    private World world;

    /** The body for this player. */
    private Body body;

    /** The fixture for this player. */
    private Fixture fixture;

    private float sizeReductionFactor;


    public LimiterEntity(World world, Vector2 position) {
        this.world = world;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.StaticBody;

        body = world.createBody(def);

        CircleShape boxLeft = new CircleShape();
        boxLeft.setRadius(0.1f);
        fixture = body.createFixture(boxLeft, 1);
        boxLeft.dispose();

        sizeReductionFactor = 0.1f;

        // Set the size to a value that is big enough to be rendered on the screen.
        setSize(Constants.PIXELS_IN_METER*sizeReductionFactor, Constants.PIXELS_IN_METER*sizeReductionFactor);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - sizeReductionFactor/2) * Constants.PIXELS_IN_METER,
                (body.getPosition().y - sizeReductionFactor/2) * Constants.PIXELS_IN_METER);
    }


    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}

