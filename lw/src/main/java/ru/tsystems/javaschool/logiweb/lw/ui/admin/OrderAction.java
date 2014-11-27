package ru.tsystems.javaschool.logiweb.lw.ui.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderService;
//import ru.tsystems.javaschool.logiweb.lw.ui.annotations.Admin;

import javax.annotation.PostConstruct;
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
import java.util.logging.Logger;

/**
 * This class provides access to services of creation and closing of orders for company employee.
 * The class is serializable.
 *
 * @author Irina Nikulina
 */
@ManagedBean(name = "orderAction")
@ViewScoped
public class OrderAction implements Serializable {
    private Order order;
    private List<Order> orders;
    private List<Integer> createdOrderNumber;
    private List<Integer> madeOrderNumber;
    private List<Long> driversInCurrentOrder;

    @Inject
    private FacesContext facesContext;

    @Inject
    private Logger logger;

    @EJB
    private OrderService orderService;

    /**
     * Initializes orders.
     */
    @PostConstruct
    public void initOrders() {
        orders = orderService.getAllOrders();
    }

    /**
     * Adds a new order.
     */
    public void addOrder() {
        try {
            orderService.addOrder();
            logger.info("New order was created");
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Order was added", "Order addition successful"));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Addition unsuccessful"));
        }
    }

    /**
     * Initializes orders with "created" status.
     */
    public void createdOrders() {
        createdOrderNumber = orderService.getCreatedOrders();
    }

    /**
     * Initializes orders with "made" status.
     */
    public void madeOrders() {
        madeOrderNumber = orderService.getMadeOrders();
    }

    /**
     * Returns the list of drivers in current order.
     * @return the list of driver's licenses
     */
    @Named
    @Produces
    public List<Long> getDriversInCurrentOrder() {
        return driversInCurrentOrder;
    }

    /**
     * Gets numbers of orders with "made" status.
     * @return the list of order numbers
     */
    @Produces
    @Named
    public List<Integer> getMadeOrderNumber() {
        madeOrders();
        return madeOrderNumber;
    }

    /**
     * Gets numbers of orders with "created" status.
     * @return the list of order numbers
     */
    @Produces
    @Named
    public List<Integer> getCreatedOrderNumber() {
        createdOrders();
        return createdOrderNumber;
    }

    /**
     * Gets an order.
     * @return
     */
    @Produces
    @Named
    public Order getOrder() {
        return order;
    }

    /**
     * Gets the list of orders.
     * @return the list of orders.
     */
    @Named
    @Produces
    public List<Order> getOrders() {
        return orders;
    }
    /**
     * Sets drivers for certain order.
     * @param driversInCurrentOrder the list of drivers
     */
    public void setDriversInCurrentOrder(List<Long> driversInCurrentOrder) {
        this.driversInCurrentOrder = driversInCurrentOrder;
    }

    /**
     * Sets numbers of orders with "made" status.
     * @param madeOrderNumber the list of order numbers
     */
    public void setMadeOrderNumber(List<Integer> madeOrderNumber) {
        this.madeOrderNumber = madeOrderNumber;
    }

    /**
     * Sets numbers of orders with "created" status.
     * @param createdOrderNumber the list of order numbers
     */
    public void setCreatedOrderNumber(List<Integer> createdOrderNumber) {
        this.createdOrderNumber = createdOrderNumber;
    }

    /**
     * Sets an order.
     * @param order
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Sets the list of orders.
     * @param orders the list of orders.
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
