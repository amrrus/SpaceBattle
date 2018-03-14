package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;

import Connections.Connection;

public class TopPlayerEntity extends Actor {

    private Texture texture;
    private Integer moveSign;
    private Boolean keepMoving;
    private Boolean alive;
    private Connection conn;

    protected TopPlayerEntity( Texture texture,Connection conn) {
        this.texture = texture;
        this.moveSign = 0;
        this.keepMoving = false;
        this.conn = conn;
        setPosition(-0.5f*Constants.PIXELS_IN_METER,5f*Constants.PIXELS_IN_METER);
        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(),0,0, getWidth(), getHeight(),1f,1f,getRotation(),0,0,158,258,false,false);
    }

    public void act(float delta) {
        if (conn.getClientId()==1) {
            if (!keepMoving && Gdx.input.justTouched()) {
                if (Gdx.input.getX() < (Gdx.graphics.getWidth() / 2)) {
                    moveSign = -1;
                    keepMoving = true;
                    //emit moveLeft event
                    conn.moveTop(moveSign);
                } else {
                    moveSign = 1;
                    keepMoving = true;
                    //emit moveRight event
                    conn.moveTop(moveSign);
                }
            }
            if (keepMoving && !Gdx.input.isTouched()) {
                keepMoving = false;
                moveSign = 0;
                //emit stop move
                conn.moveTop(moveSign);
            }
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setPosition(float x, float y,float alpha) {
        super.setPosition(x*Constants.PIXELS_IN_METER, y*Constants.PIXELS_IN_METER);
        this.setRotation(alpha);
    }
}

