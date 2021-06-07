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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class ClientSuscriptionsWindowController implements Initializable {

    @FXML
    private ListView<Collection> lvClientList;
    @FXML
    private Button btnNewSuscriptor;

    Client c;

    ObservableList collectionList;

    public void setClient(Client c) {
        this.c = c;
        loadList();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.collectionList = FXCollections.observableArrayList();
    }

    @FXML
    private void btnNewSuscriptorOnAction(ActionEvent event) {
        openNewSuscription();
        loadList();
    }

    private void loadList() {
        lvClientList.getItems().clear();
        collectionList.clear();
        collectionList.addAll(new ClientService().getSuscriptions(c));
        lvClientList.getItems().addAll(collectionList);
        initList();
    }

    private void initList() {
        lvClientList.setCellFactory(lv -> {

            ListCell<Collection> cell = new ListCell<>() {
                @Override
                protected void updateItem(Collection item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText("");
                    } else {
                        setText(item.toString());
                    }
                }
            };

            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
            deleteItem.setOnAction(event -> {
                new ClientService().deleteSuscription(new SuscriptionDTO(c.getId(), cell.getItem().getId()));
                loadList();
            });
            contextMenu.getItems().addAll(deleteItem);

            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });

            return cell;
        });

    }

    private void openNewSuscription() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewSuscriptionForm.fxml"));
            Parent root = loader.load();
            NewSuscriptionFormController controller = loader.getController();
            controller.setClient(c);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nueva Suscripcion");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ComicWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
