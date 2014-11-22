import java.util.logging.Logger;import org.junit.Before;
import org.junit.Test;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.model.basic.Role;
import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverStatus;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.service.admin.DriverServiceBean;

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
//        driver = mock(Drivers.class);
//        driverShift = mock(DriverShift.class);
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

//    @Test
//    public void addDriverTest() throws IncorrectDataException {
//        String hql = "SELECT d.license FROM Drivers d";
//        List<Integer> ids = new LinkedList<>();
//        ids.add(23);
//        DriverServiceBean service = new DriverServiceBean();
//        service.setEntityManager(entityManager);
//        service.setLogger(logger);
//        when(entityManager.createQuery(hql)).thenReturn(query);
//        when(query.getResultList()).thenReturn(ids);
//        Boolean result = service.checkIfDriverIdIsUnique(867L);
//        identityManager = mock(IdentityManager.class);
//        partitionManager = mock(PartitionManager.class);
//        role = mock(Role.class);
//        service.setIdentityManager(identityManager);
//        service.setPartitionManager(partitionManager);
////        when(getRole)
////        when(service.checkIfDriverIdIsUnique(56476567675L)).thenReturn(true);
////        assertTrue();
//        Drivers driver = mock(Drivers.class);
//        DriverShift driverShift = mock(DriverShift.class);
//        Users user = mock(Users.class);
//        service.addDriver("Surname", "Name", "Patronymic", 11000000000L);
//        verify(entityManager).persist(driver);
////        when(entityManager.persist(driver));
////        when(en)
//    }



}
