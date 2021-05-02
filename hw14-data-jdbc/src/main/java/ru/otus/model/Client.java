package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("client")
public class Client {

    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("login")
    private String login;

    @Column("password")
    private String password;

    @MappedCollection(idColumn = "client_id")
    private AddressDataSet address;

    @MappedCollection(idColumn = "client_id")
    private Set<PhoneDataSet> phoneDataSet;

    public Client() {
    }

    @PersistenceConstructor
    public Client(Long id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressDataSet getAddressDataSet() {
        return address;
    }

    public void setAddressDataSet(AddressDataSet addressDataSet) {
        this.address = addressDataSet;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public Set<PhoneDataSet> getPhoneDataSet() {
        return phoneDataSet;
    }

    public void setAddress(AddressDataSet address) {
        this.address = address;
    }

    public void setPhoneDataSet(Set<PhoneDataSet> phoneDataSet) {
        this.phoneDataSet = phoneDataSet;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phoneDataSet=" + phoneDataSet +
                '}';
    }
}
