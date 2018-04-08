package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by patry on 23/03/2018.
 */

public class Utils {
    private float positionX;
    private float positionY;
    private float impulseX;
    private ArrayList<Float> impulseY;
    private float radius;

    /*public Utils() {
        this.position = new Vector2(MathUtils.random(),MathUtils.random());
        this.impulse = new Vector2(MathUtils.random(),MathUtils.random());
        this.radius = MathUtils.random(0.3f,1f);
    }*/

    public ArrayList<ArrayList<Float>> generateAttributesAsteroids() {

        ArrayList<ArrayList<Float>> l = new ArrayList<ArrayList<Float>>();
        ArrayList<Float> sublist = new ArrayList<Float>();
            /*sublist.add(positionX);
            sublist.add(positionY);
            sublist.add(impulseX);
            sublist.add(impulseY);
            sublist.add(radius);*/
        for(int i=0; i<5; i++) {
            impulseY = generaAleatorioList();
            l.add(impulseY);
        }

        //TODO: queda método de las posiciones, ya genera una lista de listas con los impulsos.
        System.out.print(l);
       /* Integer contador=0;
        for(int i=0; i<1; i++){
            l.add(i, impulseY);
            System.out.print(l);
        }*/
        //System.out.print(sublist);
        //System.out.print(l);
        return l;
    }

    public ArrayList<Float> generaAleatorioList(){
        return iteraciones();

    }

    private static float primerElemento(){
        Integer signo=0;
        float  n= MathUtils.random(-1.0f, 1.0f);
        if(n<0){
            signo=-1;
        }else{
            signo=1;
        }
        return n;
    }

    private float calculaPrimero = primerElemento();

    private float restoElementos() {
        float res = 0f;
        res = MathUtils.random();
        if (calculaPrimero < 0) {
            res = res * (1);
            calculaPrimero = res;
        } else {
            res = res * (-1);
            calculaPrimero = res;
        }
        return res;
    }

    private ArrayList<Float> iteraciones(){
        ArrayList<Float> lista = new ArrayList<Float>();
        float res = 0;
        impulseX= MathUtils.random();
        res = restoElementos();
        lista.add(impulseX);
        lista.add(res);
        return lista;
    }
}

