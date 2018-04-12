package com.mygdx.game.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.Constants;

import java.util.ArrayList;


public class RoomGUI extends Actor {
    private TextButton play;
    private BitmapFont text;
    private String roomName;

    public RoomGUI(String room, float height){
        setSize(Constants.WIDTH_SCREEN/3,height);
        setDebug(true);
        roomName=room;
        text= new BitmapFont();
        text.getData().setScale(6);
        text.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    public void draw(Batch batch, float parentAlpha) {
        text.draw(batch,roomName,getX()+200,getY()+getHeight()/2+40);
    }



}
