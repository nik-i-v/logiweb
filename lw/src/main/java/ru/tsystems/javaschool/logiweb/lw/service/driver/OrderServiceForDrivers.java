package ru.tsystems.javaschool.logiweb.lw.service.driver;

import ru.tsystems.javaschool.logiweb.lw.server.entities.*;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.List;

@Local
public interface OrderServiceForDrivers {
    List<Order> getOrderForDrivers(Long driverId);

    List<OrderInfo> getGoodsStatusForDrivers(Long driverId);

    List<String> getGoodsList(Long driverLicense);

    void changeGoodsStatusForDrivers(String name, Long driverId);

    List<OrderInfo> currentGoodsStatusIsNo(Integer orderNumber);

    void changeDriverStatusForDrivers(Long driverId, DriverStatus status);

    DriverStatus getStatusMenuForDrivers(String currentStatus);

    String getCurrentStatusForDriver(Long driverId);

    List<Drivers> getCoDrivers(Long driverLicense);

    void isAnybodyAtWheel(Long driverId);

//    Integer getOrderNumberForDrivers(Long driverLicense);
}
