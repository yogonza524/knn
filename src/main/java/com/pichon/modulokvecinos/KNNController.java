package com.pichon.modulokvecinos;

import util.Axes;
import util.AlmacenDatos;
import util.Punto;
import util.UtilKnn;
import util.Knn;
import util.Plot;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

/**
 * Controlador 
 * @author gonza
 */
public class KNNController implements Initializable {
    
    private StackPane layout;
    private Axes axes;
    private Knn knn;
    @FXML private TextArea entradasClasificar;
    @FXML private ChoiceBox<Integer> cantVecinos;
    @FXML private StackPane canvas;
    @FXML private TextArea resultado;
    @FXML private ScrollPane scrollPane;
    @FXML private TextArea puntosEntrada;
    @FXML private ChoiceBox<Integer> dimension;

    @FXML void clasificarEntradas(ActionEvent event) {
        String entradaClasificar = entradasClasificar.getText();
        List<List<Double>> puntosClasificar = UtilKnn.cargarPuntos(entradaClasificar, dimension.getValue());
        
        //Agregamos los puntosClass
        ArrayList<Punto> puntosClass = new ArrayList<>();
	for (int i = 0; i < puntosClasificar.size(); i++) {
	    Punto p = new Punto((ArrayList<Double>) puntosClasificar.get(i));
	    puntosClass.add(p);
	}
        int cantPuntosVecinos = cantVecinos.getValue();
        AlmacenDatos almacenDatos = knn.getPuntosVecinos(puntosClass, cantPuntosVecinos);
        String salidaClasificacion = knn.clasificarPuntos(puntosClass, almacenDatos.getPuntosVecinos());
        resultado.setText(resultado.getText() + almacenDatos.getDatosProceso() + "\n" + salidaClasificacion);
        
        //Graficar punto
        List<List<Plot>> puntosPlot= addPuntosClass(puntosClasificar);
        for (int i = 0; i < puntosPlot.size(); i++) {
            for (int j = 0; j < puntosPlot.get(i).size(); j++) {
                layout.getChildren().add(puntosPlot.get(i).get(j));                 
            }
        }
        //Graficar lineas
        List<Plot> lineasCluster = generarLineasUnion(puntosClass, almacenDatos.getPuntosVecinos());
        for (int i = 0; i < lineasCluster.size(); i++) {
            layout.getChildren().add(lineasCluster.get(i));
        }
        
    }

    @FXML void generarRed(ActionEvent event) {
        int dim = dimension.getValue();
        resultado.setText("");
        String datos = puntosEntrada.getText();
        List<List<Double>> datosPuntos = UtilKnn.cargarPuntos(datos, dim+1);
        
        //Creo el knn y agrego los puntosClass
	knn = new Knn();
        knn.addPuntos(datosPuntos);
        
        //Grafico los clusters
        layout = generarGrafico(datosPuntos); //Genero grafico de puntosClass

        canvas.getChildren().clear();
        canvas.getChildren().add(layout);
        scrollPane.setHvalue(scrollPane.getHmax()/2);
        scrollPane.setVvalue(scrollPane.getVmax()/2);
    }

    @FXML void resetear(ActionEvent event) {
        // A definir
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dimension.getItems().addAll(
            2, 3, 4, 5
        );
        dimension.setValue(2);
        cantVecinos.getItems().addAll(
            3, 4, 5, 6, 7
        );
        cantVecinos.setValue(3);
    }

    private StackPane generarGrafico(List<List<Double>> datosPuntos){
        //Generar ejes cartesianos
        axes = new Axes(
                800, 800,
                -20, 20, 2,
                -20, 20, 2
        );
        //generamos el layoutAux
        StackPane layoutAux = new StackPane();
        layoutAux.setPadding(new Insets(20));
        layoutAux.setStyle("-fx-background-color: rgb(35, 39, 50);");
        layoutAux.setPrefSize(canvas.getWidth() -1, canvas.getHeight() -1);
        canvas.getChildren().add(layoutAux);
        //Agregamos los puntosClass
        List<Plot> puntosGraficar = UtilKnn.addPuntosGrafic(datosPuntos, axes);
        for (int i = 0; i < puntosGraficar.size(); i++) {
            layoutAux.getChildren().add(puntosGraficar.get(i));
        }
        return layoutAux;
    }
    
    private List<List<Plot>> addPuntosClass(List<List<Double>> puntosClass){
        List<List<Plot>> salida = new ArrayList<>();
        for (int i = 0; i < puntosClass.size(); i++) {
            ArrayList<Plot> puntosGraficar = UtilKnn.addPuntosClassGrafic(puntosClass, axes);
            salida.add(puntosGraficar);
        }
        return salida;
    }
    
    /**
     * Genera lineas de union entre los puntos
     * @param puntos Lista de puntos
     * @param nVecinos Matriz de vecinos de cada punto
     * @return Lista grafica de puntos
     */
    public List<Plot> generarLineasUnion(List<Punto> puntos, List<List<Punto>> nVecinos){
        ArrayList<Plot> salida = new ArrayList<>();
        for (int i = 0; i < puntos.size(); i++) {
            for (int j = 0; j < nVecinos.get(0).size(); j++) {
                salida.add(UtilKnn.graficarLineaACluster(puntos.get(i), nVecinos.get(i).get(j), axes));
            }
        }
        return salida;
    }
}
