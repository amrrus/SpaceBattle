package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;

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
        image = new Texture(Gdx.files.internal("transiciones-explosion.png"));
        TextureRegion[][] tmp = TextureRegion.split(image, image.getWidth()/5, image.getHeight()/2);
        regionsExplosion = new TextureRegion[10];
        for(int i=0; i<2; i++)
            for (int j=0; j<5; j++)
                regionsExplosion[i] = tmp[i][j];
        animation = new Animation(30f, regionsExplosion);
        tiempo = 0f;
    }

    public void render(SpriteBatch batch){
        tiempo += Gdx.graphics.getDeltaTime();//tiempo que pasa desde el ultimo render
        frameActual = (TextureRegion) animation.getKeyFrame(tiempo, true);
        batch.draw(frameActual, x, y);
    }
}
