/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.service.ComicService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import dtos.Comic;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
public class ComicWindowController implements Initializable {

    @FXML
    private CustomTextField txtSearch;
    @FXML
    private Button btnUpdateComic;
    @FXML
    private Button btnDeleteComic;
    @FXML
    private TableView<Comic> tvComicTable;
    @FXML
    private CheckComboBox<String> cmbFilters;

    private ComicService comicService;

    private ObservableList<Comic> comicList;

    @FXML
    private TableColumn tcId;

    @FXML
    private TableColumn tcIsbn;

    @FXML
    private TableColumn tcNombre;

    @FXML
    private TableColumn tcColeccion;

    @FXML
    private TableColumn tcFechaPublicacion;

    @FXML
    private TableColumn tcNumero;

    @FXML
    private TableColumn tcEditorial;

    @FXML
    private TableColumn tcEstado;

    private final WindowManager windowManager = new WindowManager();
    @FXML
    private Button btnNewComic;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtSearch.setLeft(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.SEARCH));
        this.comicService = new ComicService();
        initComboModel();
        initTable();
    }

    public void initTable() {

        comicList = FXCollections.observableArrayList();

        tcId.setCellValueFactory(new PropertyValueFactory("id"));
        tcIsbn.setCellValueFactory(new PropertyValueFactory("isbn"));
        tcNombre.setCellValueFactory(new PropertyValueFactory("name"));
        tcFechaPublicacion.setCellValueFactory(new PropertyValueFactory("publishDate"));
        tcEstado.setCellValueFactory(new PropertyValueFactory("state"));
        tcNumero.setCellValueFactory(new PropertyValueFactory("number"));
        tcEditorial.setCellValueFactory(new PropertyValueFactory("publisher"));
        tcColeccion.setCellValueFactory(new PropertyValueFactory("collectionName"));

        comicList.addAll(comicService.getComics());

        tvComicTable.setItems(comicList);

        FilterTable.filterComicTable(tvComicTable, txtSearch, comicList, cmbFilters.getCheckModel());
    }

    private void initComboModel() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("ID");
        list.add("ISBN");
        list.add("Nombre");
        list.add("Coleccion");
        list.add("Fecha de Publicacion");
        list.add("Numero");
        list.add("Editorial");
        list.add("Estado");
        cmbFilters.getItems().addAll(list);
        cmbFilters.getCheckModel().checkAll();
    }

    @FXML
    private void nuevoComicOnAction(ActionEvent event) {
        Stage stage = windowManager.openWindow("/fxml/NewComicForm.fxml", "Nuevo Comic");
        stage.showAndWait();
        initTable();
    }

    @FXML
    private void modificarOnAction(ActionEvent event) {
        Comic c;
        if ((c = tvComicTable.getSelectionModel().getSelectedItem()) != null) {
            openModify();
            initTable();
        } else {
            AlertManager.createAlert(Alert.AlertType.WARNING, "No hay ningun comic seleccionado", "Atención").show();
        }
    }

    @FXML
    private void eliminarOnAction(ActionEvent event) {
        Comic c = tvComicTable.getSelectionModel().getSelectedItem();
        if (c != null) {
            Optional<ButtonType> result
                    = AlertManager.createAlert(Alert.AlertType.CONFIRMATION, "¿Seguro que desea borrar el comic seleccionado?", "Borrar comic").showAndWait();
            if (result.get() == ButtonType.OK) {
                comicService.deleteComic(c);
                initTable();
            }
        } else {
            AlertManager.createAlert(Alert.AlertType.WARNING, "No hay ningun comic seleccionado", "Atención").show();
        }
    }

    @FXML
    private void tableComicsOnMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (tvComicTable.getSelectionModel().getSelectedItem() != null) {
                openModify();
                initTable();
            }
        }
    }

    private void openModify() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ComicDetail.fxml"));
            Parent root = loader.load();
            ComicDetailController controller = loader.getController();
            controller.setComic(tvComicTable.getSelectionModel().getSelectedItem());
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Detalles del comic");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ComicWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
