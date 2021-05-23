/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import dtos.Client;
import dtos.Comic;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableView;
import org.controlsfx.control.IndexedCheckModel;
import org.controlsfx.control.textfield.CustomTextField;

/**
 *
 * @author CHRISTIAN
 */
public class FilterTable {

    public static void filterComicTable(TableView<Comic> table, CustomTextField tfSearch, ObservableList<Comic> comics, IndexedCheckModel<String> checkModel) {

        FilteredList<Comic> filterList = new FilteredList<>(comics, b -> true);

        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterList.setPredicate(comic -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                //check if the columns matches the filter
                //used indexOf instead of contains to match the string from the beggining
                if (checkModel.isChecked("ID") && String.valueOf(comic.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (checkModel.isChecked("Nombre") && comic.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (checkModel.isChecked("Coleccion") && comic.getCollectionName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (checkModel.isChecked("Fecha de Publicacion") && comic.getPublishDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (checkModel.isChecked("Numero") && String.valueOf(comic.getNumber()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (checkModel.isChecked("Editorial") && comic.getPublisher().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if(checkModel.isChecked("Estado") && comic.getState().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if(checkModel.isChecked("ISBN") && comic.getIsbn().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Comic> sortedData = new SortedList<>(filterList);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
    }
    
    public static void filterClientTable(TableView<Client> table, CustomTextField tfSearch, ObservableList<Client> clients, IndexedCheckModel<String> checkModel) {

        FilteredList<Client> filterList = new FilteredList<>(clients, b -> true);

        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterList.setPredicate(client -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                //check if the columns matches the filter
                //used indexOf instead of contains to match the string from the beggining
                if (checkModel.isChecked("ID") && String.valueOf(client.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (checkModel.isChecked("Nombre") && client.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (checkModel.isChecked("Apellidos") && client.getLastname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (checkModel.isChecked("DNI") && client.getDni().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (checkModel.isChecked("Email") && client.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (checkModel.isChecked("Telefono") && client.getTlf().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Client> sortedData = new SortedList<>(filterList);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
    }
}
