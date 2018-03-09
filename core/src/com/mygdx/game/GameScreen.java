package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.entities.BottomPlayerEntity;
import com.mygdx.game.entities.EntityFactory;
import com.mygdx.game.entities.TopPlayerEntity;


import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


import java.net.URISyntaxException;
import Connections.sendPossition;

/**
 * This is the main screen for the game. All the fun happen here.
 */
public class GameScreen extends BaseScreen {

    /** Stage instance for Scene2D rendering. */
    private Stage stage;

    /** World instance for Box2D engine. */
    private World world;

    /** Player entity. */
    private TopPlayerEntity topPlayer;

    private BottomPlayerEntity bottomPlayer;

    private Texture background;

    public Socket mSocket;

    public Integer nFrame;

    private Integer idClient;

    /**
     * Create the screen. Since this constructor cannot be invoked before libGDX is fully started,
     * it is safe to do critical code here such as loading assets and setting up the stage.
     * @param game
     */
    public GameScreen(MainGame game) {
        super(game);

        // Create a new Scene2D stage for displaying things.
        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
        stage.setDebugAll(true);

        // Create a new Box2D world for managing things.
        world = new World(new Vector2(0, 0), true);

        try {
            mSocket = IO.socket(Constants.SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on("pos",pos);
        mSocket.on("setId",setId);
        mSocket.on("createAsteroid",createAsteroid);
        idClient=0;
        nFrame=0;
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
        topPlayer = factory.createTopPlayer(world, new Vector2(0f, 5.5f));
        bottomPlayer = factory.createBottomPlayer(world, new Vector2(0f, -5.5f));
        background = game.getManager().get("dividedPlanet.png");


        // Add the player to the stage too.
        stage.addActor(topPlayer);
        stage.addActor(bottomPlayer);

        // Reset the camera to the left. This is required because we have translated the camera
        // during the game. We need to put the camera on the initial position so that you can
        // use it again if you replay the game.
        stage.getCamera().position.set(0f,0f,0f);
        stage.getCamera().update();

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
        bottomPlayer.detach();
        topPlayer.detach();

    }

    /**
     * This method is executed whenever the game requires this screen to be rendered. This will
     * display things on the screen. This method is also used to update the game.
     */

    private float fps=0;
    public void render(float delta) {
        nFrame+=1;

        fps+=delta;
        if (fps>0.1f){
            JSONObject msg=new JSONObject();
            msg.put("id",2);
            msg.put("x",bottomPlayer.getX());
            msg.put("y",bottomPlayer.getY());
            msg.put("nframe",nFrame);
            //mSocket.emit("pos",msg);
            //sendPossition s=new sendPossition(msg,mSocket);
            //s.run();
            fps=fps-0.1f;
        }


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
        background.dispose();
    }


    private Emitter.Listener pos;
    {   pos = new Emitter.Listener() {
        public void call(final Object... args){
            JSONObject data = (JSONObject) args[0];
            int id = 999;
            float posx= 999;
            float posy= 999;
            try {
                id = data.getInt("id");
                posx = data.getFloat("x");
                posy = data.getFloat("y");
                //System.out.print("Cliente:"+id+", PosX:"+posx+", PosY:"+posy+"\n");
            } catch (JSONException e) {
                System.out.print("Error to receive message." + System.getProperty("line.separator"));
            }
            if (id==0){
                bottomPlayer.setPosition((posx - 0.5f) * Constants.PIXELS_IN_METER,
                        (posy - 0.5f) * Constants.PIXELS_IN_METER);
            }else  if (id==1){
                topPlayer.setPosition((posx - 0.5f) * Constants.PIXELS_IN_METER,
                        (posy - 0.5f) * Constants.PIXELS_IN_METER);
            }
        }
    };
    }
    private Emitter.Listener setId;
    {   setId = new Emitter.Listener() {
        public void call(final Object... args){
            idClient = new Integer ((Integer) args[0]);
            System.out.print("IdCliente: "+idClient+"\n");

        }
    };
    }

    private Emitter.Listener createAsteroid;
    {   createAsteroid = new Emitter.Listener() {
        public void call(final Object... args){
            JSONObject data = (JSONObject) args[0];
            float x = 999;
            float y= 999;
            float vx= 999;
            float vy=999;
            try {
                x = data.getInt("x");
                y = data.getFloat("y");
                vx = data.getFloat("vx");
                vy = data.getFloat("vy");
                System.out.print("Asteroid created: x:"+x+", y:"+y+", vx:"+vx+", vy:"+vy+"\n");
            } catch (JSONException e) {
                System.out.print("Error to receive message." + System.getProperty("line.separator"));
            }

        }
    };
    }


}

