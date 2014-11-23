package ru.tsystems.javaschool.logiweb.lw.ui.rest;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverStatus;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Order;
import ru.tsystems.javaschool.logiweb.lw.service.driver.OrderServiceForDrivers;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.List;

@Path("driverRest")
@ManagedBean
@SessionScoped
public class RestActions {

    private transient Client client;
    public String SERVICE_BASE_URI;

    @EJB
    private OrderServiceForDrivers orderServiceForDrivers;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    protected void initialize() {
//        FacesContext fc = FacesContext.getCurrentInstance();
//        SERVICE_BASE_URI = fc.getExternalContext().getInitParameter("metadata.serviceBaseURI");
        client = ClientBuilder.newClient();
    }

    public WebTarget getWebTarget(String relativeUrl) {
        if (client == null) {
            initialize();
        }
        return client.target(relativeUrl);
    }


    @POST
//    @Path("{driverLicense}/{name}")
    @Consumes("application/xml")
    public void changeGoodsStatus(@PathParam("name") String name, @PathParam("driverLicense") Long driverLicense) {
        orderServiceForDrivers.changeGoodsStatusForDrivers(name, driverLicense);
    }

    @POST
    @Path("changeStatus/{driverLicense}")
    @Consumes("application/xml")
    public void changeDriverStatus(@PathParam("driverLicense") Long driverLicense, @PathParam("status") DriverStatus status){
        if (status.equals(DriverStatus.atWeel.toString())) {
            changeStatus(driverLicense,DriverStatus.shift);
        } else {
            orderServiceForDrivers.isAnybodyAtWheel(driverLicense);
            changeStatus(driverLicense, DriverStatus.atWeel);
        }
    }

    @GET
    @Path("getDriver/{driverLicense}")
    @javax.ws.rs.Produces("text/xml")
    public Order getDriver(@PathParam("driverLicense") Long driverLicense) {
        return orderServiceForDrivers.getOrderForDrivers(driverLicense);
    }

    @GET
    @Path("getGoods/{driverLicense}")
    @javax.ws.rs.Produces("text/xml")
    private List<String> getGoodsStatus(@PathParam("driverLicense") Long driverLicense) {
        return orderServiceForDrivers.getGoodsList(driverLicense);
    }

    @GET
    @Path("getStatus/{driverLicense}")
    @javax.ws.rs.Produces("text/xml")
    public String getDriverStatus(@PathParam("driverLicense") Long driverLicense) {
        return orderServiceForDrivers.getCurrentStatusForDriver(driverLicense);
    }

    private void changeStatus(Long driverLicense, DriverStatus status) {
        try {
            orderServiceForDrivers.changeDriverStatusForDrivers(driverLicense, status);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Driver status has been changed", "Driver status change successful"));
//            currentStatus = orderServiceForDrivers.getCurrentStatusForDriver(driverLicense);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Changing unsuccessful"));
        }
    }
}
