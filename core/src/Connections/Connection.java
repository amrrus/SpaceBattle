package Connections;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Constants;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.AsteroidEntity;
import com.mygdx.game.entities.EntityFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Antonio M on 10/03/2018.
 */

public class Connection {
    private Socket mSocket;
    private Emitter.Listener config;
    private Emitter.Listener deleteAst;
    private Emitter.Listener createAst;
    private Emitter.Listener setId;
    private Emitter.Listener setPos;
    private GameScreen gs;
    private Integer idClient;


    public Connection(GameScreen gs){
        this.gs=gs;
        try {
            mSocket = IO.socket(Constants.SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on("CR_setPos",setPos);
        mSocket.on("CR_setId",setId);
        mSocket.on("CR_deleteAst", deleteAst);
        mSocket.on("CR_createAst",createAst);
    }

    public void connect(){
        mSocket.connect();
    }
    public void disconnect(){
        mSocket.disconnect();
    }
    public void setIdClient(Integer idClient){
        this.idClient = idClient;
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
            try {
                id = data.getInt("id");
                posx = data.getFloat("x");
                posy = data.getFloat("y");
                //System.out.print("Cliente:"+id+", PosX:"+posx+", PosY:"+posy+"\n");
            } catch (JSONException e) {
                System.out.println("Error to receive message.");
            }
            if (id==0){
                gs.bottomPlayer.setPosition((posx - 0.5f) * Constants.PIXELS_IN_METER,
                        (posy - 0.5f) * Constants.PIXELS_IN_METER);
            }else  if (id==1){
                gs.topPlayer.setPosition((posx - 0.5f) * Constants.PIXELS_IN_METER,
                        (posy - 0.5f) * Constants.PIXELS_IN_METER);
            }
        }
    };
    }


    {   setId = new Emitter.Listener() {
        public void call(final Object... args){
            idClient = new Integer ((Integer) args[0]);
            System.out.print("IdCliente: "+idClient+"\n");

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
                System.out.println("Asteroid created: x:"+x+", y:"+y+", vx:"+vx+", vy:"+vy+", radio: "+radius);
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
            System.out.println("Asteroid deleted, id: "+id);
        }
    };
    }

    {   config = new Emitter.Listener() {
        public void call(final Object... args){
            JSONObject data = (JSONObject) args[0];
            float ppm = 999;
            float y= 999;
            float vx= 999;
            float vy=999;
            try {
                ppm = data.getFloat("ppm");
                //System.out.print("Asteroid created: x:"+x+", y:"+y+", vx:"+vx+", vy:"+vy+"\n");
                //Constants.PIXELS_IN_METER = ppm;
            } catch (JSONException e) {
                System.out.println("Error to receive message.");
            }

        }
    };
    }
}
