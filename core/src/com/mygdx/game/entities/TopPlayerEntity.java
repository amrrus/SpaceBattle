package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;

/**
 * This is the body the user controls. It has to jump and don't die, like the title of the game
 * says. You can make it jump by touching the screen. Don't let the player touch any spike or
 * you will lose.
 */
public class TopPlayerEntity extends Actor {

    /** The player texture. */
    private Texture texture;

    /** The world instance this player is in. */
    private World world;

    /** The body for this player. */
    private Body body;

    /** The fixture for this player. */
    private Fixture fixture;

    /**
     * Is the player alive? If he touches a spike, is not alive. The player will only move and
     * jump if it's alive. Otherwise it is said that the user has lost and the game is over.
     */
    private boolean alive = true;

    private Vector2 oy;


    public TopPlayerEntity(World world, Texture texture, Vector2 position) {
        this.world = world;
        this.texture = texture;
        this.oy = new Vector2(0,1);

        // Create the player body.
        //BodyDef def = new BodyDef();                // (1) Create the body definition.
        //def.position.set(position);                 // (2) Put the body in the initial position.
        //def.type = BodyDef.BodyType.DynamicBody;    // (3) Remember to make it dynamic.

        //body = world.createBody(def);               // (4) Now create the body.

        // Give it some shape.
        //PolygonShape box = new PolygonShape();      // (1) Create the shape.
        //box.setAsBox(0.5f, 0.5f);                   // (2) 1x1 meter box.
        //fixture = body.createFixture(box, 3);       // (3) Create the fixture.
        //fixture.setUserData("player");              // (4) Set the user data.
       //box.dispose();
        // (5) Destroy the shape.

        setPosition(-0.5f*Constants.PIXELS_IN_METER,5f*Constants.PIXELS_IN_METER);
        // Set the size to a value that is big enough to be rendered on the screen.
        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //float alpha = oy.angle(new Vector2(getX(), getY()));
        //setRotation(alpha);
        //float x = (getX()+(0.5f* MathUtils.sinDeg(getRotation())))*Constants.PIXELS_IN_METER;
        //float y = (getY()-(0.5f*MathUtils.cosDeg(getRotation())))*Constants.PIXELS_IN_METER;
        //setPosition(x,y);
        batch.draw(texture, getX(), getY(),0,0, getWidth(), getHeight(),1f,1f,getRotation(),0,0,158,258,false,false);

    }


    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    // Getter and setter festival below here.

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setPosition(float x, float y,float alpha) {
        super.setPosition(x*Constants.PIXELS_IN_METER, y*Constants.PIXELS_IN_METER);
        this.setRotation(alpha);
    }
}

