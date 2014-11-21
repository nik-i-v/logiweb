package ru.tsystems.javaschool.logiweb.lw.ui;

import ru.tsystems.javaschool.logiweb.lw.server.entities.*;
import ru.tsystems.javaschool.logiweb.lw.service.driver.OrderServiceForDrivers;
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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

//@Driver
@ManagedBean(name = "action")
@RequestScoped
@Path("/driver")
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
    private Logger logger;

    @PostConstruct
    public void init() {
        driverLicense = Long.parseLong(LoginController.driverLogin);
        currentStatus = orderServiceForDrivers.getCurrentStatusForDriver(driverLicense);
        if(!currentStatus.equals("notShift")) {
            ordersDrivers = orderServiceForDrivers.getOrderForDrivers(driverLicense);
            statusMenu = orderServiceForDrivers.getStatusMenuForDrivers(currentStatus);
        }
    }

    public void changeStatus() {
        if (currentStatus.equals(DriverStatus.atWeel.toString())){
            changeStatus(DriverStatus.shift);
        } else {
            orderServiceForDrivers.isAnybodyAtWheel(driverLicense);
            changeStatus(DriverStatus.atWeel);
        }
    }

//    @Named
//    @Produces
//    @GET
//    public List<Drivers> getCoDrivers(){
//        return orderServiceForDrivers.getCoDrivers(driverLicense);
//    }

    public void changeGoodsStatus(){
        try {
            orderServiceForDrivers.changeGoodsStatusForDrivers(name, driverLicense);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Goods status has been changed", "Goods status change successful"));
//            getOrderForDrivers();
            getGoodsName();
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Changing unsuccessful"));
        }
    }

    @Named
//    @Produces
    @GET
    @Produces("text/plain")
    public List<String> getGoodsName() {
        goodsName = orderServiceForDrivers.getGoodsList(driverLicense);
        return goodsName;
    }

    @Named
    @Produces("text/plain")
    @GET
    public Long getDriverLicense() {
        return driverLicense;
    }

    @Named
    @Produces("text/plain")
    @GET
    public DriverStatus getStatusMenu() {
        return statusMenu;
    }

    @GET
    @Produces("text/plain")
    @Named
    public Order getOrdersDrivers() {
        return ordersDrivers;
    }

    @GET
    @Produces("text/plain")
    @Named
    public String getName() {
        return name;
    }

    @GET
    @Named
    @Produces("text/plain")
    public String getCurrentStatus() {
        return currentStatus;
    }

    @PUT
    @Consumes("text/plain")
    public void setDriverLicense(Long driverLicense) {
        this.driverLicense = driverLicense;
    }

    @PUT
    @Consumes("text/plain")
    public void setOrdersDrivers(Order orders) {
        this.ordersDrivers = orders;
    }

    @PUT
    @Consumes("text/plain")
    public void setName(String name) {
        this.name = name;
    }

    @PUT
    @Consumes("text/plain")
    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    @PUT
    @Consumes("text/plain")
    public void setGoodsName(List<String> goodsName) {
        this.goodsName = goodsName;
    }

    @PUT
    @Consumes("text/plain")
    public void setStatusMenu(DriverStatus statusMenu) {
        this.statusMenu = statusMenu;
    }


    private void changeStatus(DriverStatus status){
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