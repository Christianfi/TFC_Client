/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.service.ClientService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import dtos.Client;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.textfield.CustomTextField;
import tools.AlertManager;
import tools.FilterTable;
import tools.WindowManager;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class ClientWindowController implements Initializable {

    @FXML
    private CustomTextField txtSearch;
    @FXML
    private CheckComboBox<String> cmbFilters;
    @FXML
    private Button btnNewClient;
    @FXML
    private Button btnUpdateComic;
    @FXML
    private Button btnDeleteComic;
    @FXML
    private TableView<Client> tvClientTable;
    @FXML
    private TableColumn<?, ?> tcId;
    @FXML
    private TableColumn<?, ?> tcNombre;
    @FXML
    private TableColumn<?, ?> tcApellidos;
    @FXML
    private TableColumn<?, ?> tcDni;
    @FXML
    private TableColumn<?, ?> tcEmail;
    @FXML
    private TableColumn<?, ?> tcTelefono;

    private ClientService clientService;

    private ObservableList<Client> clientList;

    private final WindowManager windowManager = new WindowManager();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtSearch.setLeft(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.SEARCH));
        this.clientService = new ClientService();
        initComboModel();
        initTable();
    }

    @FXML
    private void nuevoComicOnAction(ActionEvent event) {
        windowManager.openWindow("/fxml/NewClientForm.fxml", "Alta Cliente").showAndWait();
        initTable();
    }

    @FXML
    private void modificarOnAction(ActionEvent event) {
    }

    @FXML
    private void eliminarOnAction(ActionEvent event) {
        Client c = tvClientTable.getSelectionModel().getSelectedItem();
        if (c != null) {
            Optional<ButtonType> result
                    = AlertManager.createAlert(Alert.AlertType.CONFIRMATION, "¿Seguro que desea borrar el cliente seleccionado?", "Eliminar cliente").showAndWait();
            if (result.get() == ButtonType.OK) {
                clientService.deleteClient(c);
                initTable();
            }
        } else {
            AlertManager.createAlert(Alert.AlertType.WARNING, "No hay ningun cliente seleccionado", "Atención").show();
        }
    }

    @FXML
    private void tableComicsOnMouseClicked(MouseEvent event) {
    }

    private void initComboModel() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("ID");
        list.add("Nombre");
        list.add("Apellidos");
        list.add("DNI");
        list.add("Email");
        list.add("Telefono");
        cmbFilters.getItems().addAll(list);
        cmbFilters.getCheckModel().checkAll();
    }

    private void initTable() {
        clientList = FXCollections.observableArrayList();

        tcId.setCellValueFactory(new PropertyValueFactory("id"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("name"));
        tcApellidos.setCellValueFactory(new PropertyValueFactory("lastname"));
        tcDni.setCellValueFactory(new PropertyValueFactory("dni"));
        tcEmail.setCellValueFactory(new PropertyValueFactory("email"));
        tcTelefono.setCellValueFactory(new PropertyValueFactory("tlf"));

        clientList.addAll(clientService.getClients());

        tvClientTable.setItems(clientList);

        FilterTable.filterClientTable(tvClientTable, txtSearch, clientList, cmbFilters.getCheckModel());
    }

}
