package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Fura;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class FuraSeviceBean implements FuraService {

    private static Logger logger = Logger.getLogger(FuraSeviceBean.class.getName());

    @Inject
    private EntityManager entityManager;

    @Override
    public List<Fura> getAllFura(){
        return entityManager.createQuery("SELECT f FROM Fura f", Fura.class).getResultList();
    }

    @Override
    public void addFura(String number, Byte driverCount, Fura.Capacity capacity){
        logger.info("Create new fura.");
        List<Integer> ids = entityManager.createQuery("SELECT f.furaNumber FROM Fura f").getResultList();;
        if (ids.contains(number)) {
            throw new IllegalArgumentException("Fura is already exists.");
        }
        Fura fura = new Fura();
        fura.setStatus(Fura.Status.no);
        fura.setFuraNumber(number);
        fura.setDriverCount(driverCount);
        fura.setCapacity(capacity);
        entityManager.persist(fura);
    }

    @Override
    public List<String> getFreeFuras() {
        Query query = entityManager.createQuery("SELECT f.furaNumber FROM Fura f WHERE f.status = :status");
        query.setParameter("status", Fura.Status.no);
        return query.getResultList();
    }


}
