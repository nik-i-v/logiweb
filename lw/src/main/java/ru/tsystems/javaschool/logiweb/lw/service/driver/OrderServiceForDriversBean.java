package ru.tsystems.javaschool.logiweb.lw.service.driver;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderInfo;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderStatus;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class OrderServiceForDriversBean implements OrderServiceForDrivers {

    @Inject
    private EntityManager entityManager;

    @Inject
    private OrderService orderService;

    @Override
    public List<Order> getOrderInfoForDrivers(String login) {
        Query query = entityManager.createQuery("SELECT o FROM Order o WHERE o.driverShift.drivers.license = :license");
        query.setParameter("license", login);
        return query.getResultList();
    }

    @Override
    public Boolean hasOrder(String login) {
        Query hasOrder = entityManager.createQuery("SELECT COUNT(ds.orderId) FROM DriverShift ds WHERE ds.drivers.license = :license");
        hasOrder.setParameter("license", login);
        return !(Integer.parseInt(hasOrder.getSingleResult().toString()) == 0);

    }

    @Override
    public void changeGoodsStatusForDrivers(Integer orderNumber, String name) {
        areGoodsExists(orderNumber, name);
        isGoodsStatusAlreadyYes(orderNumber, name);
        Query query = entityManager.createQuery("UPDATE OrderInfo oi SET oi.status = :status WHERE  oi.orderNumber = :number and oi.name = :name");
        query.setParameter("status", OrderInfo.Status.yes);
        query.setParameter("number", orderNumber);
        query.setParameter("name", name);
        query.executeUpdate();
        if (howManyGoodsAreNo(orderNumber) == 0) {
            orderService.changeOrderStatus(orderNumber, OrderStatus.Status.made);
        }
    }

    private void areGoodsExists(Integer orderNumber, String name) {
        Query query = entityManager.createQuery("SELECT COUNT (oi.orderInfoId) FROM OrderInfo oi WHERE oi.orderNumber = :number and oi.name = :name");
        query.setParameter("number", orderNumber);
        query.setParameter("name", name);
        if (Integer.parseInt(query.getSingleResult().toString()) == 0) {
            throw new IllegalArgumentException("Goods is not exist.");
        }
    }

    private void isGoodsStatusAlreadyYes(Integer orderNumber, String name) {
        Query query = entityManager.createQuery("SELECT oi.status FROM OrderInfo oi WHERE oi.orderNumber = :number and oi.name = :name");
        query.setParameter("number", orderNumber);
        query.setParameter("name", name);
        if (query.getSingleResult().toString().equals(OrderInfo.Status.yes.toString())) {
            throw new IllegalArgumentException("Goods status is already 'yes'");
        }
    }

    private Integer howManyGoodsAreNo(Integer orderNumber) {
        Query query = entityManager.createQuery("SELECT COUNT(oi.status) FROM OrderInfo oi WHERE oi.status = :status AND ooi.orderNumber = :number");
        query.setParameter("status", OrderInfo.Status.no);
        query.setParameter("number", orderNumber);
        return Integer.parseInt(query.getSingleResult().toString());
    }
}
