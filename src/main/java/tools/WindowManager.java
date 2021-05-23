/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import controllers.MainWindowController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author CHRISTIAN
 */
public class WindowManager {

    public Stage openWindow(String window, String title) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle(title);
            stage.setScene(scene);
            return stage;

        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public <T> T getController(String window) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
            loader.load();
            return loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(WindowManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
