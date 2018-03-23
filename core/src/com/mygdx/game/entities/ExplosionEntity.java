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
    Texture image;
    TextureRegion frameActual;

    public ExplosionEntity(Float x, Float y){
        this.x = x;
        this.y = y;
        setPosition(x,y);
        //cargar la imagen
        // TODO: usar el manager para extraer la textura como el asteroide en EntityFactory
        image = new Texture(Gdx.files.internal("transiciones-explosion.png"));
        TextureRegion[][] tmp = TextureRegion.split(image, image.getWidth()/5, image.getHeight()/2);
        regionsExplosion = new TextureRegion[10];
        int z=0;
        for(int i=0; i<2; i++) {
            for (int j = 0; j < 5; j++) {
                regionsExplosion[z] = tmp[i][j];
                z++;
            }
        }
        // TODO: Meter el 5f como constante : hecho
        //TODO: meter la clase en la EntityFactory
        //TODO: hacer mas pequeño la imagen : hecho

        animation = new Animation(FRAME_DURATION, regionsExplosion);
        setSize(Constants.PIXELS_IN_METER, Constants.PIXELS_IN_METER);
        tiempo = 0f;
    }

    public void draw(Batch batch, float parentAlpha){
        tiempo += parentAlpha;//tiempo que pasa desde el ultimo render
        frameActual = (TextureRegion) animation.getKeyFrame(tiempo, false);
        batch.draw(frameActual, x, y, WIDTH_EXPLOSION,HEIGHT_EXPLOSION);

        if(animation.isAnimationFinished(tiempo)){
            this.remove();
        }
    }
}
