/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import tools.WindowManager;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class MainWindowController implements Initializable {

    public static Locale locale;
    
    @FXML
    private Button btnComics;
    @FXML
    private Button btnColecciones;
    @FXML
    private Button btnClientes;
    private WindowManager windowManager;
    @FXML
    private GridPane root;
    @FXML
    private MenuItem menuItemAyuda;
    @FXML
    private Menu mnuIdioma;
    @FXML
    private MenuItem mnuItemCastellano;
    @FXML
    private MenuItem mnuItemGallego;
    @FXML
    private Menu mnuAyuda;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.windowManager = new WindowManager();
        locale = Locale.getDefault();
        i18n();
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

    @FXML
    private void menuItemAyuda(ActionEvent event) {
         this.windowManager.openWindow("/fxml/HelpWindow.fxml", "Ayuda").show();
    }

    @FXML
    private void mnuItemCastellanoOnAction(ActionEvent event) {
        this.locale = new Locale("es","ES");
        Locale.setDefault(locale);
        i18n();
    }

    @FXML
    private void mnuItemGallegoOnAction(ActionEvent event) {
        this.locale = new Locale("gl","ES");
        Locale.setDefault(locale);
        i18n();
    }

    private void i18n() {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.i18n");
        
        btnComics.setText(bundle.getString("main_btn_comics"));
        btnColecciones.setText(bundle.getString("main_btn_colecciones"));
        btnClientes.setText(bundle.getString("main_btn_clientes"));
        mnuAyuda.setText(bundle.getString("main_menu_ayuda"));
        mnuIdioma.setText(bundle.getString("main_menu_idioma"));
        mnuItemCastellano.setText(bundle.getString("main_menu_idioma1"));
        mnuItemGallego.setText(bundle.getString("main_menu_idioma2"));
        mnuAyuda.setText(bundle.getString("main_menu_ayuda"));
        menuItemAyuda.setText(bundle.getString("main_menu_ayuda1"));
        
    }

}
