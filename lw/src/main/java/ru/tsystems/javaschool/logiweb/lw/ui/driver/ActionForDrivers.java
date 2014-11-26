package ru.tsystems.javaschool.logiweb.lw.ui.driver;

import ru.tsystems.javaschool.logiweb.lw.server.entities.*;
import ru.tsystems.javaschool.logiweb.lw.service.driver.OrderServiceForDrivers;
import ru.tsystems.javaschool.logiweb.lw.ui.CheckUser;
import ru.tsystems.javaschool.logiweb.lw.ui.LoginController;
//import ru.tsystems.javaschool.logiweb.lw.ui.annotations.Driver;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class provides access to services for drivers.
 * The class is serializable.
 *
 * @author Irina Nikulina
 */
@ManagedBean(name = "action")
@RequestScoped
public class ActionForDrivers implements Serializable {
    private Long driverLicense;
    private Order ordersDrivers;
    private String currentStatus;
    private String name;
    private DriverStatus statusMenu;
    private List<String> goodsName;

    @EJB
    private OrderServiceForDrivers orderServiceForDrivers;

    @Inject
    private CheckUser checkUser;

    @Inject
    private FacesContext facesContext;

    @Inject
    private transient Logger logger;

    /**
     * Initializes current status of a driver and driver's license number. Also initializes an order if there is any.
     */
    @PostConstruct
    public void init() {
        driverLicense = Long.parseLong(LoginController.driverLogin);
        currentStatus = orderServiceForDrivers.getCurrentStatusForDriver(driverLicense);
        if (!currentStatus.equals("notShift")) {
            ordersDrivers = orderServiceForDrivers.getOrderForDrivers(driverLicense);
            statusMenu = orderServiceForDrivers.getStatusMenuForDrivers(currentStatus);
        }
    }

    /**
     * Changes the status of a driver.
     */
    public void changeStatus() {
        if (currentStatus.equals(DriverStatus.atWeel.toString())) {
            changeStatus(DriverStatus.shift);
        } else {
            orderServiceForDrivers.isAnybodyAtWheel(driverLicense);
            changeStatus(DriverStatus.atWeel);
        }
    }

    /**
     * Changes the status of a goods in current order.
     */
    public void changeGoodsStatus() {
        try {
            orderServiceForDrivers.changeGoodsStatusForDrivers(name, driverLicense);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Goods status has been changed", "Goods status change successful"));
            ordersDrivers = orderServiceForDrivers.getOrderForDrivers(driverLicense);
            getGoodsName();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Changing unsuccessful"));
        }
    }



    @Named
    @Produces
    public List<String> getGoodsName() {
        goodsName = orderServiceForDrivers.getGoodsList(driverLicense);
        return goodsName;
    }

    @Named
    @Produces
    public Long getDriverLicense() {
        return driverLicense;
    }

    @Named
    @Produces
    public DriverStatus getStatusMenu() {
        return statusMenu;
    }

    @Produces
    @Named
    public Order getOrdersDrivers() {
        return ordersDrivers;
    }

    @Produces
    @Named
    public String getName() {
        return name;
    }

    @Named
    @Produces
    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setDriverLicense(Long driverLicense) {
        this.driverLicense = driverLicense;
    }

    public void setOrdersDrivers(Order orders) {
        this.ordersDrivers = orders;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void setGoodsName(List<String> goodsName) {
        this.goodsName = goodsName;
    }

    public void setStatusMenu(DriverStatus statusMenu) {
        this.statusMenu = statusMenu;
    }

    /**
     * Changes driver status to a new status.
     * @param status a new status for driver
     */
    private void changeStatus(DriverStatus status) {
        try {
            orderServiceForDrivers.changeDriverStatusForDrivers(driverLicense, status);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Driver status has been changed", "Driver status change successful"));
            currentStatus = orderServiceForDrivers.getCurrentStatusForDriver(driverLicense);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Changing unsuccessful"));
        }
    }
}