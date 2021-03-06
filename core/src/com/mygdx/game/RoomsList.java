package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.GUI.EmptyRoom;
import com.mygdx.game.GUI.RoomGUI;
import java.util.ArrayList;
import com.mygdx.game.Connections.Connection;
import com.mygdx.game.Utils.ButtonListener;

public class RoomsList extends BaseScreen {

    private Connection conn;
    private Stage stage;
    private Skin skin,skin2;
    private TextButton createRoom;
    private TextButton back;
    private BitmapFont title;
    private ImageButton refresh;
    private int heightHeader;
    private Table Rooms;
    private EmptyRoom emptyHead;
    private Texture headerTexture;
    private RoomsList roomListInstance = this;

    public RoomsList(final MainGame game, final Connection conn){
        super(game);
        this.conn=conn;
        heightHeader = 120;

        stage = new Stage(new FitViewport(Constants.WIDTH_SCREEN, Constants.HEIGHT_SCREEN));

        skin = new Skin(Gdx.files.internal("skin/skin-composer-ui.json"));
        skin2 = new Skin(Gdx.files.internal("skin/uiskin.json"));

        Pixmap pix = new Pixmap(Constants.WIDTH_SCREEN,heightHeader, Pixmap.Format.RGBA8888);
        pix.setColor(new Color(0x468c7eff));
        pix.fill();
        headerTexture= new Texture(pix,true);

        createRoom = new TextButton("Crear", skin);
        createRoom.setSize(250, 120);
        createRoom.getLabel().setFontScale(4, 4);
        createRoom.setPosition(Constants.WIDTH_SCREEN-310, 60);
        createRoom.addCaptureListener(new ButtonListener(game.getManager()){
            public void changed(ChangeEvent event, Actor actor) {
                conn.createRoom();
                game.setScreen(game.waitingOpponentScreen);
            }
        });
        back = new TextButton("Menu", skin);
        back.setSize(250, 120);
        back.getLabel().setFontScale(4, 4);
        back.setPosition(60, 60);
        back.addCaptureListener(new ButtonListener(game.getManager()) {
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.menuScreen);
            }
        });

        emptyHead= new EmptyRoom(heightHeader);

        title = new BitmapFont();
        title.getData().setScale(4);
        title.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        refresh = new ImageButton(skin, "default");
        refresh.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(game.getManager().get("refresh.png", Texture.class)));
        refresh.setSize(150, 120);

        refresh.setPosition(Constants.WIDTH_SCREEN-310,Constants.HEIGHT_SCREEN-300);
        refresh.addCaptureListener(new ButtonListener(game.getManager()) {
            public void changed(ChangeEvent event, Actor actor) {
                conn.getRooms(roomListInstance);
            }
        });
    }
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
        conn.getRooms(roomListInstance);

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
        stage.getBatch().begin();
        stage.getBatch().draw(headerTexture,0,Constants.HEIGHT_SCREEN-heightHeader,Constants.WIDTH_SCREEN,heightHeader);
        title.draw(stage.getBatch(),"Salas",Constants.WIDTH_SCREEN/2-90,Constants.HEIGHT_SCREEN-40);
        title.draw(stage.getBatch(),conn.getNickName(),100,Constants.HEIGHT_SCREEN-40);
        stage.getBatch().end();
    }
    public void updateRoomList(ArrayList<String> l){

        Table scrollTable = new Table();
        scrollTable.add(emptyHead);
        scrollTable.padRight(25);

        if(l.size() > 0) {
            for (final String room : l) {
                scrollTable.row();
                scrollTable.add(new RoomGUI(room, 250));

                TextButton playRoom = new TextButton("VS", skin);
                playRoom.getLabel().setFontScale(4, 4);
                playRoom.setSize(200, 200);
                playRoom.addCaptureListener(new ButtonListener(game.getManager()) {
                    public void changed(ChangeEvent event, Actor actor) {
                        conn.joinRoom(room);
                    }
                });
                scrollTable.add(playRoom).minSize(200, 200);


            }
        }else{
            scrollTable.row();
            scrollTable.add(new RoomGUI("No hay salas", 250)).minWidth(Constants.WIDTH_SCREEN/2 + 200);
        }

        final ScrollPane scroller = new ScrollPane(scrollTable,skin2);

        Rooms = new Table();
        Rooms.setBackground(new TextureRegionDrawable(new TextureRegion(
                game.getManager().get("background.png", Texture.class))));
        Rooms.setFillParent(true);
        Rooms.add(scroller).fill().expandY();

        stage.clear();
        stage.addActor(Rooms);
        stage.addActor(createRoom);

        stage.addActor(createRoom);
        stage.addActor(back);
        stage.addActor(refresh);
    }

    public MainGame getGame() {
        return this.game;
    }

}
