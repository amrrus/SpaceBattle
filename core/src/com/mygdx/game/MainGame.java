package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import com.mygdx.game.Connections.Connection;

/**
 * This is our main game. This is the class that we pass to the Application in Android launcher
 * as well as in desktop launcher. Because we want to create a screen-based game, we use Game
 * class, which has methods for creating multiple screens.
 */
public class MainGame extends Game {

    private Connection conn;
    /** This is the asset manager we use to centralize the assets. */
    private AssetManager manager;

    public BaseScreen loadingScreen, menuScreen, gameScreen, gameOverScreen, howToPlayScreen,roomsList,waitingOpponentScreen;

    @Override
    public void create() {
        manager = new AssetManager();
        manager.load("gameover.png", Texture.class);
        manager.load("blueShipUp.png",Texture.class);
        manager.load("blueShipDown.png",Texture.class);
        manager.load("dividedPlanet.png",Texture.class);
        manager.load("logo.png", Texture.class);
        manager.load("asteroid.png", Texture.class);
        manager.load("explosion-transitions.png", Texture.class);
        manager.load("bullet-red.png", Texture.class);
        manager.load("bullet-green.png", Texture.class);
        manager.load("shots-list.png",Texture.class);
        manager.load("background.png",Texture.class);
        manager.load("1.png",Texture.class);
        manager.load("2.png",Texture.class);
        manager.load("3.png",Texture.class);
        manager.load("vs.png",Texture.class);
        manager.load("loser.png", Texture.class);
        manager.load("winner.png", Texture.class);
        manager.load("refresh.png",Texture.class);
        manager.load("audio/background-music.ogg", Music.class);
        manager.load("audio/shot.ogg", Sound.class);
        manager.load("audio/explosion.ogg", Sound.class);
        manager.load("audio/click.ogg", Sound.class);

        // Enter the loading screen to load the assets.
        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    public void finishLoading(Connection conn) {
        this.conn=conn;
        menuScreen = new MenuScreen(this,this.conn);
        gameScreen = new GameScreen(this,this.conn);
        roomsList = new RoomsList(this,this.conn);
        //gameOverScreen = new GameOverScreen(this,this.conn);
        howToPlayScreen = new HowToPlayScreen(this);
        waitingOpponentScreen = new WaitingOpponentScreen(this,this.conn);
        setScreen(menuScreen);
    }

    public AssetManager getManager() {
        return manager;
    }

}
