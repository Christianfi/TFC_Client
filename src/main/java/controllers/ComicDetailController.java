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
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
import tools.AlertManager;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class ComicDetailController implements Initializable {

    @FXML
    private CustomTextField txtNombre;
    @FXML
    private CustomTextField txtNumero;
    @FXML
    private CustomTextField txtIsbn;
    @FXML
    private CustomTextField txtEditorial;
    @FXML
    private ComboBox<Collection> cmbColeccion;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private Label lblNombre;
    @FXML
    private Label lblNumero;
    @FXML
    private Label lblIsbn;
    @FXML
    private Label lblEditorial;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblColeccion;
    @FXML
    private Label lblEstado;
    @FXML
    private Label lblErrorNombre;
    @FXML
    private Label lblErrorNumero;
    @FXML
    private Label lblErrorIsbn;
    @FXML
    private Label lblErrorEditorial;
    @FXML
    private DatePicker dpFechaPublicacion;
    @FXML
    private Label lblErrorFecha;
    @FXML
    private Button btnNuevoComic;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnEditar;

    private Comic comic;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCombos();
        formatTextFields();
    }

    private void initCombos() {
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

    @FXML
    private void nuevoComicOnAction(ActionEvent event) {
        if (validateFields()) {
            Comic c = createComic();
            c.setId(comic.getId());
            int response = new ComicService().updateComic(c);
            if (response == 200 || response == 201) {
                AlertManager.createAlert(Alert.AlertType.INFORMATION, "Comic modificado con éxito", "Comic modificado").show();
                ((Stage) this.btnNuevoComic.getScene().getWindow()).close();
            } else {
                AlertManager.createAlert(Alert.AlertType.ERROR, "Ha ocurrido un problema al modificar el comic", "Error").show();
            }
        }
    }

    @FXML
    private void cancelarOnAction(ActionEvent event) {
        ((Stage) this.btnNuevoComic.getScene().getWindow()).close();
    }

    @FXML
    private void btnEditarOnAction(ActionEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        formatter = formatter.withLocale(Locale.getDefault());
        
        txtNombre.setText(comic.getName());
        txtIsbn.setText(comic.getIsbn());
        txtEditorial.setText(comic.getPublisher());
        txtNumero.setText(String.valueOf(comic.getNumber()));
        dpFechaPublicacion.setValue(LocalDate.parse(comic.getPublishDate(), formatter));

        txtNombre.setVisible(true);
        txtIsbn.setVisible(true);
        txtEditorial.setVisible(true);
        txtNumero.setVisible(true);
        cmbColeccion.setVisible(true);
        cmbEstado.setVisible(true);
        dpFechaPublicacion.setVisible(true);

        lblNombre.setVisible(false);
        lblIsbn.setVisible(false);
        lblEstado.setVisible(false);
        lblEditorial.setVisible(false);
        lblNumero.setVisible(false);
        lblFecha.setVisible(false);
        lblColeccion.setVisible(false);

    }

    public void setComic(Comic comic) {
        this.comic = comic;
        initLabels();
    }

    private void initLabels() {

        lblNombre.setText(comic.getName());
        lblIsbn.setText(comic.getIsbn());
        lblEstado.setText(comic.getState());
        lblEditorial.setText(comic.getPublisher());
        lblNumero.setText(String.valueOf(comic.getNumber()));
        lblFecha.setText(comic.getPublishDate());
        lblColeccion.setText(comic.getCollectionName());

        cmbColeccion.getItems().forEach(collection -> {
            if (collection.getName().equalsIgnoreCase(comic.getName())) {
                cmbColeccion.getSelectionModel().select(collection);
            }
        });

        cmbEstado.getSelectionModel().select(comic.getState());

    }

    private Comic createComic() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return new Comic(txtNombre.getText().trim(), formatter.format(dpFechaPublicacion.getValue()),
                cmbEstado.getValue(), Integer.parseInt(txtNumero.getText().trim()), txtEditorial.getText().trim(),
                txtIsbn.getText().trim(), cmbColeccion.getValue().getName(), cmbColeccion.getValue().getId());
    }
}
