package ru.otus.hibernate.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Expose
    @Column(name = "name")
    private String name;

    @Expose
    @Column(name = "login")
    private String login;

    @Expose
    @Column(name = "password")
    private String password;

    @Expose
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private AddressDataSet address;

    @Expose
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PhoneDataSet> phoneDataSet;

    public Client() {
    }

    public Client(String name, String login, String password) {
        this.id = null;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public Client(Long id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public Client deepCopy() {
        var client = new Client(this.id, this.name, this.login, this.password);
        var copiedAddress = this.address.copy();
        copiedAddress.setClient(client);
        client.setAddressDataSet(copiedAddress);
        client.setPhoneDataSet(this.phoneDataSet.stream()
                .map(it -> {
                    var copiedPhone = it.copy();
                    copiedPhone.setClient(client);
                    return copiedPhone;
                }).collect(Collectors.toList())
        );
        return client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDataSet getAddressDataSet() {
        return address;
    }

    public void setAddressDataSet(AddressDataSet addressDataSet) {
        this.address = addressDataSet;
    }

    public List<PhoneDataSet> getPhoneDataSet() {
        return phoneDataSet;
    }

    public void setPhoneDataSet(List<PhoneDataSet> phoneDataSet) {
        this.phoneDataSet = phoneDataSet;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
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
