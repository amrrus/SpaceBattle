package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Connections.Connection;
import sun.awt.image.GifImageDecoder;

/**
 * This is our main game. This is the class that we pass to the Application in Android launcher
 * as well as in desktop launcher. Because we want to create a screen-based game, we use Game
 * class, which has methods for creating multiple screens.
 */
public class MainGame extends Game {

    private Connection conn;
    /** This is the asset manager we use to centralize the assets. */
    private AssetManager manager;

    /**
     * These are the screens that we use in this game. I invite you to use a better system than
     * just public variables. For instance, you could create an ArrayList or maybe use some
     * structure such as a map where you can associate a number or a string to a screen.
     */
    public BaseScreen loadingScreen, menuScreen, gameScreen, gameOverScreen, creditsScreen;

    @Override
    public void create() {
        // Initialize the asset manager. We add every aset to the manager so that it can be loaded
        // inside the LoadingScreen screen. Remember to put the name of the asset in the first
        // argument, then the type of the asset in the second argument.
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
        manager.load("1.png",Texture.class);
        manager.load("2.png",Texture.class);
        manager.load("3.png",Texture.class);

        // Enter the loading screen to load the assets.
        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    /**
     * This method is invoked by LoadingScreen when all the assets are loaded. Use this method
     * as a second-step loader. You can load the rest of the screens here and jump to the main
     * screen now that everything is loaded.
     */
    public void finishLoading() {
        this.conn = new Connection();
        this.conn.connect();
        menuScreen = new MenuScreen(this,this.conn);
        gameScreen = new GameScreen(this,this.conn);
        //gameOverScreen = new GameOverScreen(this,this.conn);
        creditsScreen = new CreditsScreen(this);
        setScreen(menuScreen);
    }

    public AssetManager getManager() {
        return manager;
    }

}
