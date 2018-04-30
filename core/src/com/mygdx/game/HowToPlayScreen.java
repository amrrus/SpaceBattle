package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Utils.ButtonListener;


public class HowToPlayScreen extends BaseScreen {

    private Stage stage;

    private Skin skin;

    private Label text;

    private TextButton back;

    public HowToPlayScreen(final MainGame game) {
        super(game);

        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));

        skin = new Skin(Gdx.files.internal("skin/skin-composer-ui.json"));

        back = new TextButton("Atras", skin);

        text = new Label("CREAR IMAGEN", skin);

        back.addCaptureListener(new ButtonListener(game.getManager()) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.menuScreen);
            }
        });

        text.setFontScale(2,2);
        text.setPosition(Constants.WIDTH_SCREEN/2 - text.getWidth(), Constants.HEIGHT_SCREEN/2 - text.getHeight());

        back.setSize(600, 240);
        back.getLabel().setFontScale(4, 4);
        back.setPosition(120, 120);

        stage.addActor(back);
        stage.addActor(text);
    }

    @Override
    public void show() {
        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) ){
                    game.setScreen(game.menuScreen);
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

