/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pichon
 */
public class Knn {
    
    private ArrayList<Punto> puntos;
    private final int cantClases = 10;
    
    public void addPuntos(List<List<Double>> datosPuntos){
        //generar los puntos a partir de la lista
        puntos = new ArrayList<>();
	for (int i = 0; i < datosPuntos.size(); i++) {
	    Punto p = new Punto((ArrayList<Double>) datosPuntos.get(i));
	    puntos.add(p);
	}
    }

    public AlmacenDatos getPuntosVecinos(List<Punto> puntosClass, int cantVecinos) {
        AlmacenDatos salida = new AlmacenDatos();
        List<List<Punto>> puntosSalida = new ArrayList<>();
        String datosProceso = "Proceso de obtener los " + cantVecinos + " mas cercanos \n";
        for (int i = 0; i < puntosClass.size(); i++) {
            datosProceso = datosProceso + "\n Punto: " + puntosClass.get(i) + "\n";
            ArrayList<Double> distancia =  new ArrayList<>();
            
            //Obtengo las distancias de todos los puntos al punto ingresado
            for (int j = 0; j < puntos.size(); j++) {
                distancia.add(puntosClass.get(i).distanciaEuclideana(puntos.get(j)));
            }
            datosProceso = datosProceso + "Distancias: \n" + distancia + "\n";
            
            int[] indexMejoresN = getNVecinosProximos(distancia, cantVecinos);
            datosProceso = datosProceso + "N mas cercanos: \n";
            
            //Obtengo los n puntos mas cercanos
            ArrayList<Punto> mejoresN = new ArrayList<>();
            for (int j = 0; j < indexMejoresN.length; j++) {
                mejoresN.add(puntos.get(indexMejoresN[j]));
                datosProceso = datosProceso + Arrays.toString(puntos.get(indexMejoresN[j]).getData()) + "\n";
            }
            puntosSalida.add(mejoresN);
        }
        salida.setPuntosVecinos(puntosSalida);
        salida.setDatosProceso(datosProceso);
        return salida;
    }
    
    public String clasificarPuntos(List<Punto> puntosClass, List<List<Punto>> puntosVecinos){
        String salida = "\n - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n"
                        + "Clasificacion:\n"
                        + "- - - - - - - - - - - - - - - - - - - - - - - - - - - -\n";
        double total = puntosVecinos.get(0).size();
        for (int i = 0; i < puntosClass.size(); i++) {
            int[] vectorClase = new int[cantClases];
            for (int j = 0; j < puntosVecinos.get(0).size(); j++) {
                vectorClase[puntosVecinos.get(i).get(j).getClase()]++;
            }
            int mayor = 0;
            int claseMayor = -1;
            for (int j = 0; j < cantClases; j++) {
                if (vectorClase[j]>mayor) {
                    mayor = vectorClase[j];
                    claseMayor = j;
                }
            }
            salida = salida + "El punto " + puntosClass.get(i) + " es de la clase " + claseMayor + "\n";
            for (int j = 0; j < cantClases; j++) {
                if (vectorClase[j]>0) {
                    double cant = vectorClase[j];
                    double prob = cant/total;
                    salida = salida + "Probabilidad de ser clase " + j + ": " + prob + "\n";
                }
            }
            salida = salida + "\n";
        }
        return salida;
    }
    
    private int[] getNVecinosProximos(ArrayList<Double> distancias, int n){
        int[] indexNMejores = new int[n];
        int mejor = 0;
        for (int i = 0; i < n; i++) {
            double dist = 99999;
            for (int j = 0; j < distancias.size(); j++) {
                if (distancias.get(j) < dist) {
                    dist = distancias.get(j);
                    mejor = j;
                }
            }
            indexNMejores[i] = mejor;
            distancias.set(mejor, new Double(99999));
        }
        return indexNMejores;
    }
    
}
