package com.mygdx.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;

public class PlayerEntity extends Actor {

    private Texture texture;
    private Boolean alive;
    private Integer lives;
    private Integer shots;

    PlayerEntity( Texture texture, Vector2 initialPos) {
        this.texture = texture;
        setPosition(initialPos.x* Constants.PIXELS_IN_METER,initialPos.y*Constants.PIXELS_IN_METER);
        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
        this.lives = 3;
        this.shots = 5;

    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,
                getX(), getY(),
                0,0,
                getWidth(), getHeight(),
                1f,1f,
                getRotation(),
                0,0,
                158,258,
                false,false);
    }

    public void setPosition(float x, float y,float alpha) {
        super.setPosition(x*Constants.PIXELS_IN_METER, y*Constants.PIXELS_IN_METER);
        this.setRotation(alpha);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Integer getLives(){ return this.lives; }

    public void setLives(Integer lives){ this.lives = lives; }

    public void setShots(Integer shots) {
        if (shots<=Constants.MAX_NUMBER_SHOTS_STORED && shots >=0)
            this.shots = shots;
    }

    public Integer getShots() { return shots; }


}
