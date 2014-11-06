package ru.tsystems.javaschool.logiweb.lw.service;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;

import javax.ejb.Local;
import java.util.List;

@Local
public interface UserService {
    List<Users> getUsers();

    void addUser(String name, String password, Users.Status status);
}
