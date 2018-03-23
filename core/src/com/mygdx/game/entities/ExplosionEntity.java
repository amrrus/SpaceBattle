package com.mygdx.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;
import com.mygdx.game.Constants;


public class ExplosionEntity extends Actor{

    private Animation animation;
    private float time;
    private float size;

    public ExplosionEntity(Texture texture, Float x, Float y, float size){
        this.size = size;
        setPosition(x,y);
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/5, texture.getHeight()/2);
        TextureRegion[] regionsExplosion = new TextureRegion[10];
        int z=0;
        for(int i=0; i<2; i++) {
            for (int j = 0; j < 5; j++) {
                regionsExplosion[z] = tmp[i][j];
                z++;
            }
        }

        this.animation = new Animation(Constants.FRAME_DURATION, regionsExplosion);
        setSize(Constants.PIXELS_IN_METER * size, Constants.PIXELS_IN_METER * size);
        this.time = 0f;
    }

    public void draw(Batch batch, float parentAlpha){
        this.time += parentAlpha;
        TextureRegion frameActual = (TextureRegion) this.animation.getKeyFrame(this.time, false);
        batch.draw(frameActual, getX(), getY(), getWidth(), getHeight());

        if(this.animation.isAnimationFinished(time)){
            this.remove();
        }
    }
}
