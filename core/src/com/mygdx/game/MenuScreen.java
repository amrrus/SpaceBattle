package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.mygdx.game.Connections.Connection;
import com.mygdx.game.Utils.ButtonListener;

import sun.applet.Main;

/**
 * This is the screen that you see when you enter the game. It has a button for playing the game.
 * When you press this button, you go to the game screen so that you can start to play. This
 * screen was done by copying the code from GameOverScreen. All the cool comments have been
 * copy-pasted.
 */
public class MenuScreen extends BaseScreen implements InputProcessor {

    private Stage stage;

    private Skin skin;

    private Image logo;
    private Image background;

    private TextButton play, howToPlay;

    private Connection conn;

    private TextField nickname;
    private Label error_longitud;

    public MenuScreen(final MainGame game, final Connection conn) {
        super(game);
        this.conn = conn;
        conn.setMainGame(game);
        conn.setMenuScreen(this);
        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));


        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        nickname=new TextField("", skin);
        TextField.TextFieldStyle textFieldStyle = skin.get(TextField.TextFieldStyle.class);
        textFieldStyle.font.getData().setScale(3);
        nickname.setStyle(textFieldStyle);
        int randomNumber = MathUtils.random(10000, 99999);
        nickname.setText("Player" + randomNumber);

        Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
        error_longitud = new Label("", skin);
        labelStyle.font.getData().setScale(3);
        error_longitud.setStyle(labelStyle);
        error_longitud.setColor(Color.RED);

        play = new TextButton("Jugar", skin);
        howToPlay = new TextButton("Como jugar", skin);

        logo = new Image(game.getManager().get("logo.png", Texture.class));
        background = new Image(game.getManager().get("background.png", Texture.class));

        play.addCaptureListener(new ButtonListener(game.getManager()) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!(nickname.getText().isEmpty() || nickname.getText().length()>15)) {
                    conn.checkNickname(nickname.getText());
                }else if(nickname.getText().isEmpty()){
                    error_longitud.setText("El nombre de usuario no puede estar vacio");
                }else if(nickname.getText().length()>15){
                    error_longitud.setText("El nombre de usuario no puede tener mas de 15 caracteres");
                }else{
                    errorNick();
                }

            }
        });

        howToPlay.addCaptureListener(new ButtonListener(game.getManager()) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.howToPlayScreen);
            }
        });


        background.setPosition(0,0);
        background.setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN);

        error_longitud.setPosition(Constants.WIDTH_SCREEN/2 - error_longitud.getWidth()-900, Constants.HEIGHT_SCREEN/2 - error_longitud.getHeight()+380);
        error_longitud.setSize(error_longitud.getWidth()*4, error_longitud.getHeight()*2);

        nickname.setPosition(Constants.WIDTH_SCREEN/2 - nickname.getWidth()-690, Constants.HEIGHT_SCREEN/2 - nickname.getHeight()+200);
        nickname.setSize(nickname.getWidth()*4, nickname.getHeight()*2);


        logo.setPosition(2*(Constants.WIDTH_SCREEN/2 - logo.getWidth()) - 60, Constants.HEIGHT_SCREEN/2 - logo.getHeight());
        logo.setSize(logo.getWidth() * 2, logo.getHeight() * 2);

        play.setSize(600, 240);
        play.getLabel().setFontScale(4, 4);
        play.setPosition(120, 420);

        howToPlay.setSize(600, 240);
        howToPlay.getLabel().setFontScale(4, 4);
        howToPlay.setPosition(120, 120);

        stage.addActor(background);
        stage.addActor(error_longitud);
        stage.addActor(nickname);
        stage.addActor(logo);
        stage.addActor(play);
        stage.addActor(howToPlay);
    }

    public void errorNick() {
        error_longitud.setText("El nombre de usuario ya existe");
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
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode== Input.Keys.BACK){
            Gdx.app.log("debug","back");
            Gdx.app.exit();
        }
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

