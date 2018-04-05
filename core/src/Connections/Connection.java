package Connections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.Constants;
import com.mygdx.game.GameScreen;

import java.io.StringWriter;
import java.net.URISyntaxException;
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
    private Emitter.Listener playerDeath;
    private GameScreen gs;

    private String room;

    public Connection(GameScreen gs){
        this.gs=gs;
        try {
            mSocket = IO.socket(Constants.SERVER_URL);
            Gdx.app.log("connection", mSocket.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on("update_player_position",setPos);
        mSocket.on("update_player_lives",setLives);
        mSocket.on("update_player_death",playerDeath);
        mSocket.on("update_player_shots",updateShots);
        mSocket.on("create_asteroid",createAst);
        mSocket.on("delete_asteroid", deleteAst);
        mSocket.on("create_shot",createShot);
        mSocket.on("delete_shot", deleteShot);
        mSocket.on("create_explosion", explosion);

        mSocket.emit("room", generateRoomMessage());

    }

    public void connect(){
        mSocket.connect();
    }
    public void disconnect(){
        mSocket.disconnect();
    }

    public void move(Integer moveSing){
        mSocket.emit("move_player",moveSing);
    }
    public void shoot(Boolean shoot){
        mSocket.emit("player_shooting",shoot);
    }

    private String generateRoomMessage(){
        this.room = "room";
        Json msg = new Json();
        StringWriter jsonText = new StringWriter();
        JsonWriter writer = new JsonWriter(jsonText);
        msg.setOutputType(JsonWriter.OutputType.json);
        msg.setWriter(writer);
        msg.writeObjectStart();
        msg.writeValue("room", room);
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

    {   playerDeath = new Emitter.Listener() {
        public void call(final Object... args){
            JsonValue data = new JsonReader().parse(args[0].toString());
            Integer playerId = data.getInt("playerId");
            //TODO: Stop client


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
}
