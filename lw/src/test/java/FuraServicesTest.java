import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Fura;
import ru.tsystems.javaschool.logiweb.lw.service.admin.FuraSeviceBean;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class FuraServicesTest {

    private EntityManager entityManager;
    private Query query;
    private Logger logger;

    @Before
    public void init() {
        entityManager = mock(EntityManager.class);
        query = mock(Query.class);
        logger = mock(Logger.class);
    }

    @Test
    public void getAllFuraTest() {
        String hql = "SELECT f FROM Fura f";
        List<Fura> all = new LinkedList();
        all.add(new Fura());
        FuraSeviceBean service = new FuraSeviceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.getResultList()).thenReturn(all);
        List<Fura> result = service.getAllFura();
        assertNotNull(result);
        assertEquals(all, result);
    }

    @Test
    public void getAllFreeFuraTest() {
        String hql = "SELECT f.furaNumber FROM Fura f WHERE f.status = :status";
        List<Fura> all = new LinkedList();
        all.add(new Fura());
        FuraSeviceBean service = new FuraSeviceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.setParameter("status", Fura.Status.no)).thenReturn(query);
        when(query.getResultList()).thenReturn(all);
        List<String> result = service.getFreeFuras();
        assertNotNull(result);
        assertEquals(all, result);
    }

    @Test
    public  void addFuraTest_success() throws IncorrectDataException {
        String hql = "SELECT f.furaNumber FROM Fura f";
        List<String> ids = new LinkedList<>();
        ids.add("XC12345");
        FuraSeviceBean service = new FuraSeviceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.getResultList()).thenReturn(ids);
        service.addFura("GH12345", new Byte("2"), Fura.Capacity.middle);
    }

    @Test(expected = IncorrectDataException.class)
    public void addFuraTest_duplicateFura() throws IncorrectDataException {
        String hql = "SELECT f.furaNumber FROM Fura f";
        List<String> ids = new LinkedList<>();
        ids.add("XC12345");
        FuraSeviceBean service = new FuraSeviceBean();
        service.setEntityManager(entityManager);
        service.setLogger(logger);
        when(entityManager.createQuery(hql)).thenReturn(query);
        when(query.getResultList()).thenReturn(ids);
        service.addFura("XC12345", new Byte("2"), Fura.Capacity.middle);
        Fura fura = mock(Fura.class);
        verify(entityManager).persist(fura);
    }
}
