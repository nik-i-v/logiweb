package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.List;

@Local
public interface UserService {
    List<Users> getUsers();

}
