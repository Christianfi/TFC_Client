/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.HttpManager;
import dtos.Collection;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author CHRISTIAN
 */
public class CollectionService {

    private final ObjectMapper mapper;
    private final HttpManager httpManager;

    public CollectionService() {
        this.mapper = new ObjectMapper();
        this.httpManager = new HttpManager();
    }

    public List<Collection> getCollections() {

        try {
            String body = httpManager.getRequest("/collections").body();

            if (body != null) {
                return mapper.readValue(body,
                        new TypeReference<List<Collection>>() {
                });
            }
        } catch (JsonProcessingException ex) {
            Logger.getLogger(CollectionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int insertComic(Collection c) {
        try {
            String body = mapper.writeValueAsString(c);

            HttpResponse<String> response = httpManager.postRequest("/collections/collection", body);

            return Integer.parseInt(response.body());

        } catch (JsonProcessingException ex) {
            Logger.getLogger(ComicService.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }

    }

    public int updateComic(Collection c) {
        try {
            String body = mapper.writeValueAsString(c);

            HttpResponse<String> response = httpManager.putRequest("/collections/collection/" + c.getId(), body);

            return response.statusCode();

        } catch (JsonProcessingException ex) {
            Logger.getLogger(ComicService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int deleteComic(Collection c) {

        HttpResponse<String> response = httpManager.deleteRequest("/collections/collection/" + c.getId());

        return response.statusCode();
    }

    public String uploadImage(int id, File f) throws IOException {
        return httpManager.uploadFile("/collections/collection/" + id + "/image", f);
    }

    public Image getImage(int id) {

        HttpResponse<byte[]> response = httpManager.getImage("/collections/collection/" + id + "/image");

        return new Image(new ByteArrayInputStream(response.body()));
    }
}
