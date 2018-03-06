package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.entities.BothEntity;
import com.mygdx.game.entities.EntityFactory;
import com.mygdx.game.entities.FloorEntity;
import com.mygdx.game.entities.PlayerEntity;
import com.mygdx.game.entities.SpikeEntity;


import io.socket.client.IO;
import io.socket.client.Socket;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;




/**
 * This is the main screen for the game. All the fun happen here.
 */
public class GameScreen extends BaseScreen {

    /** Stage instance for Scene2D rendering. */
    private Stage stage;

    /** World instance for Box2D engine. */
    private World world;

    /** Player entity. */
    private PlayerEntity player;

    private BothEntity both;

    /** List of floors attached to this level. */
    private List<FloorEntity> floorList = new ArrayList<FloorEntity>();

    /** List of spikes attached to this level. */
    private List<SpikeEntity> spikeList = new ArrayList<SpikeEntity>();

    /** Jump sound that has to play when the player jumps. */
    private Sound jumpSound;

    /** Die sound that has to play when the player collides with a spike. */
    private Sound dieSound;

    /** Background music that has to play on the background all the time. */
    private Music backgroundMusic;

    /** Initial position of the camera. Required for reseting the viewport. */
    private Vector3 position;

    public Socket mSocket;

    private Texture backgroundTexture;
    public SpriteBatch batch;

    /**
     * Create the screen. Since this constructor cannot be invoked before libGDX is fully started,
     * it is safe to do critical code here such as loading assets and setting up the stage.
     * @param game
     */
    public GameScreen(MainGame game) {
        super(game);

        // Create a new Scene2D stage for displaying things.
        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGTH_SCREEN));
        position = new Vector3(stage.getCamera().position);
        backgroundTexture = game.getManager().get("dividedPlanet.png");
        batch = new SpriteBatch();

        //sprite.setPosition(-328,-365 );--------
        //sprite.setSize(640,640);------




        // Create a new Box2D world for managing things.
        //world = new World(new Vector2(0, -10), true);
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new GameContactListener());

        // Get the sound effect references that will play during the game.
        jumpSound = game.getManager().get("audio/jump.ogg");
        dieSound = game.getManager().get("audio/die.ogg");
        backgroundMusic = game.getManager().get("audio/song.ogg");

        try {
            mSocket = IO.socket("http://217.216.84.31:3000/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * This method will be executed when this screen is about to be rendered.
     * Here, I use this method to set up the initial position for the stage.
     */
    @Override
    public void show() {
        mSocket.connect();
        EntityFactory factory = new EntityFactory(game.getManager());

        // Create the player. It has an initial position.
        player = factory.createPlayer(world, new Vector2(0f, -7.5f));
        both = factory.createBoth(world, new Vector2(0f, 7.5f));
/*
        // Create the player body.
        BodyDef def = new BodyDef();                // (1) Create the body definition.
        def.position.set(0,0);                 // (2) Put the body in the initial position.
        def.type = BodyDef.BodyType.DynamicBody;    // (3) Remember to make it dynamic.
        Body cuerpo = world.createBody(def);               // (4) Now create the body.
        // Give it some shape.
        CircleShape circle = new CircleShape();
        circle.setRadius(6f);                   // (2) 1x1 meter box.
        Fixture fixture = cuerpo.createFixture(circle, 99);       // (3) Create the fixture.
        fixture.setUserData("circle");              // (4) Set the user data.
        circle.dispose();                              // (5) Destroy the shape.
        fixture.setFriction(0f);
*/


        // This is the main floor. That is why is so long.
        //floorList.add(factory.createFloor(world, -Constants.WIDTH_SCREEN/(Constants.PIXELS_IN_METER*2),
        //        Constants.WIDTH_SCREEN/Constants.PIXELS_IN_METER, -6));

        //floorList.add(factory.createFloor(world, Constants.WIDTH_SCREEN/(Constants.PIXELS_IN_METER*2),
        //        Constants.WIDTH_SCREEN/Constants.PIXELS_IN_METER, 0));
        // Now generate some floors over the main floor. Needless to say, that on a real game
        // this should be better engineered. For instance, have all the information for floors
        // and spikes in a data structure or even some level file and generate them without
        // writing lines of code.
        //floorList.add(factory.createFloor(world, 0, 10, 6));
        //floorList.add(factory.createFloor(world, 30, 8, 2));

        // Generate some spikes too.
        //spikeList.add(factory.createSpikes(world, 8, 1));
        //spikeList.add(factory.createSpikes(world, 23, 2));
        //spikeList.add(factory.createSpikes(world, 35, 2));
        //spikeList.add(factory.createSpikes(world, 50, 1));

        // All add the floors and spikes to the stage.
        for (FloorEntity floor : floorList)
            stage.addActor(floor);
        for (SpikeEntity spike : spikeList)
            stage.addActor(spike);

        // Add the player to the stage too.
        stage.addActor(player);
        stage.addActor(both);

        // Reset the camera to the left. This is required because we have translated the camera
        // during the game. We need to put the camera on the initial position so that you can
        // use it again if you replay the game.
        stage.getCamera().position.set(0f,0f,0f);
        stage.getCamera().update();


        // Everything is ready, turn the volume up.
        //backgroundMusic.setVolume(0.75f);
        //backgroundMusic.play();


    }

    /**
     * This method will be executed when this screen is no more the active screen.
     * I use this method to destroy all the things that have been used in the stage.
     */
    @Override
    public void hide() {
        mSocket.disconnect();
        // Clear the stage. This will remove ALL actors from the stage and it is faster than
        // removing every single actor one by one. This is not shown in the video but it is
        // an improvement.
        stage.clear();

        // Detach every entity from the world they have been living in.
        player.detach();
        for (FloorEntity floor : floorList)
            floor.detach();
        for (SpikeEntity spike : spikeList)
            spike.detach();

        // Clear the lists.
        floorList.clear();
        spikeList.clear();

    }

    /**
     * This method is executed whenever the game requires this screen to be rendered. This will
     * display things on the screen. This method is also used to update the game.
     */
    @Override
    public void render(float delta) {
        // Do not forget to clean the screen.
        Gdx.gl.glClearColor(0.1f, 0.125f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the stage. This will update the player speed.
        stage.act();

        // Step the world. This will update the physics and update entity positions.
        world.step(delta, 6, 2);

        // Make the camera follow the player. As long as the player is alive, if the player is
        // moving, make the camera move at the same speed, so that the player is always
        // centered at the same position.
        //if (player.getX() > 150 && player.isAlive()) {
        //    float speed = Constants.PLAYER_SPEED * delta * Constants.PIXELS_IN_METER;
        //    stage.getCamera().translate(speed, 0, 0);
        //}

        // Render the screen. Remember, this is the last step!
        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture,-230,-230);
        stage.getBatch().end();
        stage.draw();


    }

    /**
     * This method is executed when the screen can be safely disposed.
     * I use this method to dispose things that have to be manually disposed.
     */
    @Override
    public void dispose() {
        // Dispose the stage to remove the Batch references in the graphics card.
        stage.dispose();

        // Dispose the world to remove the Box2D native data (C++ backend, invoked by Java).
        world.dispose();
        batch.dispose();
        backgroundTexture.dispose();
    }

    /**
     * This is the contact listener that checks the world for collisions and contacts.
     * I use this method to evaluate when things collide, such as player colliding with floor.
     */
    private class GameContactListener implements ContactListener {

        private boolean areCollided(Contact contact, Object userA, Object userB) {
            Object userDataA = contact.getFixtureA().getUserData();
            Object userDataB = contact.getFixtureB().getUserData();

            // This is not in the video! It is a good idea to check that user data is not null.
            // Sometimes you forget to put user data or you get collisions by entities you didn't
            // expect. Not preventing this will probably result in a NullPointerException.
            if (userDataA == null || userDataB == null) {
                return false;
            }

            // Because you never know what is A and what is B, you have to do both checks.
            return (userDataA.equals(userA) && userDataB.equals(userB)) ||
                    (userDataA.equals(userB) && userDataB.equals(userA));
        }

        /**
         * This method is executed when a contact has started: when two fixtures just collided.
         */
        @Override
        public void beginContact(Contact contact) {
            // The player has collided with the floor.
            if (areCollided(contact, "player", "floor")) {
                player.setJumping(false);
                mSocket.emit("colision", "Colision con el suelo");


                // If the screen is still touched, you have to jump again.
                if (Gdx.input.isTouched()) {
                    jumpSound.play();

                    // You just can't add a force here, because while a contact is being handled
                    // the world is locked. Therefore you have to find a way to remember to make
                    // the player jump AFTER the collision has been handled. Here I update the
                    // flag value mustJump. This will make the player jump on next frame.
                    player.setMustJump(true);
                }
            }

            // The player has collided with something that hurts.
            if (areCollided(contact, "player", "spike")) {

                // Check that is alive. Sometimes you bounce, you don't want to die more than once.
                if (player.isAlive()) {
                    player.setAlive(false);
                    mSocket.emit("colision","Colision con un pincho");


                    // Sound feedback.
                    backgroundMusic.stop();
                    dieSound.play();

                    // Add an Action. Actions are cool because they let you add animations to your
                    // game. Here I add a sequence action so that two actions happens one after
                    // the other. One action is a delay action. It just waits for 1.5 seconds.
                    // The second actions is a run action. It executes some code. Here, we go
                    // to the game over screen when we die.
                    stage.addAction(
                            Actions.sequence(
                                    Actions.delay(1.5f),
                                    Actions.run(new Runnable() {

                                        @Override
                                        public void run() {
                                            game.setScreen(game.gameOverScreen);
                                        }
                                    })
                            )
                    );
                }
            }
        }

        /**
         * This method is executed when a contact has finished: two fixtures are no more colliding.
         */
        @Override
        public void endContact(Contact contact) {
            // The player is jumping and it is not touching the floor.
            if (areCollided(contact, "player", "floor")) {
                if (player.isAlive()) {
                    jumpSound.play();
                }
            }
        }

        // Here two lonely methods that I don't use but have to override anyway.
        @Override public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
    }


}

