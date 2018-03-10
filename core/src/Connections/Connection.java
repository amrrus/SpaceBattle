package Connections;

import com.mygdx.game.Constants;
import com.mygdx.game.GameScreen;

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
    Socket mSocket;
    private Emitter.Listener createAsteroid;
    private Emitter.Listener setId;
    private Emitter.Listener setPos;
    GameScreen gs;


    public Connection(GameScreen gs){
        this.gs=gs;
        try {
            mSocket = IO.socket(Constants.SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on("pos",setPos);
        mSocket.on("setId",setId);
        mSocket.on("createAsteroid",createAsteroid);
    }
    public void connect(){
        mSocket.connect();
    }
    public void disconnect(){
        mSocket.disconnect();
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
                System.out.print("Error to receive message." + System.getProperty("line.separator"));
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
            gs.idClient = new Integer ((Integer) args[0]);
            System.out.print("IdCliente: "+gs.idClient+"\n");

        }
    };
    }


    {   createAsteroid = new Emitter.Listener() {
        public void call(final Object... args){
            JSONObject data = (JSONObject) args[0];
            float x = 999;
            float y= 999;
            float vx= 999;
            float vy=999;
            try {
                x = data.getInt("x");
                y = data.getFloat("y");
                vx = data.getFloat("vx");
                vy = data.getFloat("vy");
                System.out.print("Asteroid created: x:"+x+", y:"+y+", vx:"+vx+", vy:"+vy+"\n");
            } catch (JSONException e) {
                System.out.print("Error to receive message." + System.getProperty("line.separator"));
            }

        }
    };
    }
}
