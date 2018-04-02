package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class ScoreBoard {
    private GameScreen gs;
    private Texture textureLiveBottom;
    private Texture textureLiveTop;
    private BitmapFont font;

    public ScoreBoard(GameScreen gs){
        this.gs = gs;
        this.textureLiveBottom = this.gs.game.getManager().get("blueShipUp.png");
        this.textureLiveTop = this.gs.game.getManager().get("blueShipDown.png");
        this.font = new BitmapFont();
        this.font.getData().setScale(4);
        this.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void displayScoreBoard(){
        //only print score board if this method is call within begin-end structure of a batch
        if (this.gs.stage.getBatch().isDrawing()){
            font.draw(this.gs.stage.getBatch(),Constants.MSG_DISPLAY_LIVES,-900,530);
            for (int i=0;i<this.gs.topPlayer.getLives();i++) {
                this.gs.stage.getBatch().draw(this.textureLiveTop, -880 + 60*i, 400, 50, 50,
                        0,0,this.textureLiveTop.getWidth(),this.textureLiveTop.getHeight(),false,true);
            }
            font.draw(this.gs.stage.getBatch(),Constants.MSG_DISPLAY_LIVES,-900, -400);
            for (int i=0;i<this.gs.bottomPlayer.getLives();i++) {
                this.gs.stage.getBatch().draw(this.textureLiveBottom, -880 + 60*i, -530, 50, 50);
            }
        }
    }

    public void dispose(){
        font.dispose();
        textureLiveTop.dispose();
        textureLiveBottom.dispose();
    }
}
