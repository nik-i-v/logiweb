package ru.tsystems.javaschool.logiweb.lw.ui;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderInfo;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderStatus;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

//@Model
@ManagedBean(name = "orderAction")
@SessionScoped
public class OrderAction implements Serializable {
    private Order order;
    private List<Order> orders;
    private Integer orderNumber;
    private OrderInfo orderInfo;
    private List<Integer> createdOrderNumber;
    private List<Integer> confirmedOrderNumber;
    private List<Integer> madeOrderNumber;

    @Produces
    @Named
    public List<Integer> getConfirmedOrderNumber() {
        confirmedOrders();
        return confirmedOrderNumber;
    }

    public void setConfirmedOrderNumber(List<Integer> confirmedOrderNumber) {
        this.confirmedOrderNumber = confirmedOrderNumber;
    }

    @Produces
    @Named
    public List<Integer> getMadeOrderNumber() {
        madeOrders();
        return madeOrderNumber;
    }

    public void setMadeOrderNumber(List<Integer> madeOrderNumber) {
        this.madeOrderNumber = madeOrderNumber;
    }

    @Produces
    @Named
    public List<Integer> getCreatedOrderNumber() {
        createdOrders();
        return createdOrderNumber;
    }

    public void setCreatedOrderNumber(List<Integer> createdOrderNumber) {
        this.createdOrderNumber = createdOrderNumber;
    }

    @EJB
    private OrderService orderService;

    @PostConstruct
    public void initOrders(){
        orders = orderService.getAllOrders();

    }

    @Produces
    @Named
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

    @Named
    @Produces
    public List<Order> getOrders() {
//        orders = orderService.getAllOrders();
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

    public void createdOrders(){
        createdOrderNumber = orderService.getCreatedOrders();
    }

    public void confirmedOrders(){
        confirmedOrderNumber = orderService.getConfirmedOrders();
    }

    public void madeOrders(){
        madeOrderNumber = orderService.getMadeOrders();
    }
}
