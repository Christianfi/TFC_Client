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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
}
