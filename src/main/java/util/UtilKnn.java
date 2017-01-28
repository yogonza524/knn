/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import ru.lanwen.verbalregex.VerbalExpression;
import static ru.lanwen.verbalregex.VerbalExpression.regex;

/**
 * Utilitario 
 * @author pichon
 */
public class UtilKnn {
    
    protected static final Color[] COLORES = {Color.RED, Color.BLUE, Color.GREENYELLOW, Color.AQUAMARINE, Color.ORANGE, Color.DARKMAGENTA};
    protected static final String[] COLORES_POR_NOMBRE = {"Rojo", "Azul", "Verde", "Celeste", "Naranja", "Violeta"};
   
    private static String errorDeIndice = "";

    public static String getErrorDeIndice() {
        return errorDeIndice;
    }

    public static void setErrorDeIndice(String errorDeIndice) {
        UtilKnn.errorDeIndice = errorDeIndice;
    }
    
    
    
    public static List<List<Double>> cargarPuntos(String entradaString, int dim){
        List<List<Double>> salida = new ArrayList<>();
        String linea;
        entradaString = entradaString + "\n" + "#";
        entradaString = entradaString.replaceAll(" ", "");
        //obtengo cada punto
        while(!entradaString.equals("#")){
            linea = entradaString.substring(0, entradaString.indexOf("\n"));
            List<Double> punto = getPunto(linea,dim);
            if (punto != null && !punto.isEmpty()) {
                salida.add(punto);
            }
            entradaString = entradaString.substring(entradaString.indexOf("\n")+1);
        }
        return salida;
    }
    
    private static ArrayList<Double> getPunto(String linea, int dim){
        ArrayList<Double> salida = new ArrayList<>();
        try {
            if (!linea.isEmpty() && !linea.equals(";")) {
                String aux;
                for (int i = 0; i < dim-1; i++) {
                    System.out.println(linea);
                    aux = linea.replaceAll("\\s+" , "").substring(0, linea.indexOf(','));
                    salida.add(Double.valueOf(aux));
                    linea = linea.substring(linea.indexOf(',')+1);
                }
                aux = linea.substring(0, linea.indexOf(';'));
                salida.add(Double.valueOf(aux));
            }
        } catch (Exception e) {
            e.printStackTrace();
            salida = null;
        }
        return salida;
    }
    
    //Graficar los puntos
    public static ArrayList<Plot> addPuntosGrafic(List<List<Double>> puntos, Axes axes){
        double ancho = 0.001, diferencia = 0.00051;
        ArrayList<Plot> puntosGraf = new ArrayList<>();
        Plot puntoG;
        for (int i = 0; i < puntos.size(); i++) {
            double y = (double) puntos.get(i).get(1);
            try {
                puntoG = new Plot(
                    x -> y ,
                    puntos.get(i).get(0)-diferencia, puntos.get(i).get(0)+diferencia, ancho,
                    axes, 7.0, COLORES[puntos.get(i).get(2).intValue()]
                );
                puntosGraf.add(puntoG);
            } catch (Exception e) {
                errorDeIndice = "Entrada no válida, fuera de rango de clase: " +  puntos.get(i).get(2).intValue();
            }
        }
        return puntosGraf;
    }
    
    public static Plot graficarLineaACluster(Punto puntoClass, Punto punto, Axes axes) {
        Plot puntoG;
        double largo = 0.01;
        double ancho = 1.0;
        float aux = punto.get(0);
        if ((Math.abs(punto.get(0) - puntoClass.get(0)))<largo) {
            aux = (float) (aux + largo);
        }
        final float x0 = aux;
        if (x0< puntoClass.get(0)) {
            puntoG = new Plot(
                x -> ((puntoClass.get(1)-punto.get(1))/(puntoClass.get(0)-x0)*(x-x0))+punto.get(1),
                x0, puntoClass.get(0), largo,
                axes, ancho, COLORES[punto.getClase()]
            );
        }else{
            puntoG = new Plot(
                x -> ((punto.get(1)-puntoClass.get(1))/(x0-puntoClass.get(0))*(x-puntoClass.get(0)))+puntoClass.get(1),
                puntoClass.get(0), x0, largo,
                axes, ancho, COLORES[punto.getClase()]
            );
        }

        return puntoG;
    }
    
//    public static ArrayList<ArrayList<Double>> getCoordenadas(ArrayList<Punto> puntoClass) {
//        ArrayList<ArrayList<Double>> puntos = new ArrayList<>();
//        for (int i = 0; i < puntoClass.size(); i++) {
//            ArrayList<Double> coordenadas = new ArrayList<>();
//            Float[] data = puntoClass.get(i).getData();
//            for (int j = 0; j < 2; j++) {
//                coordenadas.add(data[j].doubleValue());
//            }
//            puntos.add(coordenadas);
//        }
//        return puntos;
//    }
    
    public static ArrayList<Plot> addPuntosClassGrafic(List<List<Double>> puntos, Axes axes){
        double ancho = 0.001, diferencia = 0.00051;
        ArrayList<Plot> puntosGraf = new ArrayList<>();
        Plot puntoG;
        for (int i = 0; i < puntos.size(); i++) {
            double y = (double) puntos.get(i).get(1);
            puntoG = new Plot(
                x -> y ,
                puntos.get(i).get(0)-diferencia, puntos.get(i).get(0)+diferencia, ancho,
                axes, 7.0, Color.WHITE
            );
            puntosGraf.add(puntoG);
        }
        return puntosGraf;
    }
}
