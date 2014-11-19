package ru.tsystems.javaschool.logiweb.lw.ui;

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

@ManagedBean(name = "modifyOrder")
@RequestScoped
public class ModifyOrderAction  implements Serializable{
    private OrderInfo orderInfo;
    private List<Integer> confirmedOrderNumber;
    private Integer orderNumber;
    private List<Integer> createdOrdersWitsGoods;
    private String furaToOrder;
    private List<Long> driversToOrder;

    @Named
    @Produces
    public List<Integer> getCreatedOrdersWitsGoods() {
        createdOrdersWitsGoods();
        return createdOrdersWitsGoods;
    }


    public void setCreatedOrdersWitsGoods(List<Integer> createdOrdersWitsGoods) {
        this.createdOrdersWitsGoods = createdOrdersWitsGoods;
    }

    @Named
    @Produces
    public List<Long> getDriversToOrder() {
        return driversToOrder;
    }

    public void setDriversToOrder(List<Long> driversToOrder) {
        this.driversToOrder = driversToOrder;
    }

    @Produces
    @Named
    public String getFuraToOrder() {
        return furaToOrder;
    }

    public void setFuraToOrder(String furaToOrder) {
        this.furaToOrder = furaToOrder;
    }

    @Named
    @Produces
    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Produces
    @Named
    public List<Integer> getConfirmedOrderNumber() {
        confirmedOrders();
        return confirmedOrderNumber;
    }

    public void setConfirmedOrderNumber(List<Integer> confirmedOrderNumber) {
        this.confirmedOrderNumber = confirmedOrderNumber;
    }
    @EJB
    private OrderService orderService;

    @Inject
    private FacesContext facesContext;

    @Produces
    @Named
    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    @PostConstruct
    public void init(){
        orderInfo = new OrderInfo();
    }

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

    public void confirmedOrders() {
        confirmedOrderNumber = orderService.getConfirmedOrders();
    }

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

    public void createdOrdersWitsGoods() {
        createdOrdersWitsGoods = orderService.getcreatedOrdersWitsGoods();
    }

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
}
