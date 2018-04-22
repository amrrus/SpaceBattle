package com.mygdx.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class ConcurrencyManager {

    private EntityFactory ef;
    private ArrayList<ArrayList<Object>> asteroidsToCreate;
    private ArrayList<Integer> asteroidsToRemove;
    private ArrayList<ArrayList<Object>> shotsToCreate;
    private ArrayList<Integer> shotsToRemove;
    private Boolean creatingAsteroids;
    private Boolean removingAsteroids;
    private Boolean creatingShots;
    private Boolean removingShots;
    private World world;
    /*private Boolean addingAsteroidToCreate;
    private Boolean addingAsteroidToRemove;
    private Boolean addingShotToCreate;
    private Boolean addingShotToRemove;*/

    ConcurrencyManager(EntityFactory ef, World world){
        this.ef = ef;
        this.world = world;
        this.asteroidsToCreate = new ArrayList<ArrayList<Object>>();
        this.asteroidsToRemove = new ArrayList<Integer>();
        this.shotsToCreate = new ArrayList<ArrayList<Object>>();
        this.shotsToRemove = new ArrayList<Integer>();

        this.creatingAsteroids = false;
        this.removingAsteroids = false;

        this.creatingShots = false;
        this.removingShots = false;


        /*this.addingAsteroidToCreate = false;
        this.addingAsteroidToRemove = false;
        this.addingShotToCreate = false;
        this.addingShotToRemove = false;*/
    }

    public synchronized void checkAsteroidsToRemove(){
        if (this.asteroidsToRemove.size()>0 ){
            this.removingAsteroids = true;
            for(Integer a : this.asteroidsToRemove){
                Gdx.app.log("debug", "AsteroidRemoved: "+a);
                ef.deleteAsteroid(a);
            }
            this.asteroidsToRemove.clear();
            this.removingAsteroids = false;
        }
        notify();
    }

    public synchronized void checkShotsToRemove(){
        if (this.shotsToRemove.size()>0){
            this.removingShots = true;
            for(Integer s : this.shotsToRemove){
                Gdx.app.log("debug", "ShotRemoved: "+s);
                ef.deleteShot(s);
            }
            this.shotsToRemove.clear();
            this.removingShots = false;
            notify();
        }
    }
    public synchronized void checkShotsToCreate(){
        if (this.shotsToCreate.size()>0){
            this.creatingShots = true;
            for(ArrayList<Object> l:this.shotsToCreate){
                Vector2 pos = new Vector2(Double.valueOf(l.get(0).toString()).floatValue(),
                        Double.valueOf(l.get(1).toString()).floatValue());
                Vector2 impulse = new Vector2(Double.valueOf(l.get(2).toString()).floatValue(),
                        Double.valueOf(l.get(3).toString()).floatValue());
                Integer idShot= Integer.valueOf(l.get(4).toString());
                Integer idClient = Integer.valueOf(l.get(5).toString());
                ef.createShot(pos,impulse, idShot,idClient);
                Gdx.app.log("debug", "ShotCreated: id: "+idShot +", player: "+idClient);
            }
            this.shotsToCreate.clear();
            this.creatingShots = false;
            notify();
        }
    }

    public synchronized void checkAsteroidsToCreate(){
        if (this.asteroidsToCreate.size()>0){
            this.creatingAsteroids=true;
            for(ArrayList<Object> l:this.asteroidsToCreate){
                Vector2 pos = new Vector2(Double.valueOf(l.get(0).toString()).floatValue(),
                        Double.valueOf(l.get(1).toString()).floatValue());
                Vector2 impulse = new Vector2(Double.valueOf(l.get(2).toString()).floatValue(),
                        Double.valueOf(l.get(3).toString()).floatValue());
                Integer idAst= Integer.valueOf(l.get(4).toString());
                float radius = Double.valueOf(l.get(5).toString()).floatValue();
                Gdx.app.log("debug", "AteroidCreated: id: "+idAst);
                ef.createAsteroid(pos,impulse,idAst,radius);
            }
            this.asteroidsToCreate.clear();
            this.creatingAsteroids = false;
            notify();
        }
    }

    public synchronized void addShotToRemove(Integer idShot) throws InterruptedException{
        while (world.isLocked() || this.removingShots) {wait();}
        this.shotsToRemove.add(idShot);
    }
    public synchronized void addAsteroidToRemove(Integer idAst) throws InterruptedException{
        while (world.isLocked() || this.removingAsteroids) {wait();}
        this.asteroidsToRemove.add(idAst);
    }
    public synchronized void addShotToCreate(float x,float y,float vx,float vy,Integer idShot,Integer idPlayer) throws InterruptedException{
        ArrayList<Object> l = new ArrayList<Object>();
        l.add(x);l.add(y);l.add(vx);l.add(vy);l.add(idShot);l.add(idPlayer);
        while (world.isLocked() || this.creatingShots) {wait();}
        this.shotsToCreate.add(l);
    }
    public synchronized void addAsteroidToCreate(float x, float y, float vx,float vy,Integer idAst,float radius) throws InterruptedException{
        ArrayList<Object> l = new ArrayList<Object>();
        l.add(x);l.add(y);l.add(vx);l.add(vy);l.add(idAst);l.add(radius);
        while (world.isLocked() || this.creatingAsteroids) {wait();}
        this.asteroidsToCreate.add(l);
    }


}
