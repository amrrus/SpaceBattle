package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import Connections.Connection;

public class PlayerMoveControlEntity extends Actor {

    private Boolean keepMoving;
    private Integer moveSign;
    private Connection conn;

    PlayerMoveControlEntity(Connection conn){
        this.conn = conn;
        this.keepMoving = false;
        this.moveSign = 0;
    }


    public void act(float delta) {
        if (!keepMoving && Gdx.input.justTouched()) {
            if (Gdx.input.getX() < (Gdx.graphics.getWidth() / 2)) {
                moveSign = -1;
                keepMoving = true;
            } else {
                moveSign = 1;
                keepMoving = true;
            }
            conn.move(moveSign);
        }
        if (keepMoving && !Gdx.input.isTouched()) {
            keepMoving = false;
            moveSign = 0;
            conn.move(moveSign);
        }
    }

}
