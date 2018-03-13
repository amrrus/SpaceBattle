package Connections;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Constants;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.AsteroidEntity;
import com.mygdx.game.entities.EntityFactory;
import com.mygdx.game.entities.ShotEntity;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Emitter.Listener explosion;
    private Emitter.Listener setId;
    private Emitter.Listener setPos;
    private GameScreen gs;
    private Integer idClient;
    private Vector2 oy;


    public Connection(GameScreen gs){
        this.gs=gs;
        this.oy=new Vector2(0f,5.5f);
        try {
            mSocket = IO.socket(Constants.SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on("CR_setPos",setPos);
        mSocket.on("CR_setId",setId);
        mSocket.on("CR_createAst",createAst);
        mSocket.on("CR_deleteAst", deleteAst);
        mSocket.on("CR_createShot",createShot);
        mSocket.on("CR_deleteShot", deleteShot);
        mSocket.on("CR_explosion", explosion);

    }

    public void connect(){
        mSocket.connect();
    }
    public void disconnect(){
        mSocket.disconnect();
    }

    public void move(Integer moveSing){
        JSONObject msg = new JSONObject();
        try {
            msg.put("id", this.idClient);
            msg.put("moveSing",moveSing);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("CS_move",msg);
    }

    {   setPos = new Emitter.Listener() {
        public void call(final Object... args){
            JSONObject data = (JSONObject) args[0];
            int id = 999;
            float posx= 999;
            float posy= 999;
            float alpha=0;
            try {
                id = data.getInt("id");
                posx = data.getFloat("x");
                posy = data.getFloat("y");
                alpha = data.getFloat("alpha");
                //System.out.print("Cliente:"+id+", PosX:"+posx+", PosY:"+posy+"\n");

            } catch (JSONException e) {
                System.out.println("Error to receive message.");
            }
            if (id==0){ //id player bottom is 0
                gs.bottomPlayer.setPosition(posx,posy,alpha);
            }else  if (id==1){ //id player top is 1
                gs.topPlayer.setPosition(posx,posy,alpha);
            }
        }
    };
    }

    {   setId = new Emitter.Listener() {
        public void call(final Object... args){
            idClient = new Integer ((Integer) args[0]);
            //System.out.print("IdCliente: "+idClient+"\n");

        }
    };
    }

    {   createAst = new Emitter.Listener() {
        public void call(final Object... args){
            JSONObject data = (JSONObject) args[0];
            Integer id=0;
            float x = 999;
            float y = 999;
            float vx = 999;
            float vy = 999;
            Float radius = 1f;
            try {
                id = data.getInt("id");
                x = data.getFloat("x");
                y = data.getFloat("y");
                vx = data.getFloat("vx");
                vy = data.getFloat("vy");
                radius= data.getFloat("radius");
                //System.out.println("Asteroid created: x:"+x+", y:"+y+", vx:"+vx+", vy:"+vy+", radio: "+radius);
                AsteroidEntity a = gs.factory.createAsteroid(gs.world,new Vector2(x,y),new Vector2(vx,vy),id,radius);
                gs.stage.addActor(a);
            } catch (JSONException e) {
                System.out.println("Error to receive message.");
            }

        }
    };
    }

    {   deleteAst = new Emitter.Listener() {
        public void call(final Object... args){
            Integer id = (Integer)args[0];
            gs.factory.deleteAsteroid(gs.world,id);
            //System.out.println("Asteroid deleted, id: "+id);
        }
    };
    }

    {   createShot = new Emitter.Listener() {
        public void call(final Object... args){
            JSONObject data = (JSONObject) args[0];
            Integer idShot=0;
            Integer idClient=0;
            float x = 999;
            float y = 999;
            float vx = 999;
            float vy = 999;
            try {
                idShot = data.getInt("idShot");
                idClient = data.getInt("idClient");
                x = data.getFloat("x");
                y = data.getFloat("y");
                vx = data.getFloat("vx");
                vy = data.getFloat("vy");
                //System.out.println("Shot created: x:"+x+", y:"+y+", vx:"+vx+", vy:"+vy+ ", idShot: "
                //        + idShot + "idClient: " +idClient);
                ShotEntity a = gs.factory.createShot(gs.world,new Vector2(x,y),new Vector2(vx,vy),idShot,idClient);
                gs.stage.addActor(a);
            } catch (JSONException e) {
                System.out.println("Error to receive message.");
            }

        }
    };
    }

    {   deleteShot = new Emitter.Listener() {
        public void call(final Object... args){
            Integer idShot = (Integer)args[0];
            gs.factory.deleteShot(gs.world,idShot);
            //System.out.println("Shot deleted, id: "+idShot);
        }
    };
    }

    {   explosion = new Emitter.Listener() {
        public void call(final Object... args){
            JSONObject data = (JSONObject) args[0];
            float x = 0;
            float y = 0;
            try {
                x = data.getFloat("x");
                y = data.getFloat("y");
                //System.out.println("Explosion produced: x: "+x+", y: "+y);
                //Constants.PIXELS_IN_METER = ppm;
            } catch (JSONException e) {
                System.out.println("Error to receive message.");
            }

        }
    };
    }
    {   config = new Emitter.Listener() {
        public void call(final Object... args){
            JSONObject data = (JSONObject) args[0];
            float ppm = 999;
            try {
                ppm = data.getFloat("ppm");
                //System.out.println("Config: ppm:"+ppm);
                //Constants.PIXELS_IN_METER = ppm;
            } catch (JSONException e) {
                System.out.println("Error to receive message.");
            }

        }
    };
    }
}
