package com.mygdx.game.Entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import java.util.HashMap;

/**
 * This class creates entities using Factory Methods.
 */

public class EntityFactory {

    private AssetManager manager;
    private GameScreen gs;
    private HashMap<Integer,AsteroidEntity> asteroids;
    private HashMap<Integer,ShotEntity> shots;
    private ConcurrencyManager cm;

    private Sound shotSound;
    private Sound explosionSound;

    public EntityFactory(AssetManager manager, GameScreen gs) {
        this.manager = manager;
        this.gs = gs;
        this.asteroids = new HashMap<Integer, AsteroidEntity>();
        this.shots =  new HashMap<Integer, ShotEntity>();
        this.cm = new ConcurrencyManager(this,this.gs.world);

        shotSound = this.getManager().get("audio/shot.ogg");
        explosionSound = this.getManager().get("audio/explosion.ogg");
    }

    public PlayerEntity createTopPlayer() {
        Texture playerTexture = manager.get("blueShipDown.png");
        PlayerEntity top = new PlayerEntity(playerTexture, new Vector2(-0.5f, 5f));
        this.gs.stage.addActor(top);
        return top;
    }

    public PlayerEntity createBottomPlayer() {
        Texture playerTexture = manager.get("blueShipUp.png");
        PlayerEntity bottom = new PlayerEntity(playerTexture, new Vector2(-0.5f, -6f));
        this.gs.stage.addActor(bottom);
        return bottom;
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
        shotSound.play();
        return s;
    }

    protected void deleteShot(Integer idShot){
        if (shots.containsKey(idShot)) {
            shots.get(idShot).detach();
            shots.remove(idShot);
            //optimization required
        }
    }
    public AssetManager getManager() {
        return this.manager;
    }

    public ConcurrencyManager getConcurrencyManager(){
        return this.cm;
    }

    public ExplosionEntity createExplosion(Float x, Float y, float size){
        Texture explosionT = manager.get("explosion-transitions.png");
        ExplosionEntity explosion = new ExplosionEntity(explosionT, x, y, size);
        gs.stage.addActor(explosion);
        explosionSound.play();
        return explosion;
    }


}

