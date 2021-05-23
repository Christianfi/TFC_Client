/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import javafx.scene.control.Alert;

/**
 *
 * @author CHRISTIAN
 */
public class AlertManager {

    public static Alert createAlert(Alert.AlertType type, String msj, String title) {

        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(msj);

        return alert;
    }
}
