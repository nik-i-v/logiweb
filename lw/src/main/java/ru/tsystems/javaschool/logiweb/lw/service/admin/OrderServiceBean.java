package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.*;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;


import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class OrderServiceBean implements OrderService {

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    @Override
    public List<Order> getAllOrders() {
        logger.info("Get all orders");
        List<Order> orders = entityManager.createQuery("SELECT o FROM Order o").getResultList();
        for (Order o : orders) {
            Integer orderNumber = o.getId();
            Query driverShift = entityManager.createQuery("SELECT DISTINCT ds FROM DriverShift ds WHERE ds.orderId = :number");
            driverShift.setParameter("number", orderNumber);
            o.setDriverShift(driverShift.getResultList());
            if (getOrderStatus(orderNumber).equals(OrderStatus.Status.shipped.toString()) || getOrderStatus(orderNumber).equals(OrderStatus.Status.made.toString())) {
                String furaId = o.getFuraId();
                o.setFura(entityManager.find(Fura.class, Integer.parseInt(furaId)));
            } else {
                o.setFura(null);
            }
            Query orderInfo = entityManager.createQuery("SELECT DISTINCT oi FROM OrderInfo oi WHERE oi.orderNumber = :number");
            orderInfo.setParameter("number", orderNumber);
            OrderStatus orderStatus = entityManager.find(OrderStatus.class, orderNumber);
            orderStatus.setOrderInfo(orderInfo.getResultList());
        }
        return orders;
    }

    @Override
    public void addOrder() {
        logger.info("Add new order");
        Order order = new Order();
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus(OrderStatus.Status.created);
        entityManager.persist(order);
        entityManager.persist(orderStatus);
        logger.info("Order " + order.getId() + " was created successful");
    }

    @Override
    public void addGoods(Integer orderNumber, String name, Double gpsLat, Double gpsLong, Double weight) {
        logger.info("Add goods to order " + orderNumber);
//        isOrderExists(orderNumber);
//        checkOrderStatus(getOrderStatus(orderNumber), OrderStatus.Status.created.toString());
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(OrderInfo.Status.no);
        orderInfo.setGpsLat(gpsLat);
        orderInfo.setGpsLong(gpsLong);
        orderInfo.setName(name);
        orderInfo.setWeight(weight);
        orderInfo.setOrderNumber(orderNumber);
        entityManager.persist(orderInfo);
        logger.info("Good was added successful");
    }

    @Override
    public void changeOrderStatus(Integer orderNumber, OrderStatus.Status status) {
        logger.info("Change status of the order " + orderNumber + " to " + status);
        Query query = entityManager.createQuery("UPDATE OrderStatus os SET os.status = :status " +
                "WHERE os.orderId = :orderId");
        query.setParameter("status", status);
        query.setParameter("orderId", orderNumber);
        query.executeUpdate();
        logger.info("Status was changed successful");
    }

    @Override
    public void addFuraAndDrivers(Integer orderNumber, List<Long> driverId, String furaNumber) throws IncorrectDataException {
        logger.info("Add fura and drivers to order " + orderNumber);
//        checkOrderStatus(getOrderStatus(orderNumber), OrderStatus.Status.confirmed.toString());
        isFuraSuitable(furaIntCapacity(getFuraCapacity(furaNumber)), weightGoodsInOrder(orderNumber));
        isDriverCountSuitable(getDriverCount(furaNumber), driverId.size());
        changeFuraStatus(furaNumber);
        changeDriverStatus(orderNumber, driverId, DriverStatus.shift);
        addFuraToOrder(orderNumber, furaNumber);
        changeOrderStatus(orderNumber, OrderStatus.Status.shipped);
        logger.info("Fura and drivers was added");
    }

    @Override
    public void closeOrder(Integer orderNumber) throws IncorrectDataException {
        logger.info("Close order number " + orderNumber);
//        isOrderExists(orderNumber);
//        checkOrderStatus(getOrderStatus(orderNumber), OrderStatus.Status.made.toString());
        List<Long> driversInOrder = getDriversInOrder(orderNumber);
        checkDriverStatus(driversInOrder, DriverStatus.atWeel);
        changeOrderStatus(orderNumber, OrderStatus.Status.closed);
        changeDriverStatus(null, driversInOrder, DriverStatus.notShift);
        changeFuraStatus(orderNumber, Fura.Status.no);
        deleteFuraFromOrder(orderNumber);
        logger.info("Order number " + orderNumber + " was closed");
    }

    private void changeFuraStatus(Integer orderNumber, Fura.Status status) {
        logger.info("Change status of the fura in order " + orderNumber + " to " + status);
        Query query = entityManager.createQuery("SELECT o.furaId FROM Order o WHERE o.id = :number");
        query.setParameter("number", orderNumber);
        Integer furaId = Integer.parseInt(query.getSingleResult().toString());
        Query changeStatus = entityManager.createQuery("UPDATE Fura f SET f.status = :status WHERE f.furasId = :id");
        changeStatus.setParameter("id", furaId);
        changeStatus.setParameter("status", status);
        changeStatus.executeUpdate();
        logger.info("Fura's status was changed");
    }

    @Override
    public List<Integer> getCreatedOrders() {
        logger.info("Get created orders");
        return getOrdersByStatus(OrderStatus.Status.created);
    }

    @Override
    public List<Integer> getConfirmedOrders() {
        logger.info("Get confirmed orders");
        return getOrdersByStatus(OrderStatus.Status.confirmed);
    }

    @Override
    public List<Integer> getMadeOrders() {
        logger.info("Get made orders");
        return getOrdersByStatus(OrderStatus.Status.made);
    }

    @Override
    public List<Integer> getCreatedOrdersWitsGoods() {
        logger.info("Get created orders with goods");
        Query query = entityManager.createQuery("SELECT  DISTINCT oi.orderNumber FROM OrderInfo oi " +
                "WHERE oi.orderStatus.status = :status");
        query.setParameter("status", OrderStatus.Status.created);
        return query.getResultList();
    }

    @Override
    public void checkIfGoodsAreNotEmpty(Integer orderNumber) throws IncorrectDataException {
        logger.info("Check if goods list exists");
        Query query = entityManager.createQuery("SELECT COUNT (oi.name) FROM OrderInfo oi WHERE oi.orderNumber = :number");
        query.setParameter("number", orderNumber);
        if (query.getSingleResult().equals(null) || Integer.parseInt(query.getSingleResult().toString()) == 0) {
            throw new IncorrectDataException("Please add goods to order");
        }
    }

//    private void isOrderExists(Integer orderNumber) {
//        List<Integer> ordersId = entityManager.createQuery("SELECT o.id FROM Order o").getResultList();
//        if (!ordersId.contains(orderNumber)) {
//            throw new IllegalArgumentException("Order is not exists");
//        }
//    }

    private String getOrderStatus(Integer orderNumber) {
        logger.info("Get status of order " + orderNumber);
        Query query = entityManager.createQuery("SELECT os.status FROM OrderStatus os WHERE os.orderId = :id");
        query.setParameter("id", orderNumber);
        String status = query.getSingleResult().toString();
        return status;
    }

//    private void checkOrderStatus(String currentStatus, String requiredStatus) {
//        if (!currentStatus.equals(requiredStatus)) {
//            throw new IllegalArgumentException("You can't change this order. Order status is not suitable");
//        }
//    }

    private Double weightGoodsInOrder(Integer orderId) {
        logger.info("Check weight goods in order");
        Query query = entityManager.createQuery("SELECT SUM(oi.weight) FROM OrderInfo oi WHERE oi.orderNumber = :number");
        query.setParameter("number", orderId);
        return Double.parseDouble(query.getSingleResult().toString());

    }

    private String getFuraCapacity(String furaNumber) {
        logger.info("Get capacity of fura " + furaNumber);
        Query query = entityManager.createQuery("SELECT f.capacity FROM  Fura f WHERE f.furaNumber = :number");
        query.setParameter("number", furaNumber);
        String capacity = query.getSingleResult().toString();
        return capacity;
    }

    private Integer furaIntCapacity(String capacity) {
        logger.info("Convert fura's capacity to int");
        if (capacity.equals(Fura.Capacity.small.toString())) {
            return 1000;
        } else if (capacity.equals(Fura.Capacity.middle.toString())) {
            return 5000;
        } else {
            return 10000;
        }
    }

    private void isFuraSuitable(Integer furaCapacity, Double goodsWeight) throws IncorrectDataException {
        logger.info("Check if fura is suitable");
        if (furaCapacity < goodsWeight) {
            throw new IncorrectDataException("Fura is too small for this order");
        }
    }

    private Integer getDriverCount(String furaNumber) {
        logger.info("Get count of drivers in fura " + furaNumber);
        Query query = entityManager.createQuery("SELECT f.driverCount FROM Fura f WHERE f.furaNumber = :number");
        query.setParameter("number", furaNumber);
        Integer furaDriverCount = Integer.parseInt(query.getSingleResult().toString());
        return furaDriverCount;
    }

    private void isDriverCountSuitable(Integer driverCount, Integer driversInListCount) throws IncorrectDataException {
        logger.info("Check that the number of drivers is enough");
        if (driverCount != driversInListCount) {
            throw new IncorrectDataException("Fura should have " + driverCount + " drivers.");
        }
    }

    private List<Long> getDriversInOrder(Integer orderNumber) {
        logger.info("Get drivers in order " + orderNumber);
        Query query = entityManager.createQuery("SELECT d.license FROM Drivers d WHERE  d.driverShift.orderId = :orderNumber");
        query.setParameter("orderNumber", orderNumber);
        return query.getResultList();
    }

    private void checkDriverStatus(List<Long> drivers, DriverStatus status) throws IncorrectDataException {
        logger.info("Check driver status");
        Query query = entityManager.createQuery("SELECT COUNT(ds.status)FROM DriverShift ds WHERE ds.status= :status AND ds.drivers.license IN :driver");
        query.setParameter("driver", drivers);
        query.setParameter("status", status);
        String driversCount = query.getSingleResult().toString();
        if (status.equals(DriverStatus.notShift)) {
            if (driversCount.equals(null) || Integer.parseInt(driversCount) != drivers.size()) {
                throw new IncorrectDataException("Some drivers are already in shift or are not exists");
            } else if (status.equals(DriverStatus.atWeel)) {
                if (driversCount.equals(null)) {
                    throw new IncorrectDataException("This order still has the driver behind the wheel");
                }
            }
        }
    }


    private void changeDriverStatus(Integer orderNumber, List<Long> license, DriverStatus status) {
        logger.info("Change drivers statur to '" + status + "'");
        for (Long l : license) {
            Integer driverId = getDriverIdByDriverLicense(l);
            DriverShift driverShift = entityManager.find(DriverShift.class, driverId);
            driverShift.setStatus(status);
            driverShift.setOrderId(orderNumber);
            entityManager.merge(driverShift);
        }
    }

    private void changeFuraStatus(String furaNumber) {
        logger.info("Change status of fura " + furaNumber);
        Query updateFuraStatus = entityManager.createQuery("UPDATE Fura f SET f.status = :status WHERE f.furaNumber = :furaId");
        updateFuraStatus.setParameter("status", Fura.Status.yes);
        updateFuraStatus.setParameter("furaId", furaNumber);
        updateFuraStatus.executeUpdate();
        logger.info("Fura's status was changed");
    }

    private void addFuraToOrder(Integer orderNumber, String furaNumber) {
        logger.info("Add fura " + furaNumber + " to order " + orderNumber);
        Query getFuraId = entityManager.createQuery("SELECT f.furasId FROM Fura f WHERE f.furaNumber = :fura");
        getFuraId.setParameter("fura", furaNumber);
        Query addDriversToOrder = entityManager.createQuery("UPDATE Order o SET  o.furaId = :fura WHERE o.id = :number");
        addDriversToOrder.setParameter("number", orderNumber);
        addDriversToOrder.setParameter("fura", getFuraId.getSingleResult().toString());
        addDriversToOrder.executeUpdate();
        logger.info("Fura " + furaNumber + "was added");
    }

    private void deleteFuraFromOrder(Integer orderNumber) {
        logger.info("Delete fura from order " + orderNumber);
        Query query = entityManager.createQuery("UPDATE Order o SET o.furaId = :nulls " +
                "WHERE o.id = :Ids");
        query.setParameter("nulls", null);
        query.setParameter("Ids", orderNumber);
        query.executeUpdate();
        logger.info("Fura was deleted from " + orderNumber);
    }

    private List<Integer> getOrdersByStatus(OrderStatus.Status status) {
        Query query = entityManager.createQuery("SELECT o.id FROM Order o WHERE o.orderStatus.status = :status");
        query.setParameter("status", status);
        return query.getResultList();
    }

    private Integer getDriverIdByDriverLicense(Long license) {
        logger.info("Get drivers id by driver license");
        Query query = entityManager.createQuery("SELECT d.driversId FROM Drivers d WHERE d.license = :license");
        query.setParameter("license", license);
        return Integer.parseInt(query.getSingleResult().toString());
    }

}
