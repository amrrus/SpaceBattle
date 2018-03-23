package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;

import static com.mygdx.game.Constants.FRAME_DURATION;
import static com.mygdx.game.Constants.HEIGHT_EXPLOSION;
import static com.mygdx.game.Constants.WIDTH_EXPLOSION;

/**
 * Created by patri on 16/03/2018.
 */

public class ExplosionEntity extends Actor{

    Float x, y;
    Animation animation;
    Float tiempo;
    TextureRegion[] regionsExplosion;
    TextureRegion frameActual;
    float size;

    public ExplosionEntity(Texture texture, Float x, Float y, float size){
        this.x = x;
        this.y = y;
        this.size = size;
        setPosition(x,y);
        //cargar la imagen
        // TODO: usar el manager para extraer la textura como el asteroide en EntityFactory:  hecho
       // texture = new Texture(Gdx.files.internal("explosion-transitions.png"));
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/5, texture.getHeight()/2);
        regionsExplosion = new TextureRegion[10];
        int z=0;
        for(int i=0; i<2; i++) {
            for (int j = 0; j < 5; j++) {
                regionsExplosion[z] = tmp[i][j];
                z++;
            }
        }
        // TODO: Meter el 5f como constante : hecho
        //TODO: meter la clase en la EntityFactory:  hecho
        //TODO: hacer mas pequeño la imagen : hecho

        animation = new Animation(FRAME_DURATION, regionsExplosion);
        setSize(Constants.PIXELS_IN_METER * size, Constants.PIXELS_IN_METER * size);
        tiempo = 0f;
    }

    public void draw(Batch batch, float parentAlpha){
        tiempo += parentAlpha;//tiempo que pasa desde el ultimo render
        frameActual = (TextureRegion) animation.getKeyFrame(tiempo, false);
        batch.draw(frameActual, x, y, WIDTH_EXPLOSION * size, HEIGHT_EXPLOSION * size);

        if(animation.isAnimationFinished(tiempo)){
            this.remove();
        }
    }
}
