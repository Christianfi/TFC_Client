/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import tools.WindowManager;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class MainWindowController implements Initializable {

    @FXML
    private Button btnComics;
    @FXML
    private Button btnColecciones;
    @FXML
    private Button btnClientes;
    private WindowManager windowManager;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.windowManager = new WindowManager();
    }    

    @FXML
    private void btnComicsOnAction(ActionEvent event) {
        this.windowManager.openWindow("/fxml/ComicWindow.fxml", "Gestion de comics").show();
    }

    @FXML
    private void btnColeccionesOnAction(ActionEvent event) {
        this.windowManager.openWindow("/fxml/CollectionWindow.fxml", "Gestion de colecciones").show();
    }

    @FXML
    private void btnClientesOnAction(ActionEvent event) {
        this.windowManager.openWindow("/fxml/ClientWindow.fxml", "Gestion de clientes").show();
    }
    
}
