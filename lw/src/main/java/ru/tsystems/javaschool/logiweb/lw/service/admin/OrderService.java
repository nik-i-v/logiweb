package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderStatus;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;

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

    void addFuraAndDrivers(Integer orderNumber, List<Long> driverId, String furaNumber) throws IncorrectDataException;

    void closeOrder(Integer number) throws IncorrectDataException;

    List<Integer> getCreatedOrders();

    List<Integer> getConfirmedOrders();

    List<Integer> getMadeOrders();

    List<Integer> getCreatedOrdersWitsGoods();

    void checkIfGoodsAreNotEmpty(Integer orderNumber) throws IncorrectDataException;

}
