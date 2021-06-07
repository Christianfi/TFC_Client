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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class CollectionComicsWindowController implements Initializable {

    @FXML
    private ListView<Comic> lvComicList;

    ObservableList comicList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comicList = FXCollections.observableArrayList();
    }

    public void setCollection(Collection c) {
        comicList.addAll(new CollectionService().getCollectionComics(c));
        lvComicList.getItems().addAll(comicList);
        initList();
    }

    private void openModify() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ComicDetail.fxml"));
            Parent root = loader.load();
            ComicDetailController controller = loader.getController();
            controller.setComic(lvComicList.getSelectionModel().getSelectedItem());
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
    private void comicListOnMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (lvComicList.getSelectionModel().getSelectedItem() != null) {
                openModify();
            }
        }
    }

    private void initList() {
        lvComicList.setCellFactory(lv -> {

            ListCell<Comic> cell = new ListCell<>() {
                @Override
                protected void updateItem(Comic item, boolean empty) {
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
                new ComicService().deleteComic(cell.getItem());
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
}
