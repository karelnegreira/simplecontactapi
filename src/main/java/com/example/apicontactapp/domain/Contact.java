package com.example.apicontactapp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="CONTACT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Contact {
    private String id;
    private String name;
    private String email;
    private String title;
    private String phone;
    private String address;
    private String status;
    private String photoUrl;

    /*
    public Contact() {
        //Demanded by JPA
    }

    public Contact(String name, String email, String title, String phone, String address, String status, String photoUrl) {
        this.name = name;
        this.email = email;
        this.title = title;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.photoUrl = photoUrl;
    }
    */
}
