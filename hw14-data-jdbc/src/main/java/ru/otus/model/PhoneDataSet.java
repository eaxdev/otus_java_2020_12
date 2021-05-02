package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone_data_set")
public class PhoneDataSet {

    @Id
    private Long id;

    @Column("number")
    private String number;

    @Column("client_id")
    private long clientId;

    @PersistenceConstructor
    public PhoneDataSet(Long id, String number, long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public PhoneDataSet() {
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public long getClientId() {
        return clientId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
