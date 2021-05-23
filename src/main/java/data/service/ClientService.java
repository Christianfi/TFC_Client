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
import dtos.Client;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CHRISTIAN
 */
public class ClientService {

    private final ObjectMapper mapper;
    private final HttpManager httpManager;

    public ClientService() {
        this.mapper = new ObjectMapper();
        this.httpManager = new HttpManager();
    }

    public List<Client> getClients() {

        try {
            String body = httpManager.getRequest("/clients").body();

            if (body != null) {
                return mapper.readValue(body,
                        new TypeReference<List<Client>>() {
                });
            }
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ComicService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int insertClient(Client c) {
        try {
            String body = mapper.writeValueAsString(c);

            HttpResponse<String> response = httpManager.postRequest("/clients/client", body);

            return response.statusCode();

        } catch (JsonProcessingException ex) {
            Logger.getLogger(ComicService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    public int updateClient(Client c) {
        try {
            String body = mapper.writeValueAsString(c);

            HttpResponse<String> response = httpManager.putRequest("/clients/client/" + c.getId(), body);

            return response.statusCode();

        } catch (JsonProcessingException ex) {
            Logger.getLogger(ComicService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int deleteClient(Client c) {

        HttpResponse<String> response = httpManager.deleteRequest("/clients/client/" + c.getId());

        return response.statusCode();
    }
}
