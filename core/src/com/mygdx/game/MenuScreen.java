package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.mygdx.game.Connections.Connection;
import com.mygdx.game.Utils.ButtonListener;


/**
 * This is the screen that you see when you enter the game. It has a button for playing the game.
 * When you press this button, you go to the game screen so that you can start to play. This
 * screen was done by copying the code from GameOverScreen. All the cool comments have been
 * copy-pasted.
 */
public class MenuScreen extends BaseScreen {

    private Stage stage;

    private Skin skin;
    private Skin skin2;

    private Image logo;
    private Image background;

    private TextButton play, howToPlay;

    private Connection conn;

    private TextField nickname;
    public Label errorMessage;

    public MenuScreen(final MainGame game, final Connection conn) {
        super(game);
        this.conn = conn;
        conn.setMainGame(game);
        conn.setMenuScreen(this);
        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));


        skin = new Skin(Gdx.files.internal("skin/skin-composer-ui.json"));
        skin2 = new Skin(Gdx.files.internal("skin/uiskin.json"));
        nickname=new TextField("", skin);
        TextField.TextFieldStyle textFieldStyle = skin.get(TextField.TextFieldStyle.class);
        textFieldStyle.font.getData().setScale(3);
        nickname.setStyle(textFieldStyle);
        int randomNumber = MathUtils.random(10000, 99999);
        nickname.setText("Player" + randomNumber);

        Label.LabelStyle labelStyle = skin2.get(Label.LabelStyle.class);
        errorMessage = new Label("", skin2);
        labelStyle.font.getData().setScale(3);
        errorMessage.setStyle(labelStyle);
        errorMessage.setColor(new Color(0xff0000ff));

        play = new TextButton("Jugar", skin);
        howToPlay = new TextButton("Como jugar", skin);

        logo = new Image(game.getManager().get("logo.png", Texture.class));
        background = new Image(game.getManager().get("background.png", Texture.class));

        play.addCaptureListener(new ButtonListener(game.getManager()) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                checkNickName();


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

        errorMessage.setPosition(120, Constants.HEIGHT_SCREEN/2 - errorMessage.getHeight()+270);
        errorMessage.setSize(errorMessage.getWidth()*4, errorMessage.getHeight()*2);

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
        stage.addActor(errorMessage);
        stage.addActor(nickname);
        stage.addActor(logo);
        stage.addActor(play);
        stage.addActor(howToPlay);
    }

    public void checkNickName() {
        if(nickname.getText().isEmpty()){
            errorMessage.setText("Introduzca al menos un caracter.");
        }else if(nickname.getText().length()>15){
            errorMessage.setText("Nombre mayor de 15 caracteres.");
        }else{
            conn.checkNickname(nickname.getText());
        }

    }


    @Override
    public void show() {
        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) ){
                    Gdx.app.exit();
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

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        errorMessage.setText("");
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

}

