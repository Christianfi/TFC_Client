/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import data.service.CollectionService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import dtos.Collection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomTextField;
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
    @FXML
    private MenuItem mnuItemNewCollection;

    private final WindowManager windowManager = new WindowManager();

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

    @FXML
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
}
