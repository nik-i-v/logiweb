package ru.tsystems.javaschool.logiweb.lw.service;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;

import javax.ejb.Local;
import java.util.List;

@Local
public interface OrderService {
    List<Order> getAllOrders();
}
