package ru.tsystems.javaschool.logiweb.lw.server.dao;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class DriverDAO {

    @Inject
    private EntityManager entityManager;

    public void add(Drivers driver, DriverShift driverShift, Users user){
        entityManager.persist(driver);
        entityManager.persist(driverShift);
        entityManager.persist(user);
    }
}
