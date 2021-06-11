/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.service.CollectionService;
import data.service.ComicService;
import dtos.Collection;
import dtos.Comic;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
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
public class NewComicFormController implements Initializable {

    @FXML
    private CustomTextField txtNombre;
    @FXML
    private CustomTextField txtIsbn;
    @FXML
    private CustomTextField txtNumero;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private ComboBox<Collection> cmbColeccion;
    @FXML
    private CustomTextField txtEditorial;
    @FXML
    private DatePicker dpFechaPublicacion;
    @FXML
    private Button btnNuevoComic;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lblErrorNombre;
    @FXML
    private Label lblErrorNumero;
    @FXML
    private Label lblErrorIsbn;
    @FXML
    private Label lblErrorEditorial;
    @FXML
    private Label lblErrorFecha;

    private ComicService comicService;
    @FXML
    private Button btnImagen;

    private File imagen;
    @FXML
    private ImageView imgView;
    @FXML
    private Menu mnuIdioma;
    @FXML
    private MenuItem mnuItemCastellano;
    @FXML
    private MenuItem mnuItemGallego;
    @FXML
    private Menu mnuAyuda;
    @FXML
    private MenuItem mnuItemAyuda;

    private ResourceBundle bundle;
    private String msg_error_nuevo;
    private String msg_confim_nuevo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comicService = new ComicService();
        formatTextFields();
        loadCombos();
    }

    @FXML
    private void nuevoComicOnAction(ActionEvent event) {
        if (validateFields()) {
            int responseurl = comicService.insertComic(createComic());
            if (responseurl > 0) {
                if (imagen != null) {
                    try {
                        comicService.uploadImage(responseurl, imagen);
                        AlertManager.createAlert(Alert.AlertType.INFORMATION, msg_confim_nuevo, "Comic añadido").show();
                        ((Stage) this.btnNuevoComic.getScene().getWindow()).close();
                    } catch (IOException ex) {
                        Logger.getLogger(NewComicFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    AlertManager.createAlert(Alert.AlertType.INFORMATION, msg_confim_nuevo, "Comic añadido").show();
                    ((Stage) this.btnNuevoComic.getScene().getWindow()).close();
                }
            } else {
                AlertManager.createAlert(Alert.AlertType.ERROR, msg_error_nuevo, "Error").show();
            }
        }
    }

    @FXML
    private void cancelarOnAction(ActionEvent event) {
        ((Stage) this.btnCancelar.getScene().getWindow()).close();
    }

    private Comic createComic() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return new Comic(txtNombre.getText().trim(), formatter.format(dpFechaPublicacion.getValue()),
                cmbEstado.getValue(), Integer.parseInt(txtNumero.getText().trim()), txtEditorial.getText().trim(),
                txtIsbn.getText().trim(), cmbColeccion.getValue().getName(), cmbColeccion.getValue().getId());
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

        mat = patron.matcher(txtEditorial.getText().trim());
        if (txtEditorial.getText().isEmpty() || !mat.matches()) {
            lblErrorEditorial.setVisible(true);
            txtEditorial.getStylesheets().add("/css/validation_error.css");
            valid = false;
        } else {
            lblErrorEditorial.setVisible(false);
            txtEditorial.getStylesheets().remove("/css/validation_error.css");
        }

        patron = Pattern.compile("([A-Za-z\\d]{5,13})");
        mat = patron.matcher(txtIsbn.getText().trim());
        if (txtIsbn.getText().isEmpty() || !mat.matches()) {
            lblErrorIsbn.setVisible(true);
            txtIsbn.getStylesheets().add("/css/validation_error.css");
            valid = false;
        } else {
            lblErrorIsbn.setVisible(false);
        }

        patron = Pattern.compile("((\\d{1,3}))");
        mat = patron.matcher(txtNumero.getText().trim());
        if (txtNumero.getText().isEmpty() || !mat.matches()) {
            lblErrorNumero.setVisible(true);
            txtNumero.getStylesheets().add("/css/validation_error.css");
            valid = false;
        } else {
            lblErrorNumero.setVisible(false);
            txtNumero.getStylesheets().remove("/css/validation_error.css");
        }

        if (dpFechaPublicacion.getValue() == null) {
            lblErrorFecha.setVisible(true);
            dpFechaPublicacion.getStylesheets().add("/css/validation_error.css");
            valid = false;
        } else {
            lblErrorFecha.setVisible(false);
            dpFechaPublicacion.getStylesheets().remove("/css/validation_error.css");
        }

        return valid;
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

        TextFormatter<?> textFormatter2 = new TextFormatter<>(change -> {
            if (textPattern.matcher(change.getControlNewText()).matches()) {
                txtEditorial.getStylesheets().remove("/css/validation_error.css");
                return change; // allow this change to happen
            } else {
                txtEditorial.getStylesheets().add("/css/validation_error.css");
                return null; // prevent change
            }
        });

        txtEditorial.setTextFormatter(textFormatter2);
        txtNombre.setTextFormatter(textFormatter);

        Pattern isbnPattern = Pattern.compile("([A-Za-z\\d]{0,13})");
        TextFormatter<?> isbnFormatter = new TextFormatter<>(change -> {
            if (isbnPattern.matcher(change.getControlNewText()).matches()) {
                txtIsbn.getStylesheets().remove("/css/validation_error.css");
                return change; // allow this change to happen
            } else {
                txtIsbn.getStylesheets().add("/css/validation_error.css");
                return null; // prevent change
            }
        });

        txtIsbn.setTextFormatter(isbnFormatter);

        Pattern numberPattern = Pattern.compile("((\\d{0,3}))");
        TextFormatter<?> numberFormatter = new TextFormatter<>(change -> {
            if (numberPattern.matcher(change.getControlNewText()).matches()) {
                txtNumero.getStylesheets().remove("/css/validation_error.css");
                return change; // allow this change to happen
            } else {
                txtNumero.getStylesheets().add("/css/validation_error.css");
                return null; // prevent change
            }
        });

        txtNumero.setTextFormatter(numberFormatter);
    }

    private void loadCombos() {
        ObservableList<Collection> list = FXCollections.observableArrayList();
        list.addAll(new CollectionService().getCollections());

        cmbColeccion.setItems(list);
        cmbColeccion.getSelectionModel().select(0);

        cmbEstado.getItems().add("Perfecto Estado");
        cmbEstado.getItems().add("Buen Estado");
        cmbEstado.getItems().add("Estado Regular");
        cmbEstado.getItems().add("Mal Estado");

        cmbEstado.getSelectionModel().select("Perfecto Estado");
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

    public void setCollectionSelected(Collection c) {
        this.cmbColeccion.getSelectionModel().select(c);
    }

    @FXML
    private void mnuItemCastellanoOnAction(ActionEvent event) {
    }

    @FXML
    private void mnuItemGallegoOnAction(ActionEvent event) {
    }

    @FXML
    private void mnuItemAyudaOnAction(ActionEvent event) {
    }

}
