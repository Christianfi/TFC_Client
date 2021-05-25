/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

/**
 *
 * @author CHRISTIAN
 */
public class Collection {
    
    private String name;
    
    private int id;
    
    private String imageURL;

    public Collection() {
    }

    public Collection(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Collection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    
    
}
