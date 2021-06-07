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
import dtos.Collection;
import dtos.SuscriptionDTO;
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

    public int newSuscription(SuscriptionDTO suscription) {

        try {
            String body = mapper.writeValueAsString(suscription);

            HttpResponse<String> response = httpManager.postRequest("/clients/client/" + suscription.getClientId() + "/suscription", body);

            return response.statusCode();

        } catch (JsonProcessingException ex) {
            Logger.getLogger(ComicService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public int deleteSuscription(SuscriptionDTO suscription) {

        try {
            String body = mapper.writeValueAsString(suscription);

            HttpResponse<String> response = httpManager.postRequest("/clients/client/" + suscription.getClientId() + "/suscription/delete", body);

            return response.statusCode();

        } catch (JsonProcessingException ex) {
            Logger.getLogger(ComicService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public List<Collection> getSuscriptions(Client c) {
        try {
            String body = httpManager.getRequest("/clients/client/" + c.getId() + "/suscriptions").body();

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
