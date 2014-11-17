package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.dao.DriverDAO;
import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;


@Stateless
public class DriverServiceBean implements DriverService{
    private Logger logger = Logger.getLogger(DriverServiceBean.class.getName());

    DriverDAO driverDAO = new DriverDAO();

    @Inject
    private EntityManager entityManager;

    @Override
    public List<DriverShift> getAllDrivers(){
        return entityManager.createQuery("SELECT ds FROM DriverShift ds").getResultList();
    }

    @Override
    public void addDriver(String surname, String name, String patronymic, Long licenseId) {
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

    @Override
    public List<Long> getAllFreeDrivers() {
        Query query = entityManager.createQuery("SELECT d.license FROM Drivers d WHERE d.driverShift.status = :status");
        query.setParameter("status", DriverShift.Status.notShift);
        return query.getResultList();
    }

    private void checkIfDriverIdIsUnique(Long licenseId) {
       List<Long> ids =  entityManager.createQuery("SELECT d.license FROM Drivers d").getResultList();
        if (ids.contains(licenseId)) {
            throw new IllegalArgumentException("Driver with this license is already exists.");
        }
    }
}
