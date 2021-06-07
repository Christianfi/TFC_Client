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
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import tools.AlertManager;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class CollectionDetailController implements Initializable {

    @FXML
    private Label lblNombre;
    @FXML
    private CustomTextField txtNombre;
    @FXML
    private Label lblErrorNombre;
    @FXML
    private Button btnImagen;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnComics;
    @FXML
    private Button btnNewComic;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnSuscription;

    private Collection collection;
    @FXML
    private ImageView imgView;

    private File imagen;

    private CollectionService collectionService;

    private boolean editMode = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCancelar.setVisible(false);
        btnGuardar.setVisible(false);
        collectionService = new CollectionService();
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
    private void btnGuardarOnAction(ActionEvent event) {
        if (editMode) {
            if (validateFields()) {
                int responseurl = collectionService.updateCollection(collection);
                if (responseurl > 0) {
                    if (imagen != null) {
                        try {
                            collectionService.uploadImage(collection.getId(), imagen);
                            AlertManager.createAlert(Alert.AlertType.INFORMATION, "Coleccion modificada con éxito", "Coleccion modificada").show();
                            ((Stage) this.btnCancelar.getScene().getWindow()).close();
                        } catch (IOException ex) {
                            Logger.getLogger(NewComicFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        AlertManager.createAlert(Alert.AlertType.INFORMATION, "Coleccion modificada con éxito", "Coleccion modificada").show();
                        ((Stage) this.btnCancelar.getScene().getWindow()).close();
                    }
                } else {
                    AlertManager.createAlert(Alert.AlertType.ERROR, "Ha ocurrido un problema al modificar la coleccion", "Error").show();
                }
            }
        } else {
            ((Stage) btnCancelar.getScene().getWindow()).close();
        }
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

    @FXML
    private void btnCancelarOnAction(ActionEvent event) {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }

    @FXML
    private void btnComicsOnAction(ActionEvent event) {
        openComicWindow();
    }

    @FXML
    private void btnNewcomicOnAction(ActionEvent event) {
        openNewComicForm();
    }

    @FXML
    private void btnEditarOnAction(ActionEvent event) {
        lblNombre.setVisible(false);
        txtNombre.setVisible(true);
        btnImagen.setVisible(true);
        btnCancelar.setVisible(true);
        btnGuardar.setVisible(true);
        editMode = true;
    }

    @FXML
    private void btnEliminarOnAction(ActionEvent event) {
        Optional<ButtonType> result
                = AlertManager.createAlert(Alert.AlertType.CONFIRMATION, "¿Seguro que desea borrar la coleccion?", "Eliminar coleccion").showAndWait();
        if (result.get() == ButtonType.OK) {
            collectionService.deleteCollection(collection);
            ((Stage) btnCancelar.getScene().getWindow()).close();
        }

    }

    @FXML
    private void btnSuscriptionOnAction(ActionEvent event) {
        openSuscriptors();
    }

    public void setCollection(Collection c) {
        this.collection = c;
        initLabels();
    }

    private void initLabels() {
        lblNombre.setText(collection.getName());
        txtNombre.setText(collection.getName());
        if (collection.getImageURL() != null) {
            imgView.setImage(collectionService.getImage(collection.getId()));
        } else {
            InputStream is = getClass().getResourceAsStream("/assets/icons/noimg.png");
            imgView.setImage(new Image(is));
        }
    }

    private void openNewComicForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewComicForm.fxml"));
            Parent root = loader.load();
            NewComicFormController controller = loader.getController();
            controller.setCollectionSelected(collection);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nuevo Comic");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ComicWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openComicWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CollectionComicsWindow.fxml"));
            Parent root = loader.load();
            CollectionComicsWindowController controller = loader.getController();
            controller.setCollection(collection);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(collection.getName() + " - Comics");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ComicWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openSuscriptors() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CollectionSuscriptorsWindow.fxml"));
            Parent root = loader.load();
            CollectionSuscriptorsWindowController controller = loader.getController();
            controller.setCollection(collection);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(collection.getName() + " - Suscriptores");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ComicWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
}
