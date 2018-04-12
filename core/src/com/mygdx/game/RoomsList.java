package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.GUI.EmptyRoom;

import java.util.ArrayList;
import java.util.Collections;

import Connections.Connection;

public class RoomsList extends BaseScreen {

    private Connection conn;
    private Stage stage;
    private Skin skin;
    private TextButton createRoom;
    private ShapeRenderer shapeRenderer;
    private BitmapFont title;
    private float heightHeader;
    private Table Rooms;
    private EmptyRoom emptyHead;

    public RoomsList(final MainGame game, Connection conn){
        super(game);
        this.conn=conn;
        heightHeader = 120;

        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));


        createRoom = new TextButton("Crear", skin);
        createRoom.setSize(250, 250);
        createRoom.getLabel().setFontScale(4, 4);
        createRoom.setPosition(Constants.WIDTH_SCREEN-350, 650-(Constants.HEIGHT_SCREEN/2));
        createRoom.addCaptureListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.gameScreen);
            }
        });

        emptyHead= new EmptyRoom(heightHeader);
        ArrayList<String>l = new ArrayList<String>();
        l.add("a");l.add("b");l.add("c");l.add("d");l.add("e");l.add("f");
        updateRoomList(l);

        //fixed header
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
        title = new BitmapFont();
        title = new BitmapFont();
        title.getData().setScale(4);
        title.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        stage.addActor(createRoom);

    }
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.rect(0,Constants.HEIGHT_SCREEN/2-heightHeader,Constants.WIDTH_SCREEN,heightHeader);
        shapeRenderer.end();
        stage.getBatch().begin();
        title.draw(stage.getBatch(),"Salas",Constants.WIDTH_SCREEN/2-80,Constants.HEIGHT_SCREEN-50);

        stage.getBatch().end();
    }
    public void updateRoomList(ArrayList<String> l){

        final Table scrollTable = new Table();
        scrollTable.add(emptyHead);
        for (String room:l) {
            scrollTable.row();
            scrollTable.add(new Image(game.getManager().get("blueShipDown.png", Texture.class)));
        }
        final ScrollPane scroller = new ScrollPane(scrollTable,skin);

        Rooms = new Table();
        Rooms.setFillParent(true);
        Rooms.add(scroller).fill().expand();
        stage.addActor(Rooms);
    }

}
