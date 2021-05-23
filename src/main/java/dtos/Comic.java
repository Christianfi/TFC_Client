/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.io.Serializable;
import java.text.SimpleDateFormat;


/**
 *
 * @author CHRISTIAN
 */
public class Comic implements Serializable{
    
    private int id;
    
    private String name;
    
    private String publishDate;
    
    private String state;
    
    private int number;
    
    private String publisher;
    
    private String isbn;
    
    private String collectionName;
    
    private int collectionId;
    
    private String imageURL;
    
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public Comic(int id, String name, String publishDate, String state, int number, String publisher,String isbn, String collectionName, int collectionId) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.state = state;
        this.number = number;
        this.publisher = publisher;
        this.isbn = isbn;
        this.collectionName = collectionName;
        this.collectionId = collectionId;
    }

    public Comic(String name, String publishDate, String state, int number, String publisher, String isbn, String collectionName, int collectionId) {
        this.name = name;
        this.publishDate = publishDate;
        this.state = state;
        this.number = number;
        this.publisher = publisher;
        this.isbn = isbn;
        this.collectionName = collectionName;
        this.collectionId = collectionId;
    }
    

    public Comic() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    
}
