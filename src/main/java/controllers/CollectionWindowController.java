/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.service.ClientService;
import data.service.CollectionService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import dtos.Collection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
import org.controlsfx.control.textfield.CustomTextField;
import tools.AlertManager;
import tools.WindowManager;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class CollectionWindowController implements Initializable {

    @FXML
    private ScrollPane root;

    private final CollectionService collectionService = new CollectionService();

    private List<Collection> lista;
    @FXML
    private CustomTextField txtSearch;

    private final WindowManager windowManager = new WindowManager();
    @FXML
    private Menu mnuInformes;
    @FXML
    private MenuItem mnuInformeSuscriptores;
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
    @FXML
    private MenuItem mnuInformeComics;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setStyle("-fx-background-color:transparent;");
        txtSearch.setLeft(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.SEARCH));
        lista = collectionService.getCollections();
        loadGrid(lista);
        filterGrid();
    }

    private void loadGrid(List<Collection> list) {
        root.setContent(null);
        int row = 0;
        int column = 0;
        GridPane gridPane = new GridPane();

        for (Collection c : list) {

            ImageView imgView = createImageView(c);

            if (c.getImageURL() != null) {
                imgView.setImage(collectionService.getImage(c.getId()));
            } else {
                InputStream is = getClass().getResourceAsStream("/assets/icons/noimg.png");
                imgView.setImage(new Image(is));
            }

            Label l = new Label(c.getName());

            l.setStyle("-fx-font: 18 arial;-fx-font-weight: bold;");
            l.setWrapText(true);

            VBox box = new VBox();
            box.setAlignment(Pos.CENTER);
            VBox.setMargin(l, new Insets(10, 0, 0, 0));
            box.getChildren().addAll(imgView, l);

            gridPane.add(box, column, row);

            GridPane.setMargin(box, new Insets(40, 40, 40, 40));
            if (column < 3) {
                column++;
            } else {
                column = 0;
                row++;
            }
        }

        root.setContent(gridPane);
        root.setFitToHeight(true);
        root.setFitToWidth(true);
    }

    private ImageView createImageView(Collection c) {
        ImageView imgView = new ImageView();
        imgView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                openDetail(c);
                lista = collectionService.getCollections();
                loadGrid(lista);
                filterGrid();
                event.consume();
            }
        });

        imgView.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                imgView.setCursor(Cursor.HAND);
                event.consume();
            }
        });
        imgView.setFitHeight(188);
        imgView.setFitWidth(166);
        imgView.setPreserveRatio(true);

        return imgView;
    }

    private void filterGrid() {
        ObservableList<Collection> list = FXCollections.observableArrayList(lista);

        FilteredList<Collection> filterList = new FilteredList<>(list, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterList.setPredicate(collection -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                //check if the columns matches the filter
                //used indexOf instead of contains to match the string from the beggining
                if (collection.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
            SortedList<Collection> sortedData = new SortedList<>(filterList);

            loadGrid(sortedData);
        });

    }

    private void newCollectionOnAction(ActionEvent event) {
        windowManager.openWindow("/fxml/NewCollectionForm.fxml", "Nueva Coleccion").showAndWait();
        lista = collectionService.getCollections();
        loadGrid(lista);
        filterGrid();
    }

    private void openDetail(Collection c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CollectionDetail.fxml"));
            Parent root = loader.load();
            CollectionDetailController controller = loader.getController();
            controller.setCollection(c);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(c.getName());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ComicWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mnuInformeSuscriptoresOnAction(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Informe suscriptores");
        dialog.setHeaderText(null);
        dialog.setContentText("Coleccion: ");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(collection -> {
            boolean exists = false;
            for (Collection c : lista) {
                if (c.getName().equalsIgnoreCase(collection)) {
                    generateReport(c);
                    exists = true;
                }
            }
            if (!exists) {
                AlertManager.createAlert(Alert.AlertType.ERROR, "No se ha encontrado la coleccion", "Error").show();
            }
        });
    }

    private void generateReport(Collection c) {
        try {
            String report = "/reports/suscriptoresColeccion.jrxml";
            JasperReport jr = JasperCompileManager.compileReport(this.getClass().getResourceAsStream(report));
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(collectionService.getSuscriptors(c));

            InputStream is = this.getClass().getResourceAsStream("/assets/icons/logo.png");
            java.awt.Image img = ImageIO.read(is);

            HashMap parametros = new HashMap();
            parametros.put("LOGO", img);
            parametros.put("COLECCION", c.getName());

            JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);

            JasperViewer.viewReport(jp, false);

        } catch (JRException ex) {
            Logger.getLogger(ClientWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    @FXML
    private void mnuInformeComicsOnAction(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Informe comics");
        dialog.setHeaderText(null);
        dialog.setContentText("Coleccion: ");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(collection -> {
            boolean exists = false;
            for (Collection c : lista) {
                if (c.getName().equalsIgnoreCase(collection)) {
                    try {
                        String report = "/reports/ComisEstado.jrxml";
                        JasperReport jr = JasperCompileManager.compileReport(this.getClass().getResourceAsStream(report));
                        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(collectionService.getCollectionComics(c));

                        InputStream is = this.getClass().getResourceAsStream("/assets/icons/logo.png");
                        java.awt.Image img = ImageIO.read(is);

                        HashMap parametros = new HashMap();
                        parametros.put("IMAGEN", img);
                        parametros.put("ESTADO", "Coleccion: "+c.getName());

                        JasperPrint jp = JasperFillManager.fillReport(jr, parametros, ds);

                        JasperViewer.viewReport(jp, false);

                    } catch (JRException ex) {
                        Logger.getLogger(ClientWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ClientWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    exists = true;
                }
            }
            if (!exists) {
                AlertManager.createAlert(Alert.AlertType.ERROR, "No se ha encontrado la coleccion", "Error").show();
            }
        });
    }
}
