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
public class NewSuscriptorFormController implements Initializable {

    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    @FXML
    private ComboBox<Client> cmbClients;
    private Collection collection;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void btnAceptarOnAction(ActionEvent event) {
        Client c = cmbClients.getValue();
        if (c != null) {
            new ClientService().newSuscription(new SuscriptionDTO(c.getId(), collection.getId()));
            AlertManager.createAlert(Alert.AlertType.INFORMATION, "Suscriptor añadido con exito", "Suscripcion completada").show();
            ((Stage) this.btnCancelar.getScene().getWindow()).close();
        } else {
            AlertManager.createAlert(Alert.AlertType.ERROR, "Debes seleccionar un cliente", "Nueva Suscripcion").show();
        }
    }

    @FXML
    private void btnCancelarOnAction(ActionEvent event) {
        ((Stage) this.btnCancelar.getScene().getWindow()).close();
    }

    private void loadCombo() {
        ObservableList<Client> clientList = FXCollections.observableArrayList(new ClientService().getClients());
        List<Client> suscriptors = new CollectionService().getSuscriptors(collection);
        
        for (Client client : clientList) {
            if(!suscriptors.contains(client)){
                cmbClients.getItems().add(client);
            }
        }
        
        cmbClients.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client c) {
                if (c == null) {
                    return null;
                } else {
                    return c.getDni() + " - " + c.getName() + " " + c.getLastname();
                }
            }

            @Override
            public Client fromString(String s) {
                for (Client c : cmbClients.getItems()) {
                    if (c.getDni().contains(s.split("-")[0].trim())) {
                        return c;
                    }
                }

                return null;
            }
        });

        TextFields.bindAutoCompletion(cmbClients.getEditor(), cmbClients.getItems());
    }

    public void setCollection(Collection c) {
        this.collection = c;
        loadCombo();
    }
}
