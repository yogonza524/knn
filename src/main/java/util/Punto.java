/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author pichon
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author gonza
 */
public class Punto {
    private final Float[] data;
    private int clase;

    /**
     * Constructor simple
     * @param data
     */
    public Punto(Float[] data) {
	this.data = data;
    }
    
    /**
     * Constructor simple
     * @param datos
     */
    public Punto(ArrayList<Double> datos) {
	super();
	List<Float> puntos = new ArrayList<>();
	for (int i = 0; i < datos.size(); i++) {
            float x = datos.get(i).floatValue();
	    puntos.add(x);
	}
        this.clase = datos.get(datos.size()-1).intValue();
	this.data = puntos.toArray(new Float[datos.size()]);
    }

    /**
     * Obtiene el elemento segun su dimension
     * @param dimension
     * @return
     */
    public float get(int dimension) {
	return data[dimension];
    }

    /**
     * obtiene el grado
     * @return
     */
    public int getGrado() {
	return data.length;
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append(data[0]);
	for (int i = 1; i < data.length; i++) {
	    sb.append(", ");
	    sb.append(data[i]);
	}
	return sb.toString();
    }
    
    /**
     * Devuelve la distancia euclidea de un punto al destino
     * @param destino
     * @return
     */
    public double distanciaEuclideana(Punto destino) {
        double d = 0.0f;
	for (int i = 0; i < data.length; i++) { //data es de mi punto
	    d += Math.pow(data[i] - destino.get(i), 2);
	}
	return Math.sqrt(d);
    }

    /**
     * Obtiene el arreglo de datos
     * @return
     */
    public Float[] getData() {
        return data;
    }

    /**
     * Obtiene la clase
     * @return
     */
    public int getClase() {
        return clase;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Arrays.deepHashCode(this.data);
        hash = 89 * hash + this.clase;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Punto other = (Punto) obj;
        if (this.clase != other.clase) {
            return false;
        }
        if (!Arrays.deepEquals(this.data, other.data)) {
            return false;
        }
        return true;
    }
    
    
}
