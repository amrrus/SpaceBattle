package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Connections.Connection;
import com.mygdx.game.Utils.ButtonListener;

public class WaitingOpponentScreen extends BaseScreen{
    private Connection conn;
    private Stage stage;
    private TextButton back;
    private Skin skin;
    private Image background;
    private Image bottomShip;
    private Image topShip;
    private Image vs;
    private BitmapFont title;
    private Integer frameMargin;
    private Integer sizeXShip;
    private Integer sizeYShip;
    private String opponent;

    public WaitingOpponentScreen(final MainGame game, final Connection conn){
        super(game);
        this.conn=conn;
        this.stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
        opponent="Nombreprueba";
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        back = new TextButton("Salir", skin);
        back.addCaptureListener(new ButtonListener(game.getManager()) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                conn.deleteRoom();
                game.setScreen(game.roomsList);
            }
        });
        back.setSize(250, 120);
        back.getLabel().setFontScale(4, 4);
        back.setPosition(60, 60);


        background = new Image(game.getManager().get("background.png", Texture.class));
        background.setPosition(0,0);
        background.setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN);

        vs = new Image(game.getManager().get("vs.png", Texture.class));
        vs.setPosition(0,0);
        vs.setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN);

        frameMargin = 100;
        sizeXShip = Constants.WIDTH_SCREEN/4;
        sizeYShip = (int )(Constants.HEIGHT_SCREEN/1.5);

        bottomShip= new Image(game.getManager().get("blueShipUp.png", Texture.class));
        bottomShip.setPosition(frameMargin,frameMargin*2);
        bottomShip.setSize(sizeXShip, sizeYShip);

        topShip= new Image(game.getManager().get("blueShipDown.png", Texture.class));
        topShip.setPosition(Constants.WIDTH_SCREEN-sizeXShip-frameMargin,frameMargin*2);
        topShip.setSize(sizeXShip, sizeYShip);

        title = new BitmapFont();
        title.getData().setScale(4);
        title.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        stage.addActor(background);
        stage.addActor(vs);
        stage.addActor(bottomShip);
        stage.addActor(topShip);
        stage.addActor(back);

    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor( stage );
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
        stage.getBatch().begin();
        title.draw(stage.getBatch(),conn.getNickName(),bottomShip.getX()+bottomShip.getImageWidth()/6,bottomShip.getTop()+100);
        title.draw(stage.getBatch(),opponent,topShip.getX()+topShip.getImageWidth()/6,topShip.getTop()+100);
        stage.getBatch().end();

    }
}
