package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Fura;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class provides operations for company employees to manage the list of furas.
 *
 * @author Irina Nikulina
 */
@Stateless
public class FuraSeviceBean implements FuraService {

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    /**
     * Gets the list of all furas.
     * @return the list of all furas.
     */
    @Override
    public List<Fura> getAllFura(){
        logger.info("Get all furas");
        return entityManager.createQuery("SELECT f FROM Fura f").getResultList();
    }

    /**
     * Adds a fura to the database.
     * @param number the number of a fura
     * @param driverCount amount of required passengers
     * @param capacity the capacity of a fura
     * @throws IncorrectDataException if there is a fura with such fura number
     */
    @Override
    public void addFura(String number, Byte driverCount, Fura.Capacity capacity) throws IncorrectDataException {
        logger.info("Create new fura");
        List<String> ids = entityManager.createQuery("SELECT f.furaNumber FROM Fura f").getResultList();;
        if (ids.contains(number)) {
            throw new IncorrectDataException("Fura is already exists.");
        }
        Fura fura = new Fura();
        fura.setStatus(Fura.Status.no);
        fura.setFuraNumber(number);
        fura.setDriverCount(driverCount);
        fura.setCapacity(capacity);
        entityManager.persist(fura);
        logger.info("Fura " + number + " was added successful");
    }

    /**
     * Gets all furas with "no" status.
     * @return the list of furas.
     */
    @Override
    public List<String> getFreeFuras() {
        logger.info("Get free furas");
        Query query = entityManager.createQuery("SELECT f.furaNumber FROM Fura f WHERE f.status = :status");
        query.setParameter("status", Fura.Status.no);
        return query.getResultList();
    }

    /**
     * Sets an EntityManager.
     * @param entityManager
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Sets an Logger.
     * @param logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

}
