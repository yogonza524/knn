package com.pichon.modulokvecinos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

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
    @FXML private Label patronesEntradaLabel;
    
    @FXML private ComboBox<Integer> xCombo;
    @FXML private ComboBox<Integer> xCombo1;
    @FXML private ComboBox<Integer> yCombo;
    @FXML private ComboBox<Integer> yCombo1;
    
    private PopOver leyendaPop;
    private PopOver dimensionPop;
    private PopOver entradasPop;

    @FXML private void mostrarEntradasAClasificarPop(ActionEvent e){
        mostrarPopConImagen(entradasPop = new PopOver(), new ImageView(new Image("/img/ayuda3.png")), entradasClasificar);
    }
    
    @FXML void clasificarEntradas(ActionEvent event) {
        String entradaClasificar = entradasClasificar.getText();
        List<List<Double>> puntosClasificar = UtilKnn.cargarPuntos(entradaClasificar, dimension.getValue());
        
        if (puntosClasificar != null && !puntosClasificar.isEmpty() && knn != null) {
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
        else{
            resultado.appendText("Error, primero debe entrenar la máquina, utilice el boton 'Generar' \nO verifique que la entrada a clasificar esta correctamente ingresada"); 
        }
    }

    @FXML private void generarRed(ActionEvent event) {
        dibujarRedSegunCombo();
    }
    
    private void dibujarRed(int x1, int x2, int y1, int y2){
        int dim = dimension.getValue();
        resultado.setText("");
        String datos = puntosEntrada.getText();
        List<List<Double>> datosPuntos = UtilKnn.cargarPuntos(datos, dim+1);
        
        if (datosPuntos != null && datosPuntos.isEmpty()) {
            resultado.appendText("\nIngrese un patron de entrada"); 
            resultado.setScrollTop(Double.MAX_VALUE);
        }
        
        //Creo el knn y agrego los puntosClass
	knn = new Knn();
        knn.addPuntos(datosPuntos);
        
        //Grafico los clusters
        layout = generarGrafico(datosPuntos, x1,x2,y1,y2); //Genero grafico de puntosClass

        canvas.getChildren().clear();
        canvas.getChildren().add(layout);
        scrollPane.setHvalue(scrollPane.getHmax()/2);
        scrollPane.setVvalue(scrollPane.getVmax()/2);
    }
    
    @FXML private void resetearGrafico(ActionEvent e){
        Integer x1 = xCombo.getSelectionModel().getSelectedItem();
        Integer x2 = xCombo1.getSelectionModel().getSelectedItem();
        Integer y1 = yCombo.getSelectionModel().getSelectedItem();
        Integer y2 = yCombo1.getSelectionModel().getSelectedItem();
        if (x1 == null){
            resultado.appendText("\nSeleccione el limite inferior de X");
            resultado.setScrollTop(Double.MAX_VALUE);
            return;
        }
        if (x2 == null){
            resultado.appendText("\nSeleccione el limite superior de X");
            resultado.setScrollTop(Double.MAX_VALUE);
            return;
        }
        if (y1 == null){
            resultado.appendText("\nSeleccione el limite inferior de Y");
            resultado.setScrollTop(Double.MAX_VALUE);
            return;
        }
        if (y2 == null){
            resultado.appendText("\nSeleccione el limite superior de Y"); 
            resultado.setScrollTop(Double.MAX_VALUE);
            return;
        }
        
        resetearGrafico(x1, x2, y1, y2);
    }
    
    private void resetearGrafico(int x1, int x2, int y1, int y2){
        int dim = dimension.getValue();
        resultado.setText("");
        String datos = puntosEntrada.getText();
        List<List<Double>> datosPuntos = new ArrayList<>();
        
        //Creo el knn y agrego los puntosClass
	knn = new Knn();
        knn.addPuntos(datosPuntos);
        
        //Grafico los clusters
        layout = generarGrafico(datosPuntos, x1,x2,y1,y2); //Genero grafico de puntosClass

        canvas.getChildren().clear();
        canvas.getChildren().add(layout);
        scrollPane.setHvalue(scrollPane.getHmax()/2);
        scrollPane.setVvalue(scrollPane.getVmax()/2);
    }

    @FXML private void borrarResultados(ActionEvent e){
        resultado.setText("");
    }
    
    @FXML private void resetear(ActionEvent event) {
        puntosEntrada.setText("");
    }
    
    @FXML private void mostrarPopDimension(ActionEvent e){
        mostrarPopConImagen(dimensionPop = new PopOver(), new ImageView(new Image("/img/ayuda2.png")), dimension);
    }
    
    private void mostrarPopConImagen(PopOver p, ImageView i, Node objetivo){
        
        BorderPane b = new BorderPane();
        b.setPadding(new Insets(10, 20, 10, 20));
        VBox vbox = new VBox();
        b.setCenter(vbox);
        vbox.getChildren().add(i);
        
        p.setContentNode(b);
        
        p.show(objetivo);
    }
    
    @FXML private void mostrarLeyendaEntradas(ActionEvent e){
        leyendaPop = new PopOver();
        
        ImageView a = new ImageView(new Image("/img/ayuda1.png"));
        
        mostrarPopConImagen(leyendaPop, a, patronesEntradaLabel);
    }
    
    @FXML
    private void cargarDesdeArchivo(){
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de Texto plano (*.txt)", "*.txt");
        chooser.getExtensionFilters().add(extFilter);
        File archivo = chooser.showOpenDialog(canvas.getScene().getWindow());
        if (archivo != null) {
            this.puntosEntrada.setText(readFile(archivo));
        }
        else{
            resultado.appendText("DEBE INGRESAR UN ARCHIVO VALIDO");
            resultado.setScrollTop(Double.MAX_VALUE);
        }
    }
    
    private String readFile(File f){
        String result = "";
        BufferedReader br = null;
        FileReader fr = null;

        try {

                fr = new FileReader(f.getAbsolutePath());
                br = new BufferedReader(fr);

                String sCurrentLine;

                br = new BufferedReader(new FileReader(f.getAbsolutePath()));

                while ((sCurrentLine = br.readLine()) != null) {
                        result += sCurrentLine;
                }

        } catch (IOException e) {

                e.printStackTrace();

        } finally {

                try {

                        if (br != null)
                                br.close();

                        if (fr != null)
                                fr.close();

                } catch (IOException ex) {

                        ex.printStackTrace();

                }

        }
        return result;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        initChoice();
    }

    private StackPane generarGrafico(List<List<Double>> datosPuntos, int x1, int x2, int y1, int y2){
        //Generar ejes cartesianos
        axes = new Axes(
                800, 800,
                -x1, x2, 2,
                -y1, y2, 2
        );
        //generamos el layoutAux
        StackPane layoutAux = new StackPane();
        layoutAux.setPadding(new Insets(20));
        layoutAux.setStyle("-fx-background-color: rgb(35, 39, 50);");
        layoutAux.setPrefSize(canvas.getWidth() -1, canvas.getHeight() -1);
        canvas.getChildren().add(layoutAux);
        //Agregamos los puntosClass
        List<Plot> puntosGraficar = UtilKnn.addPuntosGrafic(datosPuntos, axes);
        if (!UtilKnn.getErrorDeIndice().isEmpty()) {
            resultado.appendText(UtilKnn.getErrorDeIndice());
            resultado.setScrollTop(Double.MAX_VALUE);
            UtilKnn.setErrorDeIndice("");
                    
        }
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
    
    private void setPopOver(PopOver pop, String title, String... messages){
//        pop = new PopOver();
        BorderPane b = new BorderPane();
        b.setPadding(new Insets(10, 20, 10, 20));
        VBox vbox = new VBox();
        Label titleLabel = new Label(title);
        titleLabel.setFont(new Font(18));
        b.setCenter(vbox);
        
        vbox.getChildren().add(titleLabel);
        
        for(String message: messages){
            Label content = new Label(message);
            content.setWrapText(true);
            vbox.getChildren().add(content);
        }
        
        pop.setContentNode(b);
    }

    private void initChoice() {
        
        dimension.getItems().addAll(
            2, 3, 4, 5
        );
        dimension.setValue(2);
        cantVecinos.getItems().addAll(
            3, 4, 5, 6, 7
        );
        cantVecinos.setValue(3);
        
        xCombo.getItems().addAll(20,40,60,80,100,200,500,1000);
        xCombo1.getItems().addAll(20,40,60,80,100,200,500,1000);
        yCombo.getItems().addAll(20,40,60,80,100,200,500,1000);
        yCombo1.getItems().addAll(20,40,60,80,100,200,500,1000);
        
        xCombo.getSelectionModel().selectFirst();
        xCombo1.getSelectionModel().selectFirst();
        yCombo.getSelectionModel().selectFirst();
        yCombo1.getSelectionModel().selectFirst();
    }
    
    @FXML private void seleccionComboX(ActionEvent e){
        dibujarRedSegunCombo();
    }
    
    @FXML private void seleccionComboX1(ActionEvent e){
        dibujarRedSegunCombo();
    }
    
    @FXML private void seleccionComboY(ActionEvent e){
        dibujarRedSegunCombo();
    }
    
    @FXML private void seleccionComboY1(ActionEvent e){
        dibujarRedSegunCombo();
    }

    private void dibujarRedSegunCombo() {
        Integer x1 = xCombo.getSelectionModel().getSelectedItem();
        Integer x2 = xCombo1.getSelectionModel().getSelectedItem();
        Integer y1 = yCombo.getSelectionModel().getSelectedItem();
        Integer y2 = yCombo1.getSelectionModel().getSelectedItem();
        if (x1 == null){
            resultado.appendText("\nSeleccione el limite inferior de X");
            resultado.setScrollTop(Double.MAX_VALUE);
            return;
        }
        if (x2 == null){
            resultado.appendText("\nSeleccione el limite superior de X");
            resultado.setScrollTop(Double.MAX_VALUE);
            return;
        }
        if (y1 == null){
            resultado.appendText("\nSeleccione el limite inferior de Y");
            resultado.setScrollTop(Double.MAX_VALUE);
            return;
        }
        if (y2 == null){
            resultado.appendText("\nSeleccione el limite superior de Y"); 
            resultado.setScrollTop(Double.MAX_VALUE);
            return;
        }
        
        dibujarRed(x1, x2,y1,y2);
    }
    
    @FXML private void mostrarPopUpVecinos(ActionEvent e){
        mostrarPopConImagen(new PopOver(), new ImageView(new Image("/img/ayuda4.png")), cantVecinos);
    }
    
    @FXML private void cerrarVentana(ActionEvent e){
        // get a handle to the stage
        Stage stage = (Stage)entradasClasificar.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
