/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.service.CollectionService;
import dtos.Collection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import tools.AlertManager;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class NewCollectionFormController implements Initializable {

    @FXML
    private CustomTextField txtNombre;
    @FXML
    private Label lblErrorNombre;
    @FXML
    private Button btnImagen;
    @FXML
    private ImageView imgView;
    @FXML
    private Button btnNuevoComic;
    @FXML
    private Button btnCancelar;

    private CollectionService collectionService;

    private File imagen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.collectionService = new CollectionService();
        formatTextFields();
    }

    @FXML
    private void btnImagenOnAction(ActionEvent event) {
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png"));

        File f = fc.showOpenDialog(this.btnCancelar.getScene().getWindow());

        if (f != null) {
            imagen = f;
            imgView.setImage(new Image(f.toURI().toString()));
        }
    }

    @FXML
    private void nuevoComicOnAction(ActionEvent event) {
        if (validateFields()) {
            int responseurl = collectionService.insertCollection(createCollection());
            if (responseurl > 0) {
                if (imagen != null) {
                    try {
                        collectionService.uploadImage(responseurl, imagen);
                        AlertManager.createAlert(Alert.AlertType.INFORMATION, "Coleccion añadida con éxito", "Coleccion añadida").show();
                        ((Stage) this.btnNuevoComic.getScene().getWindow()).close();
                    } catch (IOException ex) {
                        Logger.getLogger(NewComicFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    AlertManager.createAlert(Alert.AlertType.INFORMATION, "Coleccion añadida con éxito", "Coleccion añadida").show();
                    ((Stage) this.btnNuevoComic.getScene().getWindow()).close();
                }
            } else {
                AlertManager.createAlert(Alert.AlertType.ERROR, "Ha ocurrido un problema al añadir la coleccion", "Error").show();
            }
        }
    }

    @FXML
    private void cancelarOnAction(ActionEvent event) {
        ((Stage) this.btnCancelar.getScene().getWindow()).close();
    }

    private void formatTextFields() {
        Pattern textPattern = Pattern.compile("([A-Za-z\\d-\\s]{0,30})");
        TextFormatter<?> textFormatter = new TextFormatter<>(change -> {
            if (textPattern.matcher(change.getControlNewText()).matches()) {
                txtNombre.getStylesheets().remove("/css/validation_error.css");
                return change; // allow this change to happen
            } else {
                txtNombre.getStylesheets().add("/css/validation_error.css");
                return null; // prevent change
            }
        });

        txtNombre.setTextFormatter(textFormatter);
    }

    private boolean validateFields() {
        boolean valid = true;
        Pattern patron = Pattern.compile("([A-Za-z\\d-\\s]{1,30})");
        Matcher mat = patron.matcher(txtNombre.getText().trim());

        if (txtNombre.getText().isEmpty() || !mat.matches()) {
            lblErrorNombre.setVisible(true);
            txtNombre.getStylesheets().add("/css/validation_error.css");
            valid = false;
        } else {
            lblErrorNombre.setVisible(false);
            txtNombre.getStylesheets().remove("/css/validation_error.css");
        }
        
        return valid;
    }
    
    private Collection createCollection(){
        return new Collection(txtNombre.getText().trim());
    }
}
