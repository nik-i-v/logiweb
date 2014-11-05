package ru.tsystems.javaschool.logiweb.lw.service;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class OrderServiceBean implements OrderService {

    private static Logger logger = Logger.getLogger(OrderServiceBean.class.getName());

    @PersistenceContext(unitName = "logiweb", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Override
    public List<Order> getAllOrders(){
        return entityManager.createQuery("SELECT o FROM Order o").getResultList();
    }
}
