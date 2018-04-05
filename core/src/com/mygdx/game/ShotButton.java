package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class ShotButton {
    private Skin skin;
    private TextButton shootRight;
    private TextButton shootLeft;
    private GameScreen gs;
    private Boolean pressedLeft;
    private Boolean pressedRight;

    public ShotButton( GameScreen gs){
        this.gs = gs;
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        this.shootRight= new TextButton(Constants.SHOT_BUTTON_LABEL, skin);
        this.shootLeft= new TextButton(Constants.SHOT_BUTTON_LABEL, skin);
        this.pressedRight=false;
        this.pressedLeft=false;


        this.shootRight.addCaptureListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressedRight=true;
                Gdx.app.log("debug","pressed: "+(pressedRight || pressedLeft));
                startShot();
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                pressedRight=false;
                stopShot();
                Gdx.app.log("debug","pressed: "+(pressedRight || pressedLeft));

            }

        });
        this.shootLeft.addCaptureListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                pressedLeft=true;
                Gdx.app.log("debug","pressed: "+(pressedRight || pressedLeft));

                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                pressedLeft=false;
                Gdx.app.log("debug","pressed: "+(pressedRight || pressedLeft));

            }
        });

        this.shootRight.setSize(300, 300);
        this.shootRight.getLabel().setFontScale(4, 4);
        this.shootRight.setPosition(600, 0);

        this.shootLeft.setSize(300, 300);
        this.shootLeft.getLabel().setFontScale(4, 4);
        this.shootLeft.setPosition(-900, 0);

        this.gs.stage.addActor(this.shootRight);
        this.gs.stage.addActor(this.shootLeft);

    }

    public void show() {
        Gdx.input.setInputProcessor(this.gs.stage);
    }

    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

    public void dispose(){
        this.skin.dispose();
    }

    private void addShot(){
        gs.topPlayer.setShots(gs.topPlayer.getShots()+1);
        gs.bottomPlayer.setShots(gs.bottomPlayer.getShots()+1);
    }

    private void subShot(){
        gs.topPlayer.setShots(gs.topPlayer.getShots()-1);
        gs.bottomPlayer.setShots(gs.bottomPlayer.getShots()-1);
    }
    private void startShot(){
        this.gs.playerMoveControl.startToShoot();
    }
    private void stopShot(){
        this.gs.playerMoveControl.stopToShoot();
    }


}
