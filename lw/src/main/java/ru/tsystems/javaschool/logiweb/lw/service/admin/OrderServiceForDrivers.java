package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderInfo;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface OrderServiceForDrivers {
    List getOrderForDrivers(Long driverId);

    List<OrderInfo> getGoodsStatusForDrivers(Long driverId);

    void changeGoodsStatusForDrivers(String name, Long driverId);

    List<OrderInfo> currentGoodsStatusIsNo(Integer orderNumber);

    void changeDriverStatusForDrivers(Long driverId, DriverShift.Status status);

    DriverShift.Status getStatusMenuForDrivers(String currentStatus);

    String getCurrentStatusForDriver(Long driverId);
}
