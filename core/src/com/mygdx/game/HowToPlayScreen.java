package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Utils.ButtonListener;
import java.util.ArrayList;


public class HowToPlayScreen extends BaseScreen {

    private Stage stage;

    private Skin skin;
    private Skin skinUgly;
    private Skin skinUgly2;

    //private Label text;

    private Image background;

    private TextButton menu;

    private ImageButton back;

    private ImageButton next;

    private ArrayList<Texture> imageList;

    private int currentImage;

    public HowToPlayScreen(final MainGame game) {
        super(game);

        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));

        skin = new Skin(Gdx.files.internal("skin/skin-composer-ui.json"));
        skinUgly = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skinUgly2 = new Skin(Gdx.files.internal("skin/uiskin.json"));


        menu = new TextButton("Menu", skin);

        back = new ImageButton(skinUgly, "ugly");

        next = new ImageButton(skinUgly2, "ugly");

        imageList = new ArrayList<Texture>();

        back.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(game.getManager().get("back.png", Texture.class)));
        next.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(game.getManager().get("next.png", Texture.class)));

        background = new Image(game.getManager().get("background.png", Texture.class));

        menu.addCaptureListener(new ButtonListener(game.getManager()) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.menuScreen);
            }
        });

        back.addCaptureListener(new ButtonListener(game.getManager()) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentImage == 1){
                    game.setScreen(game.menuScreen);
                }else {
                    backImage();
                }
            }
        });

        next.addCaptureListener(new ButtonListener(game.getManager()) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentImage == imageList.size()){
                    game.setScreen(game.menuScreen);
                }else {
                    nextImage();
                }
            }
        });

        //text.setFontScale(2,2);
        //text.setPosition(Constants.WIDTH_SCREEN/2 - text.getWidth(), Constants.HEIGHT_SCREEN/2 - text.getHeight());

        menu.setSize(240, 240);
        menu.getLabel().setFontScale(4, 4);
        menu.setPosition(80, 80);

        back.setSize(120, 480);
        back.setPosition(40, Constants.HEIGHT_SCREEN/2 - back.getHeight()/2);

        next.setSize(120, 480);
        next.setPosition( Constants.WIDTH_SCREEN - 40 - next.getWidth(), Constants.HEIGHT_SCREEN/2 - next.getHeight()/2);

        background.setPosition(0,0);
        background.setSize(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN);

        stage.addActor(background);
        //stage.addActor(menu);
        stage.addActor(back);
        stage.addActor(next);

        for (int i = 1; i <= 5; i++){
            String imageName = "howto-"+i+".png";
            Gdx.app.log("debug howToPlay", imageName);
            imageList.add(this.game.getManager().get(imageName, Texture.class));
        }
        currentImage = 1;
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

        currentImage = 1;
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

        this.background.setDrawable(new TextureRegionDrawable(new TextureRegion(imageList.get(currentImage-1))));

        stage.act();
        stage.draw();
    }

    private void backImage() {
        Gdx.app.log("debug back", String.valueOf(this.currentImage));
        if(this.currentImage > 1){
            Gdx.app.log("debug back inside", String.valueOf(this.currentImage));

            this.currentImage -= 1;
        }
    }

    private void nextImage() {
        Gdx.app.log("debug next", String.valueOf(this.currentImage));

        if(this.currentImage < this.imageList.size()){
            Gdx.app.log("debug next inside", String.valueOf(this.currentImage));

            this.currentImage += 1;
        }
    }
}

