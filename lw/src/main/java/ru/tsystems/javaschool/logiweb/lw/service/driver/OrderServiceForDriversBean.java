package ru.tsystems.javaschool.logiweb.lw.service.driver;

import ru.tsystems.javaschool.logiweb.lw.server.entities.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class OrderServiceForDriversBean implements OrderServiceForDrivers {

    @Inject
    private EntityManager entityManager;

    @Override
    public List<Order> getOrderForDrivers(Long driverId) {
        Integer orderNumber = getOrderNumberForDrivers(driverId);
        Query query = entityManager.createQuery("SELECT o FROM Order o WHERE o.id = :number");
        query.setParameter("number", orderNumber);
        return query.getResultList();
    }

    @Override
    public List<OrderInfo> getGoodsStatusForDrivers(Long driverId) {
        Integer orderNumber = getOrderNumberForDrivers(driverId);
        Query query = entityManager.createQuery("SELECT oi FROM OrderInfo oi " +
                "WHERE oi.orderNumber = :number ");
        query.setParameter("number", orderNumber);
        return query.getResultList();
    }

    @Override
    public List<String> getGoodsList(Long driverLicense){
        Integer orderNumber = getOrderNumberForDrivers(driverLicense);
        Query query = entityManager.createQuery("SELECT oi.name FROM OrderInfo oi " +
                "WHERE oi.orderNumber = :number AND oi.status = :status");
        query.setParameter("number", orderNumber);
        query.setParameter("status", OrderInfo.Status.no);
        return query.getResultList();
    }

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

    @Override
    public List<OrderInfo> currentGoodsStatusIsNo(Integer orderNumber) {
        Query query = entityManager.createQuery("SELECT oi.name FROM OrderInfo oi WHERE oi.orderNumber = :number AND oi.status = :status");
        query.setParameter("number", orderNumber);
        query.setParameter("status", OrderInfo.Status.no);
        return query.getResultList();
    }

    @Override
    public void changeDriverStatusForDrivers(Long driverId, DriverStatus status) {
        if (status.equals(DriverStatus.atWeel.toString())) {
            isAnybodyAtWheel(driverId);
            changeDriverStatus(DriverStatus.atWeel, driverId);
        } else {
            changeDriverStatus(DriverStatus.shift, driverId);

        }
    }

    @Override
    public DriverStatus getStatusMenuForDrivers(String currentStatus) {
        if (currentStatus.equals(DriverStatus.atWeel)) {
            return DriverStatus.shift;
        } else {
            return DriverStatus.atWeel;
        }
    }

    @Override
    public String getCurrentStatusForDriver(Long driverId) {
        Query query = entityManager.createQuery("SELECT d.driverShift.status FROM Drivers d WHERE d.license = :license");
        query.setParameter("license", driverId);
        return query.getSingleResult().toString();
    }

    @Override
    public List<Drivers> getCoDrivers(Long driverLicense) {
        Integer orderNumber = getOrderNumberForDrivers(driverLicense);
        Query query = entityManager.createQuery("SELECT d.license FROM Drivers d WHERE  d.driverShift.orderId = :number");
        query.setParameter("number", orderNumber);
        List<Drivers> drivers = query.getResultList();
        drivers.remove(driverLicense);
        return drivers;
    }

        private Integer getOrderNumberForDrivers(Long driverId) {
        Query query = entityManager.createQuery("SELECT ds.orderId FROM DriverShift ds WHERE ds.drivers.license = :license");
        query.setParameter("license", driverId);
            if (query.getResultList().toString().equals(null)){
                throw new IllegalArgumentException("You have no orders");
            }
        return Integer.parseInt(query.getSingleResult().toString());
    }

    private void isAnybodyAtWheel(Long driverId) {
        Integer orderNumber = getOrderNumberForDrivers(driverId);
        Query query = entityManager.createQuery("SELECT COUNT (ds.driverId) FROM  DriverShift ds " +
                "WHERE ds.status = :status AND ds.orderId = :number");
        query.setParameter("status", DriverStatus.atWeel);
        query.setParameter("number", orderNumber);
        if (Integer.parseInt(query.getSingleResult().toString()) != 0) {
            throw new IllegalArgumentException("Another driver or you is already at weel");
        }
    }

    private void changeDriverStatus(DriverStatus status, Long driverId) {
        Query newStatus = entityManager.createQuery("UPDATE Drivers d SET d.driverShift.status = :status WHERE d.license = :id");
        newStatus.setParameter("status", status);
        newStatus.setParameter("id", driverId);
        newStatus.executeUpdate();
    }
}