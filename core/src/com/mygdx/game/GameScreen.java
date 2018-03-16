package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.entities.AsteroidEntity;
import com.mygdx.game.entities.BottomPlayerEntity;
import com.mygdx.game.entities.EntityFactory;
import com.mygdx.game.entities.ExplosionEntity;
import com.mygdx.game.entities.TopPlayerEntity;
import Connections.Connection;

/**
 * This is the main screen for the game. All the fun happen here.
 */

public class GameScreen extends BaseScreen {

    public Stage stage;
    public World world;
    public TopPlayerEntity topPlayer;
    public BottomPlayerEntity bottomPlayer;
    public ExplosionEntity explosion;
    private Texture background;
    private Connection conn;
    public EntityFactory factory;


    /**
     * Create the screen. Since this constructor cannot be invoked before libGDX is fully started,
     * it is safe to do critical code here such as loading assets and setting up the stage.
     * @param game
     */
    public GameScreen(MainGame game) {
        super(game);

        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
        stage.setDebugAll(true);

        world = new World(new Vector2(0, 0), true);
        conn=new Connection(this);
    }

    public void show() {
        factory = new EntityFactory(game.getManager());
        conn.connect();

        // Create the players.
        topPlayer = factory.createTopPlayer(conn);
        bottomPlayer = factory.createBottomPlayer(conn);
        background = game.getManager().get("dividedPlanet.png");

        // Add the player to the stage too.
        stage.addActor(topPlayer);
        stage.addActor(bottomPlayer);

        // Reset the camera to the center.
        stage.getCamera().position.set(0f,0f,0f);
        stage.getCamera().update();

        explosion = new ExplosionEntity(40f, 0f, 1f );
        stage.addActor(explosion);

    }

    /**
     * This method will be executed when this screen is no more the active screen.
     * I use this method to destroy all the things that have been used in the stage.
     */
    @Override
    public void hide() {
        conn.disconnect();
        // Clear the stage. This will remove ALL actors from the stage and it is faster than
        // removing every single actor one by one. This is not shown in the video but it is
        // an improvement.
        stage.clear();
        // Detach every entity from the world they have been living in.

    }

    /**
     * This method is executed whenever the game requires this screen to be rendered. This will
     * display things on the screen. This method is also used to update the game.
     */

    public void render(float delta) {
        // Do not forget to clean the screen.
        Gdx.gl.glClearColor(0.1f, 0.125f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the stage. This will update the player speed.
        stage.act();

        // Step the world. This will update the physics and update entity positions.
        world.step(delta, 6, 2);

        // Render the screen. Remember, this is the last step!
        stage.getBatch().begin();
        stage.getBatch().draw(background,-510,-510, Constants.HEIGHT_SCREEN*0.95f, Constants.HEIGHT_SCREEN*0.95f);
        stage.getBatch().end();
        stage.draw();
    }

    public void dispose() {
        // Dispose the stage to remove the Batch references in the graphics card.
        stage.dispose();

        // Dispose the world to remove the Box2D native data (C++ backend, invoked by Java).
        world.dispose();
        background.dispose();
    }
}

