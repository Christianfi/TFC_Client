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
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
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

    private String msg_error_seleccion;
    private String msg_confirmacion_eliminar;
    @FXML
    private Menu mnuInformes;
    @FXML
    private Menu mnuInformesEstado;
    @FXML
    private MenuItem mnuItemPerfectoEstado;
    @FXML
    private MenuItem mnuItemMalEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtSearch.setLeft(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.SEARCH));
        this.comicService = new ComicService();
        Locale.setDefault(MainWindowController.locale);
        initTable();
        i18n();
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
        cmbFilters.getItems().clear();
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("ID");
        list.add("ISBN");
        list.add(bundle.getString("nombre"));
        list.add("Coleccion");
        list.add(bundle.getString("fecha_publicacion"));
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
            AlertManager.createAlert(Alert.AlertType.WARNING, msg_error_seleccion, "Atención").show();
        }
    }

    @FXML
    private void eliminarOnAction(ActionEvent event) {
        Comic c = tvComicTable.getSelectionModel().getSelectedItem();
        if (c != null) {
            Optional<ButtonType> result
                    = AlertManager.createAlert(Alert.AlertType.CONFIRMATION, msg_confirmacion_eliminar, "Borrar comic").showAndWait();
            if (result.get() == ButtonType.OK) {
                comicService.deleteComic(c);
                initTable();
            }
        } else {
            AlertManager.createAlert(Alert.AlertType.WARNING, msg_error_seleccion, "Atención").show();
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

    @FXML
    private void mnuItemCastellanoOnAction(ActionEvent event) {
        MainWindowController.locale = new Locale("es", "ES");
        Locale.setDefault(MainWindowController.locale);
        i18n();
    }

    @FXML
    private void mnuItemGallegoOnAction(ActionEvent event) {
        MainWindowController.locale = new Locale("gl", "ES");
        Locale.setDefault(MainWindowController.locale);
        i18n();
    }

    @FXML
    private void mnuItemAyudaOnAction(ActionEvent event) {

    }

    private void i18n() {
        bundle = ResourceBundle.getBundle("i18n.i18n");
        tcNombre.setText(bundle.getString("nombre"));
        tcFechaPublicacion.setText(bundle.getString("fecha_publicacion"));
        txtSearch.setPromptText(bundle.getString("search_prompt"));
        cmbFilters.setTitle(bundle.getString("filtros"));
        btnNewComic.setText(bundle.getString("comics_btn_nuevo"));
        mnuAyuda.setText(bundle.getString("main_menu_ayuda"));
        mnuIdioma.setText(bundle.getString("main_menu_idioma"));
        mnuItemCastellano.setText(bundle.getString("main_menu_idioma1"));
        mnuItemGallego.setText(bundle.getString("main_menu_idioma2"));
        mnuAyuda.setText(bundle.getString("main_menu_ayuda"));
        mnuItemAyuda.setText(bundle.getString("main_menu_ayuda1"));
        msg_confirmacion_eliminar = bundle.getString("msg_confirmacion_eliminar_comic");
        msg_error_seleccion = bundle.getString("msg_error_seleccion");
        initComboModel();
    }

    @FXML
    private void mnuItemPerfectoEstadoOnAction(ActionEvent event) {
        generateReportByState("Perfecto estado");
    }

    @FXML
    private void mnuItemMalEstadoOnAction(ActionEvent event) {
        generateReportByState("Mal estado");
    }

    private void generateReportByState(String state) {
        try {
            String report = "/reports/ComisEstado.jrxml";
            JasperReport jr = JasperCompileManager.compileReport(this.getClass().getResourceAsStream(report));

            List<Comic> list = comicService.getComics();
            List<Comic> stateList = new ArrayList<>();
            for (Comic comic : list) {
                if (comic.getState().equalsIgnoreCase(state)) {
                    stateList.add(comic);
                }
            }

            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(stateList);

            InputStream is = this.getClass().getResourceAsStream("/assets/icons/logo.png");
            Image img = ImageIO.read(is);

            HashMap parametros = new HashMap();
            parametros.put("IMAGEN", img);
            parametros.put("ESTADO", "Estado: "+state);

            JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);

            JasperViewer.viewReport(jp, false);

        } catch (JRException ex) {
            Logger.getLogger(ClientWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
