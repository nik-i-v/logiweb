package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderStatus;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.util.List;

@Local
public interface OrderService {
    List<Order> getAllOrders();

    void addOrder();

    void addGoods(Integer orderNumber, String name, Double gpsLat, Double gpsLong, Double weight);

    void changeOrderStatus(Integer orderNumber, OrderStatus.Status status);

    void addFuraAndDrivers(Integer orderNumber, List<Integer> driverId, String furaNumber) ;

    void closeOrder(Integer number);

    List<Integer> getCreatedOrders();

    List<Integer> getConfirmedOrders();

    List<Integer> getMadeOrders();

    List<Integer> getcreatedOrdersWitsGoods();
}
