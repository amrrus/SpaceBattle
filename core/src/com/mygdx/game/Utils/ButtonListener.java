package com.mygdx.game.Utils;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

abstract public class ButtonListener extends ChangeListener {

    private AssetManager manager;
    private Sound clickSound;

    public ButtonListener(AssetManager manager){
        this.manager = manager;
        clickSound = manager.get("audio/click.ogg");
    }


    @Override
    public boolean handle(Event event) {
        if (!(event instanceof ChangeEvent)) return false;
        clickSound.play(0.5f);
        changed((ChangeEvent)event, event.getTarget());
        return false;
    }
}
