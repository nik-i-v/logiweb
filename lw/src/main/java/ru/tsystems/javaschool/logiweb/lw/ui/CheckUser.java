package ru.tsystems.javaschool.logiweb.lw.ui;


import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;
import ru.tsystems.javaschool.logiweb.lw.service.UserService;
import ru.tsystems.javaschool.logiweb.lw.service.UserServiceBean;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Model
@WebServlet( urlPatterns = "/CheckUser")
public class CheckUser extends Dispatcher {
    private static Logger logger = Logger.getLogger(CheckUser.class.getName());

    @EJB
    private UserService userService;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, FileNotFoundException {
        res.setContentType("text/html");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        List<Users> users = userService.getUsers();
        for (Users u : users) {
            if (u.getName().equals(login) && u.getPassword().equals(password)) {
                if (u.getStatus().equals(Users.Status.Administrator)) {
                    logger.info("Administrator " + login + " logged in.");
                    req.getSession().setAttribute("isLogged", "true");
                    this.forward("/successLoginAdmin.jsp", req, res);
                } else {
                    logger.info("Driver " + login + " logged in.");
                    req.getSession().setAttribute("isLogged", "true");
                    this.forward("/successLoginDriver.jsp", req, res);
                }
            } else {
                this.forward("/errorLogin.jsp", req, res);
            }
        }
    }

}

