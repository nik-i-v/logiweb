package ru.tsystems.javaschool.logiweb.lw.server.dao;


import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderStatus;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class OrderDAO {

    @PersistenceContext(unitName = "logiweb")
    private EntityManager entityManager;

    public void add(OrderStatus orderStatus, Order order){
        entityManager.merge(orderStatus);
        entityManager.merge(order);
    }
}
