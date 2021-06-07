/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.service.ClientService;
import data.service.CollectionService;
import dtos.Client;
import dtos.Collection;
import dtos.SuscriptionDTO;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.TextFields;
import tools.AlertManager;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class NewSuscriptionFormController implements Initializable {

    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<Collection> cmbClients;

    private Client client;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btnAceptarOnAction(ActionEvent event) {
        Collection c = cmbClients.getValue();
        if (c != null) {
            new ClientService().newSuscription(new SuscriptionDTO(client.getId(), c.getId()));
            AlertManager.createAlert(Alert.AlertType.INFORMATION, "Suscripcion añadida con exito", "Suscripcion completada").show();
            ((Stage) this.btnCancelar.getScene().getWindow()).close();
        } else {
            AlertManager.createAlert(Alert.AlertType.ERROR, "Debes seleccionar una coleccion", "Nueva Suscripcion").show();
        }
    }

    @FXML
    private void btnCancelarOnAction(ActionEvent event) {
        ((Stage) this.btnCancelar.getScene().getWindow()).close();
    }

    public void setClient(Client c) {
        this.client = c;
        loadCombo();
    }

    private void loadCombo() {
        ObservableList<Collection> collectionList = FXCollections.observableArrayList(new CollectionService().getCollections());
        List<Collection> suscriptors = new ClientService().getSuscriptions(client);

        for (Collection collection : collectionList) {
            if (!suscriptors.contains(collection)) {
                cmbClients.getItems().add(collection);
            }
        }

        cmbClients.setConverter(new StringConverter<Collection>() {
            @Override
            public String toString(Collection c) {
                if (c == null) {
                    return null;
                } else {
                    return c.getName();
                }
            }

            @Override
            public Collection fromString(String s) {
                for (Collection c : cmbClients.getItems()) {
                    if (c.getName().contains(s)) {
                        return c;
                    }
                }

                return null;
            }
        });

        TextFields.bindAutoCompletion(cmbClients.getEditor(), cmbClients.getItems());
    }
}
