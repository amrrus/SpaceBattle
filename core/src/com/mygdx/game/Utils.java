package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;

/**
 * Created by patry on 23/03/2018.
 */

public class Utils {
    private float positionX;
    private float positionY;
    private float impulseX;
    private float impulseY;
    private float radius;

    public ArrayList<ArrayList<Float>> generateAttributesAsteroids() {

        ArrayList<ArrayList<Float>> l = new ArrayList<ArrayList<Float>>();
        ArrayList<Float> sublist;
        sublist = generaAleatorioList();
        l.add(sublist);
        System.out.print(l+"\n");
        return l;
    }

    public ArrayList<Float> generaAleatorioList(){
        return iteraciones();
    }

    private float primerImpulso(){
        Integer signo=0;
        float  n=MathUtils.random(-1.0f, 1.0f);
        if(n<0){
            signo=-1;
        }else{
            signo=1;
        }
        return signo;
    }

    private float calculaPrimero = primerImpulso();

    private float restoImpulsos() {
        float res = MathUtils.random();
        if (calculaPrimero < 0) {
            res = res * (1);
            calculaPrimero = res;
        } else {
            res = res * (-1);
            calculaPrimero = res;
        }
        return res;
    }

    private ArrayList<Float> calculaPosicionesPrimeraIteracion(){
        ArrayList<Float> lista1 = new ArrayList<Float>();
        float resX = MathUtils.random();
        float resY= MathUtils.random(-1.0f,1.0f);
        if(resX>=0 && resY>=0){
            positionX=resX;
            positionY=resY;
            lista1.add(0,positionX);
            lista1.add(1,positionY);
        }else if(resX<0 && resY>=0){
            positionX=resX;
            positionY=resY;
            lista1.add(0,positionX);
            lista1.add(1,positionY);
        }else if(resX<0 && resY<0){
            positionX=resX;
            positionY=resY;
            lista1.add(0,positionX);
            lista1.add(1,positionY);
        }else{
            positionX=resX;
            positionY=resY;
            lista1.add(0,positionX);
            lista1.add(1,positionY);
        }
        return lista1;
    }

    private ArrayList<Float> calculaPrimeraPosicion=calculaPosicionesPrimeraIteracion();

    private ArrayList<Float> calculaRestoPosiciones(){
        ArrayList<Float> lista = new ArrayList<Float>();
        float resX;
        float resY;
        if(calculaPrimeraPosicion.get(0)>=0f && calculaPrimeraPosicion.get(1)>=0){
            //System.out.print("[" + calculaPrimeraPosicion.get(0) + "," + calculaPrimeraPosicion.get(1)+"] :" + "Se encuentra en el primer cuadrante\n");
            lista.add(calculaPrimeraPosicion.get(0));
            lista.add(calculaPrimeraPosicion.get(1));
            resX = MathUtils.random(-1f,0f);
            resY = MathUtils.random(-1f,0f);
            calculaPrimeraPosicion.set(0, resX);
            calculaPrimeraPosicion.set(1, resY);
        }else if(calculaPrimeraPosicion.get(0)<0f && calculaPrimeraPosicion.get(1)>=0) {
           // System.out.print("[" + calculaPrimeraPosicion.get(0) + "," + calculaPrimeraPosicion.get(1) + "] :" + "Se encuentra en el segundo cuadrante\n");
            lista.add(calculaPrimeraPosicion.get(0));
            lista.add(calculaPrimeraPosicion.get(1));
            resX = MathUtils.random(0f,1f);
            resY = MathUtils.random(-1f,0f);
            calculaPrimeraPosicion.set(0, resX);
            calculaPrimeraPosicion.set(1, resY);
        }else  if(calculaPrimeraPosicion.get(0)<0f && calculaPrimeraPosicion.get(1)<0) {
           // System.out.print("[" + calculaPrimeraPosicion.get(0) + "," + calculaPrimeraPosicion.get(1) + "] :" + "Se encuentra en el tercer cuadrante\n");
            lista.add(calculaPrimeraPosicion.get(0));
            lista.add(calculaPrimeraPosicion.get(1));
            resX = MathUtils.random(-1f,0f);
            resY = MathUtils.random(0f,1f);
            calculaPrimeraPosicion.set(0, resX);
            calculaPrimeraPosicion.set(1, resY);
        }else if(calculaPrimeraPosicion.get(0)>=0f && calculaPrimeraPosicion.get(1)<0) {
            //System.out.print("[" + calculaPrimeraPosicion.get(0) + "," + calculaPrimeraPosicion.get(1) + "] :" + "Se encuentra en el cuarto cuadrante\n");
            lista.add(calculaPrimeraPosicion.get(0));
            lista.add(calculaPrimeraPosicion.get(1));
            resX = MathUtils.random(0f,1f);
            resY = MathUtils.random(0f, 1f);
            calculaPrimeraPosicion.set(0, resX);
            calculaPrimeraPosicion.set(1, resY);
        }
        return lista;
    }


    private ArrayList<Float> iteraciones(){
        ArrayList<Float> calculaPos = calculaRestoPosiciones();
        ArrayList<Float> lista = new ArrayList<Float>();
        radius = MathUtils.random(0.3f, 1.0f);
        lista.add(calculaPos.get(0));
        lista.add(calculaPos.get(1));
        impulseX= MathUtils.random(-1f,1f);
        impulseY = restoImpulsos();
        lista.add(impulseX);
        lista.add(impulseY);
        lista.add(radius);
        return lista;
    }
}

