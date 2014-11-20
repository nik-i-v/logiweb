package ru.tsystems.javaschool.logiweb.lw.ui;


import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;
import ru.tsystems.javaschool.logiweb.lw.service.admin.UserService;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

//@Model
@Named
@ManagedBean
@SessionScoped
public class CheckUser implements Serializable {
    private static Logger logger = Logger.getLogger(CheckUser.class.getName());

    private Users user;
private IdentityManager identityManager;

    @Produces
    @Named
    public Users getUser() {
        return user;
    }

    @EJB
    private UserService userService;

    @Inject
    private PartitionManager partitionManager;

    public String checkUser() {
        List<Users> users = userService.getUsers();
identityManager.
        if (isAdmin(users, user)){
            return "admin";
        } else  if (isDriver(users, user)){
            return "driver";
        } else {
            return "fail";
        }
    }

    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//        user = new Users();
        return "logout";
    }

    @PostConstruct
    public void initNewUser() {
        user = new Users();
        identityManager = this.partitionManager.createIdentityManager();
    }

    private boolean isAdmin(List<Users> users, Users user){
       return checkUser(users, user, Users.Status.Administrator);
    }

    private boolean isDriver(List<Users> users, Users user){
        return checkUser(users, user, Users.Status.Driver);
    }

    private boolean checkUser(List<Users> users, Users user, Users.Status status){
        for (Users u: users){
            if (u.getName().equals(user.getName()) && u.getPassword().equals(user.getPassword()) &&
                    u.getStatus().equals(status)){
                return true;
            }
        }
        return false;
    }


}

