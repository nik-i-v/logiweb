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
}
