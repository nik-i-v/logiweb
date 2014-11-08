package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.*;


import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.persistence.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class OrderServiceBean implements OrderService {

    private static Logger logger = Logger.getLogger(OrderServiceBean.class.getName());

    @Produces
    @PersistenceContext(unitName = "logiweb", type = PersistenceContextType.EXTENDED)
    private static EntityManager entityManager;

    @Override
    public List<Order> getAllOrders() {
        return entityManager.createQuery("SELECT o FROM Order o").getResultList();
    }

    /**
     * @return order number
     */
    @Override
    public Integer addOrder() {
        logger.info("Add new order");
        Order order = new Order();
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus(OrderStatus.Status.created);
        entityManager.persist(order);
        entityManager.persist(orderStatus);
        return order.getId();
    }

    @Override
    public void addGoods(Integer orderNumber, String name, Double gpsLat, Double gpsLong, Double weight) {
        logger.info("Add goods to order " + orderNumber);
        isOrderExists(orderNumber);
        checkOrderStatus(getOrderStatus(orderNumber), OrderStatus.Status.created.toString());
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(OrderInfo.Status.no);
        orderInfo.setGpsLat(gpsLat);
        orderInfo.setGpsLong(gpsLong);
        orderInfo.setName(name);
        orderInfo.setWeight(weight);
        orderInfo.setOrderNumber(orderNumber);
        entityManager.persist(orderInfo);
    }

    @Override
    public void changeOrderStatus(Integer orderNumber, OrderStatus.Status status) {
        Query query = entityManager.createQuery("UPDATE OrderStatus os SET os.status = :status " +
                "WHERE os.orderId = :orderId");
        query.setParameter("status", status);
        query.setParameter("orderId", orderNumber);
        query.executeUpdate();
    }

    @Override
    public void addFuraAndDrivers(Integer orderNumber, List<Integer> driverId, String furaNumber, EntityManager entityManager) throws SQLException, IllegalArgumentException {
        isOrderExists(orderNumber);
        isFuraExists(furaNumber);
        checkOrderStatus(getOrderStatus(orderNumber), OrderStatus.Status.confirmed.toString());
        isFuraOccupied(furaNumber);
        isFuraSuitable(furaIntCapacity(getFuraCapacity(furaNumber)), weightGoodsInOrder(orderNumber));
        isDriverCountSuitable(getDriverCount(furaNumber), driverId.size());
        checkDriverStatus(driverId, DriverShift.Status.notShift);
        changeDriverStatus(orderNumber, driverId, DriverShift.Status.shift);
        changeFuraStatus(furaNumber);
        addFuraToOrder(orderNumber, furaNumber);
        changeOrderStatus(orderNumber, OrderStatus.Status.shipped);
    }

    @Override
    public void closeOrder(Integer orderNumber) {
        logger.info("Close order number + " + orderNumber);
        isOrderExists(orderNumber);
        checkOrderStatus(getOrderStatus(orderNumber), OrderStatus.Status.made.toString());
        List<Integer> driversInOrder = getDriversInOrder(orderNumber);
        checkDriverStatus(driversInOrder, DriverShift.Status.atWeel);
        changeOrderStatus(orderNumber, OrderStatus.Status.closed);
        changeDriverStatus(null, driversInOrder, DriverShift.Status.notShift);
        deleteFuraFromOrder(orderNumber);

    }

    private void isFuraOccupied(String furaNumber) {
        Query query = entityManager.createQuery("SELECT f.status FROM Fura f WHERE f.furaNumber = :number");
        query.setParameter("number", furaNumber);
        if (query.getSingleResult().toString().equals(Fura.Status.yes)) {
            throw new IllegalArgumentException("This fura is occupied!");
        }
    }

    private void isOrderExists(Integer orderNumber) {
        List<Integer> ordersId = entityManager.createQuery("SELECT o.id FROM Order o").getResultList();
        if (!ordersId.contains(orderNumber)) {
            throw new IllegalArgumentException("Order is not exists");
        }
    }

    private void isFuraExists(String furaNumber) {
        List<Integer> furasId = entityManager.createQuery("SELECT f.furaNumber FROM Fura f").getResultList();
        if (furasId.contains(furaNumber)) {
            throw new IllegalArgumentException("Fura is not exists");
        }
    }

    private String getOrderStatus(Integer orderNumber) {
        Query query = entityManager.createQuery("SELECT os.status FROM OrderStatus os WHERE os.orderId = :id");
        query.setParameter("id", orderNumber);
        String status = query.getSingleResult().toString();
        return status;
    }

    private void checkOrderStatus(String currentStatus, String requiredStatus) {
        if (!currentStatus.equals(requiredStatus)) {
            throw new IllegalArgumentException("You can't change this order. Order status is not suitable");
        }
    }

    private Double weightGoodsInOrder(Integer orderId) {
        Query query = entityManager.createQuery("SELECT SUM(oi.weight) FROM OrderInfo oi WHERE oi.orderNumber = :number");
        query.setParameter("number", orderId);
        return Double.parseDouble(query.toString());

    }

    private String getFuraCapacity(String furaNumber) {
        Query query = entityManager.createQuery("SELECT f.capacity FROM  Fura f WHERE f.furaNumber = :number");
        query.setParameter("number", furaNumber);
        String capacity = query.getSingleResult().toString();
        return capacity;
    }

    private Integer furaIntCapacity(String capacity) {
        if (capacity.equals(Fura.Capacity.small.toString())) {
            return 1000;
        } else if (capacity.equals(Fura.Capacity.middle.toString())) {
            return 5000;
        } else {
            return 10000;
        }
    }

    private void isFuraSuitable(Integer furaCapacity, Double goodsWeight) {
        if (furaCapacity < goodsWeight) {
            throw new IllegalArgumentException("Fura is too small for this order");
        }
    }

    private Integer getDriverCount(String furaNumber) {
        Query query = entityManager.createQuery("SELECT f.driverCount FROM Fura f WHERE f.furaNumber = :number");
        query.setParameter("number", furaNumber);
        Integer furaDriverCount = Integer.parseInt(query.getSingleResult().toString());
        return furaDriverCount;
    }

    private void isDriverCountSuitable(Integer driverCount, Integer driversInListCount) {
        throw new IllegalArgumentException("Fura should have " + driverCount + " drivers.");
    }

    private List<Integer> getDriversInOrder(Integer orderNumber) {
        Query query = entityManager.createQuery("SELECT ds.driverId FROM DriverShift ds WHERE  ds.orderId = :orderNumber");
        query.setParameter("orderNumber", orderNumber);
        return query.getResultList();
    }

    private void checkDriverStatus(List<Integer> drivers, DriverShift.Status status) {
        Query query = entityManager.createQuery("SELECT COUNT(ds.status)FROM DriverShift ds WHERE ds.status= :status AND ds.driverId IN :driver");
        query.setParameter("driver", drivers);
        query.setParameter("status", status);
        String driversCount = query.getSingleResult().toString();
        if (status.equals(DriverShift.Status.notShift)) {
            if (driversCount.equals(null) || Integer.parseInt(driversCount) != drivers.size()) {
                throw new IllegalArgumentException("Some drivers are already in shift or are not exists");
            } else if (status.equals(DriverShift.Status.atWeel)) {
                if (driversCount.equals(null)) {
                    throw new IllegalArgumentException("This order still has the driver behind the wheel");
                }
            }

        }
    }


    private void changeDriverStatus(Integer orderNumber, List<Integer> driverId, DriverShift.Status status) {
        Query addOrderNumberToDriver = entityManager.createQuery("UPDATE DriverShift ds SET ds.orderId = :number, " +
                "ds.status = :status WHERE ds.driverId IN :drivers");
        addOrderNumberToDriver.setParameter("number", orderNumber);
        addOrderNumberToDriver.setParameter("drivers", driverId);
        addOrderNumberToDriver.setParameter("status", status);
        addOrderNumberToDriver.executeUpdate();
    }

    private void changeFuraStatus(String furaNumber) {
        Query updateFuraStatus = entityManager.createQuery("UPDATE Fura f SET f.status = :status WHERE f.furaNumber = :furaId");
        updateFuraStatus.setParameter("status", Fura.Status.yes);
        updateFuraStatus.setParameter("furaId", furaNumber);
        updateFuraStatus.executeUpdate();
    }

    private void addFuraToOrder(Integer orderNumber, String furaNumber) {
        Query getFuraId = entityManager.createQuery("SELECT f.furasId FROM Fura f WHERE f.furaNumber = :fura");
        getFuraId.setParameter("fura", furaNumber);
        Query addDriversToOrder = entityManager.createQuery("UPDATE Order o SET  o.furaId = :fura WHERE o.id = :number");
        addDriversToOrder.setParameter("number", orderNumber);
        addDriversToOrder.setParameter("fura", getFuraId.getSingleResult());
        addDriversToOrder.executeUpdate();
    }

    private void deleteFuraFromOrder(Integer orderNumber){
        Query query = entityManager.createQuery("UPDATE Order o SET o.furaId = :nulls " +
                "WHERE o.id = :Ids");
        query.setParameter("nulls", null);
        query.setParameter("Ids", orderNumber);
        query.executeUpdate();
    }

}
