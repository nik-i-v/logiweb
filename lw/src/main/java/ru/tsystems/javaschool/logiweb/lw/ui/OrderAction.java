package ru.tsystems.javaschool.logiweb.lw.ui;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderInfo;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderStatus;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderService;

import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.util.List;

//@Model
@ManagedBean(name = "orderAction")
@ViewScoped
public class OrderAction {
    private Order order;
    private List<Order> orders;
    private Integer orderNumber;
    private OrderInfo orderInfo;

    @EJB
    private OrderService orderService;

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Produces
    @Named
    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    @Produces
    @Named
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder() {
//        order = new Order();
        orderService.addOrder();
    }

    public void addGoods() {
        orderService.addGoods(orderNumber, orderInfo.getName(), orderInfo.getGpsLat(), orderInfo.getGpsLong(),
                orderInfo.getWeight());
    }

    public void doConfirmed() {
        orderService.changeOrderStatus(orderNumber, OrderStatus.Status.confirmed);
    }

    public void closeOrder(){
        orderService.closeOrder(orderNumber);
    }
}
