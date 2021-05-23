/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.service.ClientService;
import dtos.Client;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
public class ClientDetailController implements Initializable {

    @FXML
    private Label lblNombre;
    @FXML
    private Label lblApellidos;
    @FXML
    private Label lblDni;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblTelefono;
    @FXML
    private CustomTextField txtNombre;
    @FXML
    private Label lblErrorNombre;
    @FXML
    private CustomTextField txtApellidos;
    @FXML
    private Label lblErrorApellidos;
    @FXML
    private CustomTextField txtDni;
    @FXML
    private Label lblErrorDni;
    @FXML
    private CustomTextField txtEmail;
    @FXML
    private Label lblErrorEmail;
    @FXML
    private CustomTextField txtTelefono;
    @FXML
    private Label lblErrorTelefono;
    @FXML
    private Button btnNuevoComic;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnEditar;

    private Client client;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formatTexfields();
    }

    @FXML
    private void nuevoComicOnAction(ActionEvent event) {
        if (validateFields()) {
            Client c = createClient();
            c.setId(client.getId());
            int response = new ClientService().updateClient(c);
            if (response == 200 || response == 201) {
                AlertManager.createAlert(Alert.AlertType.INFORMATION, "Cliente modificado con éxito", "Cliente modificado").show();
                ((Stage) this.btnNuevoComic.getScene().getWindow()).close();
            } else {
                AlertManager.createAlert(Alert.AlertType.ERROR, "Ha ocurrido un problema al modificar el cliente", "Error").show();
            }
        }
    }

    @FXML
    private void cancelarOnAction(ActionEvent event) {
        ((Stage) this.btnNuevoComic.getScene().getWindow()).close();
    }

    @FXML
    private void btnEditarOnAction(ActionEvent event) {
        lblNombre.setVisible(false);
        lblApellidos.setVisible(false);
        lblDni.setVisible(false);
        lblEmail.setVisible(false);
        lblTelefono.setVisible(false);

        txtNombre.setVisible(true);
        txtApellidos.setVisible(true);
        txtDni.setVisible(true);
        txtEmail.setVisible(true);
        txtTelefono.setVisible(true);
    }

    private boolean validateFields() {

        boolean valid = true;
        Pattern patron = Pattern.compile("([\\w\\u00f1\\u00d1\\s]{2,30})");
        Matcher mat = patron.matcher(txtNombre.getText().trim());

        if (txtNombre.getText().isEmpty() || !mat.matches()) {
            lblErrorNombre.setVisible(true);
            txtNombre.getStylesheets().add("/css/validation_error.css");
            valid = false;
        } else {
            lblErrorNombre.setVisible(false);
            txtNombre.getStylesheets().remove("/css/validation_error.css");
        }

        mat = patron.matcher(txtApellidos.getText().trim());
        if (txtApellidos.getText().isEmpty() || !mat.matches()) {
            lblErrorApellidos.setVisible(true);
            txtApellidos.getStylesheets().add("/css/validation_error.css");
            valid = false;
        } else {
            lblErrorApellidos.setVisible(false);
            txtApellidos.getStylesheets().remove("/css/validation_error.css");
        }

        patron = Pattern.compile("[0-9]{8}[A-Z a-z]");
        mat = patron.matcher(txtDni.getText().trim());
        if (txtDni.getText().isEmpty() || !mat.matches()) {
            lblErrorDni.setVisible(true);
            txtDni.getStylesheets().add("/css/validation_error.css");
            valid = false;
        } else {
            lblErrorDni.setVisible(false);
            txtDni.getStylesheets().remove("/css/validation_error.css");
        }

        patron = Pattern.compile("^([\\w\\u00f1\\u00d1\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$");
        mat = patron.matcher(txtEmail.getText().trim());
        if (txtEmail.getText().isEmpty() || !mat.matches()) {
            lblErrorEmail.setVisible(true);
            txtEmail.getStylesheets().add("/css/validation_error.css");
            valid = false;
        } else {
            lblErrorEmail.setVisible(false);
            txtEmail.getStylesheets().remove("/css/validation_error.css");
        }

        patron = Pattern.compile("[0-9]{9}");
        mat = patron.matcher(txtTelefono.getText().trim());
        if (txtTelefono.getText().isEmpty() || !mat.matches()) {
            lblErrorTelefono.setVisible(true);
            txtTelefono.getStylesheets().add("/css/validation_error.css");
            valid = false;
        } else {
            lblErrorTelefono.setVisible(false);
            txtTelefono.getStylesheets().remove("/css/validation_error.css");
        }

        return valid;
    }

    private void formatTexfields() {
        Pattern textPattern = Pattern.compile("([A-Za-z\\u00f1\\u00d1\\s]{0,30})");
        TextFormatter<?> nameFormatter = new TextFormatter<>(change -> {
            if (textPattern.matcher(change.getControlNewText()).matches()) {
                txtNombre.getStylesheets().remove("/css/validation_error.css");
                return change; // allow this change to happen
            } else {
                txtNombre.getStylesheets().add("/css/validation_error.css");
                return null; // prevent change
            }
        });
        txtNombre.setTextFormatter(nameFormatter);

        TextFormatter<?> lastNameFormatter = new TextFormatter<>(change -> {
            if (textPattern.matcher(change.getControlNewText()).matches()) {
                txtApellidos.getStylesheets().remove("/css/validation_error.css");
                return change; // allow this change to happen
            } else {
                txtApellidos.getStylesheets().add("/css/validation_error.css");
                return null; // prevent change
            }
        });
        txtApellidos.setTextFormatter(lastNameFormatter);

        Pattern dniPattern = Pattern.compile("(\\w){0,9}");
        TextFormatter<?> dniFormatter = new TextFormatter<>(change -> {
            if (dniPattern.matcher(change.getControlNewText()).matches()) {
                txtDni.getStylesheets().remove("/css/validation_error.css");
                return change; // allow this change to happen
            } else {
                txtDni.getStylesheets().add("/css/validation_error.css");
                return null; // prevent change
            }
        });
        txtDni.setTextFormatter(dniFormatter);

        Pattern emailPattern = Pattern.compile("[\\w\\u00f1\\u00d1@.]{0,30}");
        TextFormatter<?> emailFormatter = new TextFormatter<>(change -> {
            if (emailPattern.matcher(change.getControlNewText()).matches()) {
                txtEmail.getStylesheets().remove("/css/validation_error.css");
                return change; // allow this change to happen
            } else {
                txtEmail.getStylesheets().add("/css/validation_error.css");
                return null; // prevent change
            }
        });
        txtEmail.setTextFormatter(emailFormatter);

        Pattern tlfPattern = Pattern.compile("[0-9]{0,9}");
        TextFormatter<?> tlfFormatter = new TextFormatter<>(change -> {
            if (tlfPattern.matcher(change.getControlNewText()).matches()) {
                txtTelefono.getStylesheets().remove("/css/validation_error.css");
                return change; // allow this change to happen
            } else {
                txtTelefono.getStylesheets().add("/css/validation_error.css");
                return null; // prevent change
            }
        });
        txtTelefono.setTextFormatter(tlfFormatter);
    }

    private Client createClient() {

        return new Client(txtNombre.getText().trim(), txtApellidos.getText().trim(),
                txtDni.getText().trim(), txtEmail.getText().trim(), txtTelefono.getText().trim());
    }

    public void setClient(Client c) {
        this.client = c;
        initLabels();
    }

    private void initLabels() {
        lblNombre.setText(client.getName());
        lblApellidos.setText(client.getLastname());
        lblDni.setText(client.getDni());
        lblEmail.setText(client.getEmail());
        lblTelefono.setText(client.getTlf());

        txtNombre.setText(client.getName());
        txtApellidos.setText(client.getLastname());
        txtDni.setText(client.getDni());
        txtEmail.setText(client.getEmail());
        txtTelefono.setText(client.getTlf());
    }
}
