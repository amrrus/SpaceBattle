package com.mygdx.game.Connections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.Constants;
import com.mygdx.game.GameScreen;
import com.mygdx.game.MainGame;
import com.mygdx.game.MenuScreen;
import com.mygdx.game.RoomsList;

import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



public class Connection {
    private Socket mSocket;
    private Emitter.Listener config;
    private Emitter.Listener deleteAst;
    private Emitter.Listener createAst;
    private Emitter.Listener deleteShot;
    private Emitter.Listener createShot;
    private Emitter.Listener updateShots;
    private Emitter.Listener explosion;
    private Emitter.Listener setPos;
    private Emitter.Listener setLives;
    private Emitter.Listener endGame;
    private Emitter.Listener countdown;
    private Emitter.Listener startGame;
    private Emitter.Listener refreshRooms;
    private Emitter.Listener showGameScreen;
    private Emitter.Listener nick;

    private GameScreen gs;
    private MainGame game;
    private RoomsList roomListInstance;
    private MenuScreen menuScreen;

    private String room;
    private String botPlayerName;
    private String topPlayerName;

    public String getBotPlayerName() {
        return botPlayerName;
    }

    public String getTopPlayerName() {
        return topPlayerName;
    }

    public void setTopPlayerName(String topPlayerName) {
        if (topPlayerName.equals("")){
            this.topPlayerName = this.defaultTopPlayer;
        }else {
            this.topPlayerName = topPlayerName;
        }
    }

    private String defaultTopPlayer = "Esperando....";
    public Connection(){
        botPlayerName = "";
        topPlayerName = defaultTopPlayer;
        try {
            mSocket = IO.socket(Constants.SERVER_URL);
            Gdx.app.log("connection", mSocket.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.on("check_nick_result", nick);
        mSocket.on("start_game",startGame);
        mSocket.on("get_rooms", refreshRooms);
        mSocket.on("show_game_screen", showGameScreen);

    }
    public void setGameScreen(GameScreen gs){
        this.gs = gs;
        mSocket.on("update_player_position",setPos);
        mSocket.on("update_player_lives",setLives);
        mSocket.on("end_game",endGame);
        mSocket.on("update_player_shots",updateShots);
        mSocket.on("create_asteroid",createAst);
        mSocket.on("delete_asteroid", deleteAst);
        mSocket.on("create_shot",createShot);
        mSocket.on("delete_shot", deleteShot);
        mSocket.on("create_explosion", explosion);
        mSocket.on("countdown",countdown);

    }
    public void connect(){
        mSocket.connect();
    }
    public void disconnect(){
        mSocket.disconnect();
    }
    public Boolean connected(){
        return this.mSocket.connected();
    }
    public void removeGameEvents() {
        String[] events = {"update_player_position", "update_player_lives", "end_game",
                "update_player_shots", "create_asteroid", "delete_asteroid", "create_shot",
                "delete_shot", "create_explosion", "countdown"};

        for(String event: events){
            this.mSocket.off(event);
        }
    }

    public void move(Integer moveSing){
        mSocket.emit("move_player",moveSing);
    }
    public void shoot(Boolean shoot){
        mSocket.emit("player_shooting",shoot);
    }

    public String getNickName(){
        return this.room;
    }
    public void setNickName(String nickName){
        this.room=nickName;
    }

    public void getRooms(RoomsList roomListInstance) {
        this.roomListInstance = roomListInstance;
        mSocket.emit("get_rooms", "");
    }

    public void deleteRoom() {
        mSocket.emit("delete_room", "");
    }

    {
        refreshRooms = new Emitter.Listener() {
            public void call(final Object... args){
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        JsonValue data = new JsonReader().parse(args[0].toString());

                        ArrayList<String> rooms = new ArrayList<String>();
                        for (String room: data.get("rooms").asStringArray()){
                            rooms.add(room);
                        }
                        roomListInstance.updateRoomList(rooms);                    }
                });

            }
        };
    }

    public void createRoom() {
        botPlayerName=room;
        mSocket.emit("create_room", roomGenerateMessage());
    }

    public void joinRoom(String room) {
        mSocket.emit("join_room", roomJoinMessage(room));
    }

    private String roomGenerateMessage(){
        Json msg = new Json();
        StringWriter jsonText = new StringWriter();
        JsonWriter writer = new JsonWriter(jsonText);
        msg.setOutputType(JsonWriter.OutputType.json);
        msg.setWriter(writer);
        msg.writeObjectStart();
        msg.writeValue("nickName", getNickName());
        msg.writeObjectEnd();
        return msg.getWriter().getWriter().toString();
    }
    private String roomJoinMessage(String room){
        Json msg = new Json();
        StringWriter jsonText = new StringWriter();
        JsonWriter writer = new JsonWriter(jsonText);
        msg.setOutputType(JsonWriter.OutputType.json);
        msg.setWriter(writer);
        msg.writeObjectStart();
        msg.writeValue("room", room);
        msg.writeValue("nickName", getNickName());
        msg.writeObjectEnd();
        return msg.getWriter().getWriter().toString();
    }




    {   setPos = new Emitter.Listener() {
        public void call(final Object... args){
            JsonValue data = new JsonReader().parse(args[0].toString());
                Integer id = data.getInt("id");
                float posx = data.getFloat("x");
                float posy = data.getFloat("y");
                float alpha = data.getFloat("alpha");

                if (id == 0) { //id player bottom is 0
                    gs.bottomPlayer.setPosition(posx, posy, alpha);
                } else if (id == 1) { //id player top is 1
                    gs.topPlayer.setPosition(posx, posy, alpha);
                }
        }
    };
    }


    {   createAst = new Emitter.Listener() {
            public void call(final Object... args){
                JsonValue data = new JsonReader().parse(args[0].toString());
                Integer id = data.getInt("id");
                float x = data.getFloat("x");
                float y = data.getFloat("y");
                float vx = data.getFloat("vx");
                float vy = data.getFloat("vy");
                float radius= data.getFloat("radius");
                //System.out.println("Asteroid created: x:"+x+", y:"+y+", vx:"+vx+", vy:"+vy+", radio: "+radius);
                try{
                    gs.factory.getConcurrencyManager().addAsteroidToCreate(x,y,vx,vy,id,radius);
                }catch (InterruptedException e){
                    Gdx.app.log("ConcurrencyException","addAsteroidToCreate");
                }



            }
        };
    }

    {   deleteAst = new Emitter.Listener() {
            public void call(final Object... args){
                Integer id = (Integer)args[0];
                try{
                    gs.factory.getConcurrencyManager().addAsteroidToRemove(id);
                }catch (InterruptedException e){
                    Gdx.app.log("ConcurrencyException","addAsteroidToRemove");
                }
            }
        };
    }

    {   createShot = new Emitter.Listener() {
            public void call(final Object... args){
                JsonValue data = new JsonReader().parse(args[0].toString());
                Integer idShot = data.getInt("idShot");
                Integer idClient = data.getInt("idClient");
                float x = data.getFloat("x");
                float y = data.getFloat("y");
                float vx = data.getFloat("vx");
                float vy = data.getFloat("vy");
                //System.out.println("Shot created: x:"+x+", y:"+y+", vx:"+vx+", vy:"+vy+ ", idShot: "+ idShot + "idClient: " +idClient);
                try{
                    gs.factory.getConcurrencyManager().addShotToCreate(x, y, vx, vy, idShot, idClient);
                }catch (InterruptedException e){
                    Gdx.app.log("ConcurrencyException","addShotToCreate");
                }




            }
        };
    }

    {   deleteShot = new Emitter.Listener() {
            public void call(final Object... args){
                Integer idShot = (Integer)args[0];
                try{
                    gs.factory.getConcurrencyManager().addShotToRemove(idShot);
                }catch (InterruptedException e){
                    Gdx.app.log("ConcurrencyException","addShotToRemove");
                }
            }
        };
    }

    {   explosion = new Emitter.Listener() {
            public void call(final Object... args){
                JsonValue data = new JsonReader().parse(args[0].toString());
                float x = data.getFloat("x");
                float y = data.getFloat("y");
                float size = data.getFloat("size");
                gs.factory.createExplosion(x,y,size);

            }
        };
    }

    {   config = new Emitter.Listener() {
            public void call(final Object... args){
                JsonValue data = new JsonReader().parse(args[0].toString());
                //float ppm = data.getFloat("ppm");
                //TODO : sync constants with server


            }
        };
    }

    {   setLives = new Emitter.Listener() {
        public void call(final Object... args){
            JsonValue data = new JsonReader().parse(args[0].toString());
            Integer playerId = data.getInt("playerId");
            Integer lives = data.getInt("lives");
            if (playerId==0){
                gs.bottomPlayer.setLives(lives);
            }else{
                gs.topPlayer.setLives(lives);
            }


        }
    };
    }

    {   endGame = new Emitter.Listener() {
        public void call(final Object... args){
            JsonValue data = new JsonReader().parse(args[0].toString());
            Boolean loser = data.getBoolean("loser");
            gs.endGame(loser);
        }
    };
    }

    {   updateShots = new Emitter.Listener() {
        public void call(final Object... args){
            JsonValue data = new JsonReader().parse(args[0].toString());
            Integer playerId = data.getInt("playerId");
            Integer shots = data.getInt("shots");
            if (playerId==0){
                gs.bottomPlayer.setShots(shots);
            }else{
                gs.topPlayer.setShots(shots);
            }
        }
    };
    }

    {   showGameScreen = new Emitter.Listener() {
        public void call(final Object... args){
            Gdx.app.log("debug","game screen showed");
            roomListInstance.getGame().setScreen(roomListInstance.getGame().gameScreen);
        }
    };
    }

    {   countdown = new Emitter.Listener() {
            public void call(final Object... args){
                Integer count = (Integer)args[0];
                Gdx.app.log("debug","Countdown:"+count);
                gs.countdown = count;

            }
        };
    }

    {
        startGame = new Emitter.Listener() {
            public void call(final Object... args){
                JsonValue data = new JsonReader().parse(args[0].toString());
                botPlayerName = data.getString("bottom_player_name");
                topPlayerName = data.getString("top_player_name");
                String config = "";
                config = data.getString("con");
                Gdx.app.log("config",config+"...");
                String[] parameters = config.split(",");
                for (String p:parameters){
                    String[] keyValue= p.split(":");
                    if (keyValue.length==2) {
                        String key = keyValue[0];
                        String value = keyValue[1];
                        if (key.equals("SHOT_DENSITY")) {
                            Gdx.app.log("config", "SHOT_DENSITY:" + value);
                            Constants.SHOT_DENSITY = new Float(value);
                        }

                        if (key.equals("SHOT_RADIUS")) {
                            Gdx.app.log("config", "SHOT_RADIUS:" + value);
                            Constants.SHOT_RADIUS = new Float(value);
                        }

                    }
                }
                Gdx.app.log("startGame","start_server received");
                roomListInstance.getGame().setScreen(roomListInstance.getGame().waitingOpponentScreen);
            }
        };
    }

    {   nick = new Emitter.Listener() {
        public void call(final Object... args){
            JsonValue data = new JsonReader().parse(args[0].toString());
            Boolean res =  data.getBoolean(0);
            String nickname = data.getString(1);
            if (res) {
                setNickName(nickname);
                game.setScreen(game.roomsList);
            }else{
                menuScreen.errorNick();
            }
        }
    };
    }

    public void setMainGame(MainGame game){
        this.game=game;
    }
    public void setMenuScreen(MenuScreen menu){
        this.menuScreen = menu;
    }
    public void checkNickname(String nick){
        mSocket.emit("check_nick", nick);
    }

}
