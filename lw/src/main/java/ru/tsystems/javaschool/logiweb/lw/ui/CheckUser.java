package ru.tsystems.javaschool.logiweb.lw.ui;


import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;
import ru.tsystems.javaschool.logiweb.lw.service.admin.UserService;

import javax.enterprise.context.ApplicationScoped;
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
import javax.ws.rs.Path;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import static org.picketlink.idm.model.basic.BasicModel.*;

//@Model
@Named
@ManagedBean
@ApplicationScoped
public class CheckUser implements Serializable {
//    private static Logger logger = Logger.getLogger(CheckUser.class.getName());

    private Users user;
    private List<Users> users;

    @Inject
    private Identity identity;

    @Inject
    private transient Logger logger;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private RelationshipManager relationshipManager;

    @Produces
    @Named
    public Users getUser() {
        return user;
    }

    @EJB
    private UserService userService;

    @Inject
    private PartitionManager partitionManager;

    @Named
    @Produces
    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public String checkUser() {
        if (isAdmin(users, user)) {
            return "admin";
        } else if (isDriver(users, user)) {
            return "driver";
        } else {
            return "fail";
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//        user = new Users();
        return "logout";
    }

    @PostConstruct
    public void initNewUser() {
        user = new Users();

    }

    private boolean isAdmin(List<Users> users, Users user) {
        return checkUser(users, user, Users.Status.Administrator);
    }

    private boolean isDriver(List<Users> users, Users user) {
        return checkUser(users, user, Users.Status.Driver);
    }

    private boolean checkUser(List<Users> users, Users user, Users.Status status) {
        for (Users u : users) {
            if (u.getName().equals(user.getName()) && u.getPassword().equals(user.getPassword()) &&
                    u.getStatus().equals(status)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasApplicationRole(String roleName) {
        Role role = getRole(this.identityManager, roleName);
        return hasRole(this.relationshipManager, this.identity.getAccount(), role);
    }

}

