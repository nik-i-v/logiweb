package ru.tsystems.javaschool.logiweb.lw.service.driver;

import org.picketlink.idm.IdentityManager;
import ru.tsystems.javaschool.logiweb.lw.server.entities.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * This class provides the services to work with orders for drivers.
 * OrderServiceForDriversBean based implementation of the OrderServiceForDrivers interface.
 *
 * @author
 */
@Stateless
public class OrderServiceForDriversBean implements OrderServiceForDrivers {

    @Inject
    private EntityManager entityManager;

    /**
     * Gets the order for the certain driver.
     * @param driverId license number of a driver
     * @return an order
     */
    @Override
    public Order getOrderForDrivers(Long driverId) {
        Integer orderNumber = getOrderNumberForDrivers(driverId);
        Order order = entityManager.find(Order.class, orderNumber);
        Query driverShift = entityManager.createQuery("SELECT DISTINCT ds FROM DriverShift ds WHERE ds.orderId = :number");
        driverShift.setParameter("number", orderNumber);
        order.setDriverShift(driverShift.getResultList());
        Query furaId = entityManager.createQuery("SELECT o.furaId FROM Order o WHERE o.id= :number");
        furaId.setParameter("number", orderNumber);
        order.setFura(entityManager.find(Fura.class, Integer.parseInt(furaId.getSingleResult().toString())));
        Query orderInfo = entityManager.createQuery("SELECT DISTINCT oi FROM OrderInfo oi WHERE oi.orderNumber = :number");
        orderInfo.setParameter("number", orderNumber);
        OrderStatus orderStatus = entityManager.find(OrderStatus.class, orderNumber);
        orderStatus.setOrderInfo(orderInfo.getResultList());
        return order;
    }

    /**
     * Shows order details and goods of the order for certain driver.
     * @param driverId the number of a driver
     * @return parameters and goods of an order
     */
    @Override
    public List<OrderInfo> getGoodsStatusForDrivers(Long driverId) {
        Integer orderNumber = getOrderNumberForDrivers(driverId);
        Query query = entityManager.createQuery("SELECT oi FROM OrderInfo oi " +
                "WHERE oi.orderNumber = :number ");
        query.setParameter("number", orderNumber);
        return query.getResultList();
    }

    /**
     * Gets the list of goods of an order for certain driver.
     * @param driverLicense the license number of a driver
     * @return the list of goods
     */
    @Override
    public List<String> getGoodsList(Long driverLicense) {
        Integer orderNumber = getOrderNumberForDrivers(driverLicense);
        Query query = entityManager.createQuery("SELECT oi.name FROM OrderInfo oi " +
                "WHERE oi.orderNumber = :number AND oi.status = :status");
        query.setParameter("number", orderNumber);
        query.setParameter("status", OrderInfo.Status.no);
        return query.getResultList();
    }

    /**
     * Changes statuses for goods of certain order. Order determined with license of a driver.
     * @param name the name of a goods
     * @param driverId license number of a driver
     */
    @Override
    public void changeGoodsStatusForDrivers(String name, Long driverId) {
        Integer orderNumber = getOrderNumberForDrivers(driverId);
        Query query = entityManager.createQuery("UPDATE OrderInfo oi SET oi.status = :status WHERE  oi.name = :name");
        query.setParameter("status", OrderInfo.Status.yes);
        query.setParameter("name", name);
        query.executeUpdate();
        if (currentGoodsStatusIsNo(orderNumber).size() == 0) {
            Query query1 = entityManager.createQuery("UPDATE OrderStatus os SET os.status = :status WHERE os.orderId = :number");
            query1.setParameter("status", OrderStatus.Status.made);
            query1.setParameter("number", orderNumber);
            query1.executeUpdate();
        }
    }

    /**
     * Returns the list of goods with "no" status for certain order.
     * @param orderNumber the number of an order
     * @return the list of goods
     */
    @Override
    public List<OrderInfo> currentGoodsStatusIsNo(Integer orderNumber) {
        Query query = entityManager.createQuery("SELECT oi.name FROM OrderInfo oi WHERE oi.orderNumber = :number AND oi.status = :status");
        query.setParameter("number", orderNumber);
        query.setParameter("status", OrderInfo.Status.no);
        return query.getResultList();
    }

    /**
     * Changes the driver status.
     * @param license the number of license for a driver
     * @param status a new status of the driver
     */
    @Override
    public void changeDriverStatusForDrivers(Long license, DriverStatus status) {
        Integer driverId = getDriverIdByDriverLicense(license);
        DriverShift driverShift = entityManager.find(DriverShift.class, driverId);
        driverShift.setStatus(status);
        entityManager.merge(driverShift);
    }

    /**
     * Return the value of the alternative status for driver's menu.
     * @param currentStatus current status
     * @return alternative status
     */
    @Override
    public DriverStatus getStatusMenuForDrivers(String currentStatus) {
        if (currentStatus.equals(DriverStatus.atWeel)) {
            return DriverStatus.shift;
        } else {
            return DriverStatus.atWeel;
        }
    }

    /**
     * Gets current status for a driver's menu.
     * @param driverId the number license of a driver
     * @return current status
     */
    @Override
    public String getCurrentStatusForDriver(Long driverId) {
        Query query = entityManager.createQuery("SELECT d.driverShift.status FROM Drivers d WHERE d.license = :license");
        query.setParameter("license", driverId);
        return query.getSingleResult().toString();
    }

    /**
     * Gets the number of the order for certain driver
     * @param driverId the number license of a driver
     * @return the number of the order
     */
    private Integer getOrderNumberForDrivers(Long driverId) {
        Query query = entityManager.createQuery("SELECT ds.orderId FROM DriverShift ds WHERE ds.drivers.license = :license");
        query.setParameter("license", driverId);
        if (query.getResultList().toString().equals(null)) {
            throw new IllegalArgumentException("You have no orders");
        }
        return Integer.parseInt(query.getSingleResult().toString());
    }

    /**
     * Checks is there any driver with the status "atWheel"
     * @param driverId license number of a driver
     */
    @Override
    public void isAnybodyAtWheel(Long driverId) {
        Integer orderNumber = getOrderNumberForDrivers(driverId);
        Query query = entityManager.createQuery("SELECT COUNT (ds.driverId) FROM  DriverShift ds " +
                "WHERE ds.status = :status AND ds.orderId = :number");
        query.setParameter("status", DriverStatus.atWeel);
        query.setParameter("number", orderNumber);
        if (Integer.parseInt(query.getSingleResult().toString()) != 0) {
            throw new IllegalArgumentException("Another driver or you is already at weel");
        }
    }

    /**
     * Gets driver id using license number of a driver
     * @param license license number of a driver
     * @return driver id
     */
    private Integer getDriverIdByDriverLicense(Long license) {
        Query query = entityManager.createQuery("SELECT d.driversId FROM Drivers d WHERE d.license = :license");
        query.setParameter("license", license);
        return Integer.parseInt(query.getSingleResult().toString());
    }

}