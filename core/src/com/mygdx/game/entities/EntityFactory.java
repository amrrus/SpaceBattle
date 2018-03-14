package com.mygdx.game.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.util.HashMap;
import Connections.Connection;


/**
 * This class creates entities using Factory Methods.
 */
public class EntityFactory {

    private AssetManager manager;
    private HashMap<Integer,AsteroidEntity> asteroids;
    private HashMap<Integer,ShotEntity> shots;

    public EntityFactory(AssetManager manager) {
        this.manager = manager;
        this.asteroids = new HashMap<Integer, AsteroidEntity>();
        this.shots =  new HashMap<Integer, ShotEntity>();
    }

    public TopPlayerEntity createTopPlayer(Connection conn) {
        Texture playerTexture = manager.get("blueShipDown.png");
        return new TopPlayerEntity(playerTexture, conn);
    }

    public BottomPlayerEntity createBottomPlayer( Connection conn) {
        Texture playerTexture = manager.get("blueShipUp.png");
        return new BottomPlayerEntity(playerTexture,conn);
    }

    public AsteroidEntity createAsteroid(World world, Vector2 position, Vector2 impulse, Integer idAsteroid,Float radius){
        Texture asteroidTexture = manager.get("asteroid.png");
        AsteroidEntity a = new AsteroidEntity(world, asteroidTexture, position, impulse,radius);
        asteroids.put(idAsteroid,a);
        return a;
    }

    public void deleteAsteroid(Integer idAsteroid){
        if (asteroids.containsKey(idAsteroid)) {
            AsteroidEntity s = asteroids.get(idAsteroid);
            s.detach();
            asteroids.remove(idAsteroid);
            //optimization required
        }
    }

    public ShotEntity createShot(World world, Vector2 position, Vector2 impulse, Integer idShot, Integer idClient){
        Texture shotTexture = manager.get("asteroid.png");
        ShotEntity s = new ShotEntity(world, shotTexture, position, impulse,idClient);
        shots.put(idShot,s);
        return s;
    }

    public void deleteShot(Integer idShot){
        if (shots.containsKey(idShot)) {
            shots.get(idShot).detach();
            shots.remove(idShot);
            //optimization required
        }
    }

 }

