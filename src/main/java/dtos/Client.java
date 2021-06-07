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
public class Client {

    private int id;

    private String name;

    private String lastname;

    private String dni;

    private String email;

    private String tlf;

    public Client(int id, String name, String lastname, String dni, String email, String tlf) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.email = email;
        this.tlf = tlf;
    }

    public Client(String name, String lastname, String dni, String email, String tlf) {
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.email = email;
        this.tlf = tlf;
    }

    public Client() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    @Override
    public String toString() {
        return this.dni + " - " + this.name + " " + this.lastname;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj == this) {
                return true;
            }

            if (((Client) obj).getId() == this.getId()) {
                return true;
            }
        }
        return false;
    }

}
