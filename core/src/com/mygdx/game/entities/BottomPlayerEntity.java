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

import Connections.Connection;
import io.socket.client.Socket;

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

    Connection conn;

    Integer idClient;

    public BottomPlayerEntity(World world, Texture texture, Vector2 position,Connection conn) {
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

        this.moveSign = 0;
        this.keepMoving = false;
        this.conn = conn;


        // Set the size to a value that is big enough to be rendered on the screen.
        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.setRotation(0.25f);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        if(!keepMoving && Gdx.input.justTouched()){
            if (Gdx.input.getX() <  (Gdx.graphics.getWidth()/2)) {
                moveSign = -1;
                keepMoving= true;
                //emit moveLeft event
                // System.out.println("moveSing:" +moveSign+", keepMoving"+keepMoving);
                conn.move(moveSign);

            }else{
                moveSign = 1;
                keepMoving= true;
                //emit moveRight event
                //System.out.println("moveSing:" +moveSign+", keepMoving"+keepMoving);
                conn.move(moveSign);
            }
        }
        if (keepMoving && !Gdx.input.isTouched()) {
            keepMoving = false;
            moveSign = 0;
            //emit stop move
            // System.out.println("moveSing:" +moveSign+", keepMoving"+keepMoving);
            conn.move(moveSign);
        }

    }


    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}

