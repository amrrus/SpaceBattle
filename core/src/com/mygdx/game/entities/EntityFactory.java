package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import Connections.Connection;


/**
 * This class creates entities using Factory Methods.
 */
public class EntityFactory {

    private AssetManager manager;
    private GameScreen gs;
    private HashMap<Integer,AsteroidEntity> asteroids;
    private HashMap<Integer,ShotEntity> shots;
    private ConcurrencyManager cm;


    public EntityFactory(AssetManager manager, GameScreen gs) {
        this.manager = manager;
        this.gs = gs;
        this.asteroids = new HashMap<Integer, AsteroidEntity>();
        this.shots =  new HashMap<Integer, ShotEntity>();
        this.cm = new ConcurrencyManager(this,this.gs.world);
    }

    public PlayerEntity createTopPlayer() {
        Texture playerTexture = manager.get("blueShipDown.png");
        return new PlayerEntity(playerTexture, new Vector2(-0.5f, 5f));
    }

    public PlayerEntity createBottomPlayer() {
        Texture playerTexture = manager.get("blueShipUp.png");
        return new PlayerEntity(playerTexture, new Vector2(-0.5f, -6f));
    }

    public PlayerMoveControlEntity createPlayerMoveControl(Connection conn){
        return new PlayerMoveControlEntity(conn);
    }

    public AsteroidEntity createAsteroid(Vector2 position, Vector2 impulse, Integer idAsteroid,Float radius){
        Texture asteroidTexture = manager.get("asteroid.png");
        AsteroidEntity a = new AsteroidEntity(gs.world, asteroidTexture, position, impulse,radius);
        gs.stage.addActor(a);
        asteroids.put(idAsteroid,a);
        return a;
    }

    protected void deleteAsteroid(Integer idAsteroid){
        if (asteroids.containsKey(idAsteroid)) {
            AsteroidEntity a = asteroids.get(idAsteroid);
            a.detach();
            asteroids.remove(idAsteroid);
        }
    }

    protected ShotEntity createShot( Vector2 position, Vector2 impulse, Integer idShot, Integer idClient){
        Texture shotTexture = manager.get("bullet-green.png");
        ShotEntity s = new ShotEntity(gs.world, shotTexture, position, impulse,idClient);
        gs.stage.addActor(s);
        shots.put(idShot,s);
        return s;
    }

    protected void deleteShot(Integer idShot){
        if (shots.containsKey(idShot)) {
            shots.get(idShot).detach();
            shots.remove(idShot);
            //optimization required
        }
    }
    public ConcurrencyManager getConcurrencyManager(){
        return this.cm;
    }

    public ExplosionEntity createExplosion(Float x, Float y, float size){
        Texture explosionT = manager.get("explosion-transitions.png");
        ExplosionEntity explosion= new ExplosionEntity(explosionT, x, y, size);
        gs.stage.addActor(explosion);
        return explosion;
    }


}

