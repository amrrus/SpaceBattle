package Connections;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.Constants;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entities.AsteroidEntity;
import com.mygdx.game.entities.ShotEntity;
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
    private Integer clientId;


    public Connection(GameScreen gs){
        this.gs=gs;
        this.clientId=0;
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

    public Integer getClientId(){
        return this.clientId;
    }
    public void connect(){
        mSocket.connect();
    }
    public void disconnect(){
        mSocket.disconnect();
    }

    public void move(Integer moveSing){
        if(getClientId()==0)
            moveBot(moveSing);
        else
            moveTop(moveSing);
    }

    public void moveTop(Integer moveSing){
        mSocket.emit("CS_moveTop",moveSing);
    }
    public void moveBot(Integer moveSing){
        mSocket.emit("CS_moveBot",moveSing);
    }

    {   setPos = new Emitter.Listener() {
        public void call(final Object... args){
            JsonValue data = new JsonReader().parse(args[0].toString());
                Integer id = data.getInt("id");
                float posx = data.getFloat("x");
                float posy = data.getFloat("y");
                float alpha = data.getFloat("alpha");
                //System.out.print("Cliente:"+id+", PosX:"+posx+", PosY:"+posy+"\n");
                if (id == 0) { //id player bottom is 0
                    gs.bottomPlayer.setPosition(posx, posy, alpha);
                } else if (id == 1) { //id player top is 1
                    gs.topPlayer.setPosition(posx, posy, alpha);
                }
        }
    };
    }

    {
        setId = new Emitter.Listener() {
            public void call(final Object... args){
                clientId = new Integer ((Integer) args[0]);
               // System.out.print("IdCliente: "+clientId+"\n");
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
                    AsteroidEntity a = gs.factory.createAsteroid(gs.world,new Vector2(x,y),new Vector2(vx,vy),id,radius);
                    gs.stage.addActor(a);

            }
        };
    }

    {   deleteAst = new Emitter.Listener() {
            public void call(final Object... args){
                Integer id = (Integer)args[0];
                gs.factory.deleteAsteroid(id);
                //System.out.println("Asteroid deleted, id: "+id);
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
                    ShotEntity a = gs.factory.createShot(gs.world,new Vector2(x,y),new Vector2(vx,vy),idShot,idClient);
                    gs.stage.addActor(a);

            }
        };
    }

    {   deleteShot = new Emitter.Listener() {
            public void call(final Object... args){
                Integer idShot = (Integer)args[0];
                gs.factory.deleteShot(idShot);
                //System.out.println("Shot deleted, id: "+idShot);
            }
        };
    }

    {   explosion = new Emitter.Listener() {
            public void call(final Object... args){
                JsonValue data = new JsonReader().parse(args[0].toString());
                    float x = data.getFloat("x");
                    float y = data.getFloat("y");
                    //System.out.println("Explosion produced: x: "+x+", y: "+y);
            }
        };
    }
    {   config = new Emitter.Listener() {
            public void call(final Object... args){
                JsonValue data = new JsonReader().parse(args[0].toString());
                    float ppm = data.getFloat("ppm");
                    //System.out.println("Config: ppm:"+ppm);
                    //Constants.PIXELS_IN_METER = ppm;


            }
        };
    }
}
