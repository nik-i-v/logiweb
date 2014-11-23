package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Logger;
@Stateless
public class UserServiceBean implements UserService {

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    /**
     * Returns the list of all users.
     * @return the list of all users
     */
    @Override
    public List<Users> getUsers() {
        logger.info("Get users");
        return entityManager.createQuery("SELECT u FROM Users u").getResultList();
    }

    /**
     * Adds an user to the database.
     * @param name the name of an user
     * @param password the password of an user
     * @param status the status of an user
     */
    @Override
    public void addUser(String name, String password, Users.Status status) {
        logger.info("Create new user " + name);
        Users user = new Users();
        user.setStatus(status);
        user.setName(name);
        user.setPassword(password);
        entityManager.persist(user);
        logger.info("User was added");
    }






}
