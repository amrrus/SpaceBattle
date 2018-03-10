package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;

import java.util.List;

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

    private Body bodyCenter;

    private Body bodyLimRight;

    private Body bodyLimLeft;

    /** The fixture for this player. */
    private Fixture fixture;

    private Fixture fixtureLimRight;

    private Fixture fixtureLimLeft;

    private Joint joint;

    Integer moveSign;

    Boolean keepMoving;

    public BottomPlayerEntity(World world, Texture texture, Vector2 position) {
        this.world = world;
        this.texture = texture;

        // Create the player body.
        BodyDef def = new BodyDef();                // (1) Create the body definition.
        def.position.set(position);                 // (2) Put the body in the initial position.
        def.type = BodyDef.BodyType.DynamicBody;    // (3) Remember to make it dynamic.

        body = world.createBody(def);               // (4) Now create the body.

        // Create the player body.
        BodyDef defCenter = new BodyDef();                // (1) Create the body definition.
        defCenter.position.set(new Vector2(0,0));                 // (2) Put the body in the initial position.
        defCenter.type = BodyDef.BodyType.StaticBody;    // (3) Remember to make it dynamic.

        bodyCenter = world.createBody(defCenter);               // (4) Now create the body.

        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = body;
        distanceJointDef.bodyB = bodyCenter;
        distanceJointDef.length = 5.5f;

        joint = world.createJoint(distanceJointDef);

        BodyDef defLimLeft = new BodyDef();                // (1) Create the body definition.
        defLimLeft.position.set(new Vector2(-5.5f,0));                 // (2) Put the body in the initial position.
        defLimLeft.type = BodyDef.BodyType.StaticBody;

        bodyLimLeft = world.createBody(defLimLeft);

        BodyDef defLimRight = new BodyDef();                // (1) Create the body definition.
        defLimRight.position.set(new Vector2(5.5f,0));                 // (2) Put the body in the initial position.
        defLimRight.type = BodyDef.BodyType.StaticBody;

        bodyLimRight = world.createBody(defLimRight);

        /** LEFT LIMIT*/
        // Give it some shape.
        PolygonShape boxLeft = new PolygonShape();      // (1) Create the shape.
        boxLeft.setAsBox(0.01f, 0.01f);                   // (2) 1x1 meter box.
        fixtureLimLeft = bodyLimLeft.createFixture(boxLeft, 3);       // (3) Create the fixture.
        boxLeft.dispose();                              // (5) Destroy the shape.


        /** RIGTH LIMIT*/
        // Give it some shape.
        PolygonShape boxRight = new PolygonShape();      // (1) Create the shape.
        boxLeft.setAsBox(0.01f, 0.01f);                   // (2) 1x1 meter box.
        fixtureLimRight = bodyLimRight.createFixture(boxRight, 3);       // (3) Create the fixture.
        boxRight.dispose();                              // (5) Destroy the shape.


        // Give it some shape.
        PolygonShape box = new PolygonShape();      // (1) Create the shape.
        box.setAsBox(0.5f, 0.5f);                   // (2) 1x1 meter box.
        fixture = body.createFixture(box, 3);       // (3) Create the fixture.
        fixture.setUserData("player");              // (4) Set the user data.
        box.dispose();                              // (5) Destroy the shape.

        moveSign=0;
        keepMoving=false;


        // Set the size to a value that is big enough to be rendered on the screen.
        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        if(!keepMoving && Gdx.input.justTouched()){
            if (Gdx.input.getX() <  (Gdx.graphics.getWidth()/2)) {
                moveSign = -1;
                keepMoving= true;
                //emit moveLeft event
            }else{
                moveSign = 1;
                keepMoving= true;
                //emit moveRight event
            }

        }
        if (keepMoving && !Gdx.input.isTouched()) {
            keepMoving = false;
            moveSign = 0;
            //emit stop move
        }

    }

    public void move(int moveSign){
        body.setLinearVelocity(Constants.IMPULSE_PLAYER*moveSign,0);
    }


    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}

