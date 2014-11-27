import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.model.basic.Role;
import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverStatus;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.service.admin.DriverServiceBean;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class DriverServicesTest {

    private EntityManager entityManager;
    private Query query;
    private Logger logger;
    private IdentityManager identityManager;
    private PartitionManager partitionManager;

    @Before
    public void init() {
        entityManager = mock(EntityManager.class);
        query = mock(Query.class);
        logger = mock(Logger.class);
    }

    @Test
    public void getAllDriversTest() {
        String hql = "SELECT ds FROM DriverShift ds";
        List<Drivers> all = new LinkedList();
        all.add(new Drivers("Petrov", "Petr", "Sergeevich", 38475647364L));
        DriverServiceBean service = new DriverServiceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.getResultList()).thenReturn(all);
        List<DriverShift> result = service.getAllDrivers();
        assertNotNull(result);
        assertEquals(all, result);
    }

    @Test
    public void getAllDriversIdTest() {
        String hql = "SELECT d.license FROM Drivers d";
        List<Drivers> all = new LinkedList();
        all.add(new Drivers("Petrov", "Petr", "Sergeevich", 38475647364L));
        DriverServiceBean service = new DriverServiceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.getResultList()).thenReturn(all);
        List<Long> result = service.getAllDriverId();
        assertNotNull(result);
        assertEquals(all, result);
    }

    @Test
    public void getAllFreeDriversTest() {
        String hql = "SELECT d.license FROM Drivers d WHERE d.driverShift.status = :status";
        List<Drivers> all = new LinkedList();
        all.add(new Drivers("Petrov", "Petr", "Sergeevich", 38475647364L));
        DriverServiceBean service = new DriverServiceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("status", DriverStatus.notShift)).thenReturn(query);
        when(query.getResultList()).thenReturn(all);
        List<Long> result = service.getAllFreeDrivers();
        assertNotNull(result);
        assertEquals(all, result);
    }

    @Test
    public void addDriverTest_success() throws IncorrectDataException, IncorrectDataException {
//        String hql = "SELECT d.license FROM Drivers d";
//        List<Long> ids = new LinkedList<>();
//        ids.add(26374873625L);
//        DriverServiceBean service = new DriverServiceBean();
//        service.setEntityManager(entityManager);
//        service.setLogger(logger);
//        when(entityManager.createQuery(hql)).thenReturn(query);
//        when(query.getResultList()).thenReturn(ids);
//        identityManager = mock(IdentityManager.class);
//        partitionManager = mock(PartitionManager.class);
//        service.setIdentityManager(identityManager);
//        service.setPartitionManager(partitionManager);
//        service.addDriver("surname", "name", "patronymic", 1L);
    }

    @Test(expected = IncorrectDataException.class)
    public void addDriverTest_duplicateDriver() throws IncorrectDataException {
        String hql = "SELECT d.license FROM Drivers d";
        List<Long> ids = new LinkedList<>();
        ids.add(26374873625L);
        DriverServiceBean service = new DriverServiceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.getResultList()).thenReturn(ids);
        service.addDriver("surname", "name", "patronymic", 26374873625L);
    }
}
