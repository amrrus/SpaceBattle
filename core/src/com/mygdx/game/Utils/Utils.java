package com.mygdx.game.Utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Utils {
    public static float getStringWidth(String text, BitmapFont font){
        GlyphLayout gl = new GlyphLayout();
        gl.setText(font, text);

        return gl.width;
    }

    public static float getStringHeight(String text, BitmapFont font){
        GlyphLayout gl = new GlyphLayout();
        gl.setText(font, text);

        return gl.height;
    }
}
