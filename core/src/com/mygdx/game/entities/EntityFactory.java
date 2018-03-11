package com.mygdx.game.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashMap;
import java.util.Map;

import Connections.Connection;


/**
 * This class creates entities using Factory Methods.
 */
public class EntityFactory {

    private AssetManager manager;

    private HashMap<Integer,Body> asteroids;

    /**
     * Create a new entity factory using the provided asset manager.
     * @param manager   the asset manager used to generate things.
     */
    public EntityFactory(AssetManager manager) {
        this.manager = manager;
        this.asteroids = new HashMap<Integer, Body>();


    }

    /**
     * Create a player using the default texture.
     * @param world     world where the player will have to live in.
     * @param position  initial position ofr the player in the world (meters,meters).
     * @return          a player.
     */
    public TopPlayerEntity createTopPlayer(World world, Vector2 position) {
        Texture playerTexture = manager.get("blueShipDown.png");
        return new TopPlayerEntity(world, playerTexture, position);
    }

    public BottomPlayerEntity createBottomPlayer(World world, Vector2 position, Connection conn) {
        Texture playerTexture = manager.get("blueShipUp.png");
        return new BottomPlayerEntity(world, playerTexture, position,conn);
    }

    public AsteroidEntity createAsteroid(World world, Vector2 position, Vector2 impulse, Integer idAsteroid,Float radius){
        Texture asteroidTexture = manager.get("asteroid.png");
        AsteroidEntity a = new AsteroidEntity(world, asteroidTexture, position, impulse,radius);
        asteroids.put(idAsteroid,a.getBody());
        System.out.println(asteroids.keySet());
        return a;
    }

    public void deleteAsteroid(World world,Integer idAsteroid){
        if (asteroids.containsKey(idAsteroid)) {
            world.destroyBody(asteroids.get(idAsteroid));
            asteroids.remove(idAsteroid);
        }
    }
 }

