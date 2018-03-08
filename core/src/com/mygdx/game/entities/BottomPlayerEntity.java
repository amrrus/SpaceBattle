package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
public class BottomPlayerEntity extends Actor {

    /** The player texture. */
    private Texture texture;

    /** The world instance this player is in. */
    private World world;

    /** The body for this player. */
    private Body body;

    /** The fixture for this player. */
    private Fixture fixture;

    public BottomPlayerEntity(World world, Texture texture, Vector2 position) {
        this.world = world;
        this.texture = texture;

        // Create the player body.
        BodyDef def = new BodyDef();                // (1) Create the body definition.
        def.position.set(position);                 // (2) Put the body in the initial position.
        def.type = BodyDef.BodyType.DynamicBody;    // (3) Remember to make it dynamic.

        body = world.createBody(def);               // (4) Now create the body.

        // Give it some shape.
        PolygonShape box = new PolygonShape();      // (1) Create the shape.
        box.setAsBox(0.5f, 0.5f);                   // (2) 1x1 meter box.
        fixture = body.createFixture(box, 3);       // (3) Create the fixture.
        fixture.setUserData("player");              // (4) Set the user data.
        box.dispose();                              // (5) Destroy the shape.


        // Set the size to a value that is big enough to be rendered on the screen.
        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Always update the position of the actor when you are going to draw it, so that the
        // position of the actor on the screen is as accurate as possible to the current position
        // of the Box2D body.
        setPosition((body.getPosition().x - 0.5f) * Constants.PIXELS_IN_METER,
                (body.getPosition().y - 0.5f) * Constants.PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.justTouched() || Gdx.input.isTouched()) {
            int moveSign = 1; // move right is the default movement
            if (Gdx.input.getX() < Constants.WIDTH_SCREEN / 2) {
                moveSign = -1;
            }
            move(moveSign);
        }
    }

    public void move(int moveSign){
        Vector2 position = body.getPosition();

        if(! (body.getLinearVelocity().x + Constants.IMPULSE_PLAYER*moveSign > Constants.MAX_PLAYER_SPEED || body.getLinearVelocity().x + Constants.IMPULSE_PLAYER*moveSign < -Constants.MAX_PLAYER_SPEED)){
            body.applyLinearImpulse(Constants.IMPULSE_PLAYER*moveSign, 0, position.x, position.y, true);
        }
    }


    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}

