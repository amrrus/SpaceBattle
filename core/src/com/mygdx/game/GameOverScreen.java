package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Connections.Connection;
import com.mygdx.game.Utils.ButtonListener;

public class GameOverScreen extends BaseScreen {

    private Connection conn;
    private Stage stage;
    private Skin skin;
    private Skin skin2;
    private Image gameOver;
    private TextButton menu;
    private boolean loser;
    private Image background;

    public GameOverScreen(final MainGame game, final Connection conn) {
        super(game);
        this.conn=conn;
        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skin2 = new Skin(Gdx.files.internal("skin/skin-composer-ui.json"));

        menu = new TextButton("Volver al menu", skin2);
        menu.setSize(600, 150);
        menu.getLabel().setFontScale(4, 4);
        menu.setPosition(Constants.WIDTH_SCREEN/2 - menu.getWidth()/2,Constants.HEIGHT_SCREEN/2 - menu.getHeight()/2 - Constants.HEIGHT_SCREEN*0.125f);
        menu.addCaptureListener(new ButtonListener(game.getManager()) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                conn.setTopPlayerName("");
                game.setScreen(game.menuScreen);
            }
        });

        gameOver = new Image();
        gameOver.setSize(Constants.WIDTH_SCREEN*0.5f, Constants.HEIGHT_SCREEN*0.5f);
        gameOver.setPosition(Constants.WIDTH_SCREEN/2 - gameOver.getWidth()/2,Constants.HEIGHT_SCREEN/2 - gameOver.getHeight()/2 + Constants.HEIGHT_SCREEN*0.25f);

        Dialog dialog = new Dialog("", skin);
        Table table = dialog.getContentTable();
        table.setFillParent(true);
        table.add(gameOver).size(gameOver.getWidth(), gameOver.getHeight());
        table.row();
        table.add(menu).size(menu.getWidth(), menu.getHeight());
        dialog.setSize(Constants.WIDTH_SCREEN*0.8f, Constants.HEIGHT_SCREEN*0.8f);
        dialog.setPosition(Constants.WIDTH_SCREEN/2 - dialog.getWidth()/2, Constants.HEIGHT_SCREEN/2 - dialog.getHeight()/2);

        background = new Image(game.getManager().get("background.png", Texture.class));

        stage.addActor(background);
        stage.addActor(dialog);

        loser = true;
    }

    public void isLoser(Boolean loser){
        this.loser = loser;
    }

    @Override
    public void show() {
        Texture t = game.getManager().get(loser ? "loser.png" : "winner.png");
        gameOver.setDrawable(new TextureRegionDrawable(new TextureRegion(t)));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }


}