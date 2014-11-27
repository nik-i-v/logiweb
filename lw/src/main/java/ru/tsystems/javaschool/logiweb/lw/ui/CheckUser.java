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
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import static org.picketlink.idm.model.basic.BasicModel.*;

/**
 * This class performs login, logout, authentication and user role checking operations.
 * The class is serializable.
 *
 * @author Irina Nikulina
 */
@Named
@ManagedBean
@ApplicationScoped
public class CheckUser implements Serializable {

    private Users user;
    private List<Users> users;


    @EJB
    private UserService userService;

    @Inject
    private Identity identity;

    @Inject
    private transient Logger logger;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private RelationshipManager relationshipManager;

    @Inject
    private PartitionManager partitionManager;

    /**
     * Initializes a new user.
     */
    @PostConstruct
    public void initNewUser() {
        user = new Users();

    }

    /**
     * Defines the role of a user.
     * @return the name of the role
     */
    public String checkUser() {
        if (isAdmin(users, user)) {
            return "admin";
        } else if (isDriver(users, user)) {
            return "driver";
        } else {
            return "fail";
        }
    }

    /**
     * Performs logout operation.
     * @return logout parameter
     */
    public void logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("../login.jsf");
    }



    /**
     * Checks is a user an admin.
     * @param users the list of all users from database
     * @param user current user
     * @return true if an user is admin and false if it's not
     */
    private boolean isAdmin(List<Users> users, Users user) {
        return checkUser(users, user, Users.Status.Administrator);
    }

    /**
     * Checks is a user a driver.
     * @param users the list of all users from database
     * @param user current user
     * @return true if an user is admin and false if it's not
     */
    private boolean isDriver(List<Users> users, Users user) {
        return checkUser(users, user, Users.Status.Driver);
    }

    /**
     * Verify existence of an user in the database.
     * @param users the list of all users from database
     * @param user current user
     * @param status
     * @return true if an user exists and false if it's not
     */
    private boolean checkUser(List<Users> users, Users user, Users.Status status) {
        for (Users u : users) {
            if (u.getName().equals(user.getName()) && u.getPassword().equals(user.getPassword()) &&
                    u.getStatus().equals(status)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verify the status of an user.
     * @param roleName the name of the role
     * @return true if an user has role equals to certain role and false if it's not
     */
    public boolean hasApplicationRole(String roleName) {
        Role role = getRole(this.identityManager, roleName);
        return hasRole(this.relationshipManager, this.identity.getAccount(), role);
    }

    @Named
    @Produces
    public Users getUser() {
        return user;
    }

    @Named
    @Produces
    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
}

