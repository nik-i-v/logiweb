package ru.tsystems.javaschool.logiweb.lw.service.driver;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface OrderServiceForDrivers {
    List<Order> getOrderInfoForDrivers(String login);

    Boolean hasOrder(String login);

    void changeGoodsStatusForDrivers(Integer orderNumber, String name);
}
