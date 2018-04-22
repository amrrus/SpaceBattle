package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.mygdx.game.Connections.Connection;

public class Controllers extends Actor implements InputProcessor{

    private GameScreen gs;
    private Connection conn;
    private Boolean accelerometerAvail;
    private Boolean keepMoving;
    private Integer moveSign;

    public Controllers(GameScreen gs,Connection conn){
        this.gs= gs;
        this.conn=conn;
        this.keepMoving = false;
        this.moveSign = 0;
        accelerometerAvail = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);
        if(accelerometerAvail) {
            Gdx.app.log("debug","actor added");
            this.gs.stage.addActor(this);
        }
        Gdx.input.setInputProcessor(this);
    }

    public void act(float delta) {
        super.act(delta);
        if (!keepMoving && getTilt()!=0) {
            moveSign = getTilt();
            keepMoving = true;
            conn.move(moveSign);
        }
        if (keepMoving && getTilt()==0) {
            keepMoving = false;
            moveSign = 0;
            conn.move(moveSign);
        }

    }

    private Integer  getTilt(){
        float gyroY = Gdx.input.getAccelerometerY();
        if (gyroY>1){
            return 1;
        }else if (gyroY<-1){
            return -1;
        }
        return 0;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == 21){
            conn.move(-1);
        }else if(keycode == 22){
            conn.move(1);
        }
        if(keycode == 20 || keycode == 19 || keycode == 62){
            this.conn.shoot(true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        Gdx.app.log("debug","keycode:"+keycode);
        if(keycode == 21 || keycode == 22){
            conn.move(0);
        }
        if(keycode == 20 || keycode == 19 || keycode == 62){
            this.conn.shoot(false);
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.conn.shoot(true);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.conn.shoot(false);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
