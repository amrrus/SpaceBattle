package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Connections.Connection;

/**
 * This is the screen that you see when you enter the game. It has a button for playing the game.
 * When you press this button, you go to the game screen so that you can start to play. This
 * screen was done by copying the code from GameOverScreen. All the cool comments have been
 * copy-pasted.
 */
public class MenuScreen extends BaseScreen {

    private Stage stage;

    private Skin skin;

    private Image logo;
    private Image background;

    private TextButton play, howToPlay;

    private Connection conn;

    private TextField nickname;

    public MenuScreen(final MainGame game,Connection conn) {
        super(game);
        this.conn = conn;

        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));


        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        nickname=new TextField("Player1", skin);
        TextField.TextFieldStyle textFieldStyle = skin.get(TextField.TextFieldStyle.class);
        textFieldStyle.font.getData().setScale(3);
        nickname.setStyle(textFieldStyle);

        play = new TextButton("Jugar", skin);
        howToPlay = new TextButton("Como jugar", skin);

        logo = new Image(game.getManager().get("logo.png", Texture.class));
        background = new Image(game.getManager().get("background.png", Texture.class));

        play.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.roomsList);
            }
        });

        howToPlay.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.howToPlayScreen);
            }
        });


        background.setPosition(0,0);
        background.setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN);

        nickname.setPosition(Constants.WIDTH_SCREEN/2 - nickname.getWidth()-690, Constants.HEIGHT_SCREEN/2 - nickname.getHeight()+200);
        nickname.setSize(300, 100);


        logo.setPosition(2*(Constants.WIDTH_SCREEN/2 - logo.getWidth()) - 60, Constants.HEIGHT_SCREEN/2 - logo.getHeight());
        logo.setSize(logo.getWidth() * 2, logo.getHeight() * 2);

        play.setSize(600, 240);
        play.getLabel().setFontScale(4, 4);
        play.setPosition(120, 420);

        howToPlay.setSize(600, 240);
        howToPlay.getLabel().setFontScale(4, 4);
        howToPlay.setPosition(120, 120);

        stage.addActor(background);
        stage.addActor(nickname);
        stage.addActor(logo);
        stage.addActor(play);
        stage.addActor(howToPlay);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
}

