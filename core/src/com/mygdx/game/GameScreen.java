package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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
import com.mygdx.game.Entities.EntityFactory;
import com.mygdx.game.Entities.PlayerEntity;
import com.mygdx.game.Connections.Connection;

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
    private Boolean endGameValue;
    private Boolean loser;
    private Skin skin;
    private Boolean finishPrepare;
    private Music backgroundMusic;


    public GameScreen(MainGame game, Connection conn) {
        super(game);
        this.conn=conn;
        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
        //stage.setDebugAll(true);

        world = new World(new Vector2(0, 0), true);

        sb = new ScoreBoard(this,conn);

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        //Create factory
        factory = new EntityFactory(game.getManager(),this);

        //Load background image
        background = factory.loadBackgroundImage();
        //Load countdown images
        textureOne = getManager().get("1.png");
        textureTwo = getManager().get("2.png");
        textureThree = getManager().get("3.png");
        backgroundMusic = getManager().get("audio/background-music.ogg");


        // Create the players.
        //topPlayer = factory.createTopPlayer();
        //bottomPlayer = factory.createBottomPlayer();

        endGameValue=false;
        loser=false;
        finishPrepare = true;
    }

    public void show() {
        // Create the players.
        topPlayer = factory.createTopPlayer();
        bottomPlayer = factory.createBottomPlayer();

        //connection establishing
        conn.setGameScreen(this);
        countdown = 3;

        //set controls to play
        new Controllers(this,conn);

        // Reset the camera to the center.
        stage.getCamera().position.set(0f,0f,0f);
        stage.getCamera().update();

        endGameValue=false;
        loser=false;
        finishPrepare = true;

        backgroundMusic.setVolume(0.5f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void hide() {
        backgroundMusic.stop();
        conn.removeGameEvents();

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


    public void render(float delta) {

        // Do not forget to clean the screen.
        Gdx.gl.glClearColor(0.1f, 0.125f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if(endGameValue
                && finishPrepare){
            Gdx.input.setInputProcessor(stage);
            finishPrepare = false;
        }else {
            stage.act();

            world.step(delta, 8, 3);
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

        sb.displayScoreBoard();
        stage.getBatch().end();
        stage.draw();

        if (endGameValue){
            this.game.gameOverScreen.isLoser(loser);
            this.game.setScreen(this.game.gameOverScreen);
        }

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
        this.endGameValue=true;
        this.loser = loser;
    }
}
