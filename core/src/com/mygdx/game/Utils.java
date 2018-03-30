package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.entities.AsteroidEntity;
import com.mygdx.game.entities.EntityFactory;

import java.util.ArrayList;

/**
 * Created by patry on 23/03/2018.
 */

public class Utils {
    EntityFactory factory;
    World world;
    ArrayList<Object> l;
    Texture texture;
    Vector2 position;
    Vector2 impulse;
    Float radius;
    private GameScreen gs;

    public Utils(EntityFactory ef, World world, GameScreen gs) {
        this.factory = ef;
        this.world = world;
        this.gs = gs;
        this.texture = new Texture("asteroid.png");
        this.position = new Vector2(MathUtils.random(),MathUtils.random());
        this.impulse = new Vector2(MathUtils.random(),MathUtils.random());
        this.radius = MathUtils.random(0.3f,1f);


    }

    public AsteroidEntity randomAsteroids(){
        AsteroidEntity ast = new AsteroidEntity(world, texture, position, impulse, radius);
        gs.stage.addActor(ast);
        return ast;
    }

}

