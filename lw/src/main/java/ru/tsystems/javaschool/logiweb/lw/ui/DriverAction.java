package ru.tsystems.javaschool.logiweb.lw.ui;


import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Users;
import ru.tsystems.javaschool.logiweb.lw.service.admin.DriverService;
import ru.tsystems.javaschool.logiweb.lw.service.admin.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@Model
@Named
public class DriverAction implements Serializable {
    private static Logger logger = Logger.getLogger(DriverAction.class.getName());

    private Drivers driver;
    private List<Drivers> drivers ;

    @Produces
    @Named
    public Drivers getDriver() {
        return driver;
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
