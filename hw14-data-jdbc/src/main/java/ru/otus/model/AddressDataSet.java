package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("address_data_set")
public class AddressDataSet {

    @Id
    private Long id;

    @Column("street")
    private String street;

    @Column("client_id")
    private long clientId;

    @PersistenceConstructor
    public AddressDataSet(Long id, String street, long clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }

    public AddressDataSet() {
    }

    public Long getId() {
        return id;
    }


    public String getStreet() {
        return street;
    }

    public long getClientId() {
        return clientId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
