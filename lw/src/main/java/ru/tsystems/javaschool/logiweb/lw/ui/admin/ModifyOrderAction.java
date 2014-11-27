package ru.tsystems.javaschool.logiweb.lw.ui.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderInfo;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderStatus;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * This class provides operations to change orders.
 *
 * @author Irina Nikulina
 */
@ManagedBean(name = "modifyOrder")
@RequestScoped
public class ModifyOrderAction  implements Serializable{
    private OrderInfo orderInfo;
    private List<Integer> confirmedOrderNumber;
    private Integer orderNumber;
    private List<Integer> createdOrdersWitsGoods;
    private String furaToOrder;
    private List<Long> driversToOrder;

    @EJB
    private OrderService orderService;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    public void init(){
        orderInfo = new OrderInfo();
    }

    /**
     * Adds goods to the order.
     */
    public void addGoods() {
        try {
            orderService.addGoods(orderInfo.getOrderNumber(), orderInfo.getName(), orderInfo.getGpsLat(), orderInfo.getGpsLong(),
                    orderInfo.getWeight());
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Goods was added", "Goods addition successful"));
            orderInfo.setName(null);
            orderInfo.setGpsLat(null);
            orderInfo.setGpsLong(null);
            orderInfo.setWeight(null);
        }catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Addition unsuccessful"));
        }
    }

    /**
     * Sets values for orders with "confirmed" status.
     */
    public void confirmedOrders() {
        confirmedOrderNumber = orderService.getConfirmedOrders();
    }

    /**
     * Changes the order's status to "confirmed".
     */
    public void doConfirmed() {
        try {
            orderService.checkIfGoodsAreNotEmpty(orderNumber);
            orderService.changeOrderStatus(orderNumber, OrderStatus.Status.confirmed);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Order confirmed", "Order confirmed successful"));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Confirmed unsuccessful"));
        }
    }

    /**
     * Show the list of non-empty orders with "created" status.
     */
    public void createdOrdersWitsGoods() {
        createdOrdersWitsGoods = orderService.getCreatedOrdersWitsGoods();
    }

    /**
     * Adds furas and the list of drivers to the order.
     */
    public void addFuraAndDriversToOrder() {
        try {
            orderService.addFuraAndDrivers(orderNumber, driversToOrder, furaToOrder);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fura and drivers was added", "Fura and drivers addition successful"));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Addition unsuccessful"));
        }
    }

    /**
     * Closes the order.
     */
    public void closeOrder() {
        try {
            orderService.closeOrder(orderNumber);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Order closed", "Order close successful"));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Closing unsuccessful"));
        }
    }

    @Named
    @Produces
    public List<Integer> getCreatedOrdersWitsGoods() {
        createdOrdersWitsGoods();
        return createdOrdersWitsGoods;
    }

    @Named
    @Produces
    public List<Long> getDriversToOrder() {
        return driversToOrder;
    }

    @Named
    @Produces
    public String getFuraToOrder() {
        return furaToOrder;
    }

    @Named
    @Produces
    public Integer getOrderNumber() {
        return orderNumber;
    }

    @Named
    @Produces
    public List<Integer> getConfirmedOrderNumber() {
        confirmedOrders();
        return confirmedOrderNumber;
    }

    @Produces
    @Named
    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setCreatedOrdersWitsGoods(List<Integer> createdOrdersWitsGoods) {
        this.createdOrdersWitsGoods = createdOrdersWitsGoods;
    }

    public void setDriversToOrder(List<Long> driversToOrder) {
        this.driversToOrder = driversToOrder;
    }

    public void setFuraToOrder(String furaToOrder) {
        this.furaToOrder = furaToOrder;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setConfirmedOrderNumber(List<Integer> confirmedOrderNumber) {
        this.confirmedOrderNumber = confirmedOrderNumber;
    }
    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
