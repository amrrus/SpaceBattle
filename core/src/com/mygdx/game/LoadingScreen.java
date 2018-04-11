package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Connections.Connection;


public class LoadingScreen extends BaseScreen {

    private Stage stage;
    private Skin skin;
    private Label loading;
    private Connection conn;
    private float timer;
    private Boolean autoDisconnect;

    public LoadingScreen(MainGame game) {

        super(game);

        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        loading = new Label("Loading...", skin);
        loading.setFontScale(2,2);
        loading.setPosition(Constants.WIDTH_SCREEN/2 - loading.getWidth(), Constants.HEIGHT_SCREEN/2 - loading.getHeight());

        stage.addActor(loading);
        timer=0;
        autoDisconnect=true;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.getManager().update() && conn!= null && conn.connected()) {
            game.finishLoading();
        } else {
            if (game.getManager().getProgress()<1){
                int progress = (int) (game.getManager().getProgress() * 100);
                loading.setText("Cargando... " + progress + "%");
            }else{
                if (conn == null){
                    this.conn = new Connection();
                    this.conn.connect();
                    loading.setText("Conectando...");
                }else{
                    timer+=delta;
                    if (timer<15){
                        loading.setText("Conectando...");
                    }else{
                        if (autoDisconnect){
                            conn.disconnect();
                            autoDisconnect = false;
                        }
                        loading.setText("Error de conexion. Pruebe mas tarde.");//faltan tildes
                    }

                }

            }

        }

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
