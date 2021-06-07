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
public class SuscriptionDTO {

    private int clientId;

    private int collectionId;

    public SuscriptionDTO() {
    }

    public SuscriptionDTO(int clientId, int collectionId) {
        this.clientId = clientId;
        this.collectionId = collectionId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

}
