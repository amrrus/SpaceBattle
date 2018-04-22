package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.mygdx.game.Connections.Connection;
import com.mygdx.game.Utils.GifDecoder;


public class LoadingScreen extends BaseScreen implements InputProcessor {

    private Stage stage;
    private Skin skin;
    private Label loading;
    private Connection conn;
    private float timer;
    private Boolean autoDisconnect;
    private Animation<TextureRegion> animation;
    private float animationWidth;
    private float animationHeight;
    private float elapsed;
    private GlyphLayout glyphLayout;
    private TextField changeUrl;
    private TextButton changeUrlButton;

    public LoadingScreen(MainGame game) {

        super(game);

        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));
        FileHandle skinStream = Gdx.files.internal("skin/uiskin.json");
        skin = new Skin(skinStream);

        glyphLayout = new GlyphLayout(); // for calculating the text width (for centering the Label)

        loading = new Label("Cargando...", skin);
        loading.setFontScale(2,2);

        glyphLayout.setText(skin.getFont("default-font"), "Cargando...");
        loading.setPosition(Constants.WIDTH_SCREEN/2 - glyphLayout.width, Constants.HEIGHT_SCREEN/2 - glyphLayout.height);

        animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("loading.gif").read());
        animationWidth = animation.getKeyFrame(0).getRegionWidth();
        animationHeight = animation.getKeyFrame(0).getRegionHeight();

        // Only works on android
        changeUrl = new TextField("", skin);
        TextField.TextFieldStyle textFieldStyle = new Skin(skinStream).get(TextField.TextFieldStyle.class);
        textFieldStyle.font.getData().setScale(3);
        changeUrl.setStyle(textFieldStyle);
        changeUrl.setText(Constants.SERVER_URL);
        changeUrl.setAlignment(Align.center);
        changeUrl.setVisible(false);
        changeUrl.setSize(changeUrl.getWidth()*4, changeUrl.getHeight()*2);
        changeUrl.setPosition(Constants.WIDTH_SCREEN/2 - changeUrl.getWidth()/2, Constants.HEIGHT_SCREEN/2 - changeUrl.getHeight()/2+Constants.HEIGHT_SCREEN/3);

        changeUrlButton = new TextButton("Conectar", skin);
        changeUrlButton.setSize(200, changeUrl.getHeight());
        changeUrlButton.getLabel().setFontScale(2, 2);
        changeUrlButton.setVisible(false);
        changeUrlButton.setPosition(Constants.WIDTH_SCREEN/2 + changeUrl.getWidth()/2, Constants.HEIGHT_SCREEN/2 - changeUrl.getHeight()/2+Constants.HEIGHT_SCREEN/3);
        changeUrlButton.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeUrl.setVisible(false);
                changeUrlButton.setVisible(false);
                Constants.SERVER_URL = changeUrl.getText();
                conn = null;
                timer = 0;
                connect(0);
            }
        });

        stage.addActor(loading);
        stage.addActor(changeUrl);
        stage.addActor(changeUrlButton);
        timer=0;
        autoDisconnect=true;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsed += delta;

        if (game.getManager().update() && conn!= null && conn.connected()) {
            game.finishLoading(conn);
        } else {
            if (game.getManager().getProgress()<1){
                int progress = (int) (game.getManager().getProgress() * 100);
                updateText("Cargando... " + progress + "%");

            }else{
                connect(delta);
            }

        }

        stage.act();
        stage.getBatch().begin();
        stage.getBatch().draw(animation.getKeyFrame(elapsed), (Constants.WIDTH_SCREEN - animationWidth)/2, (Constants.HEIGHT_SCREEN - animationHeight)/2 + Constants.HEIGHT_SCREEN/7);
        stage.getBatch().end();

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void connect(float delta){

        if (conn == null){
            this.conn = new Connection();
            this.conn.connect();

            updateText("Conectando...");

        }else{
            timer+=delta;
            if (timer>=15){
                if (autoDisconnect){
                    conn.disconnect();
                    autoDisconnect = false;
                }
                updateText("Error de conexion");

                changeUrl.setVisible(true);
                changeUrlButton.setVisible(true);
            }

        }
    }


    private void updateText(String text){
        loading.setText(text);
        glyphLayout.setText(skin.getFont("default-font"), text);

        loading.setPosition(Constants.WIDTH_SCREEN/2 - glyphLayout.width, Constants.HEIGHT_SCREEN/2 - glyphLayout.height);
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor( this );
        multiplexer.addProcessor( stage );
        Gdx.input.setInputProcessor( multiplexer );

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
