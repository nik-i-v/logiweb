package ru.tsystems.javaschool.logiweb.lw.ui;


import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;
import ru.tsystems.javaschool.logiweb.lw.service.admin.UserService;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedProperty;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        if (users.contains(user)) {
            return "success";
        } else {
            return "fail";
        }
    }

    @PostConstruct
    public void initNewUser() {
        user = new Users();
    }

}

