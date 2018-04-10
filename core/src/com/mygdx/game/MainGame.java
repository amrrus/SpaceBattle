package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MainGame extends Game {

    private AssetManager manager;

    public BaseScreen loadingScreen, menuScreen, gameScreen, gameOverScreen, howToPlayScreen;

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

        loadingScreen = new LoadingScreen(this);
        setScreen(loadingScreen);
    }

    public void finishLoading() {
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);
        gameOverScreen = new GameOverScreen(this);
        howToPlayScreen = new HowToPlayScreen(this);
        setScreen(menuScreen);
    }

    public AssetManager getManager() {
        return manager;
    }

}
