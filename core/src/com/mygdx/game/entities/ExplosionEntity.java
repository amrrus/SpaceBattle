package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constants;
/**
 * Created by patri on 16/03/2018.
 */

public class ExplosionEntity extends Actor {

    Animation<TextureRegion> animation;
    Float radius;

    public ExplosionEntity(Float x, Float y, Float radius){
        this.radius = radius;
        animation=  GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("explosion.gif").read());
        super.setPosition(x, y);
    }

    public void draw(Batch batch, float parentAlpha) {
        setPosition(radius *  Constants.PIXELS_IN_METER *2  , radius * Constants.PIXELS_IN_METER * 2 );
        batch.draw(animation.getKeyFrame(Gdx.graphics.getDeltaTime(), true),20.0f, 20.0f);
    }


}
