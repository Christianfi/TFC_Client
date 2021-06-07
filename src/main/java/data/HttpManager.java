/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author CHRISTIAN
 */
public class HttpManager {

    private HttpClient httpClient;
    public final static String SERVER_ENDPOINT = "http://192.168.1.50:8080";

    public HttpManager() {
        httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> getRequest(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_ENDPOINT + url))
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
            HttpRequest request = HttpRequest.newBuilder(URI.create(SERVER_ENDPOINT + url)).
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
            HttpRequest request = HttpRequest.newBuilder(URI.create(SERVER_ENDPOINT + url)).
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
            HttpRequest request = HttpRequest.newBuilder(URI.create(SERVER_ENDPOINT + url))
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

    public String uploadFile(String url, File file) throws IOException {

        HttpPost post = new HttpPost(SERVER_ENDPOINT+url);
        post.setHeader("Accept", "application/json");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // fileParamName should be replaced with parameter name your REST API expect.
        builder.addPart("image", new FileBody(file));
        //builder.addPart("optionalParam", new StringBody("true", ContentType.create("text/plain", Consts.ASCII)));
        post.setEntity(builder.build());
        CloseableHttpResponse response = HttpClients.createDefault().execute(post);

        int httpStatus = response.getStatusLine().getStatusCode();
        String responseMsg = EntityUtils.toString(response.getEntity(), "UTF-8");
        
        // If the returned HTTP response code is not in 200 series then
        // throw the error
        if (httpStatus < 200 || httpStatus > 300) {
            throw new IOException("HTTP " + httpStatus + " - Error during upload of file: " + responseMsg);
        }

        return responseMsg;
    }
    
     public HttpResponse<byte[]> getImage(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_ENDPOINT + url))
                    .GET()
                    .build();
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            return response;
        } catch (IOException ex) {
            Logger.getLogger(HttpManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HttpManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
