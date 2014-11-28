package ru.tsystems.javaschool.logiweb.lw;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverStatus;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderService;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderServiceBean;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(Arquillian.class)
public class OrderServiceTest {
    private EntityManager entityManager;
    private Query query;
    private Logger logger;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(OrderService.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

//    @Before
//    public void init() {
//        entityManager = mock(EntityManager.class);
//        query = mock(Query.class);
//        logger = mock(Logger.class);
//    }
//
//    @Test
//    public void getAllOrdersTest() {
//        String hql = "SELECT o FROM Order o";
//        List<Order> all = new LinkedList();
//        all.add(new Order());
//        OrderServiceBean service = new OrderServiceBean();
//        service.setEntityManager(entityManager);
//        service.setLogger(logger);
//        when(entityManager.createQuery(hql)).thenReturn(query);
//        when(query.getResultList()).thenReturn(all);
//        String hql1 = "SELECT DISTINCT ds FROM DriverShift ds WHERE ds.orderId = :number";
//        when(entityManager.createQuery(hql1)).thenReturn(query);
//        when(query.setParameter("status", DriverStatus.notShift)).thenReturn(query);
//        when(query.getResultList()).thenReturn(new LinkedList());
//        String hql2 = "SELECT DISTINCT oi FROM OrderInfo oi WHERE oi.orderNumber = :number";
//        when(entityManager.createQuery(hql2)).thenReturn(query);
//        when(query.setParameter("status", DriverStatus.notShift)).thenReturn(query);
//        List<Order> result = service.getAllOrders();
//        assertNotNull(result);
//    }
}
