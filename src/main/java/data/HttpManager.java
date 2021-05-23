/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CHRISTIAN
 */
public class HttpManager {

    private HttpClient httpClient;
    public final static String SERVER_ENDPOINT = "http://localhost:8080";

    public HttpManager() {
        httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> getRequest(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_ENDPOINT+url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response;
        } catch (IOException ex) {
            Logger.getLogger(HttpManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HttpManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public HttpResponse<String> postRequest(String url, String body) {

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(SERVER_ENDPOINT+url)).
                    POST(BodyPublishers.ofString(body))
                    .header("Content-type", "application/json").
                    build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException ex) {
            Logger.getLogger(HttpManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HttpManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public HttpResponse<String> putRequest(String url, String body) {

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(SERVER_ENDPOINT+url)).
                    PUT(BodyPublishers.ofString(body))
                    .header("Content-type", "application/json").
                    build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException ex) {
            Logger.getLogger(HttpManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HttpManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public HttpResponse<String> deleteRequest(String url) {

        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(SERVER_ENDPOINT+url))
                    .DELETE()
                    .header("Content-type", "application/json").
                    build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException ex) {
            Logger.getLogger(HttpManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HttpManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
