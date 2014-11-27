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
import java.util.List;

import java.util.logging.Logger;
import static org.picketlink.idm.model.basic.BasicModel.getRole;
import static org.picketlink.idm.model.basic.BasicModel.grantRole;

/**
 * This class provides operations for company employees to manage the list of drivers.
 * DriverServiceBean based implementation of the DriverService interface.
 *
 *
 * @author Irina Nikulina
 *
 */
@Stateless
public class DriverServiceBean implements DriverService {

    @Inject
    private IdentityManager identityManager;

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    @Inject
    private PartitionManager partitionManager;

    /**
     * Returns all drivers from the database.
     * @return the list of drivers
     */
    @Override
    public List<DriverShift> getAllDrivers() {
        return entityManager.createQuery("SELECT ds FROM DriverShift ds").getResultList();
    }

    /**
     * Returns all license numbers of drivers.
     * @return the list of license numbers
     */
    @Override
    public List<Long> getAllDriverId() {
        return entityManager.createQuery("SELECT d.license FROM Drivers d").getResultList();
    }

    /**
     * Adds a driver to the database. Also adds the record with this driver to the user list.
     * @param surname surname of a driver
     * @param name name of a driver
     * @param patronymic patronymic of a driver
     * @param licenseId license number of a driver
     * @throws IncorrectDataException if there is a driver with such license number
     */
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
        Role role = getRole(this.identityManager, "driver");
        User driverUser = new User(driver.getLicense().toString());
        identityManager.add(driverUser);
        identityManager.updateCredential(driverUser, new Password("pass"));
        grantRole(relationshipManager, driverUser, role);
        logger.info("Driver " + licenseId + " was added successful");
    }

    /**
     * Returns all free drivers from the database.
     * @return the list of drivers
     */
    @Override
    public List<Long> getAllFreeDrivers() {
        Query query = entityManager.createQuery("SELECT d.license FROM Drivers d WHERE d.driverShift.status = :status");
        query.setParameter("status", DriverStatus.notShift);
        return query.getResultList();
    }

    /**
     * Verifies is a driver license number unique.
     * @param licenseId license number of a driver
     * @throws IncorrectDataException if there is a driver with such license number
     */
    public void checkIfDriverIdIsUnique(Long licenseId) throws IncorrectDataException {
        List<Long> ids = entityManager.createQuery("SELECT d.license FROM Drivers d").getResultList();
        if (ids.contains(licenseId)) {
            throw new IncorrectDataException("Driver with this license is already exists.");
        }
    }

    /**
     * Sets an EntityManager.
     * @param entityManager
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Sets a Logger.
     * @param logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Sets an IdentityManager.
     * @param identityManager
     */
    public void setIdentityManager(IdentityManager identityManager) {
        this.identityManager = identityManager;
    }

    /**
     * Sets a PartitionManager.
     * @param partitionManager
     */
    public void setPartitionManager(PartitionManager partitionManager) {
        this.partitionManager = partitionManager;
    }
}
