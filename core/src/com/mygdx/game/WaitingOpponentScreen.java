package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Connections.Connection;
import com.mygdx.game.Utils.ButtonListener;
import com.mygdx.game.Utils.GifDecoder;
import com.mygdx.game.Utils.Utils;

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
    private float frameMargin;
    private Integer sizeXShip;
    private Integer sizeYShip;
    private Animation<TextureRegion> gifWaiting;
    private boolean waiting;
    private float elapsed;


    public WaitingOpponentScreen(final MainGame game, final Connection conn){
        super(game);
        this.conn=conn;
        this.stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));

        skin = new Skin(Gdx.files.internal("skin/skin-composer-ui.json"));

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

        frameMargin = Constants.WIDTH_SCREEN*0.17f;
        sizeXShip = Constants.WIDTH_SCREEN/5;
        sizeYShip = (Constants.HEIGHT_SCREEN/2);

        bottomShip= new Image(game.getManager().get("blueShipUp.png", Texture.class));
        bottomShip.setSize(sizeXShip, sizeYShip);
        bottomShip.setPosition(frameMargin - bottomShip.getWidth()/2,Constants.HEIGHT_SCREEN*0.5f - bottomShip.getHeight()*0.5f);

        topShip= new Image(game.getManager().get("blueShipDown.png", Texture.class));
        topShip.setSize(sizeXShip, sizeYShip);
        topShip.setPosition(Constants.WIDTH_SCREEN - frameMargin - topShip.getWidth()/2,Constants.HEIGHT_SCREEN*0.5f - topShip.getHeight()*0.5f);

        title = new BitmapFont();
        title.getData().setScale(4);
        title.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        gifWaiting = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("waitting-opponent.gif").read());
        topShip.setPosition(Constants.WIDTH_SCREEN - frameMargin - topShip.getWidth()/2,Constants.HEIGHT_SCREEN*0.5f - topShip.getHeight()*0.5f);

        stage.addActor(background);
        stage.addActor(vs);
        stage.addActor(bottomShip);
        stage.addActor(topShip);
        stage.addActor(back);

        waiting = true;
        topShip.setVisible(false);
    }


    @Override
    public void show() {
        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) ){
                    if (back.isVisible()) {
                        conn.deleteRoom();
                        game.setScreen(game.roomsList);
                    }
                }
                return false;
            }
        };
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor( backProcessor );
        multiplexer.addProcessor( stage );
        Gdx.input.setInputProcessor( multiplexer );
        Gdx.input.setCatchBackKey(true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
        waiting = true;
    }
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsed += delta;

        stage.act();
        stage.draw();
        stage.getBatch().begin();

        title.getData().setScale(Utils.getStringWidth(conn.getBotPlayerName(), title) > bottomShip.getWidth() + Constants.WIDTH_SCREEN*0.1f ? 3 : 4);
        title.draw(stage.getBatch(),conn.getBotPlayerName(), bottomShip.getX(Align.left) - Constants.WIDTH_SCREEN*0.05f, bottomShip.getY(Align.top) + Constants.HEIGHT_SCREEN*0.1f, bottomShip.getWidth() + Constants.WIDTH_SCREEN*0.1f, Align.center, true );

        title.getData().setScale(Utils.getStringWidth(conn.getTopPlayerName(), title) > topShip.getWidth()+ Constants.WIDTH_SCREEN*0.1f ? 3 : 4);
        title.draw(stage.getBatch(), conn.getTopPlayerName(), topShip.getX(Align.left) - Constants.WIDTH_SCREEN * 0.05f, topShip.getY(Align.top) + Constants.HEIGHT_SCREEN * 0.1f, topShip.getWidth() + Constants.WIDTH_SCREEN * 0.1f, Align.center, true);

        if(waiting) {
            stage.getBatch().draw(gifWaiting.getKeyFrame(elapsed), Constants.WIDTH_SCREEN - frameMargin - topShip.getWidth()/4,Constants.HEIGHT_SCREEN*0.5f - topShip.getHeight()*0.25f, sizeXShip/2, sizeXShip/2);
        }

        stage.getBatch().end();

    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
        this.topShip.setVisible(!waiting);
        this.back.setVisible(waiting);
    }
}
