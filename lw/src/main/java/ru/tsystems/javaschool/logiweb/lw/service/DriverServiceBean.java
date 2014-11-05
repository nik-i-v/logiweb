package ru.tsystems.javaschool.logiweb.lw.service;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class DriverServiceBean implements DriverService{
    private Logger logger = Logger.getLogger(DriverServiceBean.class.getName());

    @PersistenceContext(unitName = "logiweb", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Override
    public List<Drivers> getAllDrivers(){
        return entityManager.createQuery("SELECT ds FROM DriverShift ds").getResultList();

    }

    @Override
    public void addDriver(String surname, String name, String patronymic, Long licenseId) throws SQLException {
        logger.info("Add new driver with license: " + licenseId);
        List<Long> ids = checkIfDriverIdIsUnique();
        if (ids.contains(licenseId)) {
            throw new IllegalArgumentException("Driver with this license is already exists.");
        }
        Drivers driver = new Drivers();
        DriverShift driverShift = new DriverShift();
        driver.setSurname(surname);
        driver.setName(name);
        driver.setPatronymic(patronymic);
        driver.setLicense(licenseId);
        driverShift.setStatus(DriverShift.Status.notShift);
        entityManager.persist(driver);
        entityManager.persist(driverShift);
    }
    private List<Long> checkIfDriverIdIsUnique() throws SQLException {
        return entityManager.createQuery("SELECT d.license FROM Drivers d").getResultList();
    }
}
