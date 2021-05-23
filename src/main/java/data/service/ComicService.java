/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.Comic;
import java.util.List;
import data.HttpManager;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CHRISTIAN
 */
public class ComicService {

    private final ObjectMapper mapper;
    private final HttpManager httpManager;

    public ComicService() {
        this.mapper = new ObjectMapper();
        this.httpManager = new HttpManager();
    }

    public List<Comic> getComics() {

        try {
            String body = httpManager.getRequest("/comics").body();

            if (body != null) {
                return mapper.readValue(body,
                        new TypeReference<List<Comic>>() {
                });
            }
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ComicService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int insertComic(Comic c){
        try {
            String body = mapper.writeValueAsString(c);
            
            HttpResponse<String> response = httpManager.postRequest("/comics/comic", body);
            
            return response.statusCode();
            
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ComicService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
    }
    
    public int updateComic(Comic c){
          try {
            String body = mapper.writeValueAsString(c);
            
            HttpResponse<String> response = httpManager.putRequest("/comics/comic/"+c.getId(), body);
            
            return response.statusCode();
            
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ComicService.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
    public int deleteComic(Comic c){
         
         HttpResponse<String> response = httpManager.deleteRequest("/comics/comic/"+c.getId());
         
         return response.statusCode();
    }
}
