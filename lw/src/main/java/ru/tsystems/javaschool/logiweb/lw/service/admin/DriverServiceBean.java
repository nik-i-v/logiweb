package ru.tsystems.javaschool.logiweb.lw.service.admin;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverStatus;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import static org.picketlink.idm.model.basic.BasicModel.grantRole;


@Stateless
public class DriverServiceBean implements DriverService{

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    @Inject
    private PartitionManager partitionManager;

    @Override
    public List<DriverShift> getAllDrivers(){
        logger.info("Get all drivers");
        return entityManager.createQuery("SELECT ds FROM DriverShift ds").getResultList();
    }

    @Override
    public List<Long> getAllDriverId(){
        logger.info("Get all drivers id");
        return entityManager.createQuery("SELECT d.license FROM Drivers d").getResultList();
    }

    @Override
    public void addDriver(String surname, String name, String patronymic, Long licenseId) throws IncorrectDataException {
        logger.info("Add new driver with license: " + licenseId);
        checkIfDriverIdIsUnique(licenseId);
        Drivers driver = new Drivers();
        DriverShift driverShift = new DriverShift();
        Users user = new Users();
        driver.setSurname(surname);
        driver.setName(name);
        driver.setPatronymic(patronymic);
        driver.setLicense(licenseId);
        driverShift.setStatus(DriverStatus.notShift);
        user.setName(licenseId.toString());
        user.setPassword("pass");
        user.setStatus(Users.Status.Driver);
        entityManager.persist(driver);
        entityManager.persist(driverShift);
        entityManager.persist(user);
        IdentityManager identityManager = this.partitionManager.createIdentityManager();
        RelationshipManager relationshipManager = this.partitionManager.createRelationshipManager();
        Role driverRole = new Role("driver");
        identityManager.add(driverRole);
        User driverUser = new User(driver.getLicense().toString());
        identityManager.add(driverUser);
        identityManager.updateCredential(driverUser, new Password("pass"));
        grantRole(relationshipManager, driverUser, driverRole);
        logger.info("Driver " + licenseId + " was added successful");
    }

    @Override
    public List<Long> getAllFreeDrivers() {
        logger.info("Get all free drivers");
        Query query = entityManager.createQuery("SELECT d.license FROM Drivers d WHERE d.driverShift.status = :status");
        query.setParameter("status", DriverStatus.notShift);
        return query.getResultList();
    }

    private void checkIfDriverIdIsUnique(Long licenseId) throws IncorrectDataException {
       List<Long> ids =  entityManager.createQuery("SELECT d.license FROM Drivers d").getResultList();
        if (ids.contains(licenseId)) {
            throw new IncorrectDataException("Driver with this license is already exists.");
        }
    }
}
