package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;

import javax.ejb.Stateless;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;


@Stateless
public class DriverServiceBean implements DriverService{
    private Logger logger = Logger.getLogger(DriverServiceBean.class.getName());

    @Inject
    private EntityManager entityManager;

    @Override
    public List<DriverShift> getAllDrivers(){
        return entityManager.createQuery("SELECT ds FROM DriverShift ds").getResultList();

    }

    @Override
    public void addDriver(String surname, String name, String patronymic, Long licenseId) throws SQLException {
        logger.info("Add new driver with license: " + licenseId);
        checkIfDriverIdIsUnique(licenseId);
        Drivers driver = new Drivers();
        DriverShift driverShift = new DriverShift();
        Users user = new Users();
        driver.setSurname(surname);
        driver.setName(name);
        driver.setPatronymic(patronymic);
        driver.setLicense(licenseId);
        driverShift.setStatus(DriverShift.Status.notShift);
        user.setName(licenseId.toString());
        user.setPassword("pass");
        user.setStatus(Users.Status.Driver);
        entityManager.persist(driver);
        entityManager.persist(driverShift);
        entityManager.persist(user);
    }

    private void checkIfDriverIdIsUnique(Long licenseId) throws SQLException {
       List<Long> ids =  entityManager.createQuery("SELECT d.license FROM Drivers d").getResultList();
        if (ids.contains(licenseId)) {
            throw new IllegalArgumentException("Driver with this license is already exists.");
        }
    }
}
