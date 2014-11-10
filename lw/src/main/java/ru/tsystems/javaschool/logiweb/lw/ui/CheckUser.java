package ru.tsystems.javaschool.logiweb.lw.ui;


import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;
import ru.tsystems.javaschool.logiweb.lw.service.admin.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Model
@Named
public class CheckUser implements Serializable {
    private static Logger logger = Logger.getLogger(CheckUser.class.getName());

    private Users user;

    @Produces
    @Named
    public Users getUser() {
        return user;
    }

    @EJB
    private UserService userService;

    public String checkUser() {
        List<Users> users = userService.getUsers();
        if (isAdmin(users, user)){
            return "success";
        } else {
            return "fail";
        }
    }

    public void logout(){
        user = null;
    }

    @PostConstruct
    public void initNewUser() {
        user = new Users();
    }

    private boolean isAdmin(List<Users> users, Users user){
        for (Users u: users){
            if (u.getName().equals(user.getName()) && u.getPassword().equals(user.getPassword()) &&
                    u.getStatus().equals(Users.Status.Administrator)){
                return true;
            }
        }
        return false;
    }



}

