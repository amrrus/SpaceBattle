package com.mygdx.game.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This class creates entities using Factory Methods.
 */
public class EntityFactory {

    private AssetManager manager;

    /**
     * Create a new entity factory using the provided asset manager.
     * @param manager   the asset manager used to generate things.
     */
    public EntityFactory(AssetManager manager) {
        this.manager = manager;
    }

    /**
     * Create a player using the default texture.
     * @param world     world where the player will have to live in.
     * @param position  initial position ofr the player in the world (meters,meters).
     * @return          a player.
     */
    public TopPlayerEntity createTopPlayer(World world, Vector2 position) {
        Texture playerTexture = manager.get("blueShipDown.png");
        return new TopPlayerEntity(world, playerTexture, position, this);
    }

    public BottomPlayerEntity createBottomPlayer(World world, Vector2 position) {
        Texture playerTexture = manager.get("blueShipUp.png");
        return new BottomPlayerEntity(world, playerTexture, position, this);
    }

    public Asteroide createAsteroide(World world, Vector2 position){
        Texture asteroideTexture = manager.get("asteroide.png");
        return new Asteroide(world, asteroideTexture, position, new Vector2());
    }

    public LimiterEntity createLimiter(World world, Vector2 position) {
        return new LimiterEntity(world, position);
    }

    public ShotEntity createShot(World world, Vector2 position, Vector2 direction) {
        Texture shotTexture = manager.get("shot.png");
        return new ShotEntity(world, shotTexture, position, direction);
    }
 }

