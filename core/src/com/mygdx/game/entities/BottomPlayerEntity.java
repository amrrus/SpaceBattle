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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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

    /** The fixture for this player. */
    private Fixture fixture;

    private Joint joint;

    private EntityFactory factory;

    private int timerShot;
    private int numOfShots;

    public BottomPlayerEntity(World world, Texture texture, Vector2 position, EntityFactory factory) {
        this.world = world;
        this.texture = texture;
        this.factory = factory;

        this.timerShot = 0;
        this.numOfShots = 0;

        /** Player */

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = false;

        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 3;
        fixtureDef.filter.categoryBits = 18;
        fixtureDef.filter.groupIndex = -1;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData("player");
        box.dispose();

        /** Joint */

        BodyDef defCenter = new BodyDef();
        defCenter.position.set(new Vector2(0,0));
        defCenter.type = BodyDef.BodyType.StaticBody;

        bodyCenter = world.createBody(defCenter);

        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = body;
        distanceJointDef.bodyB = bodyCenter;
        distanceJointDef.length = 5.5f;

        joint = world.createJoint(distanceJointDef);

        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        /** TODO: MEJORAR LA FORMA EN LA QUE SE CREAN LOS DISPAROS */
        if(timerShot>=30) {
            ShotEntity shot = factory.createShot(world, body.getPosition(), new Vector2(0,0).sub(body.getPosition()));
            getStage().addActor(shot);
            numOfShots++;
            timerShot=0;
        }else{
            timerShot++;
        }

        setPosition((body.getPosition().x - 0.5f) * Constants.PIXELS_IN_METER,
                (body.getPosition().y - 0.5f) * Constants.PIXELS_IN_METER);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isTouched()) {
            int moveSign = 1; // right is the default movement
            if (Gdx.input.getX() <  (Gdx.graphics.getWidth()/2)) {
                moveSign = -1;
            }

            move(moveSign);
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

