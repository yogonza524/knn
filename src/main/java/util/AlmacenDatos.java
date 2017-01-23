/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author pichon
 */
public class AlmacenDatos implements Serializable{
    private transient List<List<Punto>> puntosVecinos;
    String datosProceso;

    /**
     * Constructor por defecto
     */
    public AlmacenDatos() {
        // Constructor sin parametros, no hace nada
    }

    /**
     * Constructor 
     * @param puntosVecinos
     * @param datosProceso
     */
    public AlmacenDatos(List<List<Punto>> puntosVecinos, String datosProceso) {
        this.puntosVecinos = puntosVecinos;
        this.datosProceso = datosProceso;
    }
    
    /**
     * Getter
     * @return
     */
    public List<List<Punto>> getPuntosVecinos() {
        return puntosVecinos;
    }

    /**
     * Setter
     * @param puntosVecinos
     */
    public void setPuntosVecinos(List<List<Punto>> puntosVecinos) {
        this.puntosVecinos = puntosVecinos;
    }

    /**
     * Getter
     * @return
     */
    public String getDatosProceso() {
        return datosProceso;
    }

    /**
     * Setter
     * @param datosProceso
     */
    public void setDatosProceso(String datosProceso) {
        this.datosProceso = datosProceso;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.puntosVecinos);
        hash = 67 * hash + Objects.hashCode(this.datosProceso);
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
        final AlmacenDatos other = (AlmacenDatos) obj;
        if (!Objects.equals(this.datosProceso, other.datosProceso)) {
            return false;
        }
        if (!Objects.equals(this.puntosVecinos, other.puntosVecinos)) {
            return false;
        }
        return true;
    }
    
    
}
