package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import Connections.Connection;

public class WaitingOpponentScreen extends BaseScreen{
    private Connection conn;
    private Stage stage;
    public WaitingOpponentScreen(MainGame game, Connection conn){
        super(game);
        this.conn=conn;
        this.stage=new Stage();
    }

    @Override
    public void show() {
        super.show();
    }
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
