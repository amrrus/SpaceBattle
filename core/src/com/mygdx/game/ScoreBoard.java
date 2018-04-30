package com.mygdx.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Connections.Connection;
import com.mygdx.game.Utils.Utils;

import java.util.ArrayList;

public class ScoreBoard {
    private GameScreen gs;
    private Connection conn;
    private Texture textureLiveBottom;
    private Texture textureLiveTop;
    private BitmapFont font;
    private ArrayList<TextureRegion> shotsEmpty;
    private ArrayList<TextureRegion> shotsTop;
    private ArrayList<TextureRegion> shotsBottom;
    private Integer widthShot;

    public ScoreBoard(GameScreen gs, Connection conn){
        this.conn = conn;
        this.gs = gs;
        this.textureLiveBottom = this.gs.game.getManager().get("blueShipUp.png");
        this.textureLiveTop = this.gs.game.getManager().get("blueShipDown.png");
        this.font = new BitmapFont();
        this.font.getData().setScale(4);
        this.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture shotsList = gs.getManager().get("shots-list.png");
        TextureRegion[][] tmp = TextureRegion.split(shotsList, shotsList.getWidth()/3, shotsList.getHeight()/3);
        shotsEmpty = new ArrayList<TextureRegion>();
        shotsTop = new ArrayList<TextureRegion>();
        shotsBottom= new ArrayList<TextureRegion>();
        for (int j = 0; j < 3; j++) {
            shotsEmpty.add(tmp[0][j]);
            shotsTop.add(tmp[1][j]);
            shotsBottom.add(tmp[2][j]);
        }
        this.widthShot=60;
    }

    public void displayScoreBoard(){
        //only print score board if this method is call within begin-end structure of a batch
        displayNames();
        displayLives();
        displayShots();
    }

    private void displayShots(){
        if (this.gs.stage.getBatch().isDrawing()){
            Integer shotsTop = this.gs.topPlayer.getShots();
            Integer shotsBot = this.gs.bottomPlayer.getShots();
            final Integer aux = Constants.MAX_NUMBER_SHOTS_STORED-1;
            for (int i=0; i< Constants.MAX_NUMBER_SHOTS_STORED; i++ ){
                Integer index=1;
                if (i==0)
                    index=0;
                if (i==(Constants.MAX_NUMBER_SHOTS_STORED-1))
                    index=2;

                if(i<shotsTop){
                    printTopShot(i,index,true);
                }else{
                    printTopShot(i,index,false);
                }

                if(i<shotsBot){
                    printBottomShot(i,index,true);
                }else{
                    printBottomShot(i,index,false);
                }
            }
        }
    }
    private void displayNames(){
        if (this.gs.stage.getBatch().isDrawing()){
            GlyphLayout gl = new GlyphLayout(font, conn.getTopPlayerName());
            font.draw(this.gs.stage.getBatch(),conn.getTopPlayerName(),900 - gl.width,530);
            font.draw(this.gs.stage.getBatch(),conn.getBotPlayerName(),-900,-530 + Utils.getStringHeight(conn.getBotPlayerName(), font));
        }
    }


    private void printTopShot(Integer i, Integer index,Boolean enable){
        TextureRegion printable = enable ? this.shotsTop.get(index) : this.shotsEmpty.get(index);
        this.gs.stage.getBatch().draw(printable, -900 + widthShot * i, 340, widthShot, widthShot);
    }
    private void printBottomShot(Integer i, Integer index,Boolean enable){
        TextureRegion printable = enable ? this.shotsBottom.get(2-index) : this.shotsEmpty.get(2-index);
        this.gs.stage.getBatch().draw(printable, 900 - widthShot - widthShot * i, -530, widthShot, widthShot);
    }

    private void displayLives(){
        if (this.gs.stage.getBatch().isDrawing()){
            //lives player top
            font.draw(this.gs.stage.getBatch(),Constants.MSG_DISPLAY_LIVES,-900,530);
            for (int i=0;i<this.gs.topPlayer.getLives();i++) {
                this.gs.stage.getBatch().draw(this.textureLiveTop, -880 + 60*i, 410, 50, 50,
                        0,0,this.textureLiveTop.getWidth(),this.textureLiveTop.getHeight(),false,true);
            }
            //lives player bottom
            font.draw(this.gs.stage.getBatch(),Constants.MSG_DISPLAY_LIVES,900 - Utils.getStringWidth(Constants.MSG_DISPLAY_LIVES, font), -340);
            for (int i=0;i<this.gs.bottomPlayer.getLives();i++) {
                this.gs.stage.getBatch().draw(this.textureLiveBottom, 880 - 50 - 60*i, -460, 50, 50);
            }
        }
    }
    public void dispose(){
        font.dispose();
        textureLiveTop.dispose();
        textureLiveBottom.dispose();
    }
}
