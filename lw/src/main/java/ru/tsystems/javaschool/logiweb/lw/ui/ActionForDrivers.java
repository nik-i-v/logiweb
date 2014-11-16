package ru.tsystems.javaschool.logiweb.lw.ui;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Fura;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderInfo;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderServiceForDrivers;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "action")
@RequestScoped
public class ActionForDrivers implements Serializable {
    private Long driverLicense;
    private List<Order> ordersDrivers;
    private String currentStatus;
    private String newStatus;
    private String name;


    @EJB
    private OrderServiceForDrivers orderServiceForDrivers;

    @Inject
    private CheckUser checkUser;

    @Inject
    private FacesContext facesContext;

    @Named
    @Produces
    public Long getDriverLicense() {
        return driverLicense;
    }

    @PostConstruct
    public void init() {
        driverLicense = Long.parseLong(checkUser.getUser().getName());
        currentStatus = orderServiceForDrivers.getCurrentStatusForDriver(driverLicense);
    }

    public void setDriverLicense(Long driverLicense) {
        this.driverLicense = driverLicense;
    }

    @Produces
    @Named
    public List<Order> getOrdersDrivers() {
        return ordersDrivers;
    }

    public void setOrdersDrivers(List<Order> orders) {
        this.ordersDrivers = orders;
    }

    @Produces
    @Named
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Named
    @Produces
    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Produces
    @Named
    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    @Produces
    @Named
    public void getOrderForDrivers(){
        ordersDrivers = orderServiceForDrivers.getOrderForDrivers(driverLicense);
    }

    public void changeStatus() {
        if (newStatus.equals(DriverShift.Status.atWeel)){
            changeStatus(DriverShift.Status.atWeel);
        } else {
            changeStatus(DriverShift.Status.shift);
        }
    }

    public void changeGoodsStatus(){
        try {
            orderServiceForDrivers.changeGoodsStatusForDrivers(name, driverLicense);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Goods status has been changed", "Goods status change successful"));
            getOrderForDrivers();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Changing unsuccessful"));
        }
    }

    private void changeStatus(DriverShift.Status status){
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