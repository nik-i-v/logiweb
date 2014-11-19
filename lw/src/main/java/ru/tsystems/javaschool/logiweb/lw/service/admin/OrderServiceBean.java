package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.*;


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
    private static Logger logger = Logger.getLogger(OrderServiceBean.class.getName());

    @Inject
    private EntityManager entityManager;

    @Override
    public List<Order> getAllOrders() {
//     return entityManager.find(Order.class);
        List<Order> orders = entityManager.createQuery("SELECT o FROM Order o").getResultList();
        for (Order o : orders) {
            Integer orderNumber = o.getId();
            Query driverShift = entityManager.createQuery("SELECT DISTINCT ds FROM DriverShift ds WHERE ds.orderId = :number");
            driverShift.setParameter("number", orderNumber);
            o.setDriverShift(driverShift.getResultList());
//            Order order = entityManager.find(Order.class, orderNumber);
//            Query furaIdQuery = entityManager.createQuery("SELECT o.fura.furasId FROM Order o WHERE o.id= :number");
//            furaIdQuery.setParameter("number", orderNumber);
            if (getOrderStatus(orderNumber).equals(OrderStatus.Status.shipped.toString()) || getOrderStatus(orderNumber).equals(OrderStatus.Status.made.toString())) {
                Integer furaId = o.getFuraId();
                o.setFura(entityManager.find(Fura.class, furaId));
            }
            Query orderInfo = entityManager.createQuery("SELECT DISTINCT oi FROM OrderInfo oi WHERE oi.orderNumber = :number");
            orderInfo.setParameter("number", orderNumber);
            OrderStatus orderStatus = entityManager.find(OrderStatus.class, orderNumber);
            orderStatus.setOrderInfo(orderInfo.getResultList());
        }
        return orders;
    }

    /**
     * @return order number
     */
    @Override
    public void addOrder() {
        logger.info("Add new order");
        Order order = new Order();
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus(OrderStatus.Status.created);
//        entityManager.flush();
        entityManager.persist(order);
        entityManager.persist(orderStatus);

//        orderDAO.add(orderStatus, order);
//        return order.getId();
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
    public void addFuraAndDrivers(Integer orderNumber, List<Long> driverId, String furaNumber) {
//        isOrderExists(orderNumber);
//        isFuraExists(furaNumber);
        checkOrderStatus(getOrderStatus(orderNumber), OrderStatus.Status.confirmed.toString());
//        isFuraOccupied(furaNumber);
        isFuraSuitable(furaIntCapacity(getFuraCapacity(furaNumber)), weightGoodsInOrder(orderNumber));
        isDriverCountSuitable(getDriverCount(furaNumber), driverId.size());
//        checkDriverStatus(driverId, DriverShift.Status.notShift);
        changeFuraStatus(furaNumber);
        changeDriverStatus(orderNumber, driverId, DriverStatus.shift);
        addFuraToOrder(orderNumber, furaNumber);
        changeOrderStatus(orderNumber, OrderStatus.Status.shipped);
    }

    @Override
    public void closeOrder(Integer orderNumber) {
//        logger.info("Close order number + " + orderNumber);
        isOrderExists(orderNumber);
        checkOrderStatus(getOrderStatus(orderNumber), OrderStatus.Status.made.toString());
        List<Long> driversInOrder = getDriversInOrder(orderNumber);
        checkDriverStatus(driversInOrder, DriverStatus.atWeel);
        changeOrderStatus(orderNumber, OrderStatus.Status.closed);
        changeDriverStatus(null, driversInOrder, DriverStatus.notShift);
        changeFuraStatus(orderNumber, Fura.Status.no);
        deleteFuraFromOrder(orderNumber);

    }

    private void changeFuraStatus(Integer orderNumber, Fura.Status status) {
        Query query = entityManager.createQuery("SELECT o.furaId FROM Order o WHERE o.id = :number");
        query.setParameter("number", orderNumber);
        Integer furaId = Integer.parseInt(query.getSingleResult().toString());
        Query changeStatus = entityManager.createQuery("UPDATE Fura f SET f.status = :status WHERE f.furasId = :id");
        changeStatus.setParameter("id", furaId);
        changeStatus.setParameter("status", status);
        changeStatus.executeUpdate();
    }

    @Override
    public List<Integer> getCreatedOrders() {
        return getOrdersByStatus(OrderStatus.Status.created);
    }

    @Override
    public List<Integer> getConfirmedOrders() {
        return getOrdersByStatus(OrderStatus.Status.confirmed);
    }

    @Override
    public List<Integer> getMadeOrders() {
        return getOrdersByStatus(OrderStatus.Status.made);
    }

    @Override
    public List<Integer> getcreatedOrdersWitsGoods() {
        Query query = entityManager.createQuery("SELECT  DISTINCT oi.orderNumber FROM OrderInfo oi WHERE oi.orderStatus.status = :status");
        query.setParameter("status", OrderStatus.Status.created);
        return query.getResultList();
    }

    @Override
    public void checkIfGoodsAreNotEmpty(Integer orderNumber) {
        Query query = entityManager.createQuery("SELECT COUNT (oi.name) FROM OrderInfo oi WHERE oi.orderNumber = :number");
        query.setParameter("number", orderNumber);
        if (query.getSingleResult().equals(null) || Integer.parseInt(query.getSingleResult().toString()) == 0) {
            throw new IllegalArgumentException("Please add goods to order");
        }
    }

//    @Override
//    public List<Long> getDriversInCurrentOrder() {
//        Query query = entityManager.createQuery("SELECT  DISTINCT oi.orderNumber FROM OrderInfo oi WHERE oi.orderStatus.status = :status");
//        query.setParameter("status", OrderStatus.Status.created);
//        return query.getResultList();
//    }

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
        List<String> furasId = entityManager.createQuery("SELECT f.furaNumber FROM Fura f").getResultList();
        if (!furasId.contains(furaNumber)) {
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
        return Double.parseDouble(query.getSingleResult().toString());

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
        if (driverCount != driversInListCount) {
            throw new IllegalArgumentException("Fura should have " + driverCount + " drivers.");
        }
    }

    private List<Long> getDriversInOrder(Integer orderNumber) {
        Query query = entityManager.createQuery("SELECT d.license FROM Drivers d WHERE  d.driverShift.orderId = :orderNumber");
        query.setParameter("orderNumber", orderNumber);
        return query.getResultList();
    }

    private void checkDriverStatus(List<Long> drivers, DriverStatus status) {
        Query query = entityManager.createQuery("SELECT COUNT(ds.status)FROM DriverShift ds WHERE ds.status= :status AND ds.drivers.license IN :driver");
        query.setParameter("driver", drivers);
        query.setParameter("status", status);
        String driversCount = query.getSingleResult().toString();
        if (status.equals(DriverStatus.notShift)) {
            if (driversCount.equals(null) || Integer.parseInt(driversCount) != drivers.size()) {
                throw new IllegalArgumentException("Some drivers are already in shift or are not exists");
            } else if (status.equals(DriverStatus.atWeel)) {
                if (driversCount.equals(null)) {
                    throw new IllegalArgumentException("This order still has the driver behind the wheel");
                }
            }
        }
    }


    private void changeDriverStatus(Integer orderNumber, List<Long> license, DriverStatus status) {
        for (Long l : license) {
            Integer driverId = getDriverIdByDriverLicense(l);
            DriverShift driverShift = entityManager.find(DriverShift.class, driverId);
            driverShift.setStatus(status);
            driverShift.setOrderId(orderNumber);
            entityManager.merge(driverShift);
        }
//        Query query = entityManager.createQuery("UPDATE Drivers d SET d.driverShift.status = :status, d.driverShift.orderId = :number WHERE d.license IN :drivers");
//        query.setParameter("number", orderNumber);
//        query.setParameter("drivers", driverId);
//        query.setParameter("status", status);
//        query.executeUpdate();
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
        addDriversToOrder.setParameter("fura", getFuraId.getSingleResult().toString());
        addDriversToOrder.executeUpdate();
    }

    private void deleteFuraFromOrder(Integer orderNumber) {
        Query query = entityManager.createQuery("UPDATE Order o SET o.furaId = :nulls " +
                "WHERE o.id = :Ids");
        query.setParameter("nulls", null);
        query.setParameter("Ids", orderNumber);
        query.executeUpdate();
    }

    private List<Integer> getOrdersByStatus(OrderStatus.Status status) {
        Query query = entityManager.createQuery("SELECT o.id FROM Order o WHERE o.orderStatus.status = :status");
        query.setParameter("status", status);
        return query.getResultList();
    }

    private Integer getDriverIdByDriverLicense(Long license) {
        Query query = entityManager.createQuery("SELECT d.driversId FROM Drivers d WHERE d.license = :license");
        query.setParameter("license", license);
        return Integer.parseInt(query.getSingleResult().toString());
    }

}
