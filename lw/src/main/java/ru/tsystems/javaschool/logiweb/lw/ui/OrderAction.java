package ru.tsystems.javaschool.logiweb.lw.ui;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderInfo;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderStatus;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

//@Model
@ManagedBean(name = "orderAction")
@ViewScoped
public class OrderAction implements Serializable {
    private Order order;
    private List<Order> orders;
    private List<Integer> createdOrderNumber;
    private List<Integer> madeOrderNumber;
    private List<Integer> driversToOrder;
    private String furaToOrder;



    @Inject
    private FacesContext facesContext;

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
    public List<Integer> getDriversToOrder() {
        return driversToOrder;
    }

    public void setDriversToOrder(List<Integer> driversToOrder) {
        this.driversToOrder = driversToOrder;
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
    public void initOrders() {
        orders = orderService.getAllOrders();

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
        try {
            orderService.addOrder();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order was added", "Order addition successful"));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Addition unsuccessful"));
        }
    }





    public void createdOrders() {
        createdOrderNumber = orderService.getCreatedOrders();
    }



    public void madeOrders() {
        madeOrderNumber = orderService.getMadeOrders();
    }

    public void addFuraAndDriversToOrder() {
        try {
            orderService.addFuraAndDrivers(order.getId(), driversToOrder, furaToOrder);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fura and drivers was added", "Fura and drivers addition successful"));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Addition unsuccessful"));
        }
    }


}
