package com.mygdx.game;

public class Constants {

    /**
     * How many pixels there are in a meter. As explained in the video, this is important, because
     * your simulation is in meters but you have to somehow convert these meters to pixels so that
     * they can be rendered at a size visible by the user.
     */
    public static final float PIXELS_IN_METER = 90f;

    /**
     * The force in N/s that the player uses to jump in an impulse. This force will also be applied
     * in the opposite direction to make the player fall faster multiplied by some value to make
     * it stronger.
     */
    public static final int IMPULSE_JUMP = 20;

    public static final float IMPULSE_PLAYER = 2f;
    public static final float MAX_PLAYER_SPEED = 10f;

    public static final int WIDTH_SCREEN=1920;
    public static final int HEIGHT_SCREEN =1080;

    public static final String SERVER_URL = "http://192.168.0.113:3003";
}

