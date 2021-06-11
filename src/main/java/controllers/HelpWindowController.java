/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author CHRISTIAN
 */
public class HelpWindowController implements Initializable {

    @FXML
    private Label lblComicsHome;
    @FXML
    private Label lblCollectionHome;
    @FXML
    private Label lblClientHome;
    @FXML
    private WebView webView;
    @FXML
    private Label lblNewcomic;
    @FXML
    private Label lblDetailComic;
    @FXML
    private Label lblNewCollection;
    @FXML
    private Label lblDetailCollection;
    @FXML
    private Label lblNewClient;
    @FXML
    private Label lblDetailClient;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webView.getEngine().load(this.getClass().getResource("/help/pages/index.html").toString());
    }    

    @FXML
    private void comicHomeOnMouseClicked(MouseEvent event) {
        webView.getEngine().load(this.getClass().getResource("/help/pages/comicsHome.html").toString());
    }

    @FXML
    private void collectionHomeOnMouseClicked(MouseEvent event) {
        webView.getEngine().load(this.getClass().getResource("/help/pages/collectionHome.html").toString());
    }

    @FXML
    private void clientHomeOnMouseClicked(MouseEvent event) {
        webView.getEngine().load(this.getClass().getResource("/help/pages/clientHome.html").toString());
    }
    
    public void setHelpPage(String htmlFile){
        webView.getEngine().load(this.getClass().getResource("/help/pages/"+htmlFile).toString());
    }

    @FXML
    private void newComicOnClick(MouseEvent event) {
        webView.getEngine().load(this.getClass().getResource("/help/pages/newComic.html").toString());
    }

    @FXML
    private void detailComicOnClick(MouseEvent event) {
        webView.getEngine().load(this.getClass().getResource("/help/pages/detailComic.html").toString());
    }

    @FXML
    private void newCollectionOnClick(MouseEvent event) {
        webView.getEngine().load(this.getClass().getResource("/help/pages/newCollection.html").toString());
    }

    @FXML
    private void detailCollectionOnClick(MouseEvent event) {
        webView.getEngine().load(this.getClass().getResource("/help/pages/detailCollection.html").toString());
    }

    @FXML
    private void newClientOnClick(MouseEvent event) {
        webView.getEngine().load(this.getClass().getResource("/help/pages/newClient.html").toString());
    }

    @FXML
    private void detailClientOnClick(MouseEvent event) {
        webView.getEngine().load(this.getClass().getResource("/help/pages/detailClient.html").toString());
    }
}
