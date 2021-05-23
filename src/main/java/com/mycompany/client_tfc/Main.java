/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.client_tfc;

import javafx.application.Application;
import javafx.stage.Stage;
import tools.WindowManager;

/**
 *
 * @author CHRISTIAN
 */
public class Main extends Application{
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
       new WindowManager().openWindow("/fxml/ComicWindow.fxml", "Gestion de Comics").show();
    }
    
}
