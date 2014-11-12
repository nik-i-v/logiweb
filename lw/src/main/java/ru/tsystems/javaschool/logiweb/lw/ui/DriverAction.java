package ru.tsystems.javaschool.logiweb.lw.ui;


import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;
import ru.tsystems.javaschool.logiweb.lw.service.admin.DriverService;
import ru.tsystems.javaschool.logiweb.lw.service.admin.UserService;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

//@Model // =@Named + @RequestScoped
@ManagedBean(name = "driverAction")
@ViewScoped
//@SessionScoped
//@Named
public class DriverAction implements Serializable {
    private static Logger logger = Logger.getLogger(DriverAction.class.getName());

    private Drivers driver;
    private static List<Drivers> drivers ;

    @Produces
    @Named
    public Drivers getDriver() {
        return driver;
    }

    public void setDriver(Drivers driver) {
        this.driver = driver;
    }

    public static void setDrivers(List<Drivers> drivers) {
        DriverAction.drivers = drivers;
    }

    @Produces
    @Named
    public List<Drivers> getDrivers(){
        drivers = getAllDrivers();
        return drivers;
    }
    @EJB
    private DriverService driverService;

    @PostConstruct
    public void initNewDriver(){
        drivers = getAllDrivers();
        driver = new Drivers();
//        getAllDrivers();
    }
//
    public List<Drivers> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    public boolean addDriver(){
        try {
            driverService.addDriver(driver.getSurname(), driver.getName(), driver.getPatronymic(), driver.getLicense());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
