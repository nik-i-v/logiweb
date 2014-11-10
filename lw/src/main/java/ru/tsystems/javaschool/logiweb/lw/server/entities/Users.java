package ru.tsystems.javaschool.logiweb.lw.server.entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;


import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "users")
public class Users {
    @Id
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Users() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status{
        Administrator, Driver
    }


}
