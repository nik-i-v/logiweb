package ru.tsystems.javaschool.logiweb.lw.ui;


import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.service.admin.DriverService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

//@Model // =@Named + @RequestScoped
@ManagedBean(name = "driverAction")
@ViewScoped
/**
 * This class provides access to services of actions with drivers company employee.
 * The class is serializable.
 *
 * @author Irina Nikulina
 */
public class DriverAction implements Serializable {

    @Inject
    private FacesContext facesContext;
    private static Logger logger = Logger.getLogger(DriverAction.class.getName());

    private Drivers driver;
    private List<DriverShift> drivers ;
    private List<Long> freeDrivers;

    @Produces
    @Named
    public List<Long> getFreeDrivers() {
        freeDrivers = allFreeDrivers();
        return freeDrivers;
    }

    public void setFreeDrivers(List<Long> freeDrivers) {
        this.freeDrivers = freeDrivers;
    }

    @Produces
    @Named
    public Drivers getDriver() {
        return driver;
    }

    public void setDriver(Drivers driver) {
        this.driver = driver;
    }

    public void setDrivers(List<DriverShift> drivers) {
        this.drivers = drivers;
    }

    @Produces
    @Named
    public List<DriverShift> getDrivers(){
        return drivers;
    }
    @EJB
    private DriverService driverService;

    /**
     * Initialized a new driver and sets a value for the list of drivers.
     */
    @PostConstruct
    public void initNewDriver(){
        drivers = getAllDrivers();
        driver = new Drivers();
//        getAllDrivers();
    }


//
    public List<DriverShift> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    /**
     * Adds a new driver.
     * @return true if thes driver was added and false if it's not.
     */
    public boolean addDriver(){
        try {
            driverService.addDriver(driver.getSurname(), driver.getName(), driver.getPatronymic(), driver.getLicense());
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Driver was added", "Driver addition successful"));
            drivers = getAllDrivers();
            driver.setLicense(null);
            driver.setName(null);
            driver.setSurname(null);
            driver.setPatronymic(null);
            return true;
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Addition unsuccessful"));
            return false;
        }
    }

    /**
     * Sets a value for the list of free drivers.
     */
    public List<Long> allFreeDrivers(){
        return driverService.getAllFreeDrivers();
    }



}
