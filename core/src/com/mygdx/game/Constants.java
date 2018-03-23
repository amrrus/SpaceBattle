package com.mygdx.game;

public class Constants {

    /**
     * How many pixels there are in a meter. As explained in the video, this is important, because
     * your simulation is in meters but you have to somehow convert these meters to pixels so that
     * they can be rendered at a size visible by the user.
     */
    public static final float PIXELS_IN_METER = 90f;

    public static final float ASTEROID_DENSITY = 10000000f;
    public static final float SHOT_RADIUS = 0.1f;
    public static final float SHOT_DENSITY = 5f;

    public static final float FRAME_DURATION = 5f;

    public static final int WIDTH_SCREEN=1920;
    public static final int HEIGHT_SCREEN =1080;

    public static final String SERVER_URL = "http://192.168.1.101:3000"; //0.0.0.0 not valid
}

