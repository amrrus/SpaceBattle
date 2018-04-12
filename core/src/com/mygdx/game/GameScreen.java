package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.entities.EntityFactory;
import com.mygdx.game.entities.PlayerEntity;
import Connections.Connection;

/**
 * This is the main screen for the game. All the fun happen here.
 */

public class GameScreen extends BaseScreen {

    public Stage stage;
    public World world;
    public PlayerEntity topPlayer;
    public PlayerEntity bottomPlayer;
    private Texture background;
    private Texture textureOne;
    private Texture textureTwo;
    private Texture textureThree;
    public Integer countdown;
    private Connection conn;
    public EntityFactory factory;
    private ScoreBoard sb;
    private Boolean endGame;
    private Boolean loser;
    private Skin skin;
    private TextButton accept;
    private Boolean finishPrepare;


    /**
     * Create the screen. Since this constructor cannot be invoked before libGDX is fully started,
     * it is safe to do critical code here such as loading assets and setting up the stage.
     * @param game
     */
    public GameScreen(MainGame game, Connection conn) {
        super(game);
        this.conn=conn;
        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
        //stage.setDebugAll(true);

        world = new World(new Vector2(0, 0), true);

        sb = new ScoreBoard(this);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));


        accept = new TextButton("Aceptar", skin);
        accept.setSize(250, 100);
        accept.getLabel().setFontScale(4, 4);
        accept.setPosition(-125, -50);
        stage.addActor(accept);
        Utils ut = new Utils();
        Gdx.app.log("asteroide:",ut.genera2().toString());
        Gdx.app.log("asteroide:",ut.genera2().toString());
        Gdx.app.log("asteroide:",ut.genera2().toString());
        Gdx.app.log("asteroide:",ut.genera2().toString());
        Gdx.app.log("asteroide:",ut.genera2().toString());
        Gdx.app.log("asteroide:",ut.genera2().toString());
    }

    public void show() {
        //Create factory
        factory = new EntityFactory(game.getManager(),this);

        //connection establishing
        conn.setGameScreen(this);
        countdown = 3;

        //set controls to play
        new Controllers(this,conn);
        accept.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Take me to the game screen!
                Gdx.app.log("debug","touched button");
                game.setScreen(game.menuScreen);
                //Gdx.app.exit();
            }
        });
        // Create the players.
        topPlayer = factory.createTopPlayer();
        bottomPlayer = factory.createBottomPlayer();

        //Load background image
        background = factory.loadBackgroundImage();
        //Load countdown images
        textureOne = getManager().get("1.png");
        textureTwo = getManager().get("2.png");
        textureThree = getManager().get("3.png");

        // Reset the camera to the center.
        stage.getCamera().position.set(0f,0f,0f);
        stage.getCamera().update();

        endGame=false;
        loser=false;
        finishPrepare = true;
        accept.setVisible(false);
    }
    /**
     * This method will be executed when this screen is no more the active screen.
     * I use this method to destroy all the things that have been used in the stage.
     */
    @Override
    public void hide() {
        //Disconnect to server
        conn.disconnect();

        // Clear the stage. This will remove ALL actors from the stage and it is faster than
        // removing every single actor one by one. This is not shown in the video but it is
        // an improvement.
        stage.clear();

        // Detach every entity from the world they have been living in.
        Array<Body> copy = new Array<Body>();
        world.getBodies(copy);
        for (int i=0;i<copy.size;i++){
            world.destroyBody(copy.get(i));
        }
        //remove imput processor
        Gdx.input.setInputProcessor(null);
    }

    /**
     * This method is executed whenever the game requires this screen to be rendered. This will
     * display things on the screen. This method is also used to update the game.
     */

    public void render(float delta) {

        // Do not forget to clean the screen.
        Gdx.gl.glClearColor(0.1f, 0.125f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update the stage. This will update the actors.
        stage.act();
        if(endGame && finishPrepare){
            accept.setVisible(true);
            Gdx.input.setInputProcessor(stage);
            finishPrepare = false;
        }else {
            // Step the world. This will update the physics and update entity positions.
            world.step(delta, 6, 2);
        }

        // Render the screen. Remember, this is the last step!
        stage.getBatch().begin();

        stage.getBatch().draw(background,-510,-510, Constants.HEIGHT_SCREEN*0.95f, Constants.HEIGHT_SCREEN*0.95f);
        if (countdown>0){
            switch(countdown){
                case 1:
                    stage.getBatch().draw(textureOne,-256,-256);
                    break;
                case 2:
                    stage.getBatch().draw(textureTwo,-256,-256);
                    break;
                case 3:
                    stage.getBatch().draw(textureThree,-256,-256);
                    break;
            }
        }
        if (endGame){
            //imagen de victoria o derrota en funcion de loser
        }
        sb.displayScoreBoard();
        stage.getBatch().end();
        stage.draw();


        //Create and delete bodies after render world

        factory.getConcurrencyManager().checkAsteroidsToRemove();
        factory.getConcurrencyManager().checkShotsToRemove();
        factory.getConcurrencyManager().checkAsteroidsToCreate();
        factory.getConcurrencyManager().checkShotsToCreate();
    }

    public void dispose() {
        // Dispose the stage to remove the Batch references in the graphics card.
        stage.dispose();

        // Dispose the world to remove the Box2D native data (C++ backend, invoked by Java).
        world.dispose();
        background.dispose();
        sb.dispose();
    }

    public AssetManager getManager(){
        return this.game.getManager();
    }

    public void endGame(Boolean loser){
        this.endGame=true;
        this.loser = loser;

        //this.game.setScreen(this.game.menuScreen);
    }
}
