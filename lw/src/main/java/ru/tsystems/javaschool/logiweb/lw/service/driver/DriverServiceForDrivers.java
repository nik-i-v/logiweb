package ru.tsystems.javaschool.logiweb.lw.service.driver;

import javax.ejb.Remote;

@Remote
public interface DriverServiceForDrivers {
    void changeDriverStatusForDrivers(String license, String status);
}
