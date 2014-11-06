package ru.tsystems.javaschool.logiweb.lw.service;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@Stateless
public class UserServiceBean implements UserService {

    @PersistenceContext(unitName = "logiweb", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Override
    public List<Users> getUsers(){
        return entityManager.createQuery("SELECT u FROM Users u").getResultList();
    }

    @Override
    public void addUser(String name, String password, Users.Status status){
        Users user = new Users();
        user.setStatus(status);
        user.setName(name);
        user.setPassword(password);
        entityManager.persist(user);
    }
}
