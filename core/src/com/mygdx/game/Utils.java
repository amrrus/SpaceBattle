package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.entities.AsteroidEntity;
import com.mygdx.game.entities.EntityFactory;

import java.util.ArrayList;

/**
 * Created by patry on 23/03/2018.
 */

public class Utils extends Actor {
    private static EntityFactory factory;
    World world;
    Texture texture;
    Float radius = 1f;
    Vector2 position;
    Vector2 impulse;

    public Utils() {
        //TODO: terminar


    }
}

